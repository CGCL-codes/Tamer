package net.sf.jdsc;

import static net.sf.jdsc.asserts.Assert.check;
import static net.sf.jdsc.asserts.NotNull.checkNotNull;
import java.util.Comparator;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class TreeDictionary<K, V> extends AbstractSortedDictionary<K, V> {

    protected IBinarySearchTree<IEntry<K, V>> tree;

    protected TreeDictionary(IBinarySearchTree<IEntry<K, V>> tree, Comparator<? super K> comparator) {
        super();
        if (tree == null) tree = new SortedBinaryTree<IEntry<K, V>>(new EntryComparator<K>(comparator), true);
        assert check(tree.isAllowDuplicates());
        this.tree = tree;
    }

    public TreeDictionary(IBinarySearchTree<IEntry<K, V>> tree) {
        this(tree, null);
    }

    public TreeDictionary(Comparator<? super K> comparator) {
        this(null, comparator);
    }

    public TreeDictionary() {
        this(null, null);
    }

    @Override
    public TreeDictionary<K, V> create() {
        return new TreeDictionary<K, V>(tree.create());
    }

    @Override
    public IEntry<K, V> insert(K key, V value) throws PositionAlreadyExistsException, FullDataStructureException {
        TreeEntry entry = createEntry(key, value);
        entry.node = tree.insert(entry);
        return entry;
    }

    @Override
    public IEntry<K, V> smallest() {
        IBinaryNode<IEntry<K, V>> node = tree.smallest();
        return node != null ? node.getElement() : null;
    }

    @Override
    public IEntry<K, V> greatest() {
        IBinaryNode<IEntry<K, V>> node = tree.greatest();
        return node != null ? node.getElement() : null;
    }

    @Override
    public IEntry<K, V> smaller(K key) {
        IBinaryNode<IEntry<K, V>> node = tree.smaller(createDummy(key));
        return node != null ? node.getElement() : null;
    }

    @Override
    public IEntry<K, V> greater(K key) {
        IBinaryNode<IEntry<K, V>> node = tree.greater(createDummy(key));
        return node != null ? node.getElement() : null;
    }

    @Override
    public IEntry<K, V> findEntry(K key) {
        if (isEmpty()) return null;
        IBinaryNode<IEntry<K, V>> node = tree.positionOf(createDummy(key));
        if (node != null) return node.getElement();
        return null;
    }

    @Override
    public IEnumerator<? extends IEntry<K, V>> findEntries(K key) {
        if (isEmpty()) return new EmptyEnumerator<IEntry<K, V>>();
        return new EntryFinder(key);
    }

    @Override
    public IEnumerator<? extends IEntry<K, V>> entries() {
        return tree.elements();
    }

    @Override
    public boolean containsEntry(IEntry<K, V> entry) {
        return containsEntry(convert(entry));
    }

    protected boolean containsEntry(TreeEntry entry) {
        return entry != null && tree.containsNode(entry.node);
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean remove(IEntry<K, V> entry) throws PositionNotFoundException {
        return remove(convert(entry));
    }

    protected boolean remove(TreeEntry entry) throws PositionNotFoundException {
        checkNotNull(entry, IEntry.class, PositionNotFoundException.class);
        IBinaryNode<IEntry<K, V>> node = entry.node;
        boolean removed = node != null && tree.delete(node);
        if (removed) entry.node = null;
        return removed;
    }

    @Override
    public int clear() {
        return tree.clear();
    }

    @Override
    public TreeDictionary<K, V> clone() {
        return clone(false);
    }

    @Override
    public TreeDictionary<K, V> clone(boolean deepclone) {
        TreeDictionary<K, V> clone = (TreeDictionary<K, V>) super.clone(deepclone);
        clone.tree = tree.clone(deepclone);
        return clone;
    }

    protected TreeEntry createEntry(K key, V value) {
        return new TreeEntry(key, value);
    }

    TreeEntry convert(IEntry<K, V> entry) {
        return Util.convert(entry, TreeEntry.class);
    }

    protected class TreeEntry extends Entry<K, V> {

        protected IBinaryNode<IEntry<K, V>> node = null;

        public TreeEntry(K key, V value) {
            super(key, value);
        }
    }

    protected class EntryFinder extends AbstractEnumerator<IEntry<K, V>> {

        private final IEntry<K, V> key;

        private IBinaryNode<IEntry<K, V>> current = null;

        protected EntryFinder(IEntry<K, V> key) {
            super();
            assert checkNotNull(key, IEntry.class);
            this.key = key;
        }

        public EntryFinder(K key) {
            this(createDummy(key));
        }

        @Override
        public IEntry<K, V> getCurrent() throws NoSuchObjectException {
            if (current == null) throw new NoSuchObjectException();
            return current.getElement();
        }

        @Override
        public boolean moveNext() {
            if (current == null) current = tree.find(key); else current = current.getRight();
            while (current != null && !key.equals(current.getElement())) {
                current = current.getRight();
                if (current != null) current = tree.find(current, key);
            }
            return current != null;
        }

        @Override
        public boolean movePrevious() {
            return false;
        }

        @Override
        public void reset() {
            current = null;
            current = null;
        }
    }
}
