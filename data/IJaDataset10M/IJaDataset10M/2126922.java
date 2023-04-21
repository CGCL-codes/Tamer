package org.tzi.wr;

import java.util.Hashtable;

/**
 * @author Psi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DeviceCache {

    private static DeviceCache mInstance = null;

    private Hashtable mRefTable = null;

    private Hashtable mInMemDevs = null;

    private Hashtable mInMemIds = null;

    private DeviceStore mDeviceStore = null;

    private Object mSyncRef = null;

    private Object mSyncCommit;

    AddrList mTraceList;

    AddrList mContactList;

    AddrList mRopedList;

    AddrList mFamiliarList;

    AddrList mInvisibleList;

    AddrList mRecentList;

    AddrList mTrackStationList;

    AddrList mReferencePointList;

    AddrList mLogList;

    private DeviceCache() {
        mSyncCommit = new Object();
        mSyncRef = new Object();
        mRefTable = new Hashtable();
        mInMemDevs = new Hashtable();
        mInMemIds = new Hashtable();
        mDeviceStore = new DeviceStore();
        mTraceList = new AddrListTraces();
        mContactList = new AddrListContacts();
        mFamiliarList = new AddrListFamiliar();
        mRopedList = new AddrListRoped();
        mInvisibleList = new AddrListInvisible();
        mRecentList = new AddrListRecent();
        mTrackStationList = new AddrListTrackStations();
        mReferencePointList = new AddrListReferencePoints();
        mLogList = new AddrList("NewDevs", false, null);
    }

    public static DeviceCache getInstance() {
        if (mInstance == null) {
            mInstance = new DeviceCache();
        }
        return mInstance;
    }

    public DeviceProxy refDevice(String aBdAddr) {
        synchronized (mSyncRef) {
            DeviceProxy d = getFromMemory(aBdAddr);
            if (d == null) {
                d = getFromStore(aBdAddr);
            }
            if (d != null) {
                refDevice(d);
            }
            return d;
        }
    }

    public DeviceProxy refDevice(int aID) {
        synchronized (mSyncRef) {
            DeviceProxy d = getFromMemory(aID);
            if (d == null) {
                d = getFromStore(aID);
            }
            if (d != null) {
                refDevice(d);
            }
            return d;
        }
    }

    private void refDevice(Device d) {
        Integer count = (Integer) mRefTable.get(d.getAddress());
        int i = 0;
        if (count != null) {
            i = count.intValue();
        }
        i++;
        mRefTable.put(d.getAddress(), new Integer(i));
        mInMemDevs.put(d.getAddress(), d);
        mInMemIds.put(new Integer(d.getRecordID()), d);
    }

    public void unrefDevice(Device aDevice) {
        synchronized (mSyncRef) {
            commitDevice(aDevice);
            Integer count = (Integer) mRefTable.get(aDevice.getAddress());
            if (count != null) {
                if (count.intValue() > 1) {
                    int i = count.intValue() - 1;
                    mRefTable.put(aDevice.getAddress(), new Integer(i));
                } else {
                    mRefTable.remove(aDevice.getAddress());
                    mInMemDevs.remove(aDevice.getAddress());
                    mInMemIds.remove(new Integer(aDevice.getRecordID()));
                }
            }
        }
    }

    public void addDevice(Device aDevice) {
        synchronized (mSyncRef) {
            mDeviceStore.addDevice(aDevice);
            checkLists(aDevice);
            mLogList.addAddress(aDevice.getAddress());
        }
    }

    public void commitDevice(Device aDevice) {
        synchronized (mSyncCommit) {
            if (aDevice != null) {
                mDeviceStore.commitDevice(aDevice);
                checkLists(aDevice);
            }
        }
    }

    public void deleteDevice(String aBdAddr) {
        synchronized (mSyncRef) {
            synchronized (mSyncCommit) {
            }
        }
    }

    public int getDeviceCount() {
        return mDeviceStore.getDeviceCount();
    }

    /**
	 * @param aBdAddr
	 * @return the Device, if it is initialized; null otherwise.
	 */
    private DeviceProxy getFromMemory(String aBdAddr) {
        return (DeviceProxy) mInMemDevs.get(aBdAddr);
    }

    private DeviceProxy getFromMemory(int id) {
        return (DeviceProxy) mInMemIds.get(new Integer(id));
    }

    private DeviceProxy getFromStore(String aBdAddr) {
        BtDevice d = mDeviceStore.getDevice(aBdAddr);
        if (d != null) {
            return new DeviceProxy(d);
        } else {
            return null;
        }
    }

    private DeviceProxy getFromStore(int aID) {
        BtDevice d = mDeviceStore.getDevice(aID);
        if (d != null) {
            return new DeviceProxy(d);
        } else {
            return null;
        }
    }

    /**
	 * @param address
	 * @return RecordID of the device; -1 if the device is not in the storage
	 */
    public int getRecordID(String aBdAddr) {
        return mDeviceStore.getRecordID(aBdAddr);
    }

    public String getBdAddr(int aID) {
        return mDeviceStore.getFromAddrTable(aID);
    }

    private void checkLists(Device aDev) {
        mTraceList.checkDevice(aDev);
        mContactList.checkDevice(aDev);
        mFamiliarList.checkDevice(aDev);
        mRopedList.checkDevice(aDev);
        mInvisibleList.checkDevice(aDev);
        mRecentList.checkDevice(aDev);
        mTrackStationList.checkDevice(aDev);
        mReferencePointList.checkDevice(aDev);
    }

    public AddrList getAddrListRoped() {
        return mRopedList;
    }

    public AddrList getAddrListContact() {
        return mContactList;
    }

    public AddrList getAddrListFamiliar() {
        return mFamiliarList;
    }

    public AddrList getAddrListTrace() {
        return mTraceList;
    }

    public AddrList getAddrListInvisible() {
        return mInvisibleList;
    }

    public AddrList getAddrListRecent() {
        return mRecentList;
    }

    public AddrList getAddrListTrackStations() {
        return mTrackStationList;
    }

    public AddrList getAddrListReferencePoints() {
        return mReferencePointList;
    }

    public AddrList getAddrListLog() {
        return mLogList;
    }

    public void clearEverything() {
        mDeviceStore.clearDeviceList();
        mRefTable = new Hashtable();
        mInMemDevs = new Hashtable();
        mInMemIds = new Hashtable();
        mDeviceStore = new DeviceStore();
        mTraceList.deleteAll();
        mContactList.deleteAll();
        mFamiliarList.deleteAll();
        mRopedList.deleteAll();
        mInvisibleList.deleteAll();
        mRecentList.deleteAll();
        mReferencePointList.deleteAll();
        mLogList.deleteAll();
    }
}
