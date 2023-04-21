package task;

import com.dt.iTunesController.ITFileOrCDTrack;
import com.dt.iTunesController.ITTrack;
import com.dt.iTunesController.ITTrackCollection;
import core.CopyFile;
import core.ITunesWin;
import java.io.File;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import display.Display;
import exception.ITDSMException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.GuiMain;
import ui.table.SongTable;

/**
 * This class provides functionality for backing up, deleting, and starting
 * an orphaned file removal task.  Once duplicate files have been found the
 * user selects files to be operated on, and then this class will process them.
 * 
 * @author Brian Gibowski brian@brgib.com
 */
public class CleanITunesTask extends SwingWorker<Void, Void> {

    private static Logger LOG = GuiMain.LOG;

    private ArrayList<File> filesToProcess;

    private ArrayList<File> allDuplicateFiles;

    private File backupDirectory;

    private Display display;

    private SongTable songTable;

    private int numOfOrphanedFilesRemoved = 0;

    private boolean deleteFiles;

    private boolean backupFiles;

    private boolean removeOrphanedFiles;

    private int numOfOperations;

    private int processMultiplier;

    ;

    private int completedOperations = 0;

    /**
     * Creates a new CleanDirectory SwingWorker background task.
     *
     * @param selectedFiles the files the user has selected to be operated on.
     *
     * @param allDupFiles all of the found duplicate files.  This is necessary
     * to help remove the lock that placed on a file during processing.  If the file
     * is being deleted it must be removed from both the filesToProcess/selectedFiles
     * as well as AllDuplicateFiles/allDupFiles array lists.
     *
     * @param backupDir the directory to backup filesToProcess to.
     *
     * @param disp the Display object containing both the status label and progress
     * bar of the RemoveTracksPanel.
     *
     * @param table the table displaying a list of all found duplicate files
     *
     * @param backup whether or not the user has decided to backup the list of
     * duplicated files to process.
     *
     * @param del whether or not the user has decided to delete the files to process
     *
     * @param removeOrphaned whether or not the user would like to search for
     * orphaned files in iTunes created by this task.
     */
    public CleanITunesTask(ArrayList<File> selectedFiles, ArrayList<File> allDupFiles, File backupDir, Display disp, SongTable table, boolean backup, boolean del, boolean removeOrphaned) {
        if (backup || del) {
            if (selectedFiles.size() == 0) {
                IllegalArgumentException e = new IllegalArgumentException("No selected files for processing");
                LOG.log(Level.SEVERE, this.getClass().getName(), e);
                throw e;
            }
            if (allDupFiles.size() == 0) {
                IllegalArgumentException e = new IllegalArgumentException("No duplicate files found");
                LOG.log(Level.SEVERE, this.getClass().getName(), e);
                throw e;
            }
            if (backupDir == null && backup == true) {
                NullPointerException e = new NullPointerException("Backup directory is null");
                LOG.log(Level.SEVERE, this.getClass().getName(), e);
                throw e;
            }
        }
        filesToProcess = selectedFiles;
        allDuplicateFiles = allDupFiles;
        display = disp;
        songTable = table;
        backupDirectory = backupDir;
        deleteFiles = del;
        backupFiles = backup;
        removeOrphanedFiles = removeOrphaned;
        if (backupFiles) {
            numOfOperations++;
        }
        if (deleteFiles) {
            numOfOperations++;
        }
        if (removeOrphanedFiles) {
            numOfOperations++;
            numOfOperations++;
        }
        processMultiplier = 100 / numOfOperations;
    }

    /**
     * Runs the main task of backing up and or deleting selected files.
     * @return Void here as this is the object returned by the SwingWorker task.
     */
    public Void doInBackground() {
        if (backupFiles) {
            backupFiles(filesToProcess, backupDirectory);
            completedOperations++;
        }
        if (deleteFiles) {
            deleteFiles(filesToProcess);
            completedOperations++;
        }
        if (removeOrphanedFiles) {
            ArrayList<ITFileOrCDTrack> orphanedFiles = getOrphanedFiles();
            completedOperations++;
            removeOrphanedFiles(orphanedFiles);
            completedOperations++;
        }
        setProgress(100);
        return null;
    }

    /**
     * The method called after the doInBackground method is finished by the SwingWorker
     * task.
     */
    public void done() {
        if (isCancelled()) {
            numOfOperations = completedOperations;
        }
        assert completedOperations == numOfOperations;
        display.setStatusLabel("Finishing cleaning the music directory.");
    }

