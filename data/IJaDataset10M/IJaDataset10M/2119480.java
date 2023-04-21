package com.limegroup.gnutella.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import com.limegroup.gnutella.Assert;

/**
 * An information reTRIEval tree, a.k.a., a prefix tree.  A Trie is similar to
 * a dictionary, except that keys must be strings.  Furthermore, Trie provides
 * an efficient means (getPrefixedBy()) to find all values given just a PREFIX
 * of a key.<p>
 *
 * All retrieval operations run in O(nm) time, where n is the size of the
 * key/prefix and m is the size of the alphabet.  Some implementations may
 * reduce this to O(n log m) or even O(n) time.  Insertion operations are
 * assumed to be infrequent and may be slower.  The space required is roughly
 * linear with respect to the sum of the sizes of all keys in the tree, though
 * this may be reduced if many keys have common prefixes.<p>
 *
 * The Trie can be set to ignore case.  Doing so is the same as making all
 * keys and prefixes lower case.  That means the original keys cannot be
 * extracted from the Trie.<p>
 *
 * Restrictions (not necessarily limitations!)
 * <ul>
 * <li><b>This class is not synchronized.</b> Do that externally if you desire.
 * <li>Keys and values may not be null.
 * <li>The interface to this is not complete.
 * </ul>
 *
 * See http://www.csse.monash.edu.au/~lloyd/tildeAlgDS/Tree/Trie.html for a
 * discussion of Tries.
 *
 * @modified David Soh (yunharla00@hotmail.com)
 *      added getIterator() for enhanced AutoCompleteTextField use.
 *
 */
public class StringTrie<V> {

    /**
     * Our representation consists of a tree of nodes whose edges are labelled
     * by strings.  The first characters of all labels of all edges of a node
     * must be distinct.  Typically the edges are sorted, but this is
     * determined by TrieNode.<p>
     *
     * An abstract TrieNode is a mapping from String keys to values,
     * { <K1, V1>, ..., <KN, VN> }, where all Ki and Kj are distinct for all
     * i != j.  For any node N, define KEY(N) to be the concatenation of all
     * labels on the edges from the root to that node.  Then the abstraction
     * function is:<p>
     *
     * <blockquote>
     *    { <KEY(N), N.getValue() | N is a child of root
     *                              and N.getValue() != null}
     * </blockquote>
     *
     * An earlier version used character labels on edges.  This made
     * implementation simpler but used more memory because one node would be
     * allocated to each character in long strings if that string had no
     * common prefixes with other elements of the Trie.<p>
     *
     * <dl>
     * <dt>INVARIANT:</td>
     * <dd>For any node N, for any edges Ei and Ej from N,<br>
     *     i != j &lt;==&gt;
     *     Ei.getLabel().getCharAt(0) != Ej.getLabel().getCharAt(0)</dd>
     * <dd>Also, all invariants for TrieNode and TrieEdge must hold.</dd>
     * </dl>
     */
    private TrieNode<V> root;

    /**
     * Indicates whever search keys are case-sensitive or not.
     * If true, keys will be canonicalized to lowercase.
     */
    private boolean ignoreCase;

    /**
     * Constructs a new, empty tree.
     */
    public StringTrie(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        clear();
    }

    /**
     * Makes this empty.
     * @modifies this
     */
    public void clear() {
        this.root = new TrieNode<V>();
    }

    /**
     * Returns the canonical version of the given string.<p>
     *
     * In the basic version, strings are added and searched without
     * modification. So this simply returns its parameter s.<p>
     *
     * Other overrides may also perform a conversion to the NFC form
     * (interoperable across platforms) or to the NFKC form after removal of
     * accents and diacritics from the NFKD form (ideal for searches using
     * strings in natural language).<p>
     *
     * Made public instead of protected, because the public Prefix operations
     * below may need to use a coherent conversion of search prefixes.
     */
    public String canonicalCase(final String s) {
        if (!ignoreCase) return s;
        return s.toUpperCase(Locale.US).toLowerCase(Locale.US);
    }

