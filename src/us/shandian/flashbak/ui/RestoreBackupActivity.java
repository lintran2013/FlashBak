package us.shandian.flashbak.ui;

import android.os.*;
import android.util.*;
import android.app.*;
import android.widget.*;
import android.content.*;
import android.view.*;

import java.io.File;

import us.shandian.flashbak.ui.NewBackupActivity;
import us.shandian.flashbak.helper.BackupLoader;
import us.shandian.flashbak.R;

public class RestoreBackupActivity extends NewBackupActivity
{

    private static final int MENU_DELETE_BACKUP = R.id.confirm_backup + 1;
	
	private BackupLoader mBackups;
	private String mName = "";
	
	@Override
	protected void initDisplay() {
		Bundle extras = getIntent().getExtras();
		mBackups = extras.getParcelable("loader");
		mName = extras.getString("name");
		
		mBackupName.setText(mName);
		mBackupName.setEnabled(false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				mAppArrayList = mBackups.getInfo(mName, mContext);
				mHandler.sendEmptyMessage(MSG_APP_LIST_OK);
			}
		}).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DELETE_BACKUP, 0, "").setIcon(R.drawable.ic_action_discard).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		
		switch (item.getItemId()) {
			case MENU_DELETE_BACKUP: {
				deleteDir(Environment.getExternalStorageDirectory() + "/FlashBak/" + 
				         Base64.encodeToString(mName.getBytes(), Base64.NO_WRAP) + "/");
				finish();
				ret = true;
				break;
			}
			case R.id.confirm_backup: {
				// TODO: Restore backup functions here
				ret = true;
				break;
			}
			default: {
				ret = super.onOptionsItemSelected(item);
			}
		}
		
		return ret;
	}
	
	private void deleteDir(String dirName) {
		File dir = new File(dirName);
		File[] sub = dir.listFiles();
		for (File f : sub) {
			if (f.isDirectory()) {
				deleteDir(f.getPath());
			} else {
				f.delete();
			}
		}
		dir.delete();
	}

}