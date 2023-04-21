package net.sf.jdsc;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class LinkedBinaryNode<E> extends AbstractBinaryNode<E> {

    private IBinaryNode<E> parent;

    private IBinaryNode<E> left;

    private IBinaryNode<E> right;

    public LinkedBinaryNode(E element) {
        super(element);
    }

    public LinkedBinaryNode(E element, IBinaryNode<E> parent) {
        this(element);
        setParent(parent);
    }

    public LinkedBinaryNode(E element, IBinaryNode<E> left, IBinaryNode<E> right) {
        this(element);
        setLeft(left);
        setRight(right);
    }

    @Override
    public IBinaryNode<E> getParent() {
        return parent;
    }

    @Override
    public IBinaryNode<E> getLeft() {
        return left;
    }

    @Override
    public IBinaryNode<E> getRight() {
        return right;
    }

    @Override
    protected void _setParent(IBinaryNode<E> parent) {
        this.parent = parent;
    }

    @Override
    protected void _setLeft(IBinaryNode<E> left) {
        this.left = left;
    }

    @Override
    protected void _setRight(IBinaryNode<E> right) {
        this.right = right;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public boolean hasLeft() {
        return left != null;
    }

    @Override
    public boolean hasRight() {
        return right != null;
    }

    @Override
    public boolean isLeft() {
        IBinaryNode<E> parent = getParent();
        return parent != null && this == parent.getLeft();
    }

    @Override
    public boolean isRight() {
        return parent != null && this == parent.getRight();
    }

    @Override
    public LinkedBinaryNode<E> clone() {
        return clone(false);
    }

    @Override
    public LinkedBinaryNode<E> clone(boolean deepclone) {
        LinkedBinaryNode<E> clone = (LinkedBinaryNode<E>) super.clone(deepclone);
        clone.parent = null;
        clone.left = null;
        clone.right = null;
        return clone;
    }
}
