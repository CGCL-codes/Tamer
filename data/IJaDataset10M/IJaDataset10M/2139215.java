package net.techwatch.fsindex.event;

/**
 * @author wiv
 *
 */
public interface FileSystemEventHandler {

    /**
	 * @param event
	 */
    void postDelete(FileSystemEvent event);

    /**
	 * @param event
	 */
    void postCreate(FileSystemEvent event);

    /**
	 * @param event
	 */
    void postUpdate(FileSystemEvent event);
}
