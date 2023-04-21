package tests.degree;

import java.util.Iterator;
import common.IndexEdge;
import algorithms.centralityAlgorithms.degree.AbsGreedyDegree;
import algorithms.centralityAlgorithms.degree.GreedyDegreeContribution;
import algorithms.centralityAlgorithms.degree.GreedyDegreeTopK;
import algorithms.centralityAlgorithms.degree.GroupDegreeAlgorithm;
import algorithms.centralityAlgorithms.degree.DegreeAlgorithm;
import server.common.DummyProgress;
import topology.EdgeInterface;
import topology.GraphInterface;
import topology.GraphAsHashMap;
import javolution.util.FastList;
import javolution.util.Index;
import junit.framework.TestCase;

public class GroupDegreeTest extends TestCase {

    private GraphInterface<Index> m_graph = null;

    private FastList<Index> m_vertices = new FastList<Index>();

    public GroupDegreeTest(String arg0) {
        super(arg0);
    }

    public void setUp() {
        m_graph = new GraphAsHashMap<Index>();
        for (int i = 0; i < 11; i++) {
            m_graph.addVertex(Index.valueOf(i));
            m_vertices.add(Index.valueOf(i));
        }
        m_graph.addEdge(Index.valueOf(0), Index.valueOf(1));
        m_graph.addEdge(Index.valueOf(0), Index.valueOf(2));
        m_graph.addEdge(Index.valueOf(0), Index.valueOf(3));
        m_graph.addEdge(Index.valueOf(1), Index.valueOf(8));
        m_graph.addEdge(Index.valueOf(3), Index.valueOf(4));
        m_graph.addEdge(Index.valueOf(4), Index.valueOf(2));
        m_graph.addEdge(Index.valueOf(4), Index.valueOf(5));
        m_graph.addEdge(Index.valueOf(5), Index.valueOf(6));
        m_graph.addEdge(Index.valueOf(6), Index.valueOf(7));
        m_graph.addEdge(Index.valueOf(7), Index.valueOf(8));
        m_graph.addEdge(Index.valueOf(8), Index.valueOf(9));
        m_graph.addEdge(Index.valueOf(9), Index.valueOf(10));
        m_graph.addEdge(Index.valueOf(10), Index.valueOf(2));
    }

    public void testGroupDegree() {
        Object[] vs = new Object[4];
        vs[0] = Index.valueOf(0);
        vs[1] = Index.valueOf(1);
        vs[2] = m_graph.getEdge(Index.valueOf(0), Index.valueOf(1));
        vs[3] = m_graph.getEdge(Index.valueOf(5), Index.valueOf(6));
        assertEquals(7, GroupDegreeAlgorithm.calculateMixedGroupDegree(vs, m_graph, new DummyProgress(), 1));
        vs = new Object[4];
        vs[0] = Index.valueOf(0);
        vs[1] = Index.valueOf(1);
        vs[2] = Index.valueOf(2);
        vs[3] = m_graph.getEdge(Index.valueOf(7), Index.valueOf(8));
        assertEquals(8, GroupDegreeAlgorithm.calculateMixedGroupDegree(vs, m_graph, new DummyProgress(), 1));
        vs = new Object[3];
        vs[0] = Index.valueOf(3);
        vs[1] = Index.valueOf(1);
        vs[2] = Index.valueOf(2);
        assertEquals(7, GroupDegreeAlgorithm.calculateMixedGroupDegree(vs, m_graph, new DummyProgress(), 1));
    }

    public void testSingleDegree() throws Exception {
        int[] vertices = new int[11];
        for (int i = 0; i < 11; i++) vertices[i] = i;
        DegreeAlgorithm singleDegree = new DegreeAlgorithm(vertices, m_graph, new DummyProgress(), 1);
        assertEquals(4, singleDegree.getDegree(0));
        assertEquals(3, singleDegree.getDegree(1));
        assertEquals(4, singleDegree.getDegree(2));
        assertEquals(3, singleDegree.getDegree(3));
        assertEquals(4, singleDegree.getDegree(4));
        assertEquals(3, singleDegree.getDegree(5));
        assertEquals(3, singleDegree.getDegree(6));
        assertEquals(3, singleDegree.getDegree(7));
        assertEquals(4, singleDegree.getDegree(8));
        assertEquals(3, singleDegree.getDegree(9));
        assertEquals(3, singleDegree.getDegree(10));
    }

