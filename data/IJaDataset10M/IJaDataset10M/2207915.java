package net.sf.joafip.redblacktree.service;

import java.util.Stack;
import net.sf.joafip.Fortest;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.redblacktree.entity.IRBTNode;

/**
 * red black tree integrity check<br>
 * A red-black tree satisfies the following conditions:<br>
 * <ul>
 * <li>(S) The tree is sorted, i.e., for every node v the maximal key in the
 * left subtree is smaller than the key of v, and the minimal key in the right
 * subtree is equal to or larger than the key of v.</li>
 * <li>??? ok for the root, not for the leaves -> (RL) The root and the leaves
 * are black.</li>
 * <li>(D) All leaves have the same black depth, i.e., the number of black nodes
 * on the path from the root is the same for all leaves.</li>
 * <li>(R) No path from the root to a leaf contains two consecutive red nodes.</li>
 * </ul>
 * 
 * @author luc peuvrier
 * 
 */
@Fortest
@NotStorableClass
public class RedBlackTreeIntegrityChecker<E> implements IRBTVisitor<E> {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(RedBlackTreeIntegrityChecker.class);

    @NotStorableClass
    private class ObjectIdentity {

        private final Object object;

        private final int identityHashcode;

        public ObjectIdentity(final Object object) {
            super();
            this.object = object;
            identityHashcode = System.identityHashCode(object);
        }

        public Object getObject() {
            return object;
        }

        @Override
        public int hashCode() {
            return identityHashcode;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(final Object obj) {
            return object == ((ObjectIdentity) obj).object;
        }
    }

    private final RedBlackTree<E> redBlackTree;

    private Stack<ObjectIdentity> visitStack;

    private int blackCount;

    private int expectedBlackCount;

    public RedBlackTreeIntegrityChecker(final RedBlackTree<E> redBlackTree) {
        super();
        this.redBlackTree = redBlackTree;
    }

    public void checkTree() throws RBTException {
        visitStack = new Stack<ObjectIdentity>();
        blackCount = 0;
        expectedBlackCount = -1;
        redBlackTree.accept(this);
    }

    /**
	 * @throws RBTException
	 */
    @SuppressWarnings("unchecked")
    public void beginVisit(final IRBTNode<E> node) throws RBTException {
        final ObjectIdentity identityNode = new ObjectIdentity(node);
        if (visitStack.contains(identityNode)) {
            final String message = "cycle detected in tree";
            LOGGER.fatal(message);
            throw new RBTException(message);
        }
        if (visitStack.isEmpty()) {
            if (node.getParent() != null) {
                final String message = "parent link broken for node\n" + node.toString();
                LOGGER.fatal(message);
                throw new RBTException(message);
            }
        } else {
            final IRBTNode<E> parentNode = (IRBTNode<E>) visitStack.peek().getObject();
            if (node.getParent() != parentNode) {
                final String message = "parent link broken for node\n" + node + "\nparent is\n" + node.getParent() + "\nfor expected parent node\n" + parentNode;
                LOGGER.fatal(message);
                throw new RBTException(message);
            }
        }
        visitStack.push(identityNode);
        if (!node.isSentinel() && node.getParent() == null && !node.isBlack()) {
            final String message = "root must be black";
            LOGGER.fatal(message);
            throw new RBTException(message);
        }
        if (node.isSentinel()) {
            if (!node.isBlack()) {
                final String message = "sentinel must be black";
                LOGGER.fatal(message);
                throw new RBTException(message);
            }
        } else {
            final IRBTNode<E> leftSonNode = node.getLeft();
            final IRBTNode<E> rightSonNode = node.getRight();
            try {
                if (!leftSonNode.isSentinel() && leftSonNode.compareTo(node) > 0) {
                    final String message = "left must be less than or equal to parent";
                    LOGGER.fatal(message);
                    throw new RBTException(message);
                }
            } catch (NullPointerException exception) {
                final String message = "left of not sentinel must not be null " + node;
                LOGGER.fatal(message);
                throw new RBTException(message, exception);
            }
            try {
                if (!rightSonNode.isSentinel() && rightSonNode.compareTo(node) < 0) {
                    final String message = "right must be greater than or equal to parent";
                    LOGGER.fatal(message);
                    throw new RBTException(message);
                }
            } catch (NullPointerException exception) {
                final String message = "right son of not sentinel must not be null " + node;
                LOGGER.fatal(message);
                throw new RBTException(message, exception);
            }
            if (leftSonNode.isSentinel() && rightSonNode.isSentinel()) {
            }
            if (node.isRed()) {
                if (!leftSonNode.isBlack()) {
                    final String message = "Both children of every red node are black " + node + " left " + leftSonNode;
                    LOGGER.fatal(message);
                    throw new RBTException(message);
                }
                if (!rightSonNode.isBlack()) {
                    final String message = "Both children of every red node are black " + node + " right " + rightSonNode;
                    LOGGER.fatal(message);
                    throw new RBTException(message);
                }
            }
        }
        if (node.isBlack()) {
            blackCount++;
        }
    }

    @SuppressWarnings("unchecked")
    public void endVisit(final IRBTNode<E> node) throws RBTException {
        final IRBTNode<E> popedNode = (IRBTNode<E>) visitStack.pop().getObject();
        if (popedNode != node) {
            final String message = "visit Error";
            LOGGER.fatal(message);
            throw new RBTException(message);
        }
        if (node.isSentinel()) {
            if (expectedBlackCount == -1) {
                expectedBlackCount = blackCount;
            } else if (blackCount != expectedBlackCount) {
                final String message = "bad black count " + blackCount + " for expected " + expectedBlackCount;
                LOGGER.fatal(message);
                throw new RBTException(message);
            }
        }
        if (node.isBlack()) {
            blackCount--;
        }
    }
}
