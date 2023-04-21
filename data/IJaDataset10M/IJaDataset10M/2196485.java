package goodsjpi;

/**
 * <code>BasicMetaobject</code> implements all the transaction logic. <br>
 * Specifically pre/postDaemon are implemented to call begin/end read/write Access
 * and call commit + abort Transaction. <br>
 * Locking is not implemented here, but in the subclasses. The functions begin/end
 * read/write Access serve as hooks to do so.
 *
 * @author  K.A. Knizhnik
 * @version 1.0
 */
public abstract class BasicMetaobject extends Metaobject {

    /**
   * Invocation of <code>preDaemon</code>  is inserted by MOP geneator before each
   * method invocation or object component access. See MetaObject <br>
   * This "basic" implementation increases the accesscount of the object and if 
   * the flag indicates change, calls begin read/write Access. <br>
   * Also it load, or prepares the object by calling these functions on the 
   * objects store
   *
   * @param o an <code>Object</code> value that is modified
   * @param attr an <code>int</code> flag, saying what kind of modufucation
   */
    public void preDaemon(Object o, int attr) {
        Persistent obj = (Persistent) o;
        CacheManager mng = CacheManager.getCacheManager();
        mng.enter();
        synchronized (obj) {
            if (obj.accessCount == 0) {
                synchronized (this) {
                    obj.accessCount = 1;
                }
                synchronized (mng.globalCS) {
                    mng.nestedTransactionCount += 1;
                    if (obj.opid != 0) {
                        mng.removeFromCache(obj);
                    }
                }
                if (obj.opid != 0) {
                    if ((attr & Metaobject.MUTATOR) != 0) {
                        beginWriteAccess(obj);
                    } else {
                        beginReadAccess(obj);
                    }
                    if (obj.invalidated) {
                        synchronized (this) {
                            obj.invalidated = false;
                            destroyObject(obj);
                        }
                    }
                    boolean ok = false;
                    try {
                        if ((obj.state & Persistent.DESTRUCTED) != 0) {
                            obj.storage.load(obj.opid, obj);
                        } else if ((obj.state & Persistent.RAW) != 0) {
                            obj.storage.prepare(obj);
                        }
                        ok = true;
                    } finally {
                        if (!ok) {
                            postDaemon(obj, Metaobject.EXCEPTION, false);
                        }
                    }
                }
            } else {
                obj.accessCount += 1;
            }
        }
    }

    /**
   * Invocation of <code>postDaemon</code>  is inserted by MOP geneator after each
   * method invocation or object component access. See also MetaObject <br>
   * This "basic" implementation decreases the accesscount of the object and 
   * if that hits 0 calls endAccess(obj). In that case, the object is removed from 
   * the  cache  manager and detroyObject is called. If that was the end of
   * nested Trasaction, either commitTransaction, or abortTransaction, 
   * depending on if the flag indicates Exception is called.
   *
   * @param o an <code>Object</code>, or really a Persistent, that has been changed
   * @param attr an <code>int</code>, indicating where/how the change happened. An OR
   * combination of the static flags below
   * @param modified a <code>boolean</code> value, indicating whether there has 
   * actually been a change. Because of exceptions it isn't always true.
   */
    public void postDaemon(Object o, int attr, boolean modified) {
        Persistent obj = (Persistent) o;
        CacheManager mng = CacheManager.getCacheManager();
        synchronized (obj) {
            if (modified) {
                obj.state |= Persistent.DIRTY;
            }
            if (obj.accessCount == 1) {
                if (obj.opid != 0) {
                    endAccess(obj);
                }
                synchronized (mng.globalCS) {
                    if (obj.opid != 0) {
                        if ((obj.state & (Persistent.NOTIFY | Persistent.RAW | Persistent.DESTRUCTED | Persistent.TRANSREAD | Persistent.TRANSWRITE)) == 0) {
                            mng.insertInCache(obj);
                        }
                    }
                    if (--mng.nestedTransactionCount == 0) {
                        if ((attr & Metaobject.EXCEPTION) != 0) {
                            abortTransaction();
                        } else {
                            commitTransaction();
                        }
                    }
                }
                synchronized (this) {
                    obj.accessCount = 0;
                    if (obj.invalidated) {
                        obj.invalidated = false;
                        synchronized (mng) {
                            mng.removeFromCache(obj);
                            destroyObject(obj);
                        }
                    }
                }
            } else {
                obj.accessCount -= 1;
            }
        }
        mng.leave();
    }

