package net.sf.eBus.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import net.sf.eBus.util.regex.Component;
import net.sf.eBus.util.regex.Pattern;

/**
 * Ternary search tree (TST)-based implementation of the
 * {@link java.util.Map} interface. This implementation provides all
 * optional interface methods, allows {@code null} values
 * but restricts keys to non-{@code null java.lang.CharSequence}
 * objects.
 * <p/>
 * It must be pointed out that this implementation
 * of {@link #entrySet()}, {@link #keySet()} and
 * {@link #values()} does <i>not</i> return sets backed by the
 * map. Changes to the map are not reflected in the returned set
 * nor are changes to the returned set reflected in the map.
 * <p/>
 * <b>Note: this implementation is not synchronized.</b> If
 * multiple threads currently access this map and at least one
 * thread modifies the map by adding or removing an entry, then
 * this map must be externally synchronized. External
 * synchronization is accomplished in two ways:
 * <ol>
 *   <li>
 *     Placing the map inside a {@code synchronized} block:
 *     <p>
 *     <code >
 *       <pre>
 *         synchronized (tstMap)
 *         {
 *             tstMap.put("abcd", obj);
 *         }
 *       </pre>
 *     </code>
 *   </li>
 *   <li>
 *     Constructing a {@code TernarySearchTree} within a
 *     {@code java.util.Collections.synchronizedMap()}:
 *     <p>
 *     <code>
 *       <pre>
 *         Map m =
 *             Collections.synchronizedMap(
 *                 new TernarySearchTree(...));
 *       </pre>
 *     </code>
 *   </li>
 * </ol>
 * <p/>
 * For more information on ternary search trees, see
 * Bentley, J., and Sedgewick, R. Fast algorithms for sorting
 * and searching strings. In <i>Eighth Annual ACM-SIAM Symposium
 * on Discrete Algorithms</i> (1997), SIAM Press.
 *
 * @param <V> Value type.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class TernarySearchTree<V extends Object> implements Map<CharSequence, V> {

    /**
     * Constructs an empty ternary search tree map.
     */
    public TernarySearchTree() {
        _root = null;
        _size = 0;
        _nodeCount = 0;
    }

    /**
     * Construct a new {@code TernarySearchTree} with the same
     * mappings as the specified {@code Map}.
     * @param m Copies mappings from here.
     */
    public TernarySearchTree(final Map<? extends CharSequence, ? extends V> m) {
        for (Map.Entry<? extends CharSequence, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns {@code true} if this map contains no
     * key-value mappings and {@code false} otherwise.
     * @return {@code true} if this map contains no
     * key-value mappings and {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return (_size == 0);
    }

    /**
     * Returns the number of key-value mappings in this tree.
     * @return the number of key-value mappings in this tree.
     */
    @Override
    public int size() {
        return (_size);
    }

    /**
     * Returns the number of nodes used in this map.
     * @return the number of nodes used in this map.
     */
    public long nodeCount() {
        return (_nodeCount);
    }

    /**
     * Clears out all stored values, leaving an empty map.
     */
    @Override
    public void clear() {
        if (_root != null) {
            _root.clear();
            _size = 0;
            _nodeCount = 0;
        }
        return;
    }

    /**
     * Returns {@code true} if the key is in the ternary
     * search tree and {@code false} otherwise.
     * @param key Search for this key in the tree.
     * @return {@code true} if the key is in the ternary
     * search tree and {@code false} otherwise.
     */
    @Override
    public boolean containsKey(final Object key) {
        boolean retcode = false;
        if (key == null) {
            throw (new NullPointerException("null key"));
        } else {
            final TSTNode<V> node = findNode((CharSequence) key);
            if (node != null) {
                retcode = node.isKey();
            }
        }
        return (retcode);
    }

    /**
     * Returns {@code true} if {@code value} is stored in the
     * ternary search tree and {@code false} otherwise.
     * @param value Searches for this value.
     * @return {@code true} if {@code value} is stored in the
     * ternary search tree and {@code false} otherwise.
     */
    @Override
    public boolean containsValue(final Object value) {
        return (_root == null ? false : _root.containsValue(value));
    }

    /**
     * Returns the value associated with {@code key}.
     * If the ternary search tree does not contain the key,
     * then returns {@code null}.
     * @param key Search for this key.
     * @return the value associated with {@code key}.
     * If the ternary search tree does not contain the key,
     * then returns {@code null}.
     */
    @Override
    public V get(final Object key) {
        V retval = null;
        if (key == null) {
            throw (new NullPointerException("null key"));
        } else {
            final TSTNode<V> node = findNode((CharSequence) key);
            if (node != null) {
                retval = node.value();
            }
        }
        return (retval);
    }

    /**
     * Enters a value into the ternary search tree using the
     * text key. If the key is already in the tree, then
     * replaces the existing value with the new value and
     * returns the replaced value. If the key is not in the
     * tree, then {@code null} is returned.
     * @param key The text key.
     * @param value The key's associated value.
     * @return the previous value stored under {@code key}. May
     * return {@code null}.
     */
    @Override
    public V put(final CharSequence key, final V value) {
        TSTNode<V> node;
        TSTNode<V> node2 = null;
        int index;
        final int length = key.length();
        int child = LEFT;
        V retval = null;
        for (node = _root, index = 0; node != null && index < length; index += ((Math.abs(child) * -1) + 1), node2 = node, node = node.child(child + 1)) {
            child = node.split(key.charAt(index));
        }
        if (index == length) {
            node = node2;
        } else {
            if (node2 == null) {
                _root = new TSTNode<V>(key.charAt(index));
                node = _root;
            } else {
                node = new TSTNode<V>(key.charAt(index));
                node2.child((child + 1), node);
            }
            ++index;
            ++_nodeCount;
            for (; index < length; node = node2, ++index, ++_nodeCount) {
                node2 = new TSTNode<V>(key.charAt(index));
                node.child(CENTER, node2);
            }
        }
        if (node.isKey() == false) {
            node.key(true, key);
            ++_size;
        }
        retval = node.value(value);
        return (retval);
    }

    /**
     * Copies all the mappings from the specified map to this
     * tree. This method does nothing more than iterate over
     * the specified map, calling
     * {@link #put(CharSequence, Object)} successively.
     * @param map The copied map.
     */
    @Override
    public void putAll(final Map<? extends CharSequence, ? extends V> map) {
        for (Map.Entry<? extends CharSequence, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return;
    }

    /**
     * Removes the key-value mapping from the tree and returns
     * the now removed value.
     * @param key Remove the mapping at this key.
     * @return the removed value or {@code null} if {@code key}
     * is not in the tree.
     */
    @Override
    public V remove(final Object key) {
        V retval = null;
        if (key == null) {
            throw (new NullPointerException("null key"));
        } else {
            final TSTNode<V> node = findNode((CharSequence) key);
            if (node != null) {
                retval = node.value(null);
                node.key(false, null);
                --_size;
            }
        }
        return (retval);
    }

    /**
     * Returns all keys currently stored in the tree. If the
     * tree is empty, then an empty set is returned. This set is
     * <i>not</i> backed by the tree. Changes to the returned
     * set are not reflected in the tree and changes to the tree
     * are not reflected in the set.
     * @return all keys currently stored in the tree.
     */
    @Override
    public Set<CharSequence> keySet() {
        final Set<CharSequence> retval = new HashSet<CharSequence>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet();
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getKey());
            }
        }
        return (retval);
    }

    /**
     * Returns the words matching the query. This set is
     * <i>not</i> backed by the tree. Changes to the returned
     * set are not reflected in the tree and changes to the tree
     * are not reflected in the set.
     * @param query Match against this query.
     * @return the words matching the query.
     */
    public Set<CharSequence> keySet(final Pattern query) {
        final Set<CharSequence> retval = new HashSet<CharSequence>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet(query);
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getKey());
            }
        }
        return (retval);
    }

    /**
     * Returns at most {@code maxMatches} words matching the
     * query. This set is <i>not</i> backed by the tree.
     * Changes to the returned set are not reflected in the tree
     * and changes to the tree are not reflected in the set.
     * @param query Match against this query.
     * @param maxMatches Match at most this many keys.
     * @return the words matching the query.
     * @exception IllegalArgumentException
     * if {@code maxMatches} is &lt;= zero.
     * @exception IllegalStateException
     * if {@code maxMatches} is exceeded.
     */
    public Set<CharSequence> keySet(final Pattern query, final int maxMatches) throws IllegalArgumentException, IllegalStateException {
        final Set<CharSequence> retval = new HashSet<CharSequence>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet(query, maxMatches);
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getKey());
            }
        }
        return (retval);
    }

    /**
     * Returns a collection of all the trees values. This
     * collection is <i>not</i> backed by the tree. Changes
     * to the returned collection are not reflected in the tree
     * and changes to the tree are not reflected in the
     * collection.
     * @return a collection of all the trees values.
     */
    @Override
    public Collection<V> values() {
        final Collection<V> retval = new ArrayList<V>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet();
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getValue());
            }
        }
        return (retval);
    }

    /**
     * Returns a collection of all the trees values whose
     * keys match the given pattern. This collection is
     * <i>not</i> backed by the tree. Changes to the returned
     * collection are not reflected in the tree and changes to
     * the tree are not reflected in the collection.
     * @param query Match against this query.
     * @return a collection of all the trees values.
     */
    public Collection<V> values(final Pattern query) {
        final Collection<V> retval = new ArrayList<V>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet(query);
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getValue());
            }
        }
        return (retval);
    }

    /**
     * Returns a collection of at most {@code maxMatches} values
     * whose keys match the given pattern. This collection is
     * <i>not</i> backed by the tree. Changes to the returned
     * collection are not reflected in the tree and changes to
     * the tree are not reflected in the collection.
     * @param query Match against this query.
     * @param maxMatches Match at most this many keys.
     * @return a collection of matching keys' values.
     * @exception IllegalArgumentException
     * if {@code maxMatches} is &lt;= zero.
     * @exception IllegalStateException
     * if {@code maxMatches} is exceeded.
     */
    public Collection<V> values(final Pattern query, final int maxMatches) throws IllegalArgumentException, IllegalStateException {
        final Collection<V> retval = new ArrayList<V>();
        if (_root != null) {
            final Set<Map.Entry<CharSequence, V>> entries = entrySet(query, maxMatches);
            for (Map.Entry<CharSequence, V> entry : entries) {
                retval.add(entry.getValue());
            }
        }
        return (retval);
    }

    /**
     * Returns the set of all key-value mappings. If this tree is
     * empty, then an empty set is returned. The returned set is
     * <i>not</i> backed by the tree. Changes to the returned set
     * or to this tree are not reflected in the other.
     * @return the set of all key-value mappings.
     */
    @Override
    public Set<Map.Entry<CharSequence, V>> entrySet() {
        final Set<Map.Entry<CharSequence, V>> retval = new HashSet<Map.Entry<CharSequence, V>>();
        if (_root != null) {
            entries(_root, retval);
        }
        return (retval);
    }

    /**
     * Returns the set of all key-value mappings whose keys match
     * the given query. If this tree is empty, then an empty set
     * is returned. The returned set is <i>not</i> backed by the
     * tree. Changes to the returned set or to this tree are not
     * reflected in the other.
     * @param query Match against this query.
     * @return the set of all key-value mappings.
     */
    public Set<Map.Entry<CharSequence, V>> entrySet(final Pattern query) {
        final Set<Map.Entry<CharSequence, V>> retval = new HashSet<Map.Entry<CharSequence, V>>();
        if (_root != null) {
            final Component[] components = query.components();
            final Queue<TSTSearch<V>> queue = new LinkedList<TSTSearch<V>>();
            queue.offer(new TSTSearch<V>(_root, 0, 0));
            entries(components, queue, retval, Integer.MAX_VALUE);
        }
        return (retval);
    }

    /**
     * Returns the set of at most {@code maxMatches} key-value
     * mappings whose keys match the given query. If this tree is
     * empty, then an empty set is returned. The returned set is
     * <i>not</i> backed by the tree. Changes to the returned set
     * or to this tree are not reflected in the other.
     * @param query Match against this query.
     * @param maxMatches Match at most this many keys.
     * @return the set of at most {@code maxMatches} key-value
     * mappings.
     * Throws IllegalArgumentException
     * if {@code maxMatches} is &lt;= zero.
     */
    public Set<Map.Entry<CharSequence, V>> entrySet(final Pattern query, final int maxMatches) {
        final Set<Map.Entry<CharSequence, V>> retval = new HashSet<Map.Entry<CharSequence, V>>();
        if (maxMatches <= 0) {
            throw (new IllegalArgumentException("maxMatches <= 0 (" + Integer.toString(maxMatches) + ")"));
        } else if (_root != null) {
            final Component[] components = query.components();
            final Queue<TSTSearch<V>> queue = new LinkedList<TSTSearch<V>>();
            queue.offer(new TSTSearch<V>(_root, 0, 0));
            entries(components, queue, retval, maxMatches);
        }
        return (retval);
    }

    /**
     * Returns the keys which are within a specified Hamming
     * distance of character sequence {@code s}. The Hamming
     * distance between two strings of equal length is the
     * number of positions at which corresponding characters are
     * different. One string may be transformed into the other by
     * changing the characters at these positions to the other
     * strings values. The Hamming distance may be thought of as
     * the number of errors in one string.
     * <p/>
     * If this ternary search tree contains a dictionary, then
     * this method may be used to find possible correct spellings
     * for a misspelled word.
     * @param s find the keys within the specified Hamming
     * distance to this character sequence.
     * @param distance the desired Hamming distance.
     * @return the keys which are within a specified Hamming
     * distance of character sequence {@code s}. If no such keys
     * are found, then returns an empty collection.
     */
    public Collection<CharSequence> nearSearch(final CharSequence s, final int distance) {
        final int maxIndex = (s.length() - 1);
        final Queue<TSTSearch<V>> queue = new LinkedList<TSTSearch<V>>();
        TSTSearch<V> searchNode;
        TSTNode<V> node;
        TSTNode<V> child;
        int index;
        int d;
        char c;
        char splitChar;
        final Collection<CharSequence> retval = new ArrayList<CharSequence>();
        queue.offer(new TSTSearch<V>(_root, 0, distance));
        while ((searchNode = queue.poll()) != null) {
            node = searchNode.node();
            index = searchNode.index();
            d = searchNode.matchCount();
            c = s.charAt(index);
            splitChar = node.splitChar();
            if ((d > 0 || c < splitChar) && (child = node.child(LEFT)) != null) {
                queue.offer(new TSTSearch<V>(child, index, d));
            }
            if (d > 0 || c == splitChar) {
                if (node.isKey() == true && index == maxIndex) {
                    retval.add(node.key());
                } else if (index < maxIndex && (child = node.child(CENTER)) != null) {
                    queue.offer(new TSTSearch<V>(child, (index + 1), (c == splitChar ? d : (d - 1))));
                }
            }
            if ((d > 0 || c > splitChar) && (child = node.child(RIGHT)) != null) {
                queue.offer(new TSTSearch<V>(child, index, d));
            }
        }
        return (retval);
    }

    /**
     * Returns the node associated with the literal text key.
     * @param key find the node associated with this key.
     * @return the node associated with the literal text key.
     */
    private TSTNode<V> findNode(final CharSequence key) {
        TSTNode<V> node = null;
        TSTNode<V> node2 = null;
        int index;
        final int length = key.length();
        int child;
        for (node = _root, index = 0; node != null && index < length; index += ((Math.abs(child) * -1) + 1), node2 = node, node = node.child(child + 1)) {
            child = node.split(key.charAt(index));
        }
        return (index == length ? node2 : null);
    }

    /**
     * Collects all entries in lexicographically ascending order.
     * @param node start at this node.
     * @param m the collected entry set.
     */
    private void entries(final TSTNode<V> node, final Set<Map.Entry<CharSequence, V>> m) {
        if (node != null) {
            if (node.isKey() == true) {
                m.add(new TSTEntry<V>(node));
            }
            entries(node.child(LEFT), m);
            entries(node.child(CENTER), m);
            entries(node.child(RIGHT), m);
        }
        return;
    }

    /**
     * Performs the actual entry set collection work based on a
     * search pattern.
     * @param components the search components.
     * @param queue the search entry queue.
     * @param m the matched entries.
     * @param maxMatches the matched entry set maximum allowed
     * size.
     */
    private void entries(final Component[] components, final Queue<TSTSearch<V>> queue, final Set<Map.Entry<CharSequence, V>> m, final int maxMatches) {
        final int length = components.length;
        TSTSearch<V> searchNode;
        TSTNode<V> node;
        int index;
        int matchCount;
        char splitChar;
        int minSize;
        int maxSize;
        int nextIndex;
        TSTNode<V> nextNode;
        int nextMatchCount;
        int i;
        int minRemaining;
        int mSize = 0;
        while ((searchNode = queue.poll()) != null && mSize < maxMatches) {
            node = searchNode.node();
            index = searchNode.index();
            matchCount = searchNode.matchCount();
            splitChar = node.splitChar();
            if (components[index].lessThan(splitChar) == true && (nextNode = node.child(LEFT)) != null) {
                queue.offer(new TSTSearch<V>(nextNode, index, matchCount));
            }
            if (components[index].equalTo(splitChar) == true || components[index].minimumSize() == 0) {
                minSize = components[index].minimumSize();
                maxSize = components[index].maximumSize();
                nextIndex = (index + 1);
                nextNode = node.child(CENTER);
                nextMatchCount = (matchCount + 1);
                if (node.isKey() == true && nextMatchCount >= minSize) {
                    minRemaining = 0;
                    for (i = nextIndex; i < length; ++i) {
                        minRemaining += components[i].minimumSize();
                    }
                    if (minRemaining == 0) {
                        ++mSize;
                        m.add(new TSTEntry<V>(node));
                    }
                }
                if ((nextMatchCount < maxSize || maxSize == Component.NO_MAX_MATCH_LIMIT) && nextNode != null) {
                    queue.offer(new TSTSearch<V>(nextNode, index, nextMatchCount));
                }
                if (nextMatchCount >= minSize && nextIndex < length && nextNode != null) {
                    queue.offer(new TSTSearch<V>(nextNode, nextIndex, 0));
                }
                if (nextMatchCount > minSize && nextIndex < length) {
                    queue.offer(new TSTSearch<V>(node, nextIndex, 0));
                }
            }
            if (components[index].greaterThan(splitChar) && (nextNode = node.child(RIGHT)) != null) {
                queue.offer(new TSTSearch<V>(nextNode, index, matchCount));
            }
        }
        return;
    }

    /**
     * The tree's root node. An empty tree will have a null root.
     * This field is not serialized directly because writeObject
     * serializes only the key mappings.
     */
    private TSTNode<V> _root;

    /**
     * The number of keys in the tree.
     */
    private int _size;

    /**
     * The number of allocated nodes.
     */
    private long _nodeCount;

    /**
     * The left child node is stored in index zero.
     */
    private static final int LEFT = 0;

    /**
     * The center child node is stored in index one.
     */
    private static final int CENTER = 1;

    /**
     * The right child node is stored in index two.
     */
    private static final int RIGHT = 2;

    /**
     * A ternary search tree node has three children nodes.
     */
    private static final int CHILD_COUNT = 3;

    /**
     * Ternary search trees are built out of TSTNodes. This node
     * stores the:
     * <ol>
     *   <li>
     *     comparison character,
     *   </li>
     *   <li>
     *     the left, center and right node references,
     *   </li>
     *   <li>
     *     a flag denoting if this node represents a key,
     *   </li>
     *   <li>
     *     the key text and
     *   </li>
     *   <li>
     *     the mapped value.
     *   </li>
     * </ol>
     */
    private static final class TSTNode<V> {

        /**
         * Constructs a TST node with the given split character
         * and parent node. There are no children initially.
         * @param c the node split character.
         */
        public TSTNode(final char c) {
            _splitChar = c;
            _children = new TSTNode[CHILD_COUNT];
            _keyFlag = false;
            _value = null;
        }

        /**
         * Returns the node split character.
         * @return the node split character.
         */
        public char splitChar() {
            return (_splitChar);
        }

        /**
         * Returns the node's left, center or right child.
         * @param index left, center or right child index.
         * @return the node's left, center or right child.
         */
        @SuppressWarnings("unchecked")
        public TSTNode<V> child(final int index) {
            return (_children[index]);
        }

        /**
         * Returns {@code true} if this node is the final node
         * in a map key and {@code false} otherwise.
         * @return {@code true} if this node is the final node
         * in a map key and {@code false} otherwise.
         */
        public boolean isKey() {
            return (_keyFlag);
        }

        /**
         * Returns the character sequence for this map's key
         * if this is the final node of a map key, If this is not
         * a final node, then returns {@code null}.
         * @return the character sequence for this map's key
         * if this is the final node of a map key,
         */
        public CharSequence key() {
            return (_key);
        }

        /**
         * Returns the node's value if this is a key node.
         * Otherwise returns {@code null}.
         * @return the node's value if this is a key node.
         */
        public V value() {
            return (_value);
        }

        /**
         * Returns {@code true} if this node or any of its
         * children contains {@code value} and {@code false} if
         * the {@code value} is not found. Recursively descends
         * the tree until either the value is found or the tree
         * is exhausted.
         * @param value search for this value in the tree.
         * @return {@code true} if this node or any of its
         * children contains {@code value} and {@code false} if
         * the {@code value} is not found.
         */
        public boolean containsValue(final Object value) {
            boolean retcode = (_keyFlag == true && equalObjects(_value, value) == true);
            if (retcode == false) {
                int index;
                for (index = 0; index < _children.length && retcode == false; ++index) {
                    if (_children[index] != null) {
                        retcode = _children[index].containsValue(value);
                    }
                }
            }
            return (retcode);
        }

        /**
         * Sets the left, center or right child.
         * @param index left, center or right child index.
         * @param node child node.
         */
        public void child(final int index, final TSTNode<V> node) {
            _children[index] = node;
            return;
        }

        /**
         * Stores the flag and key character sequence. This
         * is used to remove a key by setting the key flag to
         * {@code false} and setting the key sequence to
         * {@code null}.
         * @param flag {@code true} if this node is a key final
         * node and {@code false} otherwise.
         * @param key the key character sequence.
         */
        public void key(final boolean flag, final CharSequence key) {
            _keyFlag = flag;
            _key = key;
            return;
        }

        /**
         * Sets the value associated with a key and returns the
         * previously stored value. May return {@code null}.
         * @param value the key's associated value.
         * @return the previously stored value.
         */
        public V value(final V value) {
            final V retval = _value;
            _value = value;
            return (retval);
        }

        /**
         * Returns an integer value {@link #LEFT},
         * {@link #CENTER} or {@link #RIGHT} depending on whether
         * {@code c} is the left, center or right child node.
         * @param c compare this character against the split
         * character.
         * @return an integer value {@link #LEFT},
         * {@link #CENTER} or {@link #RIGHT} depending on whether
         * {@code c} is the left, center or right child node.
         */
        public int split(final char c) {
            int index = ((int) c - (int) _splitChar);
            if (index != 0) {
                index /= Math.abs(index);
            }
            return (index);
        }

        /**
         * Recursively clears its child nodes and then its own
         * children list.
         */
        public void clear() {
            int index;
            for (index = 0; index < _children.length; ++index) {
                if (_children[index] != null) {
                    _children[index].clear();
                    _children[index] = null;
                }
            }
            return;
        }

        /**
         * Returns {@code true} if {@code o1} and {@code o2} are
         * equal and {@code false} otherwise. The objects are
         * equal if:
         * <ul>
         *   <li>
         *     If both {@code o1} and {@code o2} are {@code null}
         *     or
         *   </li>
         *   <li>
         *     If neither is {@code null} and
         *     {@link java.lang.Object#equals(java.lang.Object)}
         *     returns {@code true}
         *   </li>
         * </ul>
         * @param o1 the first object.
         * @param o2 the second object.
         * @return {@code true} if {@code o1} and {@code o2} are
         * equal and {@code false} otherwise.
         */
        private static boolean equalObjects(final Object o1, final Object o2) {
            return ((o1 == null && o2 == null) || (o1 != null && o2 != null && o1.equals(o2) == true));
        }

        /**
         * Character used to decide which node to visit next.
         */
        private final char _splitChar;

        /**
         * The children nodes. The left node is for a character
         * &lt; the split character, the center node is for a
         * character equal to the split character and the
         * right node is for a character &gt; the split
         * character.
         */
        private TSTNode[] _children;

        /**
         * This flag is true if this is a final node in a
         * key.
         */
        private boolean _keyFlag;

        /**
         * If this is a key node, then this is the key.
         */
        private CharSequence _key;

        /**
         * If this is a key node, then this is the associated
         * value.
         */
        private V _value;
    }

    /**
     * TSTEntry is used to place key-value entries into an
     * entry set.
     */
    private static final class TSTEntry<V> implements Map.Entry<CharSequence, V> {

        /**
         * This entry references the given key node.
         * @param node a key node.
         * @exception IllegalArgumentException
         * if {@code node} is not a key.
         */
        public TSTEntry(final TSTNode<V> node) throws IllegalArgumentException {
            assert (node.isKey() == true) : node;
            _node = node;
        }

        /**
         * Returns the entry's key.
         * @return the entry's key.
         */
        @Override
        public CharSequence getKey() {
            return (_node._key);
        }

        /**
         * Returns the entry's value.
         * @return the entry's value.
         */
        @Override
        public V getValue() {
            return (_node._value);
        }

        /**
         * This operation is unsupported because the entry set
         * is <i>not</i> backed by the ternary search tree.
         * @exception UnsupportedOperationException
         * because this operation is not supported.
         */
        @Override
        public V setValue(final V o) {
            throw (new UnsupportedOperationException());
        }

        /**
         * Returns {@code true} if {@code o} is a
         * non-{@code null TSTEntry} instance referencing the
         * same ternary search tree node and {@code false}
         * otherwise.
         * @param o object to be compore with this map entry.
         * @return {@code true} if {@code o} is a
         * non-{@code null TSTEntry} instance referencing the
         * same ternary search tree node
         */
        @Override
        public boolean equals(final Object o) {
            boolean retcode = (this == o);
            if (retcode == false && o instanceof TSTEntry) {
                final TSTEntry entry = (TSTEntry) o;
                retcode = (_node == entry._node);
            }
            return (retcode);
        }

        /**
         * Returns the node's hash code.
         * @return the node's hash code.
         */
        @Override
        public int hashCode() {
            return (_node.hashCode());
        }

        /**
         * Returns a {@code String} representation of this map
         * entry. The string has the format of:
         * <code>
         *   <pre>
         * "&lt;key&gt;"=&lt;value&gt;
         *   </pre>
         * </code>
         * @return a {@code String} representation of this map
         * entry.
         */
        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder();
            buffer.append('"');
            buffer.append(_node._key);
            buffer.append("\"=");
            buffer.append(_node._value);
            return (buffer.toString());
        }

        /**
         * This entry's associated node.
         */
        private final TSTNode<V> _node;
    }

    /**
     * This class is used to store the search information on a
     * queue. This allows the search to be done iteratively
     * rather than recursively.
     * @param <V> the mapped value type.
     */
    private static final class TSTSearch<V> {

        /**
         * Stores a point in a search iteration. This point
         * rests on a specified node and index into the regular
         * expression component array,  and the current RE
         * component's match count. An RE component may need to
         * match multiple nodes in order to be statisfied.
         * @param node the search is at this node.
         * @param index the RE component index.
         * @param matchCount how much of the RE component has
         * been matched to this point.
         */
        public TSTSearch(final TSTNode<V> node, final int index, final int matchCount) {
            _node = node;
            _index = index;
            _matchCount = matchCount;
        }

        /**
         * Returns the next node to search.
         * @return the next node to search.
         */
        public TSTNode<V> node() {
            return (_node);
        }

        /**
         * Returns the regular expression component index.
         * @return the regular expression component index.
         */
        public int index() {
            return (_index);
        }

        /**
         * Returns the regular expression component's match
         * count.
         * @return the regular expression component's match
         * count.
         */
        public int matchCount() {
            return (_matchCount);
        }

        /**
         * Continue the search at this node.
         */
        private final TSTNode<V> _node;

        /**
         * Continue the search at this regular expression
         * component.
         */
        private final int _index;

        /**
         * Continue the search with this regular expression
         * component match count.
         */
        private final int _matchCount;
    }
}
