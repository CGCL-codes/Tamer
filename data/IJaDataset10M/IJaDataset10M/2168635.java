package cbr2.service;

import cbr2.*;
import DE.FhG.IGD.util.*;
import java.io.*;
import java.util.*;

/**
 * This class manages an index of {@link PictureEntry picture
 * entries}. The index is kept in a file in a {@link Resource
 * Resource}. Multiple instances can share a single resource.
 * However, the index files must not be shared unless the
 * instances use the same {@link Lock Lock} instance for
 * locking.<p>
 *
 * This class is thread safe and supports multiple concurrent
 * readers and exclusive write access. Readers and writers
 * are scheduled according to the first come first serve
 * strategy. No starvation can occur, each reader and writer
 * eventually gets its turn.<p>
 *
 * Management of thumbnails is delegated to a separate object.
 * The rationale for this is to remove thumbnails before the
 * picture entries are written to the index. This renders
 * lookup and insertion operations more efficient because
 * the thumbnails must not be written and read whenever the
 * index is scanned.<p>
 *
 * Lookups depend on the <code>FeatureVector.distance()</code>
 * method of the query vector. Incompatible vectors should return
 * 1.0 which is the maximum normalized distance.
 *
 * The index simply consists of a stream of serialized
 * PictureEntry instances terminated by a string (&quot;EOF&quot;).
 *
 * @author Volker Roth
 * @version "$Id: PicsStore.java 601 2002-04-17 16:00:44Z upinsdor $"
 */
public class PicsStore extends Object {

    /**
     * The mode for inserting entries into the index.
     */
    public static final int MODE_INSERT = 1;

    /**
     * The mode for deleting entries from the index.
     */
    public static final int MODE_DELETE = 0;

    /**
     * The resource where the index is kept.
     */
    private Resource store_;

    /**
     * The lock used to manage access to the index file.
     */
    private Lock lock_ = new Lock();

    /**
     * The name of the index file in the resource.
     */
    private String idx_;

    /**
     * The name of the temporary index file in the resource.
     * This file is used when the index is updated either by
     * importing entries into it or by deleting entries from
     * it.
     */
    private String tmp_;

    /**
     * The delegate that handles treatment of thumbnails.
     */
    private StoreDelegate delegate_;

    /**
     * Creates a new instance.
     *
     * @param store The {@link Resource Resource} in which the
     *   index file is stored. A resource can be shared among
     *   multiple instances of this class.
     * @param idx The name of index file in the resource. The
     *   index file cannot be shared among multiple instances
     *   of this class unless these instances share the same
     *   lock instance.
     * @param tmp The name of the temporary file that is used
     *   when the index is updated. The temporary file cannot
     *   be shared.
     * @exception NullPointerException if some argument is
     *   <code>null</code>.
     */
    public PicsStore(Resource store, String idx, String tmp) {
        if (store == null || idx == null || tmp == null) {
            throw new NullPointerException("Some argument is null!");
        }
        store_ = store;
        idx_ = idx;
        tmp_ = tmp;
    }

    /**
     * Sets the {@link StoreDelegate StoreDelegate} of this
     * PicsStore. The delegate handles thumbnails on behalf
     * of this object. 
     *
     * @param delegate The StoreDelegate instance or <code>
     *   null</code> if the delegate shall be cleared.
     */
    public void setDelegate(StoreDelegate delegate) {
        delegate_ = delegate;
    }

    /**
     * Returns the {@link StoreDelegate StoreDelegate} of
     * this instance.
     *
     * @return The StoreDelegate or <code>null</code> if
     *   none is set.
     */
    public StoreDelegate getDelegate() {
        return delegate_;
    }

    /**
     * Initialises the store. If the index file already exists
     * and contains data then all this data will be lost. The
     * method attempts to get a write lock on the index file.
     */
    public void init() throws IOException {
        ObjectOutputStream oos;
        OutputStream os;
        try {
            lock_.beginWrite();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted!");
        }
        try {
            os = store_.getOutputStream(idx_);
            oos = new ObjectOutputStream(os);
            oos.writeObject("EOF");
            oos.close();
        } finally {
            lock_.endWrite();
        }
    }