    /**
   * Assign persistent identifier to the transient object of persistent
   * capable class. The object is placed in the same storage as 
   * persistent object containing reference to this object unless
   * explicit storage attachment was specified for this object.
   *
   * @param obj a <code>Persistent</code> value that is made persistent
   * @param parent a <code>Persistent</code> into whose storage obj is placed, if 
   *      obj is new
   */
    protected void makePersistent(Persistent obj, Persistent parent) {
        if (obj.storage == null) {
            obj.storage = parent.storage;
        } else {
            Assert.that(obj.storage.database == parent.storage.database);
        }
        obj.storage.allocate(obj, 0);
        CacheManager mng = CacheManager.getCacheManager();
        mng.linkAfter(parent, obj);
        obj.state |= Persistent.TRANSWRITE | Persistent.DIRTY;
    }

    /**  
   * Object thrown by GC from weak cache are linked into forgottenObjectList
   * list and notifications are send to servers at the transaction
   * commit time. This is done because clearing weak reference by GC 
   * is not synchronized with invocation of finalize() method. So there is
   * no warrenty that in the moment between object is removed from weak cache
   * and ionvocation of finilize method, the object with the same identifier
   * will not be retirieved from the server. Delayed strategy of sending
   * forget message together with checking object for presence in the cache 
   * before sending foget message to the server makes sending of wrong
   * forgot message not possible. <br>
   * OId's of Object that were "forgotten" are collected and handed to 
   * Storage.forgetObjects
   *
   * @param mng a <code>CacheManager</code> value
   */
    protected static void sendForgottenObjectsToServers(CacheManager mng) {
        ForgottenObject list;
        synchronized (mng) {
            list = mng.forgottenObjectList;
            mng.forgottenObjectList = null;
        }
        while (list != null) {
            Storage store = list.storage;
            int n = 0;
            for (ForgottenObject obj = list, prev = null; obj != null; obj = obj.next) {
                if (obj.storage == store) {
                    Object o = store.objectCache.get(obj.opid);
                    if (o == obj) {
                        store.objectCache.remove(obj.opid);
                        n += 1;
                    } else if (o == null) {
                        n += 1;
                    } else {
                        if (prev == null) {
                            list = obj.next;
                        } else {
                            prev.next = obj.next;
                        }
                        continue;
                    }
                }
                prev = obj;
            }
            if (n > 0) {
                int opids[] = new int[n];
                for (ForgottenObject obj = list, prev = null; obj != null; obj = obj.next) {
                    if (obj.storage == store) {
                        opids[--n] = obj.opid;
                        if (prev == null) {
                            list = obj.next;
                        } else {
                            prev.next = obj.next;
                        }
                        continue;
                    }
                    prev = obj;
                }
                store.forgetObjects(opids);
            }
        }
    }