    /**
     * Matches the pattern <tt>b</tt> against the text
     * <tt>a[startOffset...stopOffset - 1]</tt>.
     *
     * @return the first <tt>j</tt> so that:<br>
     *  <tt>0 &lt;= i &lt; b.length()</tt> AND<br>
     *  <tt>a[startOffset + j] != b[j]</tt> [a and b differ]<br>
     *  OR <tt>stopOffset == startOffset + j</tt> [a is undefined];<br>
     *  Returns -1 if no such <tt>j</tt> exists, i.e., there is a match.<br>
     *  Examples:
     *  <ol>
     *  <li>a = "abcde", startOffset = 0, stopOffset = 5, b = "abc"<br>
     *      abcde ==&gt; returns -1<br>
     *      abc
     *  <li>a = "abcde", startOffset = 1, stopOffset = 5, b = "bXd"<br>
     *      abcde ==&gt; returns 1
     *      bXd
     *  <li>a = "abcde", startOffset = 1, stopOffset = 3, b = "bcd"<br>
     *      abc ==&gt; returns 2<br>
     *      bcd
     *  </ol>
     *
     * @requires 0 &lt;= startOffset &lt;= stopOffset &lt;= a.length()
     */
    private final int match(String a, int startOffset, int stopOffset, String b) {
        int i = startOffset;
        for (int j = 0; j < b.length(); j++) {
            if (i >= stopOffset) return j;
            if (a.charAt(i) != b.charAt(j)) return j;
            i++;
        }
        return -1;
    }

    /**
     * Maps the given key (which may be empty) to the given value.
     *
     * @return the old value associated with key, or <tt>null</tt> if none
     * @requires value != null
     * @modifies this
     */
    public V add(String key, V value) {
        key = canonicalCase(key);
        TrieNode<V> node = root;
        int i = 0;
        while (i < key.length()) {
            TrieEdge<V> edge = node.get(key.charAt(i));
            if (edge == null) {
                TrieNode<V> newNode = new TrieNode<V>(value);
                node.put(key.substring(i), newNode);
                return null;
            }
            String label = edge.getLabel();
            int j = match(key, i, key.length(), label);
            Assert.that(j != 0, "Label didn't start with prefix[0].");
            if (j >= 0) {
                TrieNode<V> child = edge.getChild();
                TrieNode<V> intermediate = new TrieNode<V>();
                String a = label.substring(0, j);
                String b = label.substring(j);
                String c = key.substring(i + j);
                if (c.length() > 0) {
                    TrieNode<V> newNode = new TrieNode<V>(value);
                    node.remove(label.charAt(0));
                    node.put(a, intermediate);
                    intermediate.put(b, child);
                    intermediate.put(c, newNode);
                } else {
                    node.remove(label.charAt(0));
                    node.put(a, intermediate);
                    intermediate.put(b, child);
                    intermediate.setValue(value);
                }
                return null;
            }
            Assert.that(j == -1, "Bad return value from match: " + i);
            node = edge.getChild();
            i += label.length();
        }
        V ret = node.getValue();
        node.setValue(value);
        return ret;
    }

    /**
     * Returns the node associated with prefix, or null if none. (internal)
     */
    private TrieNode<V> fetch(String prefix) {
        TrieNode<V> node = root;
        for (int i = 0; i < prefix.length(); ) {
            TrieEdge<V> edge = node.get(prefix.charAt(i));
            if (edge == null) return null;
            String label = edge.getLabel();
            int j = match(prefix, i, prefix.length(), label);
            Assert.that(j != 0, "Label didn't start with prefix[0].");
            if (j != -1) return null;
            i += label.length();
            node = edge.getChild();
        }
        return node;
    }

    /**
     * Returns the value associated with the given key, or null if none.
     *
     * @return the <tt>Object</tt> value or <tt>null</tt>
     */
    public V get(String key) {
        key = canonicalCase(key);
        TrieNode<V> node = fetch(key);
        if (node == null) return null;
        return node.getValue();
    }

    /**
     * Ensures no values are associated with the given key.
     *
     * @return <tt>true</tt> if any values were actually removed
     * @modifies this
     */
    public boolean remove(String key) {
        key = canonicalCase(key);
        TrieNode<V> node = fetch(key);
        if (node == null) return false;
        boolean ret = node.getValue() != null;
        node.setValue(null);
        return ret;
    }

    /**
     * Returns an iterator (of V) of the values mapped by keys in this
     * that start with the given prefix, in any order.  That is, the returned
     * iterator contains exactly the values v for which there exists a key k
     * so that k.startsWith(prefix) and get(k) == v.  The remove() operation
     * on the iterator is unimplemented.
     *
     * @requires this not modified while iterator in use
     */
    public Iterator<V> getPrefixedBy(String prefix) {
        prefix = canonicalCase(prefix);
        return getPrefixedBy(prefix, 0, prefix.length());
    }

