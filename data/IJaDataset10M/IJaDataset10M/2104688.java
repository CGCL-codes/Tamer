package org.jmlspecs.eclipse.example.rac.bst.test;

import junit.framework.Assert;
import org.jmlspecs.eclipse.example.rac.bst.BinarySearchTree;
import org.junit.Test;

/**
 * Tests over a tree with one element.
 * 
 * @author Patrice Chalin, SAnToS & DSRG.org
 * 
 */
public class OneNodeTree extends TreeTestSupertype {

    public OneNodeTree() {
        super();
        tree.insert(theElement);
    }

    @Test
    public void isNotEmpty() {
        Assert.assertTrue(!this.tree.isEmpty());
    }

    @Test
    public void min() {
        Assert.assertEquals(this.theElement, this.tree.findMin());
    }

    @Test
    public void max() {
        Assert.assertEquals(this.theElement, this.tree.findMax());
    }

    @Test
    public void find() {
        Assert.assertEquals(this.theElement, this.tree.find(this.theElement));
        Assert.assertEquals(BinarySearchTree.NULL_ELEMENT, this.tree.find(this.elementNotInTree));
    }

    @Test
    public void makeEmpty() {
        Assert.assertTrue(!this.tree.isEmpty());
        this.tree.makeEmpty();
        Assert.assertTrue(this.tree.isEmpty());
        this.tree.insert(this.theElement);
    }

    @Test
    public void insertAndRemove() {
        Assert.assertTrue(!this.tree.isEmpty());
        this.tree.insert(this.elementNotInTree);
        Assert.assertTrue(!this.tree.isEmpty());
        Assert.assertEquals("find the inserted element", this.elementNotInTree, this.tree.find(this.elementNotInTree));
        this.tree.remove(this.elementNotInTree);
        Assert.assertTrue(!this.tree.isEmpty());
        Assert.assertEquals("find theElement", this.theElement, this.tree.find(this.theElement));
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("[" + this.theElement + "]", this.tree.toString());
    }
}
