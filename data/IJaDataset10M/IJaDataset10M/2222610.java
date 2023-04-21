package org.snipsnap.snip.storage;

/**
 * Interface for loading classes
 *
 * @author stephan
 * @version $Id: Storage.java 648 2003-01-09 09:49:12Z stephan $
 */
public interface Storage {

    public Object loadObject(String name);
}
