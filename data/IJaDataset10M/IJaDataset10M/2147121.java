package de.nionex.logcrawler;

/**
 * This Interface must be implemented by all observers of an
 * {@link IFileWatchEvent} on an {@link FileWatcher} Object.
 * 
 * @author <a href="mailto:prani09@bertelsmann.de">Daniel D&ouml;rr</a>
 * 
 */
public interface IFileWatchEventListener {

    /**
     * This method will be invoked if an {@link IFileWatchEvent} has occurred.
     * Observers which implement this interface and have been registered at an
     * {@link FileWatcher} for a specific type of event will be informed using
     * this method.
     * 
     * @param type
     *            Type of event of which has occurred.
     * @param event
     *            {@link IFileWatchEvent}
     */
    public void handleEvent(String type, IFileWatchEvent event);
}