    /**
   * At this moment there are no more active methods for 
   * classes controlled by this metaobject (or metaobjects derived from it).
   * This method will try to commit all transactions in all opened databases.
   * Until this method is completed no other invocation or access to
   * objects controled by this MOP is possible (it is forced by declaring
   * this static method "synchronized" as well as static method fixObject(),
   * which is called before any access to the object). <br>
   *
   * First sendForgottenObject is called. Then, for all modifed objects (all
   * others are release by metaobject.releaseObject()) we find it's database
   * and collect all object involved in the trasaction. If any is invalidated, 
   * the transaction is aborted. <br>
   *
   * Each objects storage store(obj) function is called, which packs the byte
   * into the tranaction buffer. Then the storage's commitTransaction is called,
   * and if the trasaction spans several storages, those storages 
   * commitSubtransaction are called. This send the bytes to the server, and if
   * anything goes wrong, abortTransaction is called. <br>
   *
   * If all went well, obj.metaObject.commitchanegs is called for every object
   */
    protected static void commitTransaction() {
        Persistent next;
        CacheManager mng = CacheManager.getCacheManager();
        sendForgottenObjectsToServers(mng);
        Persistent transObjects = mng.transObjects;
        loop: while (transObjects.next != transObjects) {
            Persistent obj = transObjects.next;
            if ((obj.state & Persistent.DIRTY) == 0) {
                do {
                    next = obj.next;
                    obj.metaobject.releaseObject(obj);
                } while ((obj = next) != transObjects);
                break;
            }
            int nTransServers = 0;
            Storage coordinator = obj.storage;
            Database db = coordinator.database;
            Storage[] servers = new Storage[db.storages.length];
            do {
                Storage storage = obj.storage;
                if (storage.database == db) {
                    if (obj.invalidated) {
                        abortTransaction(mng, db);
                        continue loop;
                    }
                    if (!storage.isInvolvedInTransaction()) {
                        if (storage.id < coordinator.id) {
                            coordinator = storage;
                        }
                        servers[nTransServers++] = storage;
                    }
                    storage.store(obj);
                }
                obj = obj.next;
            } while (obj != transObjects);
            if (nTransServers != 0) {
                int tid = coordinator.commitTransaction(nTransServers, servers);
                if (tid == -1) {
                    abortTransaction(mng, db);
                    continue;
                }
                if (nTransServers > 1) {
                    for (int i = 0; i < nTransServers; i++) {
                        if (servers[i] != coordinator) {
                            servers[i].commitSubtransaction(coordinator, nTransServers, servers, tid);
                        }
                    }
                    for (int i = 0; i < nTransServers; i++) {
                        if (!servers[i].waitTransactionCompletion()) {
                            abortTransaction(mng, db);
                            continue loop;
                        }
                    }
                }
            }
            for (obj = transObjects.next; obj != transObjects; obj = next) {
                next = obj.next;
                if (obj.storage.database == db) {
                    obj.metaobject.commitObjectChanges(obj);
                }
            }
        }
    }

    /**
   *  <code>abortTransaction</code> obviously aborts the Transaction in progress.
   * This is done by calling undoObjectChanges on each Objects metaobject, and 
   * calling abortTransaction on the DB.
   *
   * @param mng a <code>CacheManager</code> that provides the list of objects
   *   that participate in the Transaction. (mng.transObjects)
   * @param db a <code>Database</code>, in which all participating Objects must be
   */
    protected static void abortTransaction(CacheManager mng, Database db) {
        Persistent obj, next;
        Persistent transObjects = mng.transObjects;
        for (obj = transObjects.next; obj != transObjects; obj = next) {
            next = obj.next;
            if (obj.storage.database == db) {
                obj.metaobject.undoObjectChanges(obj);
            }
        }
        db.abortTransaction();
    }

    /**
   *  <code>abortTransaction</code> aborts for the static 
   * CacheManager.getCacheManger (per thread) and the database that the objects
   * in that are in. calls 
   *
   */
    protected static void abortTransaction() {
        CacheManager mng = CacheManager.getCacheManager();
        Persistent transObjects = mng.transObjects;
        while (transObjects.next != transObjects) {
            abortTransaction(mng, transObjects.next.storage.database);
        }
    }

    /**
   *  <code>commitObjectChanges</code> is called during commitTransaction. The 
   * object is removed from the transaction list of the cache manager. If the obj
   * is invalidated, it's destroyed (this.destroyObject()).
   *
   * @param obj a <code>Persistent</code> object to commit
   */
    protected synchronized void commitObjectChanges(Persistent obj) {
        CacheManager mng = CacheManager.getCacheManager();
        Assert.that((obj.state & (Persistent.TRANSREAD | Persistent.TRANSWRITE)) != 0);
        CacheManager.unlink(obj);
        obj.state &= ~(Persistent.TRANSREAD | Persistent.TRANSWRITE | Persistent.XLOCKED | Persistent.SLOCKED | Persistent.DIRTY | Persistent.NEW);
        if (obj.invalidated) {
            obj.invalidated = false;
            destroyObject(obj);
        } else if ((obj.state & Persistent.NOTIFY | Persistent.RAW | Persistent.DESTRUCTED) == 0) {
            mng.insertInCache(obj);
        }
    }

