package com.antlersoft.odb;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Works in concert with ObjectRef and PersistentImpl objects to provide caching,
 * object tracking, and transaction semantics, and all client-visible
 * persistence functionality.   Written to make use of an object that
 * implements the ObjectStore interface.
 */
public class ObjectDB {

    private static ObjectDB current = null;

    private ObjectCache cachedObjects;

    private ObjectStore store;

    private ObjectRef rootObjects;

    private ArrayList dirtyObjects;

    public ObjectDB(ObjectStore objectStore) throws ObjectStoreException {
        store = objectStore;
        cachedObjects = new ObjectCache();
        dirtyObjects = new ArrayList();
        current = this;
        ObjectKey rootKey = (ObjectKey) store.getRootObject();
        if (rootKey == null) {
            rootObjects = new ObjectRef(new PersistentHashtable());
            rootKey = getObjectKey(rootObjects.getReferenced());
            store.updateRootObject(rootKey);
            commit();
        } else {
            rootObjects = new ObjectRef((PersistentHashtable) getObjectByKey(rootKey));
        }
    }

    public static ObjectDB getObjectDB() {
        return current;
    }

    public static void makeDirty(Persistent toDirty) {
        PersistentImpl impl = toDirty._getPersistentImpl();
        if (!impl.isDirty()) {
            ObjectDB db = getObjectDB();
            synchronized (db) {
                impl.makeDirty(toDirty);
                db.dirtyObjects.add(toDirty);
            }
        }
    }

    public void makeCurrent() {
        current = this;
    }

    public synchronized void setPersistent(Object toStore) throws ObjectStoreException {
        PersistentImpl impl = ((Persistent) toStore)._getPersistentImpl();
        if (impl.objectKey == null) {
            impl.objectKey = store.insert((Persistent) toStore);
            assert (impl.objectKey != null);
            if (!impl.isDirty()) {
                impl.makeDirty((Persistent) toStore);
                dirtyObjects.add(toStore);
            }
        }
        cachedObjects.put(impl.objectKey, toStore);
    }

    public static void makePersistent(Object toStore) {
        getObjectDB().setPersistent(toStore);
    }

    protected final synchronized Persistent getObjectByKey(ObjectKey key) throws ObjectStoreException {
        Persistent retVal = (Persistent) cachedObjects.get(key);
        if (retVal == null) {
            retVal = (Persistent) store.retrieve(key);
            PersistentImpl impl = retVal._getPersistentImpl();
            impl.objectKey = key;
            impl.setCached(retVal);
            cachedObjects.put(key, retVal);
        }
        return retVal;
    }

    public Object get(ObjectKey key) {
        return getObjectByKey(key);
    }

    public ObjectKey getObjectKey(Object object) {
        return ((Persistent) object)._getPersistentImpl().objectKey;
    }

    public void deleteObject(Object object) {
        PersistentImpl impl = ((Persistent) object)._getPersistentImpl();
        boolean wasDirty = impl.isDirty();
        impl.markDeleted((Persistent) object);
        if (!wasDirty) {
            synchronized (this) {
                dirtyObjects.add(object);
            }
        }
    }

    public synchronized void makeRootObject(String key, Object object) {
        ((PersistentHashtable) rootObjects.getReferenced()).put(key, object);
    }

    public synchronized Object getRootObject(String key) {
        return ((PersistentHashtable) rootObjects.getReferenced()).get(key);
    }

    public synchronized void uprootObject(String key) {
        ((PersistentHashtable) rootObjects.getReferenced()).remove(key);
    }

    synchronized void commitDirty() {
        try {
            for (Iterator i = dirtyObjects.iterator(); i.hasNext(); ) {
                Persistent toCommit = (Persistent) i.next();
                synchronized (toCommit) {
                    PersistentImpl impl = toCommit._getPersistentImpl();
                    if (impl.isDeleted()) store.delete(impl.objectKey); else {
                        store.update(impl.objectKey, toCommit);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                store.rollback();
            } catch (ObjectStoreException ose) {
                throw new ObjectStoreException("Error cleaning up failed commit", new ObjectStoreException(ose.getMessage(), e));
            }
            throw new ObjectDBException("Commit failed", e);
        }
        store.sync();
    }

    public synchronized void commitAndRetain() {
        commitDirty();
        for (Iterator i = dirtyObjects.iterator(); i.hasNext(); ) {
            PersistentImpl impl = ((Persistent) i.next())._getPersistentImpl();
            impl.fromDirty();
        }
        PersistentImpl impl = ((Persistent) rootObjects.getReferenced())._getPersistentImpl();
        impl.makeObsolete();
        dirtyObjects.clear();
    }

    public synchronized void commit() {
        commitDirty();
        for (Iterator i = cachedObjects.iterator(); i.hasNext(); ) {
            PersistentImpl impl = ((Persistent) i.next())._getPersistentImpl();
            impl.makeObsolete();
        }
        cachedObjects.clear();
        dirtyObjects.clear();
    }

    public synchronized void rollback() throws ObjectStoreException {
        store.rollback();
        cachedObjects.clear();
        dirtyObjects.clear();
    }

    public synchronized void close() throws ObjectStoreException {
        commitDirty();
        store.close();
        cachedObjects = null;
        dirtyObjects = null;
        current = null;
        store = null;
        rootObjects = null;
    }

    /**
     * Return a string representation of the given key.  If the key was not
     * created by the underlying object store, or is for a deleted object,
     * the implementation may throw an exception or return a string that can not
     * be converted back to an ObjectKey.  The implementation will not return
     * a null string.
     * <p>
     * In general, the returned value would be used for communicating with an
     * external system and not persisted in the database.  Persisting an actual
     * ObjectRef will always be more efficient.
     * @param key Key for an object in this database
     * @return String representation of the key
     * @throws ObjectStoreException When string representation could not be created, as if the
     * key is not from the store or is for a deleted object. 
     */
    public String keyToString(ObjectKey key) throws ObjectStoreException {
        return store.keyToString(key);
    }

    /**
     * Return a object key for an object in the database for a string that is the same as a string that would be
     * produced by a call to keyToString for that object.  If the supplied string can not be mapped to key
     * for a valid object, an exception is thrown.
     * @param str
     * @return Key for the object
     * @throws ObjectStoreException
     */
    public ObjectKey stringToKey(String str) throws ObjectStoreException {
        return store.stringToKey(str);
    }
}