    /**
     * Updates the index file according to the {@link PictureEntry
     * picture entries} returned by the given iterator. The iterator
     * must return the picture entries in the natural order of
     * the picture entries as determined by the {@link
     * PictureEntry#compareTo compareTo} method.<p>
     *
     * If the mode is {@link #MODE_DELETE MODE_DELETE} then all
     * entries in the index matching the ones returned by the
     * given iterator are deleted. Otherwise the entries returned
     * by the given iterator are inserted into the index.<p>
     *
     * The following comment is relevant only if the mode is
     * not {@link #MODE_DELETE MODE_DELETE}.<p>
     *
     * If no {@link StoreDelegate StoreDelegate} has been set
     * then thumbnails are embedded into the stream of serialised
     * object instances. This is inefficient since the thumbnails
     * are not required for feature matching. Still they must be
     * read when the search method sifts through the index. Thus
     * setting an appropriate delegate is strongly recommended.
     *
     * @param entries An iterator that iterates over the picture
     *   entries that shall be inserted into the index.
     */
    public void update(Iterator entries, int mode) throws IOException {
        ObjectOutputStream oos;
        PictureEntry en;
        PictureEntry ei;
        PicsIterator i;
        int n;
        oos = null;
        i = null;
        try {
            lock_.beginWrite();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted!");
        }
        try {
            oos = new ObjectOutputStream(store_.getOutputStream(tmp_));
            i = new PicsIterator(store_.getInputStream(idx_));
            en = entries.hasNext() ? (PictureEntry) entries.next() : null;
            ei = i.hasNext() ? (PictureEntry) i.next() : null;
            while (true) {
                if (en == null) {
                    if (ei == null) {
                        break;
                    } else {
                        n = 1;
                    }
                } else if (ei == null) {
                    n = -1;
                } else {
                    n = en.compareTo(ei);
                }
                if (n < 0) {
                    if (mode != MODE_DELETE) {
                        if (delegate_ != null) {
                            delegate_.cut(en);
                        }
                        oos.writeObject(en);
                    }
                    if (entries.hasNext()) {
                        en = (PictureEntry) entries.next();
                    } else {
                        en = null;
                    }
                } else {
                    if (mode != MODE_DELETE || n > 0) {
                        oos.writeObject(ei);
                    }
                    if (i.hasNext()) {
                        ei = (PictureEntry) i.next();
                    } else {
                        ei = null;
                    }
                }
            }
            oos.writeObject("EOF");
            oos.flush();
            oos.close();
            i.close();
            if (File.separatorChar == '\\') {
                try {
                    store_.delete(idx_);
                } catch (Exception e2) {
                }
            }
            store_.rename(tmp_, idx_);
        } catch (IOException e) {
            throw e;
        } finally {
            lock_.endWrite();
        }
    }

    /**
     * Matches the given feature vector against the ones of
     * the known pictures and returns the picture entries
     * of at most <code>max</code> pictures that have a
     * normalised distance less than the given threshold to
     * the query vector.
     *
     * @param v The feature vector to match known picture
     *   entries against.
     * @param threshold The maximum distance a picture in
     *   the result set is allowed to have from the query
     *   vector.
     * @param max The maximum number of results that shall
     *   be returned.
     */
    public PictureEntry[] find(FeatureVector v, float threshold, int max) throws IOException {
        if (max < 1 || max > 100) {
            throw new IllegalArgumentException("max must be in the range [1,100]!");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("threshold must be positive!");
        }
        if (v == null) {
            throw new NullPointerException("Need a feature vector!");
        }
        PictureEntry entry;
        PicsIterator i;
        ArrayList list;
        Iterator j;
        float d;
        int n;
        n = (int) (max + Math.max(10, max * 0.2));
        list = new ArrayList(n);
        try {
            lock_.beginRead();
        } catch (InterruptedException e) {
            throw new IOException("Interupted!");
        }
        try {
            i = new PicsIterator(store_.getInputStream(idx_));
            while (i.hasNext()) {
                entry = (PictureEntry) i.next();
                d = v.distance(entry.features);
                if (d <= threshold) {
                    entry.distance = d;
                    list.add(entry);
                    if (list.size() >= n) {
                        truncate(list, max);
                    }
                }
            }
            i.close();
            truncate(list, max);
            if (delegate_ != null) {
                for (j = list.iterator(); j.hasNext(); ) {
                    entry = (PictureEntry) j.next();
                    delegate_.paste(entry);
                }
            }
            return (PictureEntry[]) list.toArray(new PictureEntry[0]);
        } finally {
            lock_.endRead();
        }
    }

    /**
     * Truncates the given list to the given number of
     * entries. Before truncation the list is sorted
     * according to the distance values in the entries.<p>
     *
     * In other words this methods makes sure that the <code>max
     * </code> best results are kept in the given list.
     *
     * @param list The list of {@link PictureEntry picture entries}.
     * @param max The maximum number of entries that shall be kept
     *   in the list after truncation.
     */
    private void truncate(ArrayList list, int max) {
        int num;
        Collections.sort(list, new DistanceComparator());
        num = list.size();
        while (num > max) {
            list.remove(--num);
        }
    }

    /**
     * Returns an iterator for the pictures in the index file.
     * <b>Important note:</b> accessing the index entries by
     * means of this method bypasses the locking mechanisms.
     * The caller is responsible for assuring that no
     * concurrent accesses to the store occur while the
     * iterator is in use. Moreover <b>all</b> elements
     * of the iterator should be requested via its next()
     * method such that the iterator can free its resources
     * after completition.<p>
     *
     * This iterator returns {@link PictureEntry picture
     * entries} with thumbnails filled in. For this, the
     * {@link StoreDelegate StoreDelegate} of this instance
     * is used.<p>
     *
     * Callers should {@link PicsIterator#close close} the
     * returned iterator as soon as it is not longer used.
     *
     * @return The PicsIterator for all entries in the index.
     *   The elements are returned in sorted order; the
     *   order is the natural ordering as determined by
     *   {@link PictureEntry#compareTo PictureEntry.compareTo()}
     */
    public PicsIterator iterator() {
        try {
            return new PicsIterator(store_.getInputStream(idx_), delegate_);
        } catch (IOException e) {
            return new PicsIterator();
        }
    }
}
