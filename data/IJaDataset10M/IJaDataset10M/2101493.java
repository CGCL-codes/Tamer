package edu.ucla.sspace.dependency;

import java.util.Iterator;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Keith Stevens
 */
public class SimpleDependencyPathTest extends AbstractPathTestBase {

    @Test
    public void testLength() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        assertEquals(3, path.length());
    }

    @Test
    public void testFirst() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        assertEquals("cat", path.first().word());
        assertEquals("n", path.first().pos());
    }

    @Test
    public void testFirstRelation() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        DependencyRelation rel = path.firstRelation();
        assertEquals("SBJ", rel.relation());
        assertEquals("cat", rel.headNode().word());
        assertEquals("dog", rel.dependentNode().word());
    }

    @Test
    public void testLast() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        assertEquals("pig", path.last().word());
        assertEquals("n", path.last().pos());
    }

    @Test
    public void testFirstRelationRelation() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        DependencyRelation rel = path.lastRelation();
        assertEquals("noarelation", rel.relation());
        assertEquals("whale", rel.headNode().word());
        assertEquals("pig", rel.dependentNode().word());
    }

    @Test
    public void testGetRelation() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        assertEquals("SBJ", path.getRelation(0));
        assertEquals("OBJ", path.getRelation(1));
        assertEquals("noarelation", path.getRelation(2));
    }

    @Test
    public void testGetNode() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        assertEquals("cat", path.getNode(0).word());
        assertEquals("dog", path.getNode(1).word());
        assertEquals("whale", path.getNode(2).word());
        assertEquals("pig", path.getNode(3).word());
    }

    @Test
    public void testIterator() {
        String[][] pathString = { { "cat", "n", "SBJ", "dog", "n" }, { "dog", "n", "OBJ", "whale", "n" }, { "whale", "n", "noarelation", "pig", "n" } };
        DependencyPath path = makePath(pathString);
        Iterator<DependencyRelation> relIter = path.iterator();
        assertTrue(relIter.hasNext());
        assertEquals("SBJ", relIter.next().relation());
        assertEquals("OBJ", relIter.next().relation());
        assertEquals("noarelation", relIter.next().relation());
        assertFalse(relIter.hasNext());
    }
}
