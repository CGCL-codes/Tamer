package filesystem_test.operations;

import java.util.concurrent.TimeUnit;
import javaframework.datalayer.filesystem.StorageSystem;
import javaframework.datalayer.filesystem.operations.InterfaceFileSystemWatcher.WatchedEvents;
import javaframework.datalayer.filesystem.paths.FileSystemPath;

public class FileSystemWatcher_Test {

    public static void main(String[] args) {
        try {
            final FileSystemPath FSP = new FileSystemPath("C:\\TEMP2");
            final Watcher w = new Watcher(new StorageSystem());
            final WatchedEvents[] WATCHED_EVENTS = { WatchedEvents.CREATION, WatchedEvents.DELETION, WatchedEvents.MODIFICATION, WatchedEvents.OVERFLOW };
            w.configureWatcher(FSP, WATCHED_EVENTS);
            while (w.keepWatching) {
                w.waitForEventsUntilTimeoutAndProcess(WatchedEvents.DELETION, 10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }
}
