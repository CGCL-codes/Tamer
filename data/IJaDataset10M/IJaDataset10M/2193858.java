package org.personalsmartspace.sre.pem.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import org.knopflerfish.util.Base64;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.sre.pem.api.platform.Control;
import org.personalsmartspace.sre.pem.api.platform.Debug;
import org.personalsmartspace.sre.pem.api.platform.IPersistenceManager;
import org.personalsmartspace.sre.pem.api.platform.Timestamp;
import org.personalsmartspace.sre.pem.api.pss3p.IDependableCollection;
import org.personalsmartspace.sre.pem.api.pss3p.IDependableMap;
import org.personalsmartspace.sre.pem.api.pss3p.IDependableSet;
import org.personalsmartspace.sre.pem.api.pss3p.IDependableTable;
import org.personalsmartspace.sre.pem.api.pss3p.ITimestamp;
import org.personalsmartspace.sre.pem.api.pss3p.PMException;
import org.personalsmartspace.sre.pem.impl.SharedMemory.Data;
import org.personalsmartspace.sre.pem.impl.SharedMemory.SharedMemoryException;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;

final class PersistenceImpl implements org.personalsmartspace.sre.pem.api.pss3p.IPersistence {

    protected static final int PIVOT_UPDATE_TIME = 0;

    protected static final int CREATION_TIME = 1;

    protected static final int LAST_ACCESS_TIME = 2;

    protected static final int LAST_MODIFICATION_TIME = 3;

    private static final String LOCATION_CLASS = "PersistenceImpl::";

    private static final int DEFAULT_WAIT = 100;

    private static String keyPattern = "(/\\p{Alnum}+)+|/";

    private SharedMemory data;

    private String myEndpoint;

    private PSSLog log;

    private IPersistenceManager localPersistenceManager;

    public PersistenceImpl(String ep, IPersistenceManager pm) {
        myEndpoint = ep;
        localPersistenceManager = pm;
        data = (SharedMemory) pm.getSharedMemory();
        log = new PSSLog(this);
    }

    public void reset() {
        debugDebug(LOCATION_CLASS + "reset()", "");
    }

    public void unset() {
        debugDebug(LOCATION_CLASS + "unset()", "");
    }

    public boolean isUnstable() {
        debugFinest(LOCATION_CLASS + "isUnstable()", "");
        return localPersistenceManager.isUnstable();
    }

    public boolean wasUnstable(Object asker) throws PMException {
        debugFinest(LOCATION_CLASS + "wasUnstable()", "asker = [" + asker.getClass() + "," + asker.hashCode() + "]");
        return localPersistenceManager.wasUnstable(asker);
    }

    public String[] listAvailableKeys() throws PMException {
        return listAvailableKeys(true);
    }

