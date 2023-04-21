package subsearch.index.features.extractor;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import subsearch.graph.DefaultEdge;
import subsearch.graph.DefaultGraph;
import subsearch.graph.DefaultNode;
import subsearch.graph.Edge;
import subsearch.graph.Graph;
import subsearch.graph.Node;

public class Subtree extends DefaultGraph {

    int maxSize = 5;

    int n;

    int m;

    ArrayList<IndexEdge> edges;

    LinkedList<Node> activeNodes;

    BitSet iActiveNodes;

    LinkedList<IndexEdge> activeEdges;

    BitSet iActiveEdges;

    int[] activeDegree;

    final String[] canonicalLabelings;

    final Comparator<Node> nodeLabelingComparator;

    ArrayList<ArrayList<Node>> children;

    ArrayList<Node> level;

    ArrayList<Node> nextLevel;

    public boolean[] forbid;

    /**
	 * 
	 * @param g
	 * @param maxSize the number of edges a subtree may contain
	 */
    public Subtree(Graph g, int maxSize) {
        this.maxSize = maxSize;
        List<Node> gNodes = g.nodes();
        List<Edge> gEdges = g.edges();
        this.n = gNodes.size();
        this.m = gEdges.size();
        edges = new ArrayList<IndexEdge>(gEdges.size());
        for (Node u : gNodes) this.addNode(u);
        for (Edge e : gEdges) this.addEdge((DefaultNode) this.getNode(e.getFirstNode().getIndex()), (DefaultNode) this.getNode(e.getSecondNode().getIndex()), e);
        activeNodes = new LinkedList<Node>();
        iActiveNodes = new BitSet(n);
        activeEdges = new LinkedList<IndexEdge>();
        iActiveEdges = new BitSet(m);
        activeDegree = new int[n];
        forbid = new boolean[m];
        canonicalLabelings = new String[n];
        nodeLabelingComparator = new Comparator<Node>() {

            public int compare(Node o1, Node o2) {
                return canonicalLabelings[o1.getIndex()].compareTo(canonicalLabelings[o2.getIndex()]);
            }
        };
        children = new ArrayList<ArrayList<Node>>(n);
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<Node>());
        }
        level = new ArrayList<Node>();
        nextLevel = new ArrayList<Node>();
        clearLists();
    }

    /**
	 * @super
	 */
    public void addEdge(DefaultNode u, DefaultNode v, Object label) {
        IndexEdge edge = new IndexEdge(u, v, label, edges.size());
        u.addEdge(edge);
        v.addEdge(edge);
        edges.add(edge);
        super.edgeCount++;
    }

    /**
	 * Extends the default edge by an index.
	 */
    public class IndexEdge extends DefaultEdge {

        private int index;

        protected IndexEdge(Node u, Node v, Object label, int index) {
            super(u, v, label);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public void addActiveNode(Node v) {
        iActiveNodes.set(v.getIndex());
        activeNodes.push(v);
    }

    public void removeLastActiveNode() {
        Node v = activeNodes.pop();
        iActiveNodes.clear(v.getIndex());
    }

    public void addActiveEdge(IndexEdge e) {
        iActiveEdges.set(e.getIndex());
        activeEdges.push(e);
        Node u = e.getFirstNode();
        Node v = e.getSecondNode();
        if (iActiveNodes.get(u.getIndex())) {
            addActiveNode(v);
        } else {
            addActiveNode(u);
        }
        activeDegree[u.getIndex()]++;
        activeDegree[v.getIndex()]++;
    }

    public void removeLastActiveEdge() {
        IndexEdge e = activeEdges.pop();
        iActiveEdges.clear(e.getIndex());
        removeLastActiveNode();
        activeDegree[e.getFirstNode().getIndex()]--;
        activeDegree[e.getSecondNode().getIndex()]--;
    }

    public void forbidEdge(IndexEdge e) {
        forbid[e.getIndex()] = true;
    }

    public void allowEdges(Collection<IndexEdge> edges) {
        for (IndexEdge e : edges) {
            forbid[e.getIndex()] = false;
        }
    }

    public List<IndexEdge> getExtensions() {
        LinkedList<IndexEdge> result = new LinkedList<IndexEdge>();
        if (activeNodes.size() > maxSize) return result;
        for (Node v : activeNodes) {
            for (Edge e : v.getEdges()) {
                int edgeIndex = ((IndexEdge) e).getIndex();
                if (!iActiveEdges.get(edgeIndex) && !iActiveNodes.get(e.getOppositeNode(v).getIndex()) && !forbid[edgeIndex]) {
                    result.add((IndexEdge) e);
                }
            }
        }
        return result;
    }

    private Iterable<IndexEdge> getActiveEdges(final Node u) {
        return new Iterable<IndexEdge>() {

            public Iterator<IndexEdge> iterator() {
                return new Iterator<IndexEdge>() {

                    Iterator<Edge> i = u.getEdges().iterator();

                    IndexEdge current;

                    public boolean hasNext() {
                        while (i.hasNext()) {
                            if (iActiveEdges.get((current = (IndexEdge) i.next()).getIndex())) return true;
                        }
                        return false;
                    }

                    public IndexEdge next() {
                        return current;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public String getCanonicalLabeling() {
        for (Node v : activeNodes) {
            if (activeDegree[v.getIndex()] <= 1) {
                nextLevel.add(v);
            }
        }
        int[] activeDegreeClone = activeDegree.clone();
        while (!nextLevel.isEmpty()) {
            ArrayList<Node> tmp = level;
            level = nextLevel;
            nextLevel = tmp;
            nextLevel.clear();
            for (Node u : level) {
                for (Edge e : getActiveEdges(u)) {
                    Node v = e.getOppositeNode(u);
                    if (activeDegreeClone[v.getIndex()] != 1 || (!nextLevel.isEmpty() && v == nextLevel.get(0))) {
                        int degree = --activeDegreeClone[v.getIndex()];
                        children.get(v.getIndex()).add(u);
                        if (degree == 1) {
                            nextLevel.add(v);
                        }
                    }
                }
            }
        }
        String label;
        ArrayList<Node> roots = level;
        if (roots.size() == 1) {
            label = getCanonicalLabeling(roots.get(0), children);
        } else {
            String label1 = getCanonicalLabeling(roots.get(0), children);
            String label2 = getCanonicalLabeling(roots.get(1), children);
            String edgeLabel = FeatureExtractor.getLabel((Edge) getEdge(roots.get(0), roots.get(1)).getLabel());
            if (label1.compareTo(label2) < 0) label = label1 + edgeLabel + label2; else label = label2 + edgeLabel + label1;
        }
        clearLists();
        return label;
    }

    private String getCanonicalLabeling(Node u, ArrayList<? extends List<Node>> children) {
        int childCount = children.get(u.getIndex()).size();
        if (childCount == 0) {
            return FeatureExtractor.getLabel((Node) u.getLabel()) + "$";
        } else if (childCount == 1) {
            Node child = children.get(u.getIndex()).get(0);
            Edge edge = getEdge(u, child);
            String edgeLabel = FeatureExtractor.getLabel((Edge) edge.getLabel());
            canonicalLabelings[child.getIndex()] = edgeLabel + getCanonicalLabeling(child, children);
            return FeatureExtractor.getLabel((Node) u.getLabel()) + canonicalLabelings[child.getIndex()] + "$";
        } else {
            List<Node> uChildren = children.get(u.getIndex());
            for (Node v : uChildren) {
                Edge edge = getEdge(u, v);
                String edgeLabel = FeatureExtractor.getLabel((Edge) edge.getLabel());
                canonicalLabelings[v.getIndex()] = edgeLabel + getCanonicalLabeling(v, children);
            }
            Collections.sort(uChildren, nodeLabelingComparator);
            StringBuilder bc = new StringBuilder(FeatureExtractor.getLabel((Node) u.getLabel()));
            for (Node c : uChildren) bc.append(canonicalLabelings[c.getIndex()]);
            bc.append("$");
            return bc.toString();
        }
    }

    private void clearLists() {
        for (int i = 0; i < n; i++) {
            children.get(i).clear();
        }
        level.clear();
        nextLevel.clear();
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        if (activeEdges.isEmpty()) {
            b.append(activeNodes.getFirst().getIndex());
        } else {
            for (IndexEdge e : activeEdges) {
                b.append(e.getFirstNode().getIndex() + " -(" + e.getIndex() + ")- " + e.getSecondNode().getIndex() + "\n");
            }
        }
        return b.toString();
    }
}
