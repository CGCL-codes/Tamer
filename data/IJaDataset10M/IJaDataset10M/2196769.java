package stixar.graph.order;

import stixar.graph.gen.BasicDGFactory;
import stixar.graph.BasicDigraph;
import stixar.graph.Digraph;
import stixar.graph.BasicNode;
import stixar.graph.BasicEdge;
import stixar.graph.Node;
import stixar.graph.Edge;
import stixar.graph.search.DFS;
import stixar.graph.order.TopSorter;
import junit.framework.TestCase;
import java.util.Random;
import java.util.LinkedList;

public class TopSortTest extends TestCase {

    static final int nodeSize = 1000;

    static final int edgeSize = 4000;

    public TopSortTest() {
        super();
    }

    public void testTopSort() {
        BasicNode[] nodes = new BasicNode[nodeSize];
        BasicDGFactory factory = new BasicDGFactory(nodeSize, edgeSize);
        Random rnd = new Random();
        factory.genNodes(nodeSize);
        factory.genEdges(edgeSize);
        Digraph digraph = factory.digraph();
        Acyclic ac = new Acyclic(nodeSize);
        DFS dfs = new DFS(digraph, ac);
        dfs.run();
        Digraph acyclic = ac.acyclic();
        TopSorter tsorter = new TopSorter(acyclic);
        tsorter.run();
        assertTrue(tsorter.valid());
        for (Node n : acyclic.nodes()) {
            DFS tdfs = new DFS(acyclic, new TSVis(this, n, tsorter));
            tdfs.visit(n);
        }
    }

    public static void main(String[] args) {
        TopSortTest t = new TopSortTest();
        t.testTopSort();
    }
}

class TSVis extends DFS.Visitor {

    protected int sourceNum;

    protected TopSorter tsorter;

    protected TestCase t;

    public TSVis(TestCase t, Node n, TopSorter tsorter) {
        this.t = t;
        sourceNum = tsorter.tsNum(n);
        this.tsorter = tsorter;
    }

    public void discover(Node n) {
        t.assertTrue(tsorter.tsNum(n) >= sourceNum);
    }
}

class Acyclic extends DFS.Visitor {

    BasicDGFactory factory;

    public Acyclic(int nnodes) {
        factory = new BasicDGFactory();
        factory.genNodes(nnodes);
    }

    public void treeEdge(Edge e) {
        factory.edge(e.source().nodeId(), e.target().nodeId());
    }

    public void crossEdge(Edge e) {
        factory.edge(e.source().nodeId(), e.target().nodeId());
    }

    public void fwdEdge(Edge e) {
        factory.edge(e.source().nodeId(), e.target().nodeId());
    }

    public Digraph acyclic() {
        return factory.digraph();
    }
}