    public void testSumOfGroup() throws Exception {
        Index[] group = new Index[1];
        group[0] = Index.valueOf(0);
        assertEquals(4.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(1);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(2);
        assertEquals(4.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(3);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(4);
        assertEquals(4.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(5);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(6);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(7);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(8);
        assertEquals(4.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(9);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[1];
        group[0] = Index.valueOf(10);
        assertEquals(3.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[2];
        group[0] = Index.valueOf(0);
        group[1] = Index.valueOf(1);
        assertEquals(7.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
        group = new Index[3];
        group[0] = Index.valueOf(0);
        group[1] = Index.valueOf(1);
        group[2] = Index.valueOf(7);
        assertEquals(10.0, GroupDegreeAlgorithm.calculateSumGroup(group, m_graph, new DummyProgress(), 1));
    }

    public void testAddition() throws Exception {
        FastList<Index> members = new FastList<Index>();
        members.add(Index.valueOf(0));
        add(members, 4);
        members.add(Index.valueOf(1));
        add(members, 5);
        members = new FastList<Index>();
        members.add(Index.valueOf(1));
        members.add(Index.valueOf(2));
        add(members, 6);
        members = new FastList<Index>();
        members.add(Index.valueOf(2));
        members.add(Index.valueOf(8));
        add(members, 8);
        members = new FastList<Index>();
        members.add(Index.valueOf(1));
        members.add(Index.valueOf(2));
        members.add(Index.valueOf(3));
        add(members, 7);
        members = new FastList<Index>();
        members.add(Index.valueOf(0));
        members.add(Index.valueOf(1));
        members.add(Index.valueOf(2));
        add(members, 7);
        members = new FastList<Index>();
        members.add(Index.valueOf(4));
        members.add(Index.valueOf(6));
        members.add(Index.valueOf(8));
        add(members, 9);
        members = new FastList<Index>();
        members.add(Index.valueOf(4));
        members.add(Index.valueOf(2));
        members.add(Index.valueOf(9));
        members.add(Index.valueOf(8));
        add(members, 10);
        members = new FastList<Index>();
        members.add(Index.valueOf(5));
        members.add(Index.valueOf(3));
        members.add(Index.valueOf(7));
        members.add(Index.valueOf(1));
        add(members, 8);
    }

    public void add(FastList<Index> members, int expected) throws Exception {
        GroupDegreeAlgorithm groupAlg = new GroupDegreeAlgorithm(m_graph, new DummyProgress(), 1);
        for (FastList.Node<Index> vNode = members.head(), end = members.tail(); ((vNode = vNode.getNext()) != end); ) {
            groupAlg.add(vNode.getValue().intValue());
        }
        assertEquals(expected, groupAlg.getGroupDegree());
    }

    public void testContributionSorting() {
        try {
            int[] vertices = new int[m_graph.getNumberOfVertices()];
            int i = 0;
            Iterator<Index> vs = m_graph.getVertices();
            while (vs.hasNext()) {
                vertices[i++] = vs.next().intValue();
            }
            DegreeAlgorithm degree = new DegreeAlgorithm(vertices, m_graph, new DummyProgress(), 1);
            GreedyDegreeContribution alg = new GreedyDegreeContribution(degree);
            alg.setVerticesCandidates(m_vertices);
            Object[] result = alg.getOptimalGroupSizeBounded(m_vertices.size(), new int[0], new IndexEdge[0], new DummyProgress(), 1);
            systemOut(result);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public void testSorting() throws Exception {
        int[] vertices = new int[m_graph.getNumberOfVertices()];
        int i = 0;
        Iterator<Index> vs = m_graph.getVertices();
        while (vs.hasNext()) {
            vertices[i++] = vs.next().intValue();
        }
        DegreeAlgorithm degree = new DegreeAlgorithm(vertices, m_graph, new DummyProgress(), 1);
        AbsGreedyDegree topKDegreeAlg = new GreedyDegreeTopK(degree);
        topKDegreeAlg.setVerticesCandidates(m_vertices);
        int[] given = new int[1];
        given[0] = 1;
        try {
            Index[] result = topKDegreeAlg.getOptimalGroupSizeBounded(4, given, new IndexEdge[0], new DummyProgress(), 1);
            systemOut(result);
        } catch (Exception ex) {
        }
        FastList<EdgeInterface<Index>> pairs = new FastList<EdgeInterface<Index>>();
        EdgeInterface<Index>[] givenPairs = new EdgeInterface[1];
        pairs.add(m_graph.getEdge(Index.valueOf(6), Index.valueOf(7)));
        pairs.add(m_graph.getEdge(Index.valueOf(5), Index.valueOf(6)));
        pairs.add(m_graph.getEdge(Index.valueOf(0), Index.valueOf(1)));
        pairs.add(m_graph.getEdge(Index.valueOf(6), Index.valueOf(5)));
        givenPairs[0] = (EdgeInterface<Index>) m_graph.getEdge(Index.valueOf(7), Index.valueOf(8));
        pairs.add(m_graph.getEdge(Index.valueOf(7), Index.valueOf(6)));
        degree = new DegreeAlgorithm(vertices, m_graph, new DummyProgress(), 1);
        topKDegreeAlg = new GreedyDegreeTopK(degree);
        topKDegreeAlg.setEdgesCandidates(pairs);
        try {
            EdgeInterface<Index>[] resultLinks = topKDegreeAlg.getOptimalEdgesSizeBounded(4, new int[0], givenPairs, new DummyProgress(), 1);
            systemOut(resultLinks);
            topKDegreeAlg = new GreedyDegreeContribution(degree);
            topKDegreeAlg.setEdgesCandidates(pairs);
            resultLinks = topKDegreeAlg.getOptimalEdgesSizeBounded(4, new int[0], givenPairs, new DummyProgress(), 1);
            systemOut(resultLinks);
        } catch (Exception ex) {
        }
    }

    private void systemOut(Object[] result) {
        System.out.print("[");
        for (Object o : result) if (o != null) System.out.print(o + ", ");
        System.out.print("]");
        System.out.println();
    }
}
