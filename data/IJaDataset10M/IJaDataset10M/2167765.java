package org.apache.harmony.xnet.provider.jsse;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;

/**
 * 
 * SSLSessionContext implementation
 * @see javax.net.ssl.SSLSessionContext
 */
public class SSLSessionContextImpl implements SSLSessionContext {

    private int cacheSize = 0;

    private long timeout = 0;

    private final Hashtable<IdKey, SSLSessionImpl> sessions = new Hashtable<IdKey, SSLSessionImpl>();

    @SuppressWarnings("unchecked")
    public Enumeration getIds() {
        return new Enumeration() {

            Enumeration<IdKey> keys = sessions.keys();

            public boolean hasMoreElements() {
                return keys.hasMoreElements();
            }

            public Object nextElement() {
                return keys.nextElement().id;
            }
        };
    }

    /**
     *
     * @see javax.net.ssl.SSLSessionContext.getSession(byte[] sessionId)
     */
    public SSLSession getSession(byte[] sessionId) {
        return sessions.get(new IdKey(sessionId));
    }

    /**
     * @see javax.net.ssl.SSLSessionContext.getSessionCacheSize()
     */
    public int getSessionCacheSize() {
        return cacheSize;
    }

    /**
     * @see javax.net.ssl.SSLSessionContext.getSessionTimeout()
     */
    public int getSessionTimeout() {
        return (int) (timeout / 1000);
    }

    /**
     * @see javax.net.ssl.SSLSessionContext.setSessionCacheSize(int size)
     */
    public void setSessionCacheSize(int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException("size < 0");
        }
        cacheSize = size;
        if (size > 0 && sessions.size() < size) {
            removeOldest(size - sessions.size());
        }
    }

    public void setSessionTimeout(int seconds) throws IllegalArgumentException {
        if (seconds < 0) {
            throw new IllegalArgumentException("seconds < 0");
        }
        timeout = seconds * 1000;
        for (Enumeration<IdKey> en = sessions.keys(); en.hasMoreElements(); ) {
            IdKey key = en.nextElement();
            SSLSessionImpl ses = (sessions.get(key));
            if (!ses.isValid()) {
                sessions.remove(key);
            }
        }
    }

    /**
     * Adds session to the session cache
     * @param ses
     */
    void putSession(SSLSessionImpl ses) {
        if (cacheSize > 0 && sessions.size() == cacheSize) {
            removeOldest(1);
        }
        ses.context = this;
        sessions.put(new IdKey(ses.getId()), ses);
    }

    private void removeOldest(int num) {
    }

    private class IdKey {

        private byte[] id;

        private IdKey(byte[] id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof IdKey)) {
                return false;
            }
            return Arrays.equals(id, ((IdKey) o).id);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(id);
        }
    }
}
