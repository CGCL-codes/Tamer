package goodsjpi;

import java.io.*;

/**
 * Root class for all persistent capable objects
 */
public class Persistent implements Serializable {

    public Metaobject metaobject;

    transient ClassDescriptor desc;

    transient boolean invalidated;

    transient byte[] data;

    transient int opid;

    transient Storage storage;

    transient Persistent next;

    transient Persistent prev;

    transient int accessCount;

    transient int state;

    static final int DIRTY = 0x0001;

    static final int RAW = 0x0002;

    static final int CACHED = 0x0004;

    static final int DESTRUCTED = 0x0008;

    static final int TRANSREAD = 0x0010;

    static final int TRANSWRITE = 0x0020;

    static final int USEFUL = 0x0040;

    static final int ACCESSED = 0x0080;

    static final int SLOCKED = 0x0100;

    static final int XLOCKED = 0x0200;

    static final int NEW = 0x0400;

    static final int OPTIMISTIC = 0x0800;

    static final int NOTIFY = 0x1000;

    public Object clone() throws CloneNotSupportedException {
        Persistent p = (Persistent) super.clone();
        p.invalidated = false;
        p.data = null;
        p.opid = 0;
        p.storage = null;
        p.next = null;
        p.prev = null;
        p.accessCount = 0;
        p.state = 0;
        return p;
    }

    protected final void finalize() {
        if (opid != 0) {
            metaobject.forgetObject(this);
        }
    }

    public synchronized void attachToStorage(Database db, int sid) {
        Assert.that("persistent object can't change its location", opid == 0);
        storage = db.storages[sid];
    }

    public synchronized void clusterWith(Persistent obj) {
        Assert.that("persistent object can't change its location", opid == 0);
        storage = obj.storage;
    }

    public Database getDatabase() {
        return storage != null ? storage.database : null;
    }

    /**
     * Get the dabase assigned id of the object
     */
    public int getOid() {
        return opid;
    }

    public int getStorageId() {
        return storage != null ? storage.id : -1;
    }

    public int hashCode() {
        Assert.that(opid != 0);
        return opid;
    }

    /**
     * Method to be invoked after loading object from the storage.
     * It can be used to initilize transient and new fields of the object.
     */
    public void onLoad() {
    }

    protected Persistent() {
        desc = ClassDescriptor.lookup(getClass());
        metaobject = desc.defaultMetaobject;
        state = NEW;
        metaobject.preDaemon(this, Metaobject.MUTATOR | Metaobject.CONSTRUCTOR);
    }

    protected Persistent(Metaobject metaobject) {
        this.metaobject = metaobject;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        desc = ClassDescriptor.lookup(getClass());
        state = NEW;
        stream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        metaobject.preDaemon(this, 0);
        stream.defaultWriteObject();
        metaobject.postDaemon(this, 0, false);
    }

    public static Metaobject defaultMetaobject = new PessimisticRepeatableReadMetaobject();
}
