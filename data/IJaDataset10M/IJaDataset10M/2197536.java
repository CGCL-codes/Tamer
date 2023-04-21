package com.leclercb.taskunifier.gui.threads.autobackup;

import javax.swing.SwingUtilities;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class AutoBackupThread extends Thread {

    public AutoBackupThread() {
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                Thread.sleep(60 * 1000);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        int nbHours = Main.getSettings().getIntegerProperty("general.backup.auto_backup_every");
                        BackupUtils.getInstance().autoBackup(nbHours);
                    }
                });
            } catch (InterruptedException e) {
            }
        }
    }
}