    /**
     * Copies the selected files to a destination directory using the CopyFile
     * class.
     *
     * @param filesToBackup The files to copy to the given backup directory.
     *
     * @param backupDir The directory to send the files to copy to.
     */
    public void backupFiles(ArrayList<File> filesToBackup, File backupDir) {
        if (backupDir == null) {
            NullPointerException e = new NullPointerException("Backup directory is null");
            LOG.log(Level.SEVERE, this.getClass().getName(), e);
            throw e;
        }
        if (filesToBackup.size() == 0) {
            LOG.log(Level.SEVERE, "No Files to backup");
        }
        LOG.info("Backup directory:  " + backupDir.getPath());
        int total = filesToBackup.size();
        for (int i = 0; i < total; i++) {
            if (isCancelled()) {
                display.setStatusLabel("Backup duplicate song to " + backupDir.getPath() + " cancelled");
                break;
            }
            File fileToBackup = filesToBackup.get(i);
            String fileDestinationName = backupDir.getPath() + CopyFile.FILE_SEPERATOR + fileToBackup.getName();
            File fileDestination = new File(fileDestinationName);
            display.setStatusLabel("Copying " + fileToBackup.getName() + " to " + backupDir.getPath());
            new CopyFile(fileToBackup).write(fileDestination);
            LOG.fine("Copied file:  " + fileToBackup.getPath() + "\r\n" + "to:  " + fileDestination.getPath());
            setProgress(100 * completedOperations / numOfOperations + processMultiplier * i / total);
        }
        display.setStatusLabel("Finished duplicate song backup process");
    }

    /**
     * Deletes the user selected files.
     *
     * @param filesToDelete The files to delete.
     *
     * @return The array of boolean values containing the results
     * of the deleting files.
     */
    public boolean[] deleteFiles(ArrayList<File> filesToDelete) {
        if (filesToDelete.size() == 0) {
            LOG.log(Level.SEVERE, "No Files to delete");
        }
        display.setStatusLabel("Deleting duplicate song files");
        boolean[] deletedBool = new boolean[filesToDelete.size()];
        int total = filesToDelete.size();
        int i = 0;
        while (filesToDelete.size() > 0) {
            if (isCancelled()) {
                display.setStatusLabel("Duplicate file delete process cancelled");
                break;
            }
            File fileToDelete = filesToDelete.get(0);
            filesToDelete.remove(0);
            allDuplicateFiles.remove(fileToDelete);
            songTable.removeRow(fileToDelete);
            display.setStatusLabel("Deleting file " + fileToDelete.getName());
            boolean deleted = fileToDelete.delete();
            deletedBool[i] = deleted;
            if (!deleted) {
                LOG.log(Level.WARNING, "Unable to delete file:  " + fileToDelete.getPath());
                Display.newWarningMessage("Unable to delete file", "Unable to delete file " + fileToDelete.getName() + "\n" + "There may be another program using the file.");
            } else {
                LOG.fine("Deleted file:  " + fileToDelete.getPath());
            }
            i++;
            setProgress(100 * completedOperations / numOfOperations + processMultiplier * i / total);
        }
        return deletedBool;
    }

    /**
     * This method communicates with iTunes through an ITunesWin object
     * to retrieve orphaned files from iTunes.  Orphaned files are iTunes
     * library media items that refer to non-existent files.  If a file is
     * orphaned iTunes will return an empty string.
     *
     * @return The list of orphaned files from iTunes.
     */
    public ArrayList<ITFileOrCDTrack> getOrphanedFiles() {
        display.setStatusLabel("Starting up iTunes...");
        ITunesWin itunes = new ITunesWin();
        display.setStatusLabel("Scanning for orphaned files");
        ArrayList<ITFileOrCDTrack> orphanedTracks = new ArrayList<ITFileOrCDTrack>();
        ITTrackCollection collection = new ITunesWin().getLibraryCollection();
        int total = collection.getCount();
        for (int i = 1; i <= total; i++) {
            display.setStatusLabel("Scanning for orphaned files " + i + "/" + total);
            if (isCancelled()) {
                display.setStatusLabel("Search for orphaned files cancelled");
                break;
            }
            try {
                ITTrack track = collection.getItem(i);
                ITFileOrCDTrack trackFile = (ITFileOrCDTrack) track;
                if (itunes.isOrphaned(trackFile)) {
                    orphanedTracks.add(trackFile);
                    LOG.fine("iTunes Track is orphaned:  " + trackFile.getName());
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, this.getClass().getName(), e);
            }
            setProgress(100 * completedOperations / numOfOperations + processMultiplier * i / total);
        }
        LOG.info("Found " + orphanedTracks.size() + " orphaned files");
        return orphanedTracks;
    }

    /**
     * Removes orphaned files from iTuens given an ArrayList of orphaned files
     * from iTunes.
     *
     * @param orphanedTracks the array list of orphaned files from iTunes that
     * need to be removed.
     */
    public void removeOrphanedFiles(ArrayList<ITFileOrCDTrack> orphanedTracks) {
        display.setStatusLabel("Removing orphaned files");
        int size = orphanedTracks.size();
        int i = 0;
        while (orphanedTracks.size() > 0) {
            if (isCancelled()) {
                display.setStatusLabel("Orphaned file removal process cancelled");
                break;
            }
            display.setStatusLabel("Removing orphaned file " + orphanedTracks.get(0).getName());
            orphanedTracks.get(0).delete();
            orphanedTracks.remove(0);
            numOfOrphanedFilesRemoved++;
            i++;
            setProgress(100 * completedOperations / numOfOperations + processMultiplier * i / size);
        }
    }
}
