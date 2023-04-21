package net.wotonomy.access;

import net.wotonomy.foundation.NSRecursiveLock;

/**
 * This class offers a very simple interface to a global locking
 * mechanism to be used by the whole access layer.
 *
 * @author ezamudio@nasoft.com
 * @author $Author: cgruber $
 * @version $Revision: 894 $
 */
public class EOAccessLock {

    private static NSRecursiveLock _lock = new NSRecursiveLock();

    private EOAccessLock() {
        super();
    }

    public static void lock() {
        _lock.lock();
    }

    public static void unlock() {
        _lock.unlock();
    }
}
