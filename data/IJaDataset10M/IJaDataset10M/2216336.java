package annas.test.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import annas.graph.DefaultArc;
import annas.graph.DefaultArcFactory;
import annas.graph.DefaultWeight;
import annas.graph.UndirectedGraph;

public class TestUndirectedGraph {

    private UndirectedGraph<String, DefaultArc<String>> graph;

    private String a;

    private String b;

    private String c;

    private String d;

    private String e;

    private String f;

    private String g;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>();
        this.a = new String("A");
        this.b = new String("B");
        this.c = new String("C");
        this.d = new String("D");
        this.e = new String("E");
        this.f = new String("F");
        this.g = new String("G");
    }

    @After
    public void tearDown() throws Exception {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.graph = null;
        System.gc();
    }

    @Test
    public void testUndirectedGraph() {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>();
        assertTrue(this.graph != null);
    }

    @Test
    public void testUndirectedGraphArcFactory() {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>(new DefaultArcFactory());
        assertTrue(this.graph != null);
    }

    @Test
    public void testUndirectedGraphGraphObserver() {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>(new DefaultGraphObserver());
        assertTrue(this.graph.getObserver() != null);
    }

    @Test
    public void testGraph() {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>();
        assertTrue(this.graph != null);
    }

    @Test
    public void testGraphGraphObserver() {
        this.graph = new UndirectedGraph<String, DefaultArc<String>>(new DefaultGraphObserver());
        assertTrue(this.graph.getObserver() != null);
    }

    @Test
    public void testGetObserver() {
        DefaultGraphObserver go = new DefaultGraphObserver();
        this.graph = new UndirectedGraph<String, DefaultArc<String>>(go);
        assertTrue(this.graph.getObserver() != null);
        assertTrue(this.graph.getObserver() == go);
    }

    @Test
    public void testSetObserver() {
        DefaultGraphObserver go = new DefaultGraphObserver();
        DefaultGraphObserver go1 = new DefaultGraphObserver();
        this.graph = new UndirectedGraph<String, DefaultArc<String>>(go);
        assertTrue(this.graph.getObserver() != null);
        assertTrue(this.graph.getObserver() == go);
        this.graph.setObserver(go1);
        assertTrue(this.graph.getObserver() != null);
        assertTrue(this.graph.getObserver() != go);
        assertTrue(this.graph.getObserver() == go1);
    }

    @Test
    public void testAddArc() {
    }

    @Test
    public void testAddNode() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        assertTrue(this.graph.getNuNodes() == 4);
        this.graph.addNode(a);
        assertTrue(this.graph.getNuNodes() == 4);
    }

    @Test
    public void testContains() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        assertTrue(this.graph.getNuNodes() == 4);
        assertTrue(this.graph.contains(a));
        assertTrue(this.graph.contains(b));
        assertTrue(this.graph.contains(c));
        assertTrue(this.graph.contains(d));
        assertFalse(this.graph.contains(e));
    }

    @Test
    public void testGetArcN() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        this.graph.addArc(a, c, new DefaultWeight(1d));
        assertTrue(this.graph.getArc(a).size() == 2);
        assertTrue(this.graph.getArc(b).size() == 1);
        assertTrue(this.graph.getArc(c).size() == 1);
    }

    @Test
    public void testGetArcNN() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        this.graph.addArc(a, b, new DefaultWeight(2d));
        this.graph.addArc(a, c, new DefaultWeight(1d));
        assertTrue(this.graph.getArc(a, b).size() == 2);
        assertTrue(this.graph.getArc(a, c).size() == 1);
        assertTrue(this.graph.getArc(a, d).size() == 0);
        assertTrue(this.graph.getArc(a, e).size() == 0);
    }

    @Test
    public void testGetArcFactory() {
        assertTrue(this.graph.getArcFactory() instanceof DefaultArcFactory);
    }

    @Test
    public void testGetNodeMap() {
        assertTrue(this.graph.getNodeMap() instanceof ArrayList);
        assertTrue(this.graph.getNodeMap().size() == 0);
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        assertTrue(this.graph.getNodeMap().size() == 4);
    }

    @Test
    public void testRemoveArc() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        this.graph.addArc(a, d, new DefaultWeight(2d));
        this.graph.addArc(a, c, new DefaultWeight(1d));
        assertTrue(this.graph.getNodeMap().size() == 4);
        assertTrue(this.graph.getNuArcs() == 6);
        this.graph.removeArc(a, this.graph.getArc(a, b).get(0));
        assertTrue(this.graph.getNuArcs() == 5);
    }

    @Test
    public void testRemoveArcNN() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        this.graph.addArc(a, d, new DefaultWeight(2d));
        this.graph.addArc(a, c, new DefaultWeight(1d));
        assertTrue(this.graph.getNodeMap().size() == 4);
        assertTrue(this.graph.getNuArcs() == 6);
        this.graph.removeArc(a, b);
        assertTrue(this.graph.getNuArcs() == 5);
    }

    @Test
    public void testRemoveArcN() {
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        this.graph.addArc(a, d, new DefaultWeight(2d));
        this.graph.addArc(a, c, new DefaultWeight(1d));
        this.graph.addArc(b, c, new DefaultWeight(1d));
        assertTrue(this.graph.getNodeMap().size() == 4);
        assertTrue(this.graph.getNuArcs() == 8);
        this.graph.removeArc(a);
        assertTrue(this.graph.getNuArcs() == 5);
    }

    @Test
    public void testRemoveNode() {
        this.graph.addNode(a);
        assertTrue(this.graph.contains(a));
        this.graph.removeNode(a);
        assertFalse(this.graph.contains(a));
        this.graph.addNode(a);
        this.graph.addNode(b);
        this.graph.addNode(c);
        this.graph.addNode(d);
        this.graph.addArc(b, a, new DefaultWeight(1d));
        this.graph.addArc(c, a, new DefaultWeight(2d));
        this.graph.addArc(d, a, new DefaultWeight(1d));
        assertTrue(this.graph.contains(a));
        this.graph.removeNode(a);
        assertFalse(this.graph.contains(a));
        assertTrue(this.graph.getNuArcs() == 0);
    }

    @Test
    public void testGetVersion() {
        assertTrue(this.graph.getVersion() == 0);
        this.graph.addNode(a);
        assertTrue(this.graph.getVersion() == 1);
        this.graph.addNode(b);
        assertTrue(this.graph.getVersion() == 2);
        this.graph.addNode(c);
        assertTrue(this.graph.getVersion() == 3);
        this.graph.addNode(d);
        assertTrue(this.graph.getVersion() == 4);
        this.graph.addArc(a, b, new DefaultWeight(1d));
        assertTrue(this.graph.getVersion() == 6);
        this.graph.addArc(a, b, new DefaultWeight(2d));
        assertTrue(this.graph.getVersion() == 8);
        this.graph.addArc(a, c, new DefaultWeight(1d));
        assertTrue(this.graph.getVersion() == 10);
    }
}
