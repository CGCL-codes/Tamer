package net.sf.joafip.btree.entity;

import java.util.Iterator;
import net.sf.joafip.btree.BTreeException;
import net.sf.joafip.btree.service.BTreePageNodeIterator;
import net.sf.joafip.btree.service.IBTreePageAndNodeManager;
import net.sf.joafip.redblacktree.entity.IRBTNode;
import net.sf.joafip.redblacktree.entity.RBTSentinel;
import net.sf.joafip.redblacktree.service.IRBTNodeManager;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.redblacktree.service.RedBlackTree;

/**
 * all that is common to btree page implementation, the specialization must
 * define {@link IBTreePage#isTooLarge()} and
 * {@link IBTreePage#canJoin(IBTreePage)}<br>
 * page store btree node in a red black tree<br>
 * 
 * @author luc peuvrier
 * 
 * @param <E>
 */
public abstract class AbstractBTreePage<E> implements IBTreePage<E>, IRBTNodeManager<E> {

    /** root node of the red black tree for node storing */
    private IRBTNode<E> rootNode;

    /** red black tree manager for nodes of this page */
    private final RedBlackTree<E> tree;

    /** btree page and node manager */
    private final IBTreePageAndNodeManager<E> btreePageAndNodeManager;

    /**
	 * the father node of this page, it is the node in father page that have
	 * this page for its previous element page<br>
	 */
    private IBTreeNode<E> fatherNode;

    /** number of child element of this page */
    private int numberOfChild;

    /**
	 * default constructor for persistence
	 * 
	 */
    public AbstractBTreePage() {
        super();
        tree = null;
        btreePageAndNodeManager = null;
    }

    /**
	 * set the btree page manager at construction
	 * 
	 * @param btreePageAndNodeManager
	 */
    public AbstractBTreePage(final IBTreePageAndNodeManager<E> btreePageAndNodeManager) {
        super();
        tree = new RedBlackTree<E>(this);
        this.btreePageAndNodeManager = btreePageAndNodeManager;
    }

    public IBTreeNode<E> getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(final IBTreeNode<E> fatherNode) {
        this.fatherNode = fatherNode;
    }

    public IBTreePage<E> getFatherPage() {
        final IBTreePage<E> fatherPage;
        if (fatherNode == null) {
            fatherPage = null;
        } else {
            fatherPage = fatherNode.getPage();
        }
        return fatherPage;
    }

    public void addNode(final IBTreeNode<E> btreeNode) throws BTreeException {
        try {
            tree.append(btreeNode.getRbtNode());
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        btreeNode.setPage(this);
    }

    /**
	 * return iterator on node of this page
	 */
    public Iterator<IBTreeNode<E>> iterator() {
        try {
            return new BTreePageNodeIterator<E>(tree);
        } catch (BTreeException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void addNode(final IBTreePage<E> page) throws BTreeException {
        for (IBTreeNode<E> node : page) {
            addNode(node);
        }
    }

    public int getNumberOfElement() throws BTreeException {
        try {
            int numberOfElement = tree.getNumberOfElement();
            final IBTreeNode<E> last = getLastNode();
            if (last != null && last.getElement() == null) {
                numberOfElement--;
            }
            return numberOfElement;
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
    }

    public boolean isEmpty() throws BTreeException {
        try {
            return tree.getNumberOfElement() == 0;
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    public IBTreeNode<E> searchGreater(final E referenceElement) throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = ((BTreeRBTNode<E>) tree.strictlyGreater(referenceElement));
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    @SuppressWarnings("unchecked")
    public IBTreeNode<E> searchGreaterOrEquals(final E referenceElement) throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.closestGreaterOrEqual(referenceElement);
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    public IBTreeNode<E> getFirstNode() throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.first();
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    public IBTreeNode<E> getLastNode() throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.last();
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    public IBTreeNode<E> nextNode(final IBTreeNode<E> currentNode) throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.next(currentNode.getRbtNode());
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    public IBTreeNode<E> previousNode(final IBTreeNode<E> currentNode) throws BTreeException {
        final IBTreeNode<E> node;
        try {
            final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.previous(currentNode.getRbtNode());
            if (treeRBTNode == null) {
                node = null;
            } else {
                node = treeRBTNode.getBtreeNode();
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return node;
    }

    public void removeExisting(final IBTreeNode<E> node) throws BTreeException {
        try {
            tree.deleteExistingNode(node.getRbtNode());
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
    }

    public IBTreePage<E> split() throws BTreeException {
        final IBTreePage<E> page = btreePageAndNodeManager.createNewPage();
        final int size = getNumberOfElement();
        if (size < 3) {
            throw new BTreeException("number of element in page must be greater or equal to 3");
        }
        try {
            for (int count = 0; count < (size / 2) + 1; count++) {
                final BTreeRBTNode<E> treeRBTNode = (BTreeRBTNode<E>) tree.first();
                tree.deleteExistingNode(treeRBTNode);
                page.addNode(treeRBTNode.getBtreeNode());
            }
        } catch (RBTException exception) {
            throw new BTreeException(exception);
        }
        return page;
    }

    public int getNumberOfChild() {
        return numberOfChild;
    }

    public void incrementNumberOfChild() {
        numberOfChild++;
    }

    public void decrementNumberOfChild() {
        numberOfChild--;
    }

    public void updateNumberOfChild() throws BTreeException {
        numberOfChild = 0;
        for (IBTreeNode<E> node : this) {
            final IBTreePage<E> page = node.getPageOfPreviousElement();
            if (page != null) {
                numberOfChild += page.getNumberOfChild() + page.getNumberOfElement();
            }
        }
    }

    public IRBTNode<E> getRootNode() {
        return rootNode;
    }

    public void setRootNode(final IRBTNode<E> rootNode) {
        this.rootNode = rootNode;
    }

    public IRBTNode<E> newSentinel() {
        return new RBTSentinel<E>();
    }
}
