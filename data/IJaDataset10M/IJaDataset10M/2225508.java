package alto.io.u;

import alto.io.u.hasharray.Entry;
import alto.io.u.hasharray.Index;
import alto.io.u.hasharray.IndexObject;
import alto.io.u.hasharray.Values;

/**
 * Integer map for an efficient and compact map from integers to
 * objects.  Maintains input order for keys and values.  The integer
 * value <tt>`Long.MIN_VALUE' (0xffffffffffffffff)</tt> is a special
 * <tt>`NIL'</tt> key value that cannot be used as a key, it
 * represents the empty (or "undefined") key.  In normal usage it is
 * not possible to employ the NIL key as keys are 32 bit integers.
 * 
 * <pre>
 * STANDARD DICTIONARY API
 * 
 * Object put (Object key, Object value)
 * 
 * Object get (Object key)
 * 
 * INTMAP DICTIONARY API
 * 
 * Object put (int key, Object value)
 * 
 * Object get (int key)
 *
 * </pre>
 * 
 * <p> The standard dictionary API simply wraps the int- key
 * dictionary API.  Users should not use the standard dictionary API
 * unless they have object keys in memory already.  The principal
 * purpose of intmap is for applications that need to avoid creating
 * objects for keys that are integer numbers using a java Hashtable.
 * 
 * <p> A good object key should be an <tt>`Integer'</tt> class object,
 * or any <tt>`Number'</tt> subclass, <tt>`Character'</tt> class
 * object, or have a hashCode value that is consistent with respect to
 * the semantic value (content) of the object.  For example, both the
 * AWT <tt>`Color'</tt> class and <tt>`InetAddress'</tt> class make
 * good keys for this table because their hashcodes are a consistent
 * and semantically meaningful value (a color value and an IP address,
 * respectively).
 * 
 * <p> For good keys, the intmap is an excellent lookup table,
 * performing marginally faster than a java Hashtable, while having a
 * heavier memory footprint (with its <tt>`keys'</tt> and
 * <tt>`vals'</tt> arrays in addition to the index table).  The intmap
 * weighs more than the java Hashtable, but is faster and provides
 * input- ordered output (storage).
 * 
 * <p> Most classes of object keys will be used solely for an
 * ambiguous <tt>`hashCode'</tt>, which in most cases will lead to
 * unpredictable and undesireable behavior, and cannot be recommended.
 * 
 * <p> The <tt>`String'</tt> class is a very bad intmap key because
 * its hashcode can represent only part of the identity of the
 * semantic value, or content, of most strings (ones more than four
 * bytes long).  However, even short strings of four or fewer
 * characters are not good intmap keys because the string's hash
 * function is not "crisp" or "tight".
 * 
 * <p><b>API Caution</b>
 *
 * <p> Note that <i>indeces</i> and <i>keys</i> are two distinct and
 * separate spaces.  An integer key must be mapped into an integer
 * index in order to access the <tt>`keys'</tt> and <tt>`vals'</tt>
 * arrays (the function <tt>`indexOf(int)'</tt> performs the mapping
 * from key to index).  An index directly retrieves a key or value
 * from the <tt>`keys'</tt> and <tt>`vals'</tt> arrays.
 * 
 * <p><b>Not Synchronized</b>
 *
 * <p> Note that the `Intmap' is not multi-thread safe.  This class is
 * optimized for high frequency, single- threaded use as required for
 * thread specific data structures.
 *
 * @author John Pritchard
 * @since 1.2
 */
public class Intmap extends Hasharray {

    public Intmap(int initial, float load) {
        super(Types.Indeces.Long, initial, load, Values.Types.Object);
    }

    public Intmap(int initial) {
        super(Types.Indeces.Long, initial, Values.Types.Object);
    }

    public Intmap() {
        super(Types.Indeces.Long, Values.Types.Object);
    }

    public int[] keyary() {
        return this.longKeyary(0);
    }

    public java.lang.Object[] valary() {
        return this.objectValary(0);
    }

    public java.lang.Object[] valary(Class comp) {
        return this.objectValary(0, comp);
    }

    public java.lang.Object[] valaryFilter(Class comp) {
        return this.objectValaryFilter(0, comp);
    }

    public long lastKey() {
        return this.longGetLastKey(0);
    }

    public java.lang.Object lastValue() {
        return this.objectGetLastValue(0);
    }

    public void lastValue(java.lang.Object val) {
        this.objectSetLastValue(0, val);
    }

    public java.util.Enumeration keys() {
        return this.enumerateKeys(0);
    }

    public java.util.Enumeration elements() {
        return this.enumerateValues(0);
    }

    public boolean containsValue(java.lang.Object value) {
        return this.objectContainsValue(0, value);
    }

    public boolean contains(java.lang.Object value) {
        return this.objectContainsValue(0, value);
    }

