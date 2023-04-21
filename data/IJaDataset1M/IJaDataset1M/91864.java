package nz.ac.waikato.jdsl.core.ref;

import nz.ac.waikato.jdsl.core.api.*;
import java.util.NoSuchElementException;

/** 
 * The postorder iterator gives a postorder traversal
 * of any tree.
 *
 * Creating this iterator takes O(N) where N = the number of positions in the tree,
 * assuming that root, rightChild, leftChild, isInternal are O(1) in the tree
 * implementation.
 * All other methods take O(1) time.
 *
 * @author Ryan Shaun Baker
 * @version JDSL 2.1.1 
 */
public class PostOrderIterator implements PositionIterator {

    /**
    * The array of positions in the tree
    */
    private Position[] positions_;

    /**
    * The tree we are iterating over
    * (only used during construction of array)
    */
    private InspectableBinaryTree btree_;

    /**
    * The tree we are iterating over
    * (only used during construction of array)
    */
    private InspectableTree gtree_;

    /**
    * The current Index
    */
    private int iCurrentIndex_;

    /**
    * The last index of the array
    */
    private int iLastIndex_;

    /** 
     * Constructs a new PostOrderIterator to iterate over the given container
     * Puts a reference to each position into the array --
     * takes O(N) time where N = the number of positions in the container
     * @param tree The tree to iterate over
     */
    public PostOrderIterator(InspectableBinaryTree tree) {
        btree_ = tree;
        positions_ = new Position[tree.size()];
        iCurrentIndex_ = 0;
        iLastIndex_ = positions_.length - 1;
        traverse(tree.root());
        iCurrentIndex_ = -1;
    }

    /**
     * Traverses the tree to fill the array (for binary trees)
     * takes O(N) time when called on the root
     * where N = the number of positions in the container
     * and O(S) time when called on another position
     * where S = the number of positions in the subtree rooted at that position
     * @param curpos Our current position along the tree
     */
    private void traverse(Position curpos) {
        if (btree_.isInternal(curpos)) {
            traverse(btree_.leftChild(curpos));
            traverse(btree_.rightChild(curpos));
        }
        positions_[iCurrentIndex_++] = curpos;
    }

    /** 
     * Constructs a new PostOrderIterator to iterate over the given container
     * Puts a reference to each position into the array --
     * takes O(N) time where N = the number of positions in the container
     * @param tree The tree to iterate over
     */
    public PostOrderIterator(InspectableTree tree) {
        gtree_ = tree;
        positions_ = new Position[tree.size()];
        iCurrentIndex_ = 0;
        iLastIndex_ = positions_.length - 1;
        traverseGenTree(tree.root());
        iCurrentIndex_ = -1;
    }

    /**
     * Traverses the tree to fill the array (for generic trees)
     * takes O(N) time when called on the root
     * where N = the number of positions in the container
     * and O(S) time when called on another position
     * where S = the number of positions in the subtree rooted at that position
     *
     * @param curpos Our current position along the tree
     */
    private void traverseGenTree(Position curpos) {
        PositionIterator kids = gtree_.children(curpos);
        while (kids.hasNext()) {
            traverseGenTree(kids.nextPosition());
        }
        positions_[iCurrentIndex_++] = curpos;
    }

    /**
    * Takes O(1) time
    */
    public boolean hasNext() {
        return iCurrentIndex_ < iLastIndex_;
    }

    /**
    * Takes O(1) time
    */
    public Object nextObject() {
        if (!hasNext()) {
            throw new NoSuchElementException("End of iterator contents reached");
        }
        return (positions_[++iCurrentIndex_]);
    }

    /**
    * Takes O(1) time
    */
    public Object object() {
        return position();
    }

    /** 
   * Takes O(1) time
   * Sets the current node to the first node.
   */
    public void reset() {
        iCurrentIndex_ = -1;
    }

    /**
    * Takes O(1) time
    */
    public Object element() throws NoSuchElementException {
        checkPastEnd();
        return position().element();
    }

    /**
   * Takes O(1) time
   */
    public Position nextPosition() {
        if (!hasNext()) {
            throw new NoSuchElementException("End of iterator contents reached");
        }
        return (positions_[++iCurrentIndex_]);
    }

    /**
    * Takes O(1) time
    */
    public Position position() throws NoSuchElementException {
        checkPastEnd();
        return positions_[iCurrentIndex_];
    }

    /**
    * Takes O(1) time
    * Checks if we're past the end of the iterator
    */
    private void checkPastEnd() throws NoSuchElementException {
        if (iCurrentIndex_ > iLastIndex_) throw new NoSuchElementException("Past End");
        if (iCurrentIndex_ < 0) throw new NoSuchElementException("You need to call next() at least once before you can call element() or position().");
    }

    /** 
     * Takes O(1) time
     * Sets the current node to the first node.
     */
    public void first() {
        iCurrentIndex_ = -1;
    }
}
