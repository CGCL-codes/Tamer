package org.ozoneDB.collections;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

/**
 * This class provides a red-black tree implementation of the SortedMap
 * interface.  Elements in the Map will be sorted by either a user-provided
 * Comparator object, or by the natural ordering of the keys.
 *
 * The algorithms are adopted from Corman, Leiserson, and Rivest's
 * <i>Introduction to Algorithms.</i>  TreeMap guarantees O(log n)
 * insertion and deletion of elements.  That being said, there is a large
 * enough constant coefficient in front of that "log n" (overhead involved
 * in keeping the tree balanced), that TreeMap may not be the best choice
 * for small collections. If something is already sorted, you may want to
 * just use a LinkedHashMap to maintain the order while providing O(1) access.
 *
 * TreeMap is a part of the JDK1.2 Collections API.  Null keys are allowed
 * only if a Comparator is used which can deal with them; natural ordering
 * cannot cope with null.  Null values are always allowed. Note that the
 * ordering must be <i>consistent with equals</i> to correctly implement
 * the Map interface. If this condition is violated, the map is still
 * well-behaved, but you may have suprising results when comparing it to
 * other maps.<p>
 *
 * This implementation is not synchronized. If you need to share this between
 * multiple threads, do something like:<br>
 * <code>SortedMap m
 *       = Collections.synchronizedSortedMap(new TreeMap(...));</code><p>
 *
 * The iterators are <i>fail-fast</i>, meaning that any structural
 * modification, except for <code>remove()</code> called on the iterator
 * itself, cause the iterator to throw a
 * <code>ConcurrentModificationException</code> rather than exhibit
 * non-deterministic behavior.
 *
 * <p>Note that the first call to <code>entrySet()</code>, <code>keySet()</code>
 * and <code>values()</code> results in the creation of a new ozone object.</p>
 *
 * <p><b>NOTE:</b> Because of issues with implementing 2 Ozone objects that have
 * to share non-Ozone objects, it can happen that a <code>FullXxx</code> 
 * instance is written to the backend-store while the <code>Iterator</code>
 * remains in memory. If that happens you will receive a <code>ConcurrentModificationException</code>.
 * If you receive such an exception and are completely sure that the collection
 * has not been changed (objects added or removed from it) by another process, 
 * you should increase the memory available to the Ozone server, or decrease the
 * time between successive calls on the methods of the iterator. It should be
 * noted that there is no way to guarantee that an exception is thrown in this
 * situation, just like there is no guarantee that an exception will be thrown
 * when a 'regular' <code>java.util.Collection</code> is changed while an
 * iterator is at the same time iterating over it. This is theory, but in the 
 * real world the changes of this happening are negligable.</code>
 
 </p>
 *
 * @author Jon Zeppieri
 * @author Bryce McKinlay
 * @author Eric Blake <ebb9@email.byu.edu>
 * @author <a href="mailto:ozoneATmekenkampD0Tcom">Leo Mekenkamp (mind the anti-sp@m)</a> (adaptation for ozone)
 * @see java.util.TreeMap
 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
 */
public class FullTreeMapImpl extends BaseTreeMapImpl implements FullTreeMap {

    private static final long serialVersionUID = 1L;

    private class Node extends AbstractOzoneMap.AbstractNode implements BaseTreeMap.Node {

        private static final long serialVersionUID = 1L;

        private int color;

        private BaseTreeMap.Node left = BaseTreeMapImpl.nilNode;

        private BaseTreeMap.Node right = BaseTreeMapImpl.nilNode;

        private BaseTreeMap.Node parent = BaseTreeMapImpl.nilNode;

        public Node(Object key, Object value, int color) {
            super(key, value);
            setColor(color);
        }

        public void setRight(BaseTreeMap.Node right) {
            this.right = right;
        }

        public void setParent(BaseTreeMap.Node parent) {
            this.parent = parent;
        }

        public void setLeft(BaseTreeMap.Node left) {
            this.left = left;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public BaseTreeMap.Node getRight() {
            return right;
        }

        public BaseTreeMap.Node getParent() {
            return parent;
        }

        public BaseTreeMap.Node getLeft() {
            return left;
        }

        public int getColor() {
            return color;
        }

        public boolean isNil() {
            return false;
        }
    }

