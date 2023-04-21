package com.android.backuptest;

import android.backup.BackupHelperAgent;
import android.backup.FileBackupHelper;
import android.backup.SharedPreferencesBackupHelper;

public class BackupTestAgent extends BackupHelperAgent {

    public void onCreate() {
        addHelper("data_files", new FileBackupHelper(this, BackupTestActivity.FILE_NAME));
        addHelper("more_data_files", new FileBackupHelper(this, "another_file.txt", "3.txt", "empty.txt"));
        addHelper("shared_prefs", new SharedPreferencesBackupHelper(this, "settings", "raw"));
    }
}
