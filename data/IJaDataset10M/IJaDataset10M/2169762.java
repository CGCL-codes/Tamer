package net.sf.jdsc;

import static net.sf.jdsc.asserts.NotNull.checkNotNull;
import java.util.Comparator;
import net.sf.jdsc.anno.Sorted;
import net.sf.jdsc.traversal.BinaryInorderTraversal;
import net.sf.jdsc.traversal.TraversalEnumerator;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
@Sorted
public class SortedBinaryTree<E> extends LinkedBinaryTree<E> implements IBinarySearchTree<E> {

    private Comparator<? super E> comparator;

    private final boolean allowDuplicates;

    public SortedBinaryTree(Comparator<? super E> comparator, boolean allowDuplicates) {
        super();
        if (comparator == null) comparator = new ComparableComparator<E>();
        this.comparator = comparator;
        this.allowDuplicates = allowDuplicates;
    }

    public SortedBinaryTree(Comparator<? super E> comparator) {
        this(comparator, true);
    }

    public SortedBinaryTree(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    public SortedBinaryTree() {
        this(null);
    }

    @Override
    public SortedBinaryTree<E> create() {
        return new SortedBinaryTree<E>(comparator, allowDuplicates);
    }

    @Override
    public IBinaryNode<E> insert(E element) throws FullDataStructureException {
        IBinaryNode<E> node = createNode(element);
        add(node);
        return node;
    }

    @Override
    public boolean add(IBinaryNode<E> node) throws PositionNotFoundException, FullDataStructureException {
        if (isEmpty()) return super.addRoot(node);
        IBinaryNode<E> parent = root;
        do {
            E element = node.getElement();
            parent = find(parent, element);
            int comp = compare(element, parent.getElement());
            if (comp < 0) {
                assert !parent.hasLeft();
                return super.addLeft(parent, node);
            }
            if (comp > 0) {
                assert !parent.hasRight();
                return super.addRight(parent, node);
            }
            if (!allowDuplicates) throw new PositionAlreadyExistsException();
            if (!parent.hasRight()) return super.addRight(parent, node);
            parent = parent.getRight();
        } while (true);
    }

    @Override
    public IBinaryNode<E> smallest() {
        IBinaryNode<E> node = root();
        while (node != null && node.hasLeft()) node = node.getLeft();
        return node;
    }

    @Override
    public IBinaryNode<E> greatest() {
        IBinaryNode<E> node = root();
        while (node != null && node.hasRight()) node = node.getRight();
        return node;
    }

    @Override
    public IBinaryNode<E> positionOf(E element) {
        if (isEmpty()) return null;
        IBinaryNode<E> node = find(root(), element);
        if (equals(element, node.getElement())) return node;
        return null;
    }

    @Override
    public IEnumerator<? extends IBinaryNode<E>> nodes() {
        if (root == null) return new EmptyEnumerator<IBinaryNode<E>>();
        return new TraversalEnumerator<E, IBinaryNode<E>>(new BinaryInorderTraversal<E>(root));
    }

    @Override
    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    @Override
    public Comparator<? super E> getComparator() {
        return comparator;
    }

    @Override
    public IBinaryNode<E> delete(E element) {
        IBinaryNode<E> node = positionOf(element);
        if (node != null) delete(node);
        return node;
    }

    @Override
    public boolean delete(IBinaryNode<E> node) throws PositionNotFoundException {
        checkNotNull(node, IBinaryNode.class, PositionNotFoundException.class);
        boolean contained = containsNode(node);
        if (contained) {
            substitute(node);
            size--;
            assert size >= 0;
        }
        return contained;
    }

    protected IBinaryNode<E> substitute(IBinaryNode<E> node) {
        IBinaryNode<E> substitute = null;
        if (node.isInnerNode()) {
            if (!node.hasRight()) {
                substitute = node.getLeft();
                node.setLeft(null);
            } else if (!node.hasLeft()) {
                substitute = node.getRight();
                node.setRight(null);
            } else {
                substitute = locateSubstitute(node);
                substitute(substitute);
                assert substitute.isLeaf();
                substitute.setLeft(node.getLeft());
                substitute.setRight(node.getRight());
            }
        }
        if (isRoot(node)) {
            _setRoot(substitute);
        } else {
            IBinaryNode<E> parent = node.getParent();
            if (parent.isLeft(node)) {
                parent.setLeft(substitute);
            } else {
                assert parent.isRight(node);
                parent.setRight(substitute);
            }
        }
        return substitute;
    }

    protected IBinaryNode<E> locateSubstitute(IBinaryNode<E> node) {
        assert node.hasLeft() && node.hasRight();
        IBinaryNode<E> left = node.getLeft();
        IBinaryNode<E> right = node.getRight();
        IBinaryNode<E> substitute;
        if (left != null && (right == null || height(left) > height(right))) {
            substitute = left;
            while (substitute.hasRight()) substitute = substitute.getRight();
        } else {
            assert right != null;
            substitute = right;
            while (substitute.hasLeft()) substitute = substitute.getLeft();
        }
        return substitute;
    }

    @Override
    public SortedBinaryTree<E> clone() {
        return clone(false);
    }

    @Override
    public SortedBinaryTree<E> clone(boolean deepclone) {
        SortedBinaryTree<E> clone = (SortedBinaryTree<E>) super.clone(deepclone);
        clone.comparator = this.comparator;
        return clone;
    }

    @Override
    public IBinaryNode<E> find(E element) {
        if (isEmpty()) return null;
        return find(root(), element);
    }

    @Override
    public IBinaryNode<E> find(IBinaryNode<E> node, E element) throws PositionNotFoundException {
        checkNotNull(node, IBinaryNode.class, PositionNotFoundException.class);
        while (!node.isLeaf()) {
            int comp = compare(element, node.getElement());
            if (comp < 0 && node.hasLeft()) node = node.getLeft(); else if (comp > 0 && node.hasRight()) node = node.getRight(); else return node;
        }
        return node;
    }

    @Override
    public IEnumerator<? extends IBinaryNode<E>> findAll(E from, E to) {
        return new TraversalEnumerator<E, IBinaryNode<E>>(new BinaryInorderTraversal<E>(root(), greater(from), smaller(to)));
    }

    @Override
    public IBinaryNode<E> smaller(E element) {
        if (isEmpty()) return null;
        IBinaryNode<E> node = find(root(), element);
        int comp = compare(element, node.getElement());
        while (comp < 0) {
            if (node == root()) return null;
            node = node.getParent();
            comp = compare(element, node.getElement());
        }
        return node;
    }

    @Override
    public IBinaryNode<E> greater(E element) {
        if (isEmpty()) return null;
        IBinaryNode<E> node = find(root(), element);
        int comp = compare(element, node.getElement());
        while (comp > 0) {
            if (node == root()) return null;
            node = node.getParent();
            comp = compare(element, node.getElement());
        }
        return node;
    }

    protected int compare(E first, E second) {
        return comparator.compare(first, second);
    }
}
