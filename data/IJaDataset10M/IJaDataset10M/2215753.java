package net.sf.jdsc;

import static net.sf.jdsc.asserts.NotNull.checkNotNull;
import java.util.Comparator;

/**
 * @author <a href="mailto:ywyss@users.sourceforge.net">ywyss</a>
 * @version 1.0
 */
public class SplayTree<E> extends RotatingSortedBinaryTree<E> {

    public SplayTree(Comparator<? super E> comparator, boolean allowDuplicates) {
        super(comparator, allowDuplicates);
    }

    public SplayTree(Comparator<? super E> comparator) {
        super(comparator);
    }

    public SplayTree(boolean allowDuplicates) {
        super(allowDuplicates);
    }

    public SplayTree() {
        super();
    }

    @Override
    public SplayTree<E> create() {
        return new SplayTree<E>(getComparator(), isAllowDuplicates());
    }

    @Override
    public IBinaryNode<E> insert(E element) throws FullDataStructureException {
        IBinaryNode<E> x = createNode(element);
        IBinaryNode<E> v = super.find(element);
        if (v != null) {
            splay(v);
            split(x, v);
        }
        setRoot(x);
        return x;
    }

    @Override
    public boolean delete(IBinaryNode<E> node) throws PositionNotFoundException {
        checkNotNull(node, IBinaryNode.class, PositionNotFoundException.class);
        IBinaryNode<E> parent = node.getParent();
        boolean deleted = super.delete(node);
        if (deleted && parent != null) splay(parent);
        return deleted;
    }

    protected void splay(IBinaryNode<E> node) {
        assert checkNotNull(node);
        while (!isRoot(node)) {
            IBinaryNode<E> parent = node.getParent();
            assert parent != null;
            if (parent.isLeft(node)) {
                if (isRoot(parent)) {
                    rotateRight(parent);
                } else {
                    IBinaryNode<E> grandParent = parent.getParent();
                    assert grandParent != null;
                    if (grandParent.isLeft(parent)) {
                        rotateRight(grandParent);
                        rotateRight(parent);
                    } else {
                        rotateRight(parent);
                        rotateLeft(grandParent);
                    }
                }
            } else {
                if (isRoot(parent)) {
                    rotateLeft(parent);
                } else {
                    IBinaryNode<E> grandParent = parent.getParent();
                    assert grandParent != null;
                    if (grandParent.isRight(parent)) {
                        rotateLeft(grandParent);
                        rotateLeft(parent);
                    } else {
                        rotateLeft(parent);
                        rotateRight(grandParent);
                    }
                }
            }
        }
    }

    protected void split(IBinaryNode<E> x, IBinaryNode<E> v) {
        int comp = compare(x.getElement(), v.getElement());
        if (comp == 0 && !isAllowDuplicates()) throw new PositionAlreadyExistsException();
        if (comp > 0) {
            x.setLeft(v);
            x.setRight(v.getRight());
        } else if (comp <= 0) {
            x.setRight(v);
            x.setLeft(v.getLeft());
        }
    }

    @Override
    public IBinaryNode<E> find(IBinaryNode<E> root, E element) throws PositionNotFoundException {
        IBinaryNode<E> node = super.find(root, element);
        if (equals(element, node.getElement())) splay(node);
        return node;
    }
}