    /**
     * Same as getPrefixedBy(prefix.substring(startOffset, stopOffset).
     * This is useful as an optimization in certain applications to avoid
     * allocations.<p>
     *
     * Important: canonicalization of prefix substring is NOT performed here!
     * But it can be performed early on the whole buffer using the public
     * method <tt>canonicalCase(String)</tt> of this.
     *
     * @requires 0 &lt;= startOffset &lt;= stopOffset &lt;= prefix.length
     * @see #canonicalCase(String)
     */
    public Iterator<V> getPrefixedBy(String prefix, int startOffset, int stopOffset) {
        TrieNode<V> node = root;
        for (int i = startOffset; i < stopOffset; ) {
            TrieEdge<V> edge = node.get(prefix.charAt(i));
            if (edge == null) {
                return EmptyIterator.emptyIterator();
            }
            node = edge.getChild();
            String label = edge.getLabel();
            int j = match(prefix, i, stopOffset, label);
            Assert.that(j != 0, "Label didn't start with prefix[0].");
            if (i + j == stopOffset) {
                break;
            } else if (j >= 0) {
                node = null;
                break;
            } else {
                Assert.that(j == -1, "Bad return value from match: " + i);
            }
            i += label.length();
        }
        if (node == null) return EmptyIterator.emptyIterator(); else return new ValueIterator(node);
    }

    /**
     * Returns all values (entire Trie)
     */
    public Iterator<V> getIterator() {
        return new ValueIterator(root);
    }

    /**
     * Returns all the (non-null) values associated with a given
     * node and its children. (internal)
     */
    private class ValueIterator extends UnmodifiableIterator<V> {

        private NodeIterator delegate;

        ValueIterator(TrieNode<V> start) {
            delegate = new NodeIterator(start, false);
        }

        public V next() {
            return delegate.next().getValue();
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }
    }

    /**
     * Ensures that this consumes the minimum amount of memory.  If
     * valueCompactor is not null, also sets each node's value to
     * valueCompactor.apply(node).  Any exceptions thrown by a call to
     * valueCompactor are thrown by this.<p>
     *
     * This method should typically be called after add(..)'ing a number of
     * nodes.  Insertions can be done after the call to compact, but they might
     * be slower.  Because this method only affects the performance of this,
     * there is no <tt>modifies</tt> clause listed.
     */
    public void trim(Function<V, ? extends V> valueCompactor) throws IllegalArgumentException, ClassCastException {
        if (valueCompactor != null) {
            for (Iterator<TrieNode<V>> iter = new NodeIterator(root, true); iter.hasNext(); ) {
                TrieNode<V> node = iter.next();
                node.trim();
                V value = node.getValue();
                if (value != null) node.setValue(valueCompactor.apply(value));
            }
        }
    }

    public class NodeIterator extends UnmodifiableIterator<TrieNode<V>> {

        /** Stack for DFS. Push and pop from back. */
        private ArrayList<Iterator<TrieNode<V>>> stack = new ArrayList<Iterator<TrieNode<V>>>();

        /** The next node to return. */
        private TrieNode<V> nextNode;

        private boolean withNulls;

        /**
         * Creates a new iterator that yields all the nodes of start and its
         * children that have values (ignoring internal nodes).
         */
        public NodeIterator(TrieNode<V> start, boolean withNulls) {
            this.withNulls = withNulls;
            if (withNulls || start.getValue() != null) {
                nextNode = start;
            } else {
                nextNode = null;
                advance(start);
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty() || nextNode != null;
        }

        public TrieNode<V> next() {
            if (nextNode == null) {
                throw new NoSuchElementException();
            }
            TrieNode<V> node = nextNode;
            nextNode = null;
            advance(node);
            return node;
        }

        /**
         * Scan the tree (top-down) starting at the already visited node
         * until finding an appropriate node with not null value for next().
         * Keep unvisited nodes in a stack of siblings iterators.  Return
         * either an empty stack, or a stack whose top will be the next node
         * returned by next().
         */
        private void advance(TrieNode<V> node) {
            Iterator<TrieNode<V>> children = node.childrenForward();
            while (true) {
                int size;
                if (children.hasNext()) {
                    node = children.next();
                    if (children.hasNext()) stack.add(children);
                    if (withNulls || node.getValue() == null) children = node.childrenForward(); else {
                        nextNode = node;
                        return;
                    }
                } else if ((size = stack.size()) == 0) return; else children = stack.remove(size - 1);
            }
        }
    }

