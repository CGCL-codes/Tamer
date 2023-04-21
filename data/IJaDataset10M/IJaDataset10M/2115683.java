package org.middleheaven.io.repository.watch;

/**
 * 
 */
public interface FileChangeStrategy {

    public void onChange(WatchEvent event);
}
