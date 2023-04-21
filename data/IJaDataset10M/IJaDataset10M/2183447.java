package org.middleheaven.core.wiring;

/**
 * 
 */
public interface PropertyManager {

    public String getName();

    public Object getProperty(String key);

    public boolean containsProperty(String key);
}