    /**
     * Returns a string representation of the tree state of this, i.e., the
     * concrete state.  (The version of toString commented out below returns
     * a representation of the abstract state of this.
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("<root>");
        toStringHelper(root, buf, 1);
        return buf.toString();
    }

    /**
     * Prints a description of the substree starting with start to buf.
     * The printing starts with the given indent level. (internal)
     */
    private void toStringHelper(TrieNode start, StringBuilder buf, int indent) {
        if (start.getValue() != null) {
            buf.append(" -> ");
            buf.append(start.getValue().toString());
        }
        buf.append("\n");
        for (Iterator iter = start.labelsForward(); iter.hasNext(); ) {
            for (int i = 0; i < indent; i++) buf.append(" ");
            String label = (String) iter.next();
            buf.append(label);
            TrieNode child = start.get(label.charAt(0)).getChild();
            toStringHelper(child, buf, indent + 1);
        }
    }
}

/**
 * A node of the Trie.  Each Trie has a list of children, labelled by strings.
 * Each of these [String label, TrieNode child] pairs is considered an "edge".
 * The first character of each label must be distinct.  When managing
 * children, different implementations may trade space for time.  Each node
 * also stores an arbitrary Object value.<p>
 *
 * Design note: this is a "dumb" class.  It is <i>only</i> responsible for
 * managing its value and its children.  None of its operations are recursive;
 * that is Trie's job.  Nor does it deal with case.
 */
final class TrieNode<E> {

    /**
     * The value of this node.
     */
    private E value = null;

    /**
     * The list of children.  Children are stored as a sorted Vector because
     * it is a more compact than a tree or linked lists.  Insertions and
     * deletions are more expensive, but they are rare compared to
     * searching.<p>
     *
     * INVARIANT: children are sorted by distinct first characters of edges,
     * i.e., for all i &lt; j,<br>
     *       children[i].edge.charAt(0) &lt; children[j].edge.charAt(0)
     */
    private ArrayList<TrieEdge<E>> children = new ArrayList<TrieEdge<E>>(0);

    /**
     * Creates a trie with no children and no value.
     */
    public TrieNode() {
    }

    /**
     * Creates a trie with no children and the given value.
     */
    public TrieNode(E value) {
        this.value = value;
    }

    /**
     * Gets the value associated with this node, or null if none.
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value associated with this node.
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Get the nth child edge of this node.
     *
     * @requires 0 &lt;= i &lt; children.size()
     */
    private final TrieEdge<E> get(int i) {
        return children.get(i);
    }

    /**
     * (internal) If exact, returns the unique i so that:
     * children[i].getLabelStart() == c<br>
     * If !exact, returns the largest i so that:
     * children[i].getLabelStart() &lt;= c<br>
     * In either case, returns -1 if no such i exists.<p>
     *
     * This method uses binary search and runs in O(log N) time, where
     * N = children.size().<br>
     * The standard Java binary search methods could not be used because they
     * only return exact matches.  Also, they require allocating a dummy Trie.
     *
     * Example1: Search non exact c == '_' in {[0] => 'a...', [1] => 'c...'};
     *           start loop with low = 0, high = 1;
     *           middle = 0, cmiddle == 'a', c < cmiddle, high = 0 (low == 0);
     *           middle = 0, cmiddle == 'a', c < cmiddle, high = -1 (low == 0);
     *           end loop; return high == -1 (no match, insert at 0).
     * Example2: Search non exact c == 'a' in {[0] => 'a', [1] => 'c'}
     *           start loop with low = 0, high = 1;
     *           middle = 0, cmiddle == 'a', c == cmiddle,
     *           abort loop by returning middle == 0 (exact match).
     * Example3: Search non exact c == 'b' in {[0] => 'a...', [1] => 'c...'};
     *           start loop with low = 0, high = 1;
     *           middle = 0, cmiddle == 'a', cmiddle < c, low = 1 (high == 1);
     *           middle = 1, cmiddle == 'c', c < cmiddle, high = 0 (low == 1);
     *           end loop; return high == 0 (no match, insert at 1).
     * Example4: Search non exact c == 'c' in {[0] => 'a...', [1] => 'c...'};
     *           start loop with low = 0, high = 1;
     *           middle = 0, cmiddle == 'a', cmiddle < c, low = 1 (high == 1);
     *           middle = 1, cmiddle == 'c', c == cmiddle,
     *           abort loop by returning middle == 1 (exact match).
     * Example5: Search non exact c == 'd' in {[0] => 'a...', [1] => 'c...'};
     *           start loop with low = 0, high = 1;
     *           middle = 0, cmiddle == 'a', cmiddle < c, low = 1 (high == 1);
     *           middle = 1, cmiddle == 'c', cmiddle < c, low = 2 (high == 1);
     *           end loop; return high == 1 (no match, insert at 2).
     */
    private final int search(char c, boolean exact) {
        int low = 0;
        int high = children.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            char cmiddle = get(middle).getLabelStart();
            if (cmiddle < c) low = middle + 1; else if (c < cmiddle) high = middle - 1; else return middle;
        }
        if (exact) return -1;
        return high;
    }

