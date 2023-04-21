package com.ibm.wala.dila.tests.data.graphs.cg;

import junit.framework.TestCase;
import com.ibm.wala.dila.data.graphs.IEdge;
import com.ibm.wala.dila.data.graphs.cg.Edge;
import com.ibm.wala.dila.data.graphs.cg.INode;
import com.ibm.wala.dila.data.graphs.cg.Node;
import com.ibm.wala.dila.data.java.Method;
import com.ibm.wala.dila.data.java.Type;

/**
 *
 * @author Jan Wloka
 * @version $Id: EdgeTest.java,v 1.2 2008/10/08 21:21:17 jwloka Exp $
 */
public class EdgeTest extends TestCase {

    private Edge<INode> testObj;

    @Override
    protected void setUp() throws Exception {
        INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
        INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
        testObj = new Edge<INode>(n1, n2);
    }

    @Override
    protected void tearDown() throws Exception {
        testObj = null;
    }

    public void testEdge1() {
        try {
            testObj = new Edge<INode>(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testEdge2() {
        try {
            INode n = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
            testObj = new Edge<INode>(n, null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testEdge4() {
        try {
            INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
            INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
            testObj = new Edge<INode>(n1, n2);
        } catch (IllegalArgumentException ex) {
            fail();
        }
    }

    public void testEdge3() {
        try {
            INode n = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
            testObj = new Edge<INode>(null, n);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testGetDest1() {
        INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
        INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
        testObj = new Edge<INode>(n1, n2);
        assertEquals(n2, testObj.getTarget());
    }

    public void testGetSource1() {
        INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
        INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
        testObj = new Edge<INode>(n1, n2);
        assertEquals(n1, testObj.getSource());
    }

    public void testToString1() throws Exception {
        assertEquals(testObj.getSignature(), testObj.toString());
    }

    public void testIsVirtual1() throws Exception {
        assertFalse(testObj.isVirtualEdge());
    }

    public void testIsVirtual2() throws Exception {
        testObj = new Edge<INode>(INode.EXTERNAL_NODE, INode.EXTERNAL_NODE, Type.makeType("LBar;"));
        assertTrue(testObj.isVirtualEdge());
    }

    public void testIsVirtual3() throws Exception {
        testObj = new Edge<INode>(INode.EXTERNAL_NODE, INode.EXTERNAL_NODE, Type.makeType("LBar;"), INode.EXTERNAL_NODE);
        assertTrue(testObj.isVirtualEdge());
    }

    public void testIsVirtual4() throws Exception {
        testObj = new Edge<INode>(INode.EXTERNAL_NODE, INode.EXTERNAL_NODE, null, INode.EXTERNAL_NODE);
        assertFalse(testObj.isVirtualEdge());
    }

    public void testGetSignature1() throws Exception {
        String expected = "Lspec/benchmarks/_209_db/Database;.<init>(Ljava/lang/String;)V --> Ljava/lang/Object;.<init>()V";
        INode n1 = new Node("Lspec/benchmarks/_209_db/Database;.<init>(Ljava/lang/String;)V");
        INode n2 = new Node("Ljava/lang/Object;.<init>()V");
        testObj = new Edge<INode>(n1, n2);
        assertEquals(expected, testObj.getSignature());
    }

    public void testGetSignature2() throws Exception {
        String expected = "Lspec/benchmarks/_209_db/Database;.end()V --> [#receiver:Lspec/benchmarks/_209_db/Database;#static:Lspec/benchmarks/_209_db/Database;.printRec()V]Lspec/benchmarks/_209_db/Database;.printRec()V";
        INode n1 = new Node("Lspec/benchmarks/_209_db/Database;.end()V");
        INode n2 = new Node("Lspec/benchmarks/_209_db/Database;.printRec()V");
        testObj = new Edge<INode>(n1, n2, Type.makeType("Lspec/benchmarks/_209_db/Database;"), new Node("Lspec/benchmarks/_209_db/Database;.printRec()V"));
        assertEquals(expected, testObj.getSignature());
    }

    public void testEquals1() throws Exception {
        INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
        INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
        IEdge<INode> e1 = new Edge<INode>(n1, n2);
        assertTrue(e1.equals(e1));
    }

    public void testEquals2() throws Exception {
        INode n1 = new Node(new Method(Type.makeType("LBar;"), "foo1", "()V"));
        INode n2 = new Node(new Method(Type.makeType("LBar;"), "foo2", "()V"));
        IEdge<INode> e1 = new Edge<INode>(n1, n2);
        IEdge<INode> e2 = new Edge<INode>(n1, n2);
        assertTrue(e1.equals(e2));
    }

    public void testEquals3() throws Exception {
        Method meth1 = new Method(Type.makeType("LBar;"), "foo1", "()V");
        Method meth2 = new Method(Type.makeType("LBar;"), "foo2", "()V");
        INode n1 = new Node(meth1);
        INode n2 = new Node(meth2);
        INode n3 = new Node(meth1);
        INode n4 = new Node(meth2);
        IEdge<INode> e1 = new Edge<INode>(n1, n2);
        IEdge<INode> e2 = new Edge<INode>(n3, n4);
        assertTrue(e1.equals(e2));
    }

    public void testEquals4() throws Exception {
        Method meth1 = new Method(Type.makeType("LBar;"), "foo1", "()V");
        Method meth2 = new Method(Type.makeType("LBar;"), "foo2", "()V");
        Method meth3 = new Method(Type.makeType("LBar;"), "zap1", "()V");
        INode n1 = new Node(meth1);
        INode n2 = new Node(meth2);
        INode n3 = new Node(meth1);
        INode n4 = new Node(meth3);
        IEdge<INode> e1 = new Edge<INode>(n1, n2);
        IEdge<INode> e2 = new Edge<INode>(n3, n4);
        assertFalse(e1.equals(e2));
    }

    public void testEquals5() throws Exception {
        IEdge<INode> e1 = new Edge<INode>(new Node("Ltest/Tests;.<init>()"), new Node("Ljunit/framework/TestCase;.<init>()"));
        IEdge<INode> e2 = new Edge<INode>(new Node("Ltest/Tests;.<init>()"), new Node("Ljunit/framework/TestCase;.<init>()"));
        assertTrue(e1.equals(e2));
    }
}