    /**
   * <code>undoObjectChanges</code> is called during an abortTransaction. <br>
   * The object is removed  from the cache managers Trasaction list and 
   * if locked, unlocked. If the object has been modified (dirty), new objects 
   * are destroyed and old removed from their storage (storage.forgetObject())
   *
   * @param obj a <code>Persistent</code> value
   */
    protected synchronized void undoObjectChanges(Persistent obj) {
        CacheManager mng = CacheManager.getCacheManager();
        Assert.that((obj.state & (Persistent.TRANSREAD | Persistent.TRANSWRITE)) != 0);
        CacheManager.unlink(obj);
        if ((obj.state & (Persistent.SLOCKED | Persistent.XLOCKED)) != 0) {
            obj.storage.unlock(obj, Protocol.lck_none);
        }
        if ((obj.state & Persistent.DIRTY) != 0) {
            if ((obj.state & Persistent.NEW) == 0) {
                destroyObject(obj);
            } else {
                obj.storage.forgetObject(obj);
                obj.opid = 0;
                obj.storage = null;
            }
        } else if ((obj.state & (Persistent.NOTIFY | Persistent.RAW | Persistent.DESTRUCTED)) == 0) {
            mng.insertInCache(obj);
        }
        obj.state &= ~(Persistent.TRANSREAD | Persistent.TRANSWRITE | Persistent.XLOCKED | Persistent.SLOCKED | Persistent.DIRTY | Persistent.NEW);
    }

    /**
   *  <code>preloadObject</code> is called by Storage.read()  when it's bytes
   * have been received and just before they are copied into it's data member <br>
   * The function adjusts the cache Managers cacheSize appropriately
   *
   * @param obj a <code>Persistent</code> that has been read (from the net)
   * @param size an <code>int</code>, how many bytes the data member will hold
   */
    protected void preloadObject(Persistent obj, int size) {
        CacheManager mng = CacheManager.getCacheManager();
        synchronized (mng) {
            if (obj.data != null) {
                mng.cacheSize0 -= obj.data.length;
            } else {
                mng.cacheSize0 += CacheManager.objectAllocationOverhead + obj.desc.fixedSize;
            }
            mng.cacheSize0 += size;
        }
    }

    /**
   *  <code>loadObject</code> is not so much the loading of the Object, ,but a 
   * notification thereof. The object is really loaded by the Storage class  
   * (in prepare(obj) method) and after the deed is done, this method is called. 
   * <br> This only increases the cache size appropriately
   *
   * @param obj a <code>Persistent</code> object that has been loaded
   */
    protected void loadObject(Persistent obj) {
        CacheManager mng = CacheManager.getCacheManager();
        synchronized (mng) {
            mng.cacheSize0 -= CacheManager.objectAllocationOverhead + obj.data.length + obj.desc.fixedSize;
        }
    }

    /**
   * <code>forgetObject</code> is called when the objects finalize() is called, if 
   * it's opid is still valid. If all data were local, this would mean the object
   * could be desroyed because it's not reachable anymore. But because more 
   * references may exist at the servers end, this basically reduces the reference 
   * count at the servers side. <br>
   * Server side garbage collection will eventually reclaim the object space, if
   * it really has become unreachable. <br>
   * The function adjusts the cache manager's cache size and links the object into the
   * forgottenObjectList of the cache manager
   *
   * @param obj a <code>Persistent</code> that has become localy unreachable
   */
    protected void forgetObject(Persistent obj) {
        CacheManager cm = obj.storage.database.cacheManager;
        if (cm == null) {
            cm = CacheManager.defaultCacheManager;
        }
        cm.forgetObject(obj);
    }

    /**
   * <code>releaseObject</code> is called during a commitTransaction, for objects
   * that have not been modified. The Object is removed from the Transaction list
   * and unlocked (if it was). If NOTIFY is set on the object we keep it in cache.
   * <br> DIRTY,NEW,TANS*,*LOCK flags are cleared.
   *
   * @param obj a <code>Persistent</code> to be released.
   */
    protected synchronized void releaseObject(Persistent obj) {
        CacheManager mng = CacheManager.getCacheManager();
        Assert.that((obj.state & (Persistent.TRANSREAD | Persistent.TRANSWRITE)) != 0);
        CacheManager.unlink(obj);
        if ((obj.state & (Persistent.SLOCKED | Persistent.XLOCKED)) != 0) {
            obj.storage.unlock(obj, Protocol.lck_none);
        }
        if ((obj.state & Persistent.NOTIFY | Persistent.RAW | Persistent.DESTRUCTED) == 0) {
            mng.insertInCache(obj);
        }
        obj.state &= ~(Persistent.TRANSREAD | Persistent.TRANSWRITE | Persistent.XLOCKED | Persistent.SLOCKED | Persistent.DIRTY | Persistent.NEW);
    }