    public String[] listAvailableKeys(boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "listAvailableKeys()", blocking ? "blocking" : "non-blocking");
        if (!blocking && localPersistenceManager.isUnstable()) return null;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        Set<String> keys;
        try {
            keys = data.keySet();
        } catch (SharedMemoryException e) {
            throw new PMException(e);
        }
        debugFinest(LOCATION_CLASS + "listAvailableKeys()", "" + keys);
        List<String> rkeys = new ArrayList<String>(keys);
        Collections.sort(rkeys);
        debugFinest(LOCATION_CLASS + "listAvailableKeys()", "" + rkeys);
        return rkeys.toArray(new String[0]);
    }

    public String[] listKeysAvailableUnder(String key) throws PMException {
        return listKeysAvailableUnder(key, true);
    }

    public String[] listKeysAvailableUnder(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "1");
        if (!blocking && localPersistenceManager.isUnstable()) return null;
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "2");
        while (localPersistenceManager.isUnstable()) try {
            debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "3");
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (Exception e) {
            debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "4");
            throw new PMException(e);
        }
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "5");
        Set<String> keys;
        try {
            keys = data.keySet();
            debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "6");
        } catch (SharedMemoryException e) {
            throw new PMException(e);
        }
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "7");
        debugFinest(LOCATION_CLASS + "listKeysAvailableUnder()", "" + keys);
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "8");
        final String pattern = (key.endsWith("/") ? key.substring(0, key.length() - 1) : key) + "(/\\p{Alnum}+)+";
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "9");
        List<String> rkeys = new ArrayList<String>();
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "10");
        for (String k : keys) if (k.matches(pattern)) rkeys.add(k);
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "11");
        debugFinest(LOCATION_CLASS + "listKeysAvailableUnder()", "" + rkeys);
        debugDebug(LOCATION_CLASS + "listKeysAvailableUnder()", "12");
        Collections.sort(rkeys);
        return rkeys.toArray(new String[] {});
    }

    public int setPeriodicCommit(String key, int rate) throws PMException {
        return setPeriodicCommit(key, rate, true);
    }

    public int setPeriodicCommit(String key, int rate, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "setPeriodicCommit()", (blocking ? "blocking" : "non-blocking") + ", key = " + key + ", rate = " + rate);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return -1;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        return localPersistenceManager.setPeriodicCommit(key, rate);
    }

    public int getPeriodicCommit(String key) throws PMException {
        return getPeriodicCommit(key, true);
    }

    public int getPeriodicCommit(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "getPeriodicCommit()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return -1;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        return localPersistenceManager.getPeriodicCommit(key);
    }

    public void unsetPeriodicCommit(String key) throws PMException {
        unsetPeriodicCommit(key, true);
    }

    public boolean unsetPeriodicCommit(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "unsetPeriodicCommit()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        localPersistenceManager.unsetPeriodicCommit(key);
        return true;
    }

    private String[] getKeysUnder(String key) throws Exception {
        final String pattern = (key.endsWith("/") ? key.substring(0, key.length() - 1) : key) + "(/\\p{Alnum}+)*";
        Set<String> keys = data.keySet();
        Set<String> rkeys = new HashSet<String>();
        for (String k : keys) if (k.matches(pattern)) rkeys.add(k);
        return rkeys.toArray(new String[] {});
    }

    public void setPivot(String key) throws PMException {
        setPivot(key, true);
    }

    public boolean setPivot(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "setPivot()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        debugFinest(LOCATION_CLASS + "setPivot()", "key = " + key);
        String[] keysToPivotize;
        try {
            keysToPivotize = getKeysUnder(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        for (String k : keysToPivotize) try {
            data.acquire(k);
            Data d = data.get(k);
            if (d == null) {
                debugFinest(LOCATION_CLASS + "setPivot()", "No such data with key " + k);
                throw new PMException("No such data with key " + k);
            }
            debugFinest(LOCATION_CLASS + "setPivot()", "Previous pivot = " + d.pivot);
            d.pivot = myEndpoint.toString();
            debugFinest(LOCATION_CLASS + "setPivot()", "New pivot = " + d.pivot);
            d.lastPivotUpdateTime = new Timestamp();
            debugFinest(LOCATION_CLASS + "setPivot()", "Last pivot update time = " + d.lastPivotUpdateTime);
            data.put(k, d);
            data.release(k);
        } catch (Exception e) {
            throw new PMException(e);
        }
        return true;
    }

    public ITimestamp getLastModificationTime(String key) throws PMException {
        return getLastModificationTime(key, true);
    }

    public ITimestamp getLastAccessTime(String key) throws PMException {
        return getLastAccessTime(key, true);
    }

    public ITimestamp getCreationTime(String key) throws PMException {
        return getCreationTime(key, true);
    }

    public ITimestamp getLastPivotUpdateTime(String key) throws PMException {
        return getLastPivotUpdateTime(key, true);
    }

    public ITimestamp getLastModificationTime(String key, boolean blocking) throws PMException {
        return getTimeInternal(key, LAST_MODIFICATION_TIME, blocking);
    }

    public ITimestamp getLastAccessTime(String key, boolean blocking) throws PMException {
        return getTimeInternal(key, LAST_ACCESS_TIME, blocking);
    }

    public ITimestamp getCreationTime(String key, boolean blocking) throws PMException {
        return getTimeInternal(key, CREATION_TIME, blocking);
    }

    public ITimestamp getLastPivotUpdateTime(String key, boolean blocking) throws PMException {
        return getTimeInternal(key, PIVOT_UPDATE_TIME, blocking);
    }

    private ITimestamp getTimeInternal(String key, int kind, boolean blocking) throws PMException {
        String dkind = "UNKNOWN_KIND_TIME";
        switch(kind) {
            case PIVOT_UPDATE_TIME:
                dkind = "PIVOT_UPDATE_TIME";
                break;
            case CREATION_TIME:
                dkind = "CREATION_TIME";
                break;
            case LAST_ACCESS_TIME:
                dkind = "LAST_ACCESS_TIME";
                break;
            case LAST_MODIFICATION_TIME:
                dkind = "LAST_MODIFICATION_TIME";
                break;
        }
        debugDebug(LOCATION_CLASS + "getTimeInternal()", (blocking ? "blocking" : "non-blocking") + ", kind = " + dkind + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return null;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        Data d;
        try {
            data.acquire(key);
            d = data.get(key);
            data.release(key);
        } catch (SharedMemoryException e) {
            throw new PMException(e);
        }
        if (d == null) {
            debugFinest(LOCATION_CLASS + "getTimeInternal()", "No such data with key " + key);
            throw new PMException("No such data with key " + key);
        }
        Timestamp time = null;
        switch(kind) {
            case PIVOT_UPDATE_TIME:
                time = d.lastPivotUpdateTime;
                break;
            case CREATION_TIME:
                time = d.creationTime;
                break;
            case LAST_ACCESS_TIME:
                time = d.lastAccessTime;
                break;
            case LAST_MODIFICATION_TIME:
                time = d.lastModificationTime;
                break;
            default:
                throw new PMException("Persistence internal error: no such kind of time of data!");
        }
        debugFinest(LOCATION_CLASS + "getTimeInternal()", "dkind = " + time);
        return time;
    }

    public <T> void store(String key, T t) throws PMException {
        store(key, t, true);
    }

    public <T> void update(String key, T t, boolean pivot) throws PMException {
        update(key, t, pivot, true);
    }

    public <T> boolean store(String key, T t, boolean blocking) throws PMException {
        return update(key, t, true, blocking);
    }

    public <T> boolean update(String key, T t, boolean pivot, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "update()", (blocking ? "blocking" : "non-blocking") + ", key = " + key + ", data = " + t + ", pivot = " + pivot);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        ObjectOutputStream renderDataStream;
        String base64Data;
        try {
            renderDataStream = new ObjectOutputStream(dataStream);
            renderDataStream.writeObject(t);
            renderDataStream.flush();
            base64Data = Base64.encode(dataStream.toByteArray(), 0);
            renderDataStream.close();
        } catch (IOException e) {
            throw new PMException(e);
        }
        Data d;
        try {
            debugDebug(LOCATION_CLASS + "update()", "acquiring");
            data.acquire(key);
            d = data.get(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        Timestamp now = new Timestamp();
        if (d == null) {
            debugDebug(LOCATION_CLASS + "update()", "d = null");
            d = data.new Data(null, null, null);
            pivot = true;
            d.creationTime = now;
            debugFinest(LOCATION_CLASS + "update()", "key = " + key + ", CREATION_TIME = " + d.creationTime);
        }
        if (pivot) {
            d.pivot = myEndpoint.toString();
            d.lastPivotUpdateTime = now;
            debugFinest(LOCATION_CLASS + "update()", "key = " + key + ", PIVOT_UPDATE_TIME = " + d.lastPivotUpdateTime);
        }
        d.type = t.getClass().getName();
        d.data = base64Data;
        d.lastModificationTime = now;
        debugFinest(LOCATION_CLASS + "update()", "key = " + key + ", LAST_MODIFICATION_TIME = " + d.lastModificationTime);
        try {
            debugDebug(LOCATION_CLASS + "update()", "releasing");
            data.put(key, d);
            data.release(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        debugDebug(LOCATION_CLASS + "update()", "end");
        return true;
    }

    public Object retrieve(String key) throws PMException {
        return retrieve(key, true);
    }

    public <T> T retrieve(String key, final T t) throws PMException {
        return retrieve(key, t, true);
    }

    @SuppressWarnings("unchecked")
    public <T> T retrieve(String key, final T t, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "retrieve()", (blocking ? "blocking" : "non-blocking") + ", key = " + key + ", data = " + t);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return null;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        Data d;
        try {
            debugDebug(LOCATION_CLASS + "retrieve()", "acquiring");
            data.acquire(key);
            d = data.get(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        if (d == null) {
            debugFinest(LOCATION_CLASS + "retrieve()", "No such data with key " + key);
            throw new PMException("No such data with key = " + key);
        }
        try {
            if (d.isvoid) {
                data.release(key);
                return null;
            }
            final PersistenceImpl me = this;
            InputStream dataStream = new ByteArrayInputStream(Base64.decode(d.data));
            ObjectInputStream readDataStream = new ObjectInputStream(dataStream) {

                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(name, false, t.getClass().getClassLoader());
                    } catch (ClassNotFoundException e) {
                        clazz = Class.forName(name, false, me.getClass().getClassLoader());
                    }
                    return clazz;
                }
            };
            d.lastAccessTime = new Timestamp();
            debugFinest(LOCATION_CLASS + "retrieve()", "key = " + key + ", LAST_ACCESS_TIME = " + d.lastAccessTime);
            debugDebug(LOCATION_CLASS + "retrieve()", "releasing");
            data.put(key, d);
            data.release(key);
            T obj = (T) readDataStream.readObject();
            readDataStream.close();
            debugDebug(LOCATION_CLASS + "retrieve()", "key = " + key + ", data = " + obj);
            debugDebug(LOCATION_CLASS + "retrieve(), end", "return " + obj);
            return obj;
        } catch (Exception e) {
            throw new PMException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Object retrieve(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "retrieve()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return null;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        Data d;
        try {
            debugDebug(LOCATION_CLASS + "retrieve()", "acquiring");
            data.acquire(key);
            d = data.get(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        if (d == null) {
            debugFinest(LOCATION_CLASS + "retrieve()", "No such data with key " + key);
            throw new PMException("No such data with key = " + key);
        }
        try {
            if (d.isvoid) {
                data.release(key);
                return null;
            }
            InputStream dataStream = new ByteArrayInputStream(Base64.decode(d.data));
            ObjectInputStream readDataStream = new ObjectInputStream(dataStream) {

                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    return loadClass(name);
                }
            };
            d.lastAccessTime = new Timestamp();
            debugFinest(LOCATION_CLASS + "retrieve()", "key = " + key + ", LAST_ACCESS_TIME = " + d.lastAccessTime);
            debugDebug(LOCATION_CLASS + "retrieve()", "releasing");
            data.put(key, d);
            data.release(key);
            Object obj = readDataStream.readObject();
            readDataStream.close();
            debugDebug(LOCATION_CLASS + "retrieve()", "key = " + key + ", data = " + obj);
            debugDebug(LOCATION_CLASS + "retrieve(), end", "return " + obj);
            return obj;
        } catch (Exception e) {
            throw new PMException(e);
        }
    }

    public void remove(String key) throws PMException {
        remove(key, true);
    }

    public boolean remove(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "remove()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        try {
            localPersistenceManager.removePersistedDataFile(key, "false");
            String[] keysToRemove = getKeysUnder(key);
            for (String k : keysToRemove) {
                data.acquire(key);
                data.remove(k);
                data.release(key);
            }
        } catch (Exception e) {
            throw new PMException(e);
        }
        return true;
    }

    public void commit(String key) throws PMException {
        commit(key, true);
    }

    public boolean commit(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "commit()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (InterruptedException e) {
            throw new PMException(e);
        }
        try {
            localPersistenceManager.commit(key, "false", "", new ICallbackListener() {

                public void handleCallbackObject(Object returnObject) {
                }

                public void handleCallbackString(String returnObjectAsXML) {
                }

                public void handleErrorMessage(String errorMessage) {
                }
            });
        } catch (Exception e) {
            throw new PMException(e);
        }
        return true;
    }

    public void rollback(String key) throws PMException {
        rollback(key, true);
    }

    public boolean rollback(String key, boolean blocking) throws PMException {
        debugDebug(LOCATION_CLASS + "rollback()", (blocking ? "blocking" : "non-blocking") + ", key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        if (!blocking && localPersistenceManager.isUnstable()) return false;
        while (localPersistenceManager.isUnstable()) try {
            synchronized (this) {
                wait(DEFAULT_WAIT);
            }
        } catch (Exception e) {
            throw new PMException(e);
        }
        try {
            localPersistenceManager.rollback(key);
        } catch (Exception e) {
            throw new PMException(e);
        }
        return true;
    }

    private static final String typePattern = "\\[*(\\p{Alpha}\\p{Alnum}*?\\.)*?\\p{Alpha}\\p{Alnum}*?\\;*?(\\<(\\[*(\\p{Alpha}\\p{Alnum}*?\\.)*?\\p{Alpha}\\p{Alnum}*?\\;*?(\\<(\\[*(\\p{Alpha}\\p{Alnum}*?\\.)*?\\p{Alpha}\\p{Alnum}*?\\;*?(\\<(\\[*(\\p{Alpha}\\p{Alnum}*?\\.)*?\\p{Alpha}\\p{Alnum}*?\\;*?(\\<(\\[*(\\p{Alpha}\\p{Alnum}*?\\.)*?\\p{Alpha}\\p{Alnum}*?\\;*?(\\<(.*\\,?)+?\\>)??\\,?)+?\\>)??\\,?)+?\\>)??\\,?)+?\\>)??\\,?)+?\\>)??";

    public boolean isCollection(String key) throws PMException {
        debugDebug(LOCATION_CLASS + "isCollection()", "key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        String type;
        try {
            type = retrieve(key + "/type", "");
        } catch (Throwable t) {
            return false;
        }
        if (type == null) return false;
        if (!type.matches(typePattern)) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    public IDependableCollection<?> getDependableCollection(String key) throws PMException {
        debugDebug(LOCATION_CLASS + "getDependableCollection()", "key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        String type = retrieve(key + "/type", "");
        if (type == null) throw new PMException("No collection defined on the specified location");
        if (!type.matches(typePattern)) throw new PMException("Malformed type of the collection");
        String basicType = type.substring(0, type.indexOf("<"));
        try {
            if (DependableSet.class.getName().equals(basicType)) {
                String genericType = "";
                try {
                    genericType = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
                } catch (IndexOutOfBoundsException e) {
                    throw new PMException("Malformed generic type of the collection");
                }
                if (!genericType.matches(typePattern)) throw new PMException("Malformed generic type of the collection");
                Class clazz = loadClass(genericType);
                Object sample;
                if (clazz.isArray()) {
                    sample = Array.newInstance(clazz.getComponentType(), 0);
                } else {
                    sample = clazz.newInstance();
                }
                return new DependableSet(type, key + "/data", sample);
            } else if (DependableMap.class.getName().equals(basicType)) {
                String genericTypeK = "", genericTypeV = "";
                try {
                    genericTypeK = type.substring(type.indexOf("<") + 1, type.indexOf(","));
                    genericTypeV = type.substring(type.indexOf(",") + 1, type.indexOf(">"));
                } catch (IndexOutOfBoundsException e) {
                    throw new PMException("Malformed generic type of the collection");
                }
                if (!genericTypeK.matches(typePattern) || !genericTypeV.matches(typePattern)) throw new PMException("Malformed generic type of the collection");
                Class clazzK = loadClass(genericTypeK);
                Class clazzV = loadClass(genericTypeV);
                Object sampleK, sampleV;
                if (clazzK.isArray()) {
                    sampleK = Array.newInstance(clazzK.getComponentType(), 0);
                } else {
                    sampleK = clazzK.newInstance();
                }
                if (clazzV.isArray()) {
                    sampleV = Array.newInstance(clazzV.getComponentType(), 0);
                } else {
                    sampleV = clazzV.newInstance();
                }
                return new DependableMap(type, key + "/data", sampleK, sampleV);
            } else if (DependableTable.class.getName().equals(basicType)) {
                String genericType = "";
                try {
                    genericType = type.substring(type.indexOf("<") + 1, type.indexOf(">"));
                } catch (IndexOutOfBoundsException e) {
                    throw new PMException("Malformed generic type of the collection");
                }
                if (!genericType.matches(typePattern)) throw new PMException("Malformed generic type of the collection");
                Class clazz = loadClass(genericType);
                Object sample;
                if (clazz.isArray()) {
                    sample = Array.newInstance(clazz.getComponentType(), 0);
                } else {
                    sample = clazz.newInstance();
                }
                return new DependableTable(type, key + "/data", sample);
            } else {
                throw new PMException("Unknown or malformed type of the collection");
            }
        } catch (ClassNotFoundException cnfe) {
            throw new PMException(cnfe);
        } catch (IllegalAccessException iae) {
            throw new PMException(iae);
        } catch (InstantiationException ie) {
            throw new PMException(ie);
        }
    }

    @SuppressWarnings("unchecked")
    private Class loadClass(String clazz) throws ClassNotFoundException {
        BundleContext bc = localPersistenceManager.getMyBundle().getBundleContext();
        Bundle[] bundles = bc.getBundles();
        Class result = null;
        for (int i = 0; i < bundles.length; i++) try {
            result = bundles[i].loadClass(clazz);
        } catch (ClassNotFoundException cnfe) {
            result = null;
            continue;
        } catch (IllegalStateException ise) {
            result = null;
            continue;
        } catch (Throwable t) {
            throw new ClassNotFoundException("Cause:" + t.toString());
        }
        if (result == null) {
            result = Class.forName(clazz);
            if (result == null) throw new ClassNotFoundException("No class with name " + clazz + "found in existing bundles");
        }
        return result;
    }

    public <K extends Serializable, V extends Serializable> IDependableMap<K, V> getDependableMap(String key, K sampleK, V sampleV) throws PMException {
        debugDebug(LOCATION_CLASS + "getDependableMap()", "key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        String desiredType = DependableMap.class.getName() + "<" + sampleK.getClass().getName() + "," + sampleV.getClass().getName() + ">";
        String alreadyDefinedType = retrieve(key + "/type", "");
        if (alreadyDefinedType != null) if (!alreadyDefinedType.equals(desiredType)) throw new PMException("Request for a collection of type " + desiredType + " on an already existing collection of type " + alreadyDefinedType + " under " + key);
        if (alreadyDefinedType == null) {
            store(key + "/type", desiredType);
            commit(key + "/type");
        }
        return new DependableMap<K, V>(desiredType, key + "/data", sampleK, sampleV);
    }

    public <E extends Serializable> IDependableSet<E> getDependableSet(String key, E sample) throws PMException {
        debugDebug(LOCATION_CLASS + "getDependableSet()", "key = " + key);
        debugDebug(LOCATION_CLASS + "getDependableSet()", "1");
        if (key == null) throw new PMException("Invalid key argument " + key);
        debugDebug(LOCATION_CLASS + "getDependableSet()", "2");
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        debugDebug(LOCATION_CLASS + "getDependableSet()", "3");
        String desiredType = DependableSet.class.getName() + "<" + sample.getClass().getName() + ">";
        String alreadyDefinedType = retrieve(key + "/type", "");
        if (alreadyDefinedType != null) if (!alreadyDefinedType.equals(desiredType)) throw new PMException("Request for a collection of type " + desiredType + " on an already existing collection of type " + alreadyDefinedType + " under " + key);
        if (alreadyDefinedType == null) {
            store(key + "/type", desiredType);
            commit(key + "/type");
        }
        return new DependableSet<E>(desiredType, key + "/data", sample);
    }

    public <V extends Serializable> IDependableTable<V> getDependableTable(String key, V sample) throws PMException {
        debugDebug(LOCATION_CLASS + "getDependableTable()", "key = " + key);
        if (key == null) throw new PMException("Invalid key argument " + key);
        if (!key.matches(keyPattern)) throw new PMException("Invalid form of key argument");
        String desiredType = DependableTable.class.getName() + "<" + sample.getClass().getName() + ">";
        String alreadyDefinedType = retrieve(key + "/type", "");
        if (alreadyDefinedType != null) if (!alreadyDefinedType.equals(desiredType)) throw new PMException("Request for a collection of type " + desiredType + " on an already existing collection of type " + alreadyDefinedType + " under " + key);
        if (alreadyDefinedType == null) {
            store(key + "/type", desiredType);
            commit(key + "/type");
        }
        return new DependableTable<V>(desiredType, key + "/data", sample);
    }

    private final void complain(Throwable t) {
        try {
            log.error("Error in Persistence - ", t);
            Control.get().alert(t);
        } catch (Exception e) {
            log.error("Complaining failed due to " + e);
            log.error("Details of the complain : " + t);
        }
    }

    private void debugDebug(String loc, String mess) {
        try {
            Debug.debug(loc, mess);
        } catch (Exception e) {
            complain(e);
        }
    }

    private void debugFinest(String loc, String mess) {
        try {
            Debug.finest(loc, mess);
        } catch (Exception e) {
            complain(e);
        }
    }
}

class PersistenceClassLoader extends ClassLoader {

    public PersistenceClassLoader(ClassLoader cl) {
        super();
    }
}
