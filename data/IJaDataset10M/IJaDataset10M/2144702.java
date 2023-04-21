package com.sd_editions.collatex.Block;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for BlockStructure and its containing Blocks.
 */
public class BlockStructureTest extends TestCase {

    /**
   * Create the test case
   *
   * @param testName name of the test case
   */
    public BlockStructureTest(String testName) {
        super(testName);
    }

    /**
   * @return the suite of tests being tested
   */
    public static Test suite() {
        return new TestSuite(BlockStructureTest.class);
    }

    public void testBlockRelationships() {
        String firstContent = "First Word";
        String twoContent = "Second Word";
        String threeContent = "Third Word";
        String fourContent = "Fourth Word";
        String fifthContent = "Fifth Word";
        Word word1 = new Word(firstContent);
        Word word2 = new Word(twoContent);
        Word word3 = new Word(threeContent);
        Word word4 = new Word(fourContent);
        Word word5 = new Word(fifthContent);
        BlockStructure document = new BlockStructure();
        assertEquals(document.getNumberOfBlocks(), 0);
        try {
            document.setRootBlock(word1);
        } catch (BlockStructureCascadeException e) {
            fail("Could not set root block to word1." + e);
        }
        assertEquals(document.getNumberOfBlocks(), 1);
        this.checkBlockHasNoRelations(word1);
        this.checkBlockHasNoRelations(word2);
        this.checkBlockHasNoRelations(word3);
        document.setChildBlock(word1, word2);
        assertEquals(document.getNumberOfBlocks(), 2);
        assertTrue(word1.hasFirstChild());
        assertTrue(word1.hasLastChild());
        assertFalse(word1.hasStartParent());
        assertFalse(word1.hasPreviousSibling());
        assertFalse(word1.hasNextSibling());
        assertFalse(word1.hasEndParent());
        assertEquals(word1.getFirstChild(), word2);
        assertEquals(word1.getLastChild(), word2);
        assertTrue(word2.hasStartParent());
        assertTrue(word2.hasEndParent());
        assertFalse(word2.hasPreviousSibling());
        assertFalse(word2.hasNextSibling());
        assertEquals(word2.getStartParent(), word1);
        assertEquals(word2.getEndParent(), word1);
        document.setChildBlock(word1, word3);
        assertEquals(document.getNumberOfBlocks(), 3);
        assertTrue(word1.hasFirstChild());
        assertTrue(word1.hasLastChild());
        assertFalse(word1.hasStartParent());
        assertFalse(word1.hasEndParent());
        assertFalse(word1.hasPreviousSibling());
        assertFalse(word1.hasNextSibling());
        assertEquals(word1.getFirstChild(), word2);
        assertEquals(word1.getLastChild(), word3);
        assertTrue(word2.hasStartParent());
        assertTrue(word2.hasEndParent());
        assertFalse(word2.hasFirstChild());
        assertFalse(word2.hasLastChild());
        assertFalse(word2.hasPreviousSibling());
        assertTrue(word2.hasNextSibling());
        assertEquals(word2.getStartParent(), word1);
        assertEquals(word2.getEndParent(), word1);
        assertEquals(word2.getNextSibling(), word3);
        assertTrue(word3.hasStartParent());
        assertTrue(word3.hasEndParent());
        assertFalse(word3.hasFirstChild());
        assertFalse(word3.hasLastChild());
        assertTrue(word3.hasPreviousSibling());
        assertFalse(word3.hasNextSibling());
        assertEquals(word3.getStartParent(), word1);
        assertEquals(word3.getEndParent(), word1);
        assertEquals(word3.getPreviousSibling(), word2);
        try {
            document.removeBlock(word2);
        } catch (BlockStructureCascadeException e) {
            fail("Could not remove word2 from word1: " + e);
        }
        assertEquals(document.getNumberOfBlocks(), 2);
        assertTrue(word1.hasFirstChild());
        assertTrue(word1.hasLastChild());
        assertFalse(word1.hasStartParent());
        assertFalse(word1.hasEndParent());
        assertFalse(word1.hasPreviousSibling());
        assertFalse(word1.hasNextSibling());
        assertEquals(word1.getFirstChild(), word3);
        assertEquals(word1.getLastChild(), word3);
        assertTrue(word3.hasStartParent());
        assertTrue(word3.hasEndParent());
        assertFalse(word3.hasFirstChild());
        assertFalse(word3.hasLastChild());
        assertFalse(word3.hasPreviousSibling());
        assertFalse(word3.hasNextSibling());
        assertFalse(word2.hasStartParent());
        assertFalse(word2.hasEndParent());
        assertFalse(word2.hasFirstChild());
        assertFalse(word2.hasFirstChild());
        assertFalse(word2.hasPreviousSibling());
        assertFalse(word2.hasNextSibling());
        try {
            document.removeBlock(word1);
            fail("word1 was removed even thou it contains word2");
        } catch (BlockStructureCascadeException e) {
        }
        try {
            document.removeBlock(word1, true);
        } catch (BlockStructureCascadeException e) {
        }
        assertEquals(document.getNumberOfBlocks(), 0);
        this.checkBlockHasNoRelations(word1);
        this.checkBlockHasNoRelations(word2);
        this.checkBlockHasNoRelations(word3);
        try {
            document.setRootBlock(word1);
        } catch (BlockStructureCascadeException e) {
            fail("Could not set root block to word1." + e);
        }
        try {
            document.setRootBlock(word2);
            fail("setRootBlock to word2, but document already has a root Block.");
        } catch (BlockStructureCascadeException e) {
        }
        assertEquals(1, document.getNumberOfBlocks());
        document.setChildBlock(word1, word2);
        assertEquals(2, document.getNumberOfBlocks());
        assertTrue(word1.hasFirstChild());
        assertTrue(word1.hasLastChild());
        assertEquals(word1.getFirstChild(), word2);
        assertEquals(word1.getLastChild(), word2);
        assertTrue(word2.hasStartParent());
        assertTrue(word2.hasEndParent());
        assertEquals(word2.getStartParent(), word1);
        assertEquals(word2.getEndParent(), word1);
        document.setChildBlock(word2, word3);
        assertEquals(3, document.getNumberOfBlocks());
        assertTrue(word2.hasFirstChild());
        assertTrue(word2.hasLastChild());
        assertEquals(word3, word2.getFirstChild());
        assertEquals(word3, word2.getLastChild());
        assertTrue(word3.hasStartParent());
        assertTrue(word3.hasEndParent());
        assertEquals(word2, word3.getStartParent());
        assertEquals(word2, word3.getEndParent());
        document.setNextSibling(word3, word4);
        assertTrue(word3.hasNextSibling());
        assertEquals(word4, word3.getNextSibling());
        assertFalse(word3.hasPreviousSibling());
        assertTrue(word4.hasPreviousSibling());
        assertEquals(word3, word4.getPreviousSibling());
        assertFalse(word3.hasPreviousSibling());
        assertTrue(word3.hasStartParent());
        assertEquals(word2, word3.getStartParent());
        assertTrue(word3.hasEndParent());
        assertEquals(word2, word3.getEndParent());
        assertTrue(word4.hasStartParent());
        assertTrue(word4.hasEndParent());
        assertEquals(word2, word4.getStartParent());
        assertEquals(word2, word4.getEndParent());
        assertTrue(word2.hasFirstChild());
        assertEquals(word3, word2.getFirstChild());
        assertTrue(word2.hasLastChild());
        assertEquals(word4, word2.getLastChild());
        document.setPreviousSibling(word3, word5);
        assertTrue(word3.hasPreviousSibling());
        assertEquals(word5, word3.getPreviousSibling());
        assertTrue(word5.hasNextSibling());
        assertEquals(word3, word5.getNextSibling());
        assertFalse(word5.hasPreviousSibling());
        assertTrue(word3.hasNextSibling());
        assertEquals(word4, word3.getNextSibling());
        assertTrue(word5.hasStartParent());
        assertEquals(word2, word5.getStartParent());
        assertTrue(word5.hasEndParent());
        assertEquals(word2, word5.getEndParent());
        assertTrue(word4.hasStartParent());
        assertEquals(word2, word4.getStartParent());
        assertTrue(word4.hasEndParent());
        assertEquals(word2, word4.getEndParent());
        assertTrue(word2.hasFirstChild());
        assertEquals(word5, word2.getFirstChild());
        assertTrue(word2.hasLastChild());
        assertEquals(word4, word2.getLastChild());
    }

    public void checkBlockHasNoRelations(Block b) {
        assertFalse(b.hasFirstChild());
        assertFalse(b.hasLastChild());
        assertFalse(b.hasStartParent());
        assertFalse(b.hasEndParent());
        assertFalse(b.hasPreviousSibling());
        assertFalse(b.hasNextSibling());
    }
}