    /**
     * Returns the edge (at most one) whose label starts with the given
     * character, or null if no such edge.
     */
    public TrieEdge<E> get(char labelStart) {
        int i = search(labelStart, true);
        if (i < 0) return null;
        TrieEdge<E> ret = get(i);
        Assert.that(ret.getLabelStart() == labelStart);
        return ret;
    }

    /**
     * Inserts an edge with the given label to the given child to this.
     * Keeps all edges binary sorted by their label start.
     *
     * @requires label not empty.
     * @requires for all edges E in this, label.getLabel[0] != E not already
     *  mapped to a node.
     * @modifies this
     */
    public void put(String label, TrieNode<E> child) {
        char labelStart;
        int i;
        if ((i = search(labelStart = label.charAt(0), false)) >= 0) {
            Assert.that(get(i).getLabelStart() != labelStart, "Precondition of TrieNode.put violated.");
        }
        children.add(i + 1, new TrieEdge<E>(label, child));
    }

    /**
     * Removes the edge (at most one) whose label starts with the given
     * character.  Returns true if any edges where actually removed.
     */
    public boolean remove(char labelStart) {
        int i;
        if ((i = search(labelStart, true)) < 0) return false;
        Assert.that(get(i).getLabelStart() == labelStart);
        children.remove(i);
        return true;
    }

    /**
     * Ensures that this's children take a minimal amount of storage.  This
     * should be called after numerous calls to add().
     *
     * @modifies this
     */
    public void trim() {
        children.trimToSize();
    }

    /**
     * Returns the children of this in forward order,
     * as an iterator of TrieNode.
     */
    public Iterator<TrieNode<E>> childrenForward() {
        return new ChildrenForwardIterator();
    }

    /**
     * Maps (lambda(edge) edge.getChild) on children.iterator().
     */
    private class ChildrenForwardIterator extends UnmodifiableIterator<TrieNode<E>> {

        int i = 0;

        public boolean hasNext() {
            return i < children.size();
        }

        public TrieNode<E> next() {
            if (i < children.size()) return get(i++).getChild();
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns the labels of the children of this in forward order,
     * as an iterator of Strings.
     */
    public Iterator<String> labelsForward() {
        return new LabelForwardIterator();
    }

    /**
     * Maps (lambda(edge) edge.getLabel) on children.iterator()
     */
    private class LabelForwardIterator extends UnmodifiableIterator<String> {

        int i = 0;

        public boolean hasNext() {
            return i < children.size();
        }

        public String next() {
            if (i < children.size()) return get(i++).getLabel();
            throw new NoSuchElementException();
        }
    }

    public String toString() {
        Object val = getValue();
        if (val != null) return val.toString();
        return "NULL";
    }
}

/**
 * A labelled edge, i.e., a String label and a TrieNode endpoint.
 */
final class TrieEdge<E> {

    private String label;

    private TrieNode<E> child;

    /**
     * @requires label.size() > 0
     * @requires child != null
     */
    TrieEdge(String label, TrieNode<E> child) {
        this.label = label;
        this.child = child;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Returns the first character of the label, i.e., getLabel().charAt(0).
     */
    public char getLabelStart() {
        return label.charAt(0);
    }

    public TrieNode<E> getChild() {
        return child;
    }
}