    /**
     * Instantiate a new TreeMap with no elements, using the keys' natural
     * ordering to sort. All entries in the map must have a key which implements
     * Comparable, and which are <i>mutually comparable</i>, otherwise map
     * operations may throw a {@link ClassCastException}. Attempts to use
     * a null key will throw a {@link NullPointerException}.
     *
     * @see Comparable
     */
    public FullTreeMapImpl() {
    }

    /**
     * Instantiate a new TreeMap with no elements, using the provided comparator
     * to sort. All entries in the map must have keys which are mutually
     * comparable by the Comparator, otherwise map operations may throw a
     * {@link ClassCastException}.
     *
     * @param c (comparator) the sort order for the keys of this map, or null
     *        for the natural order
     */
    public FullTreeMapImpl(Comparator c) {
        super(c);
    }

    /**
     * Instantiate a new TreeMap, initializing it with all of the elements in
     * the provided Map.  The elements will be sorted using the natural
     * ordering of the keys. This algorithm runs in n*log(n) time. All entries
     * in the map must have keys which implement Comparable and are mutually
     * comparable, otherwise map operations may throw a
     * {@link ClassCastException}.
     *
     * @param map a Map, whose entries will be put into this TreeMap
     * @throws ClassCastException if the keys in the provided Map are not
     *         comparable
     * @throws NullPointerException if map is null
     * @see Comparable
     */
    public FullTreeMapImpl(Map map) {
        super(map);
    }

    /**
     * Instantiate a new TreeMap, initializing it with all of the elements in
     * the provided SortedMap.  The elements will be sorted using the same
     * comparator as in the provided SortedMap. This runs in linear time.
     *
     * @param sm a SortedMap, whose entries will be put into this TreeMap
     * @throws NullPointerException if sm is null
     */
    public FullTreeMapImpl(SortedMap sm) {
        super(sm);
    }

    protected BaseTreeMap emptyClone() {
        return (BaseTreeMap) database().createObject(FullTreeMapImpl.class);
    }

    protected BaseTreeMap.Node newNode(Object key, Object value, int color) {
        BaseTreeMap.Node result = new Node(key, value, color);
        return result;
    }

    /**
     * Deserializes this object from the given stream.
     *
     * @param s the stream to read from
     * @throws ClassNotFoundException if the underlying stream fails
     * @throws IOException if the underlying stream fails
     * @serialData the <i>size</i> (int), followed by key (Object) and value
     *             (Object) pairs in sorted order
     */
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        root = nilNode;
        s.defaultReadObject();
        putFromObjStream(s, size, true);
    }

    /**
     * @param s the stream to read from
     * @param count the number of keys to read
     * @param readValues true to read values, false to insert "" as the value
     * @throws ClassNotFoundException if the underlying stream fails
     * @throws IOException if the underlying stream fails
     * @see #readObject(ObjectInputStream)
     * @see java.util.TreeSet#readObject(ObjectInputStream)
     */
    private void putFromObjStream(ObjectInputStream s, int count, boolean readValues) throws IOException, ClassNotFoundException {
        _org_ozoneDB_fabricateTree(count);
        BaseTreeMap.Node node = _org_ozoneDB_firstNode();
        while (--count >= 0) {
            node.setKey(s.readObject());
            node.setValue(readValues ? s.readObject() : "");
            node = _org_ozoneDB_successor(node);
        }
    }

    /**
     * Serializes this object to the given stream.
     *
     * @param s the stream to write to
     * @throws IOException if the underlying stream fails
     * @serialData the <i>size</i> (int), followed by key (Object) and value
     *             (Object) pairs in sorted order
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        BaseTreeMap.Node node = _org_ozoneDB_firstNode();
        while (!node.isNil()) {
            s.writeObject(node.getKey());
            s.writeObject(node.getValue());
            node = _org_ozoneDB_successor(node);
        }
    }

    public boolean _org_ozoneDB_alwaysUseInternalIterator() {
        return false;
    }

    /**
     * Called automatically by Ozone. Increments the modification counter to
     * make sure all <code>Iterator</code>s that were connected to a previous
     * (memory) instance of this database object are invalidated.
     */
    public void onActivate() {
        modCount += Integer.MAX_VALUE / 2;
    }
}
