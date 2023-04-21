package javaframework.datalayer.filesystem.operations;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import javaframework.base.AbstractClassReleasingResources;
import javaframework.datalayer.filesystem.StorageSystem;
import javaframework.datalayer.filesystem.paths.FileSystemPath;

public abstract class FileSystemWatcher extends AbstractClassReleasingResources implements InterfaceFileSystemWatcher {

    private WatchService watchService;

    private WatchKey directoryKey;

    private void setWatchService(final WatchService watchService) {
        this.watchService = watchService;
    }

    private WatchService getWatchService() {
        return this.watchService;
    }

    private void setDirectoryKey(final WatchKey directoryKey) {
        this.directoryKey = directoryKey;
    }

    @Override
    public WatchKey getDirectoryKey() {
        return this.directoryKey;
    }

    protected FileSystemWatcher(final StorageSystem fileSystem) throws IOException {
        this.setWatchService(fileSystem.getStorageSystem().newWatchService());
    }

    @Override
    public void configureWatcher(final FileSystemPath directoryPath, final WatchedEvents[] watchedEvents) throws IOException {
        final WatchEvent.Kind[] ARRAY_WATCHEVENT_KIND = new WatchEvent.Kind[watchedEvents.length];
        int i = 0;
        for (WatchedEvents we : watchedEvents) ARRAY_WATCHEVENT_KIND[i++] = we.getValue();
        this.setDirectoryKey(directoryPath.getPath().register(this.getWatchService(), ARRAY_WATCHEVENT_KIND));
    }

    @Override
    public void stopWatchingDirectory() {
        this.getDirectoryKey().cancel();
    }

    private void receiveMoreEvents() {
        this.getDirectoryKey().reset();
    }

    @Override
    public void waitForEventsAndProcess(final WatchedEvents watchedEvent) throws InterruptedException {
        try {
            this.setDirectoryKey(this.getWatchService().take());
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
        this.pollEvents(watchedEvent);
    }

    @Override
    public void waitForEventsUntilTimeoutAndProcess(final WatchedEvents watchedEvent, final long timeout, final TimeUnit timeUnits) throws InterruptedException {
        try {
            this.setDirectoryKey(this.getWatchService().poll(timeout, timeUnits));
        } catch (Exception e) {
            throw new InterruptedException();
        }
        if (this.getDirectoryKey() != null) this.pollEvents(watchedEvent);
    }

    private void pollEvents(final WatchedEvents watchedEvent) {
        for (WatchEvent<?> event : this.getDirectoryKey().pollEvents()) {
            final WatchEvent.Kind<?> EVENT_FIRED = event.kind();
            if (EVENT_FIRED.name().equals(watchedEvent.getValue().name())) {
                final FileSystemPath EVENT_PATH = new FileSystemPath(((Path) event.context()).toString());
                this.manageEvents(EVENT_FIRED, event.count(), EVENT_PATH);
            }
        }
        this.receiveMoreEvents();
    }

    protected abstract void manageEvents(final WatchEvent.Kind<?> eventKind, final int count, final FileSystemPath eventPath);

    @Override
    public void releaseResources() {
        if (this.getDirectoryKey() != null) this.stopWatchingDirectory();
        this.setDirectoryKey(null);
        this.setWatchService(null);
    }
}
