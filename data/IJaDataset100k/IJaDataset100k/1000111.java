package org.jpos.util;

/**
 * @author apr@cs.com.uy
 * @since jPOS 1.1
 * @version $Id: LockManager.java 2854 2010-01-02 10:34:31Z apr $
 */
public interface LockManager {

    public interface Ticket {

        public boolean renew(long duration);

        public long getExpiration();

        public boolean isExpired();

        public void cancel();
    }

    public Ticket lock(String resourceName, long duration, long wait);
}