    public boolean containsKey(int key) {
        return this.longContainsKey(0, key);
    }

    public int indexOf(int key) {
        return this.longIndexOfKey(0, key);
    }

    public int[] indexOfList(int key) {
        return this.longIndexListOfKey(0, key);
    }

    public int indexOf(int key, int fromIdx) {
        return this.longIndexOfKey(0, key, fromIdx);
    }

    public int lastIndexOf(int key) {
        return this.longLastIndexOfKey(0, key, Integer.MAX_VALUE);
    }

    public int indexOfValue(java.lang.Object val) {
        return this.objectIndexOfValue(0, val, 0);
    }

    public int indexOfValue(java.lang.Object val, int fromIdx) {
        return this.objectIndexOfValue(0, val, fromIdx);
    }

    public int lastIndexOfValue(java.lang.Object val) {
        return this.objectLastIndexOfValue(0, val, Integer.MAX_VALUE);
    }

    public int lastIndexOfValue(java.lang.Object val, int fromIdx) {
        return this.objectLastIndexOfValue(0, val, fromIdx);
    }

    public int indexOfValueClass(Class sup) {
        return this.indexOfValueClass(sup, -1);
    }

    public int indexOfValueClass(Class sup, int fromIdx) {
        return this.objectIndexOfValueClass(0, sup, fromIdx);
    }

    public int lastIndexOfValueClass(Class sup) {
        return this.objectLastIndexOfValueClass(0, sup, Integer.MAX_VALUE);
    }

    public int lastIndexOfValueClass(Class sup, int fromIdx) {
        return this.objectLastIndexOfValueClass(0, sup, fromIdx);
    }

    public java.lang.Object get(java.lang.Object key) {
        int idx = this.indexOfKeyAsObject(0, key);
        if (-1 < idx) return this.objectGetValue(0, idx); else return null;
    }

    public java.lang.Object get(int key) {
        int idx = this.longIndexOfKey(0, key);
        if (-1 < idx) return this.objectGetValue(0, idx); else return Values.NilObject;
    }

    public java.lang.Object[] list(int key) {
        int[] lidx = this.longIndexListOfKey(0, key);
        if (null != lidx) {
            Values.Object values = this.objectGetValues(0);
            return values.list(lidx);
        } else return Values.NilArrayObject;
    }

    public java.lang.Object[] list(int key, Class comp) {
        int[] lidx = this.longIndexListOfKey(0, key);
        if (null != lidx) {
            Values.Object values = this.objectGetValues(0);
            return (java.lang.Object[]) values.list(lidx, comp);
        } else return Values.NilArrayObject;
    }

    public int key(int idx) {
        return this.longGetKey(0, idx);
    }

    public java.lang.Object value(int idx) {
        return this.objectGetValue(0, idx);
    }

    public java.lang.Object value(int idx, java.lang.Object value) {
        return this.objectSetValue(0, idx, value);
    }

    public java.lang.Object put(java.lang.Object key, java.lang.Object value) {
        Entry ent = this.putKeyAsObject(0, key);
        return this.setValueAsObject(0, ent.aryix, value);
    }

    public java.lang.Object put(int key, java.lang.Object value) {
        Entry ent = this.longPutKey(0, key);
        return this.objectSetValue(0, ent.aryix, value);
    }

    public int append(int key, java.lang.Object val) {
        Entry ent = this.longAppendKey(0, key);
        if (null != ent) {
            int idx = ent.aryix;
            this.objectSetValue(0, idx, val);
            return idx;
        } else return Index.NotFound;
    }

    public int insert(int idx, int key, java.lang.Object val) {
        Entry ent = this.longInsertKey(0, idx, key);
        if (null != ent) {
            idx = ent.aryix;
            this.objectInsertValue(0, idx, val);
            return idx;
        } else return Index.NotFound;
    }

    public int replace(int idx, int nkey, java.lang.Object nval) {
        Entry ent = this.longReplaceKey(0, idx, nkey);
        if (null != ent) {
            idx = ent.aryix;
            this.objectSetValue(0, idx, nval);
            return idx;
        } else return Index.NotFound;
    }

    public java.lang.Object drop(int idx) {
        Entry ent = this.longRemoveKeyByIndex(0, idx);
        if (null != ent) return this.objectRemoveValue(0, ent.aryix); else return Values.NilInt;
    }

    public java.lang.Object remove(java.lang.Object key) {
        Entry ent = this.removeKeyAsObject(0, key);
        if (null != ent) return this.objectRemoveValue(0, ent.aryix); else return Values.NilObject;
    }

    public java.lang.Object remove(int key) {
        Entry ent = this.longRemoveKeyByValue(0, key);
        if (null != ent) return this.objectRemoveValue(0, ent.aryix); else return Values.NilObject;
    }

    public final Intmap cloneIntmap() {
        return (Intmap) super.cloneHasharray();
    }
}
