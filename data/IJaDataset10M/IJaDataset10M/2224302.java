package sun.management;

import java.lang.management.LockInfo;

/**
 * This interface is used for data conversion from LockInfo
 * to CompositeData (its mapped type) or vice versa.
 */
public interface LockDataConverterMXBean {

    public void setLockInfo(LockInfo l);

    public LockInfo getLockInfo();

    public void setLockedSynchronizers(LockInfo[] l);

    public LockInfo[] getLockedSynchronizers();
}
