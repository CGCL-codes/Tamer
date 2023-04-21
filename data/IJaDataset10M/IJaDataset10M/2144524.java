package net.sf.webwarp.util.filemonitor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import net.sf.webwarp.util.file.FiledState;
import org.apache.log4j.Logger;

/**
 * The FileScanner is used to find changes (= FileMonitorEvent's) in a amount of defined files between the calls to scan those files. <p> Optionally the
 * FileScanner can scan the complete file tree below the files to monitor.
 */
class FileScanner {

    Logger log = Logger.getLogger(this.getClass());

    private File[] files;

    private boolean recursive;

    private FilenameFilter filter;

    private Snapshot lastSnapshot;

    private FiledState state;

    private static final String SNAPSHOT_KEY = "SNAPSHOT";

    /**
     * Creates a new FileScanner.
     * 
     * @param files,
     *            the files to scan
     * @param should
     *            the file tree below the initial files be scanned to?
     * @param filter
     *            file filter to define which files inside directories are to be considered (null = all files).
     * @param state
     *            filed state object to save current snapshot, if null, no persistence is maintained.
     */
    @SuppressWarnings("unchecked")
    public FileScanner(File[] files, boolean recursive, FilenameFilter filter, FiledState state) {
        this.files = files;
        this.recursive = recursive;
        this.filter = filter;
        this.state = state;
        if (state != null) {
            HashMap<MonitoredFile, MonitoredFile> map = (HashMap<MonitoredFile, MonitoredFile>) state.get(SNAPSHOT_KEY);
            if (map == null) {
                log.info("No stored snapshot was available, using empty snapshot as start value.");
                lastSnapshot = new Snapshot();
            } else {
                lastSnapshot = new Snapshot(map);
            }
        }
        if (lastSnapshot == null) {
            try {
                lastSnapshot = Snapshots.createSnapshot(files, recursive, filter);
            } catch (IOException ioex) {
                log.error("IOException while creating Snapshot - using empty snapshot as start value.", ioex);
                lastSnapshot = new Snapshot();
            }
        }
    }

    /**
     * Is the FileScanner scanning the files/ folders below the initial files to?
     * 
     * @return true = the FileScanner is scanning the files/ folders below the initial files to.
     */
    public boolean isRecursive() {
        return this.recursive;
    }

    /** Returns files from last snapshot. */
    public synchronized Snapshot getLastSnapshot() {
        return lastSnapshot;
    }

    /**
     * Checks if the files to be monitored have changed since that last call of scan.
     * 
     * @return the FileMonitorEvent's that are found, comparing the current file states with those when scan was last called.
     */
    public synchronized FileMonitorEvent[] scan() throws IOException {
        Snapshot currentSnapshot = Snapshots.createSnapshot(files, recursive, filter);
        FileMonitorEvent fileMonitorEvents[] = scan(lastSnapshot, currentSnapshot);
        lastSnapshot = currentSnapshot;
        if (state != null && fileMonitorEvents.length != 0) {
            state.set(SNAPSHOT_KEY, currentSnapshot.getMap());
        }
        return fileMonitorEvents;
    }

    /**
     * Checks if the files to be monitored have changed compared to given snapshot.
     * 
     * @return the FileMonitorEvent's that are found.
     */
    public FileMonitorEvent[] scan(Snapshot oldSnapshot) throws IOException {
        Snapshot currentSnapshot = Snapshots.createSnapshot(files, recursive, filter);
        FileMonitorEvent fileMonitorEvents[] = scan(oldSnapshot, currentSnapshot);
        return fileMonitorEvents;
    }

    /**
     * Checks if the files to be monitored have changed compared to given snapshot.
     * 
     * @return the FileMonitorEvent's that are found, comparing the current file states with those when scan was last called.
     */
    public synchronized FileMonitorEvent[] scan(Snapshot oldSnapshot, Snapshot newSnapshot) {
        ArrayList<FileMonitorEvent> fileMonitorEvents = new ArrayList<FileMonitorEvent>();
        Collection<MonitoredFile> newMonitoredFiles = new HashSet<MonitoredFile>(newSnapshot.getMap().values());
        newMonitoredFiles.removeAll(oldSnapshot.getMap().values());
        fileMonitorEvents.addAll(Snapshots.toFileMonitorEvents(newMonitoredFiles, FileMonitorEvent.FILE_ADDED));
        Collection<MonitoredFile> deletedMonitoredFiles = new HashSet<MonitoredFile>(oldSnapshot.getMap().values());
        deletedMonitoredFiles.removeAll(newSnapshot.getMap().values());
        fileMonitorEvents.addAll(Snapshots.toFileMonitorEvents(deletedMonitoredFiles, FileMonitorEvent.FILE_REMOVED));
        Collection<MonitoredFile> modifiedMonitoredFiles = Snapshots.modifiedFilesIntersection(oldSnapshot, newSnapshot).getMap().values();
        fileMonitorEvents.addAll(Snapshots.toFileMonitorEvents(modifiedMonitoredFiles, FileMonitorEvent.FILE_MODIFIED));
        return (FileMonitorEvent[]) fileMonitorEvents.toArray(new FileMonitorEvent[0]);
    }
}
