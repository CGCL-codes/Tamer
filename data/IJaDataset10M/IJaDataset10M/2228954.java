package org.openscience.cdk.graph;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.CDKTestCase;
import java.util.List;

/**
 * This class tests the MinimalPathIteratorTest class.
 *
 * @cdk.module test-standard
 *
 * @author     Ulrich Bauer <baueru@cs.tum.edu>
 */
public class MinimalPathIteratorTest extends CDKTestCase {

    public SimpleGraph g;

    @Before
    public void createGraph() {
        g = new SimpleGraph();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        g.addVertex("d");
        g.addVertex("e");
        g.addVertex("f");
        g.addVertex("g");
        g.addVertex("h");
        g.addVertex("i");
        g.addVertex("j");
        g.addVertex("k");
        g.addVertex("l");
        g.addVertex("m");
        g.addVertex("n");
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("c", "d");
        g.addEdge("a", "e");
        g.addEdge("b", "f");
        g.addEdge("c", "g");
        g.addEdge("d", "h");
        g.addEdge("e", "f");
        g.addEdge("f", "g");
        g.addEdge("g", "h");
        g.addEdge("e", "i");
        g.addEdge("f", "j");
        g.addEdge("g", "k");
        g.addEdge("h", "l");
        g.addEdge("i", "j");
        g.addEdge("j", "k");
        g.addEdge("k", "l");
        g.addEdge("l", "m");
        g.addEdge("l", "n");
        g.addEdge("m", "n");
    }

    @Test
    public void testMinimalPathIterator() {
        int count = 0;
        for (MinimalPathIterator i = new MinimalPathIterator(g, "a", "l"); i.hasNext(); ) {
            Assert.assertTrue(((List) i.next()).size() == 5);
            count++;
        }
        Assert.assertEquals(10, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        for (MinimalPathIterator i = new MinimalPathIterator(g, "a", "l"); i.hasNext(); ) {
            Assert.assertTrue(((List) i.next()).size() == 5);
            i.remove();
        }
    }
}
