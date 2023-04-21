package rice.persistence;

import java.io.*;
import java.util.*;
import rice.*;
import rice.pastry.*;

/**
 * This class is an encapsulation of a least-recently-used (LRU)
 * cache.  It uses the provided storage service in order to
 * store the cached data.  If the Storage provides non-corruption
 * services, these services will also be provided by this cache.
 */
public class LRUCache implements Cache {

    private int maximumSize;

    private Storage storage;

    private LinkedList order;

    /**
   * Builds a LRU cache given a storage object to store the cached
   * data in and a maximum cache size.
   *
   * @param storage The storage service to use as a back-end storage
   * @param maximumSize The maximum size, in bytes, of storage to use
   */
    public LRUCache(Storage storage, int maximumSize) {
        this.storage = storage;
        this.maximumSize = maximumSize;
        this.order = new LinkedList();
    }

    /**
   * Caches an object in this Cache. This method is non-blocking.
   * If the object has already been stored at the location id, this
   * method has the effect of calling <code>uncachr(id)</code> followed
   * by <code>cache(id, obj)</code>. This method finishes by calling
   * receiveResult() on the provided continuation with whether or not
   * the object was cached.  Note that the object may not actually be
   * stored (if it is bigger than the entire cache size).
   *
   * @param id The object's id.
   * @param obj The object to cache.
   * @param c The command to run once the operation is complete
   * @return <code>True</code> if the cache actaully stores the object, else
   * <code>False</code> (through receiveResult on c).
   */
    public synchronized void cache(final Comparable id, final Serializable obj, final Continuation c) {
        final int size = getSize(obj);
        if (size > maximumSize) {
            c.receiveResult(new Boolean(false));
            return;
        }
        final Continuation store = new Continuation() {

            public void receiveResult(Object o) {
                order.addFirst(id);
                storage.store(id, obj, c);
            }

            public void receiveException(Exception e) {
                c.receiveException(e);
            }
        };
        Continuation resize = new Continuation() {

            public void receiveResult(Object o) {
                int totalSize = ((Integer) o).intValue();
                if (maximumSize - size < totalSize) {
                    resize(maximumSize - size, store);
                } else {
                    store.receiveResult(new Boolean(true));
                }
            }

            public void receiveException(Exception e) {
                c.receiveException(e);
            }
        };
        storage.getTotalSize(resize);
    }

    /**
   * Removes the object from the list of cached objects. This method is
   * non-blocking. If the object was not in the cached list in the first place,
   * nothing happens and <code>False</code> is returned.
   *
   * @param pid The object's id
   * @param c The command to run once the operation is complete
   * @return <code>True</code> if the action succeeds, else
   * <code>False</code>  (through receiveResult on c).
   */
    public synchronized void uncache(Comparable id, Continuation c) {
        order.remove(id);
        storage.unstore(id, c);
    }

    /**
   * Returns whether or not an object is cached in the location <code>id</code>.
   * The result is returned via the receiveResult method on the provided
   * Continuation with an Boolean represnting the result.
   *
   * @param c The command to run once the operation is complete
   * @param id The id of the object in question.
   * @return Whether or not an object is present at id.
   */
    public void exists(Comparable id, Continuation c) {
        c.receiveResult(new Boolean(order.contains(id)));
    }

    /**
   * Returns the object identified by the given id.
   *
   * @param id The id of the object in question.
   * @param c The command to run once the operation is complete
   * @return The object, or <code>null</code> if there is no cooresponding
   * object (through receiveResult on c).
   */
    public synchronized void getObject(Comparable id, Continuation c) {
        if (!order.contains(id)) {
            c.receiveResult(null);
            return;
        }
        order.remove(id);
        order.addFirst(id);
        storage.getObject(id, c);
    }

    /**
   * Returns the keys of cached objects identified by the given range of ids. The array
   * returned contains the Comparable ids of the stored objects. The range is
   * completely inclusive, such that if the range is (A,B), objects with
   * ids of both A and B would be returned.
   *
   * Note that the two Comparable objects should be of the same class
   * (otherwise no range can be created).
   *
   * When the operation is complete, the receiveResult() method is called
   * on the provided continuation with a Comparable[] result containing the
   * resulting IDs.
   *
   * @param start The staring id of the range.
   * @param end The ending id of the range.
   * @param c The command to run once the operation is complete
   * @return The objects
   */
    public synchronized void scan(Comparable start, Comparable end, Continuation c) {
        storage.scan(start, end, c);
    }

    /**
   * Returns the maximum size of the cache, in bytes. The result
   * is returned via the receiveResult method on the provided
   * Continuation with an Integer representing the size.
   *
   * @param c The command to run once the operation is complete
   * @return The maximum size, in bytes, of the cache.
   */
    public void getMaximumSize(Continuation c) {
        c.receiveResult(new Integer(maximumSize));
    }

    /**
   * Returns the total size of the stored data in bytes. The result
   * is returned via the receiveResult method on the provided
   * Continuation with an Integer representing the size.
   *
   * @param c The command to run once the operation is complete
   * @return The total size, in bytes, of data stored.
   */
    public void getTotalSize(Continuation c) {
        storage.getTotalSize(c);
    }

    /**
   * Sets the maximum size of the cache, in bytes. Setting this
   * value to a smaller value than the current value may result in
   * object being evicted from the cache.
   *
   * @param size The new maximum size, in bytes, of the cache.
   * @param c The command to run once the operation is complete
   * @return The success or failure of the setSize operation
   * (through receiveResult on c).
   */
    public void setMaximumSize(final int size, final Continuation c) {
        Continuation local = new Continuation() {

            public void receiveResult(Object o) {
                maximumSize = size;
                c.receiveResult(new Boolean(true));
            }

            public void receiveException(Exception e) {
                c.receiveException(e);
            }
        };
        if (size < maximumSize) {
            resize(size, local);
        } else {
            local.receiveResult(new Boolean(true));
        }
    }

    /**
   * Internal method which removes objects from the cache until the cache
   * is smaller than the specified size
   *
   * @param size The maximum number of bytes to make the cache
   * @param c The command to run once the operation is complete
   */
    private void resize(final int size, final Continuation c) {
        final Continuation remove = new Continuation() {

            private boolean waitingForSize = true;

            public void receiveResult(Object o) {
                if (waitingForSize) {
                    waitingForSize = false;
                    if (((Integer) o).intValue() > size) {
                        Comparable thisID = (Comparable) order.getLast();
                        uncache(thisID, this);
                    } else {
                        c.receiveResult(new Boolean(true));
                    }
                } else {
                    waitingForSize = true;
                    storage.getTotalSize(this);
                }
            }

            public void receiveException(Exception e) {
                c.receiveException(e);
            }
        };
        storage.getTotalSize(remove);
    }

    /**
   * Returns the size of the given object, in bytes.
   *
   * @param obj The object to determine the size of
   * @return The size, in bytes
   */
    private int getSize(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray().length;
        } catch (IOException e) {
            throw new RuntimeException("Object " + obj + " was not serialized correctly!");
        }
    }
}
