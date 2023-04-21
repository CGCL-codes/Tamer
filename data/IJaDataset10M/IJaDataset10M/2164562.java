package org.sf.net.sopf.cache;

/**
 * @author admin
 *
 */
public interface ICacheEventSource {

    public void addListener(ICacheEventListener listener);

    public void removeListener(ICacheEventListener listener);
}
