package de.flomain.secdata.program;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import de.flomain.secdata.backup.BackupFile;
import de.flomain.secdata.backup.BackupItem;
import de.flomain.secdata.backup.Task;
import de.flomain.secdata.logging.Logging;

public class Engine {

    private Task task;

    private ArrayList<BackupFile> items = new ArrayList<BackupFile>();

    private DaemonGui gui;

    private static boolean stop = false;

    public Engine(Task task, DaemonGui gui) {
        this.task = task;
        this.gui = gui;
        runTask();
    }

    private void runTask() {
        gui.startTimer();
        items = task.getObsoleteFiles();
        int files = 0;
        gui.setProgressIndikator(files, items.size());
        if (items != null) {
            for (BackupItem item : items) {
                if (stop) {
                    Engine.stop = false;
                    break;
                }
                File file = new File(item.getDrive() + ":" + item.getPath());
                if (file.exists()) {
                    if (copyFile(item)) {
                        this.task.itemProcessed(new BackupFile(file.getAbsolutePath(), new Date(), file.length()));
                        files++;
                        gui.setProgressstatus(files, file.getAbsolutePath());
                    }
                } else {
                    if (removeFileFromBackup(item)) {
                        this.task.removeBackupFile(item);
                        Logging.logMessage("file " + task.getDestinationPath() + "\\" + item.getDrive() + item.getPath() + " was removed!");
                        files++;
                    }
                }
            }
        }
        task.finish();
        gui.stopTimer();
    }

    private void createFolderStructure(String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        String dir = file.getParent();
        file = new File(dir);
        file.mkdirs();
    }

    private boolean copyFile(BackupItem item) {
        try {
            FileChannel src = new FileInputStream(item.getDrive() + ":" + item.getPath()).getChannel();
            createFolderStructure(this.task.getDestinationPath() + "\\" + item.getDrive() + item.getPath());
            FileChannel dest = new FileOutputStream(this.task.getDestinationPath() + "\\" + item.getDrive() + item.getPath()).getChannel();
            dest.transferFrom(src, 0, src.size());
            src.close();
            dest.close();
            Logging.logMessage("file " + item.getDrive() + ":" + item.getPath() + " was backuped");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean removeFileFromBackup(BackupItem item) {
        File file = new File(this.task.getDestinationPath() + item.getDrive() + item.getPath());
        return file.delete();
    }

    public void cancelBackup() {
        Engine.stop = true;
    }
}