    /**
   * Destoroy all object references to make it possible to provide
   * more unaccessible objects to GC <br>
   * The objects state is set to DESTRUCTED | RAW
   *
   * @param obj a <code>Persistent</code> value
   */
    protected void destroyObject(Persistent obj) {
        if ((obj.state & Persistent.DESTRUCTED) == 0) {
            try {
                FieldDescriptor fd;
                for (fd = obj.desc.refFields; fd != null; fd = fd.nextRef) {
                    fd.field.set(obj, null);
                }
            } catch (Exception x) {
                throw new Error(x.getMessage());
            }
            obj.state |= Persistent.DESTRUCTED | Persistent.RAW;
            obj.storage.throwObject(obj);
        }
    }

    /**
   * This method is asynchronously called by server storag agent when
   * deterioration notification from server is received. <br>
   * If the object does not participate in a current Transaction, it is removed 
   * from the cache and detroyed. Otherwise the invalidated flag is set and 
   * detruction happends after commit. <br>
   * If NOTIFY is set on the object, the associated CondEvent's signal is called
   *
   * @param obj a <code>Persistent</code> object to be invalidated.
   */
    protected synchronized void invalidateObject(Persistent obj) {
        CacheManager mng = CacheManager.getCacheManager();
        if ((obj.state & Persistent.DESTRUCTED) == 0) {
            if (obj.accessCount != 0 || (obj.state & (Persistent.TRANSREAD | Persistent.TRANSWRITE)) != 0) {
                obj.invalidated = true;
            } else {
                obj.invalidated = false;
                synchronized (mng) {
                    mng.removeFromCache(obj);
                    destroyObject(obj);
                }
            }
        }
        if ((obj.state & Persistent.NOTIFY) != 0) {
            CondEvent event = (CondEvent) obj.storage.database.notifications.get(obj);
            if (event != null) {
                event.signal();
            }
        }
    }

    /**
   * Enable or disable notification on receving invalidation messages from
   * the server for this object. If enable, set NOTIFY flag on the object and
   * remove from cache (?). Else unset the flag and insert into cache, if not 
   * accessed.
   *
   * @param obj a <code>Persistent</code> to make the notification changes for
   * @param enabled a <code>boolean</code>, whether notifications are used for this
   * object
   */
    protected synchronized void notifyOnModification(Persistent obj, boolean enabled) {
        CacheManager mng = CacheManager.getCacheManager();
        synchronized (mng) {
            if (enabled) {
                obj.state |= Persistent.NOTIFY;
                if ((obj.state & Persistent.CACHED) != 0) {
                    mng.removeFromCache(obj);
                }
            } else {
                obj.state &= ~Persistent.NOTIFY;
                if (obj.accessCount == 0 && (obj.state & (Persistent.CACHED | Persistent.RAW | Persistent.DESTRUCTED | Persistent.TRANSREAD | Persistent.TRANSWRITE)) == 0) {
                    mng.insertInCache(obj);
                }
            }
        }
    }

    /**
   * By incrementing nested transaction count we delay implicit transaction 
   * commit until the moment when counter is explicitly decremented by
   * endNestedTransactionMethod()
   *
   */
    public static void beginNestedTransaction() {
        CacheManager mng = (CacheManager) CacheManager.getCacheManager();
        synchronized (mng.globalCS) {
            mng.nestedTransactionCount += 1;
        }
    }

    /**
   * <code>endNestedTransaction</code> decreases the cache managers 
   * nestedTransaction counter, and if that hits 0 calls commitTransaction <br>
   *
   */
    public static void endNestedTransaction() {
        CacheManager mng = (CacheManager) CacheManager.getCacheManager();
        synchronized (mng.globalCS) {
            if (--mng.nestedTransactionCount == 0) {
                commitTransaction();
            }
        }
    }

    /**
   * <code>abortNestedTransaction</code> decreases the cache managers 
   * nestedTransaction counter, and if that hits 0 calls abortTransaction <br>
   *
   */
    public static void abortNestedTransaction() {
        CacheManager mng = (CacheManager) CacheManager.getCacheManager();
        synchronized (mng.globalCS) {
            if (--mng.nestedTransactionCount == 0) {
                abortTransaction();
            }
        }
    }
}
