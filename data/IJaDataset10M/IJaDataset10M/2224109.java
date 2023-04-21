package net.sf.tdsl.tests;

import org.junit.Before;
import org.junit.Test;
import net.sf.tdsl.structures.graphs.*;

public class GraphStructuresExceptionsTest {

    Graph<Integer> falseGraph;

    Graph<Integer> trueGraph;

    @Before
    public void setUp() {
        falseGraph = new AdjacencyList<Integer>(false, false, false, false, false);
        trueGraph = new AdjacencyList<Integer>(true, true, true, true, true);
        falseGraph.add(1);
        falseGraph.add(2);
        falseGraph.add(3);
        falseGraph.addEdge(1, 2);
        falseGraph.addEdge(2, 3);
        trueGraph.add(1, 1);
        trueGraph.add(2, 2);
        trueGraph.add(3, 3);
        trueGraph.addEdge(1, 2, 12);
        trueGraph.addEdge(2, 3, 23);
        trueGraph.addEdge(3, 1, 31);
    }

    @Test(expected = DuplicateVertexException.class)
    public void testAdd1() {
        try {
            trueGraph.add(1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = DuplicateVertexException.class)
    public void testAdd2() {
        try {
            falseGraph.add(1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = VertexWeightException.class)
    public void testAdd3() {
        try {
            falseGraph.add(4, 1.0);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = VertexWeightException.class)
    public void testAdd4() {
        try {
            trueGraph.add(4);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = VertexWeightException.class)
    public void testAdd5() {
        try {
            trueGraph.add(4, "weight");
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MultiedgeCreationException.class)
    public void testAddEdge1() {
        try {
            falseGraph.addEdge(1, 2);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = SelfLoopCreationException.class)
    public void testAddEdge2() {
        try {
            falseGraph.addEdge(1, 1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = EdgeWeightException.class)
    public void testAddEdge3() {
        try {
            falseGraph.addEdge(1, 3, 1.0);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = EdgeWeightException.class)
    public void testAddEdge4() {
        try {
            trueGraph.addEdge(1, 3);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = EdgeWeightException.class)
    public void testAddEdge5() {
        try {
            trueGraph.addEdge(1, 3, "w");
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MissingVertexException.class)
    public void testGetWeight1() {
        try {
            trueGraph.getWeightDouble(4);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = VertexWeightException.class)
    public void testGetWeight2() {
        try {
            falseGraph.getWeightDouble(1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = EdgeWeightException.class)
    public void testGetWeight3() {
        try {
            falseGraph.getWeightDouble(1, 2);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = VertexWeightException.class)
    public void testGetWeight4() {
        try {
            trueGraph.getWeightString(1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = NonMultiedgeMethodException.class)
    public void testGetWeight5() {
        try {
            trueGraph.getWeightDouble(1, 2);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = EdgeWeightException.class)
    public void testGetWeight6() {
        try {
            trueGraph.getWeightString(1, 2, 0);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MissingEdgeException.class)
    public void testGetWeight7() {
        try {
            trueGraph.getWeightDouble(1, 2, -1);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MultiedgeMethodException.class)
    public void testRemove1() {
        try {
            falseGraph.removeEdge(1, 2, 0);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MultiedgeMethodException.class)
    public void testGetNumEdgesBetween() {
        try {
            falseGraph.getNumEdgesBetween(1, 2);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }

    @Test(expected = MissingEdgeException.class)
    public void testRemove2() {
        try {
            falseGraph.removeEdge(4, 5);
        } catch (RuntimeException e) {
            e.getMessage();
            throw e;
        }
    }
}
