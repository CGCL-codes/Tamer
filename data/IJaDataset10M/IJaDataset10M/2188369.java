package netkit.graph.edgecreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import netkit.classifiers.Classification;
import netkit.graph.Attribute;
import netkit.graph.AttributeCategorical;
import netkit.graph.Edge;
import netkit.graph.Graph;
import netkit.graph.Node;
import netkit.graph.Type;
import netkit.util.VectorMath;

public class BaseCategoricalEdgeCreator extends EdgeCreatorImp {

    private boolean byVal;

    private int numVal;

    private Node[][] nodesByVal = null;

    @Override
    public void initialize(final Graph graph, final String nodeType, final int attributeIndex, final double attributeValue, final int maxEdges) {
        super.initialize(graph, nodeType, attributeIndex, attributeValue, maxEdges);
        byVal = isByAttributeValue();
        numVal = ((AttributeCategorical) attrib).size();
    }

    @Override
    public String getName() {
        return "baseCategoricalEdgeCreator";
    }

    @Override
    public double getWeight(final Node src, final Node dest) {
        if (attributeIndex == -1) throw new IllegalStateException("EdgeCreator[" + getName() + "] has not yet been initialized!");
        if (src.isMissing(attributeIndex) || dest.isMissing(attributeIndex)) return Double.NaN;
        final double v1 = src.getValue(attributeIndex);
        if (byVal && v1 != attributeValue) return Double.NaN;
        final double v2 = dest.getValue(attributeIndex);
        return ((v1 == v2) ? 1 : Double.NaN);
    }

    @Override
    public boolean canHandle(final Attribute attribute) {
        return (attribute.getType() == Type.CATEGORICAL);
    }

    @Override
    public boolean canHandleAttributeValue(final Attribute attribute) {
        return (attribute.getType() == Type.CATEGORICAL);
    }

    @Override
    public boolean isByAttributeValue() {
        return !Double.isNaN(attributeValue);
    }

    @SuppressWarnings("unchecked")
    private void buildNodeCache() {
        if (nodesByVal != null) return;
        List<Node>[] nodes = new ArrayList[numVal];
        Arrays.fill(nodes, null);
        if (byVal) {
            nodes[(int) attributeValue] = new ArrayList<Node>();
        } else {
            for (int i = 0; i < numVal; i++) {
                nodes[i] = new ArrayList<Node>();
            }
        }
        for (Node node : graph.getNodes(nodeType)) {
            if (node.isMissing(attributeIndex)) continue;
            final double v = node.getValue(attributeIndex);
            if (byVal) {
                if (v != attributeValue) continue;
            } else if (Double.isNaN(v)) continue;
            nodes[(int) v].add(node);
        }
        nodesByVal = new Node[numVal][];
        Arrays.fill(nodesByVal, null);
        if (byVal) {
            nodesByVal[(int) attributeValue] = nodes[(int) attributeValue].toArray(new Node[0]);
        } else {
            for (int i = 0; i < numVal; i++) nodesByVal[i] = nodes[i].toArray(new Node[0]);
        }
    }

    @Override
    public Edge[] getEdgesToNearestNeighbors(final Node node) {
        if (graph == null) throw new IllegalArgumentException("EdgeCreator has not yet been initialized!");
        final double v = node.getValue(attributeIndex);
        if (node.isMissing(attributeIndex) || (byVal && v != attributeValue)) return new Edge[0];
        buildNodeCache();
        final List<Edge> edgeList = new ArrayList<Edge>();
        Node[] nbrs = nodesByVal[(int) v];
        if (nbrs == null) return new Edge[0];
        for (Node dest : nbrs) {
            if (dest == node) continue;
            edgeList.add(new Edge(edgetype, node, dest, 1));
        }
        return edgeList.toArray(new Edge[0]);
    }

    @Override
    public double[][] getAssortativityMatrix(final boolean useTrueAssort) {
        if (split == null) throw new IllegalArgumentException("EdgeCreator has not yet built a model!");
        final int numC = split.getView().getAttribute().size();
        final double[][] all = new double[numVal][];
        final double[] classCount = new double[numC];
        Arrays.fill(all, null);
        Arrays.fill(classCount, 0);
        if (byVal) {
            all[(int) attributeValue] = new double[numC];
            Arrays.fill(all[(int) attributeValue], 0);
        } else {
            for (int i = 0; i < numVal; i++) {
                all[i] = new double[numC];
                Arrays.fill(all[i], 0);
            }
        }
        final Classification known = split.getView().getTruth();
        final Node[] nodes = (useTrueAssort ? split.getView().getGraph().getNodes() : split.getTrainSet());
        double num = 0;
        for (Node node : nodes) {
            if (known.isUnknown(node)) continue;
            final double v = node.getValue(attributeIndex);
            if (byVal) {
                if (v != attributeValue) continue;
            } else if (Double.isNaN(v)) continue;
            final int c = known.getClassValue(node);
            num++;
            all[(int) v][c]++;
            classCount[c]++;
        }
        final double[][] matrix = new double[numC][numC];
        for (double[] row : matrix) Arrays.fill(row, 0);
        if (byVal) addCliqueToAssortMatrix(matrix, all[(int) attributeValue]); else {
            for (double[] row : all) addCliqueToAssortMatrix(matrix, row);
        }
        for (int i = 0; i < matrix.length; i++) {
            VectorMath.normalize(matrix[i]);
            VectorMath.multiply(matrix[i], classCount[i] / num);
        }
        return matrix;
    }

    private void addEdges(final ArrayList<Edge> edgeList, int attrVal) {
        final Node[] nodes = nodesByVal[attrVal];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = i + 1; j < nodes.length; j++) {
                edgeList.add(new Edge(edgetype, nodes[i], nodes[j], 1));
                edgeList.add(new Edge(edgetype, nodes[j], nodes[i], 1));
            }
        }
    }

    @Override
    protected void buildEdges() {
        if (edges != null) return;
        if (graph == null) throw new IllegalArgumentException("EdgeCreator has not yet been initialized!");
        buildNodeCache();
        final ArrayList<Edge> edgeList = new ArrayList<Edge>();
        if (byVal) addEdges(edgeList, (int) attributeValue); else for (int v = 0; v < numVal; v++) addEdges(edgeList, v);
        edges = (Edge[]) edgeList.toArray(new Edge[0]);
    }
}
