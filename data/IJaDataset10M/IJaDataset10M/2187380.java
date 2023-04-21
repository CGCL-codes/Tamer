package com.manning.aip.mymoviesdatabase.data.backup;

import android.app.backup.BackupManager;
import android.content.Context;

/**
 * Use a wrapper BackupManager, because regular is only available on API level 8 and above.
 * 
 * @author ccollins
 *
 */
public class BackupManagerWrapper {

    private BackupManager instance;

    static {
        try {
            Class.forName("android.app.backup.BackupManager");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void isAvailable() {
    }

    public BackupManagerWrapper(final Context context) {
        instance = new BackupManager(context);
    }

    public void dataChanged() {
        instance.dataChanged();
    }
}
