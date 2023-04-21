package com.funambol.util;

/**
 * the watcher of a watchable. <p>
 * This interface implements a single method <code>update</code> 
 * that should be called by the watchable each time his state changes
 */
public interface Observer {

    public void update(Object o);
}
