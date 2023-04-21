package net.sf.joafip.btree.entity;

import java.util.Comparator;
import net.sf.joafip.btree.impl.memory.entity.BTreeNode;
import net.sf.joafip.redblacktree.entity.IRBTComparableNode;
import net.sf.joafip.redblacktree.impl.memory.entity.RBTNode;
import net.sf.joafip.redblacktree.service.RBTException;

public class BTreeRBTNode<E> extends RBTNode<E> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -761328358779497152L;

    private final BTreeNode<E> btreeNode;

    /**
	 * default constructor for persistence
	 * 
	 */
    public BTreeRBTNode() {
        super();
        btreeNode = null;
    }

    public BTreeRBTNode(final BTreeNode<E> btreeNode) {
        super();
        this.btreeNode = btreeNode;
    }

    public BTreeRBTNode(final BTreeNode<E> btreeNode, final Comparator<E> comparator) {
        super(comparator);
        this.btreeNode = btreeNode;
    }

    public BTreeRBTNode(final BTreeNode<E> btreeNode, final E element, final Comparator<E> comparator) {
        super(element, comparator);
        this.btreeNode = btreeNode;
    }

    public BTreeRBTNode(final BTreeNode<E> btreeNode, final E element) {
        super(element);
        this.btreeNode = btreeNode;
    }

    public BTreeNode<E> getBtreeNode() {
        return btreeNode;
    }

    @Override
    public int compareTo(final E element) throws RBTException {
        return btreeNode.compareTo(element);
    }

    @Override
    public int compareTo(final IRBTComparableNode<E> node) throws RBTException {
        return btreeNode.compareTo(((BTreeRBTNode<E>) node).getBtreeNode());
    }
}
