package edu.iastate.tripletsat.struct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;
import edu.iastate.tripletsat.AuroraTest;
import edu.iastate.tripletsat.struct.PhyloTree;
import edu.iastate.tripletsat.struct.PhyloTreeNode;

public class PhyloTreeParseTest extends AuroraTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testParseNewickTree() {
        PhyloTree<String> t1 = null;
        try {
            t1 = PhyloTree.parseNewickTree("t1", "(a,b,c)");
        } catch (ParseException ex) {
            fail(ex.toString());
        }
        assertEquals("t1", t1.getName());
        PhyloTreeNode<String> root = t1.getRoot();
        assertEquals(3, root.getNumberOfChildren());
        assertEquals("a", root.getChild(0).getName());
        assertEquals("b", root.getChild(1).getName());
        assertEquals("c", root.getChild(2).getName());
    }

    @Test
    public void testToNewickTreeSimple() {
        String treeString = "";
        treeString = "a";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "(a)";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "(a,b,c)";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
    }

    @Test
    public void testToNewickTreeSimpleWithBlank() throws ParseException {
        String treeString = "";
        treeString = "()";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "(,b,c)";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "(,,)";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
    }

    @Test
    public void testToNewickTreeNested() throws ParseException {
        String treeString = "";
        treeString = "((a,b),c,(d,e))";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "((((a,b),c),d))";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
        treeString = "((a,(b,(c,d))))";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
    }

    @Test
    public void testToNewickTreeNestedWithInternalNames() throws ParseException {
        String treeString = "";
        treeString = "((a,b)x,c,(d,e)y)";
        assertEquals(treeString, makeTree(treeString).toNewickTree());
    }

    @Test
    public void testToNewickTreeIgnoreBlanks() throws ParseException {
        String treeString = "", expected = "";
        treeString = " ( ( a , b ) x , c , ( d , e ) y ) ";
        expected = "((a,b)x,c,(d,e)y)";
        assertEquals(expected, makeTree(treeString).toNewickTree());
        treeString = " ( a x, b x ) ";
        expected = "(a x,b x)";
        assertEquals(expected, makeTree(treeString).toNewickTree());
    }

    @Test
    public void testToNewickTreeIgnoreWeghts() throws ParseException {
        String treeString = "", expected = "";
        treeString = "((a:0,b:123)x:0.9,c,(d,e:1)y:9)";
        expected = "((a,b)x,c,(d,e)y)";
        assertEquals(expected, makeTree(treeString).toNewickTree());
    }
}
