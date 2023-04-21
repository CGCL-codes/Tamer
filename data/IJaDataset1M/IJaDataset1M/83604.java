package fr.inria.zvtm.nodetrix;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.SwingUtilities;
import fr.inria.zvtm.animation.AnimationManager;
import fr.inria.zvtm.engine.SwingWorker;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.nodetrix.lll.Edge;
import fr.inria.zvtm.nodetrix.lll.LinLogEdge;
import fr.inria.zvtm.nodetrix.lll.LinLogNode;
import fr.inria.zvtm.nodetrix.lll.LinLogOptimizerModularity;
import fr.inria.zvtm.nodetrix.lll.MinimizerBarnesHut;
import fr.inria.zvtm.nodetrix.lll.Node;
import fr.inria.zvtm.nodetrix.MatrixSizeComparator;

public class NodeTrixViz {

    public static final double CELL_SIZE = 20;

    public static final double CELL_SIZE_HALF = CELL_SIZE / 2;

    public static final int GROUP_LABEL_HALF_WIDTH = 50;

    public static final int LINLOG_ITERATIONS = 100;

    public static final int MATRIX_NODE_LABEL_DIST_BORDER = 3;

    public static final int MATRIX_NODE_LABEL_OCCLUSION_WIDTH = 150;

    public static final int DURATION_GENERAL = 300;

    public static final int DURATION_NODEMOVE = 300;

    public static final int IA_STATE_DEFAULT = 0;

    public static final int IA_STATE_HIGHLIGHT = 1;

    public static final int IA_STATE_HIGHLIGHT_INCOMING = 2;

    public static final int IA_STATE_HIGHLIGHT_OUTGOING = 3;

    public static final int IA_STATE_SELECTED = 4;

    public static final int IA_STATE_FADE = 5;

    public static final int IA_STATE_RELATED = 6;

    public static final int IA_STATE_EXPAND = 7;

    public static final int IA_STATE_COLLAPSE = 8;

    public static final float EXTRA_ALPHA_MAX_LENGHT = 1500;

    public static final float EXTRA_ALPHA_MIN_LENGHT = 100;

    public static final float EXTRA_ALPHA_MIN = .1f;

    public static final double LINLOG_QUALITY = 100;

    public static final int APPEARANCE_EXTRA_EDGE = 0;

    public static final int APPEARANCE_INTRA_EDGE = 1;

    private VirtualSpace vs;

    double SCALE = 40;

    double REPU_EXPONENT = -1.0;

    double ATTR_EXPONENT = 2.0;

    double GRAV_FACTOR = 0.05;

    private Vector<Matrix> matrices = new Vector<Matrix>();

    private HashSet<NTNode> nodes = new HashSet<NTNode>();

    private HashSet<NTEdge> edges = new HashSet<NTEdge>();

    private HashSet<NTNode> highlightedNodes = new HashSet<NTNode>();

    private HashSet<NTEdge> highlightedEdges = new HashSet<NTEdge>();

    private AnimationManager am;

    /**Vector that stores all edges considered by the linLog cluster algorithm*
     */
    public NodeTrixViz() {
    }

    public NodeTrixViz(double s, double re, double ae, double gv) {
        SCALE = s;
        REPU_EXPONENT = re;
        ATTR_EXPONENT = ae;
        GRAV_FACTOR = gv;
    }

    public Matrix createAddMatrix(String name, Vector<NTNode> nodes) {
        Matrix m = new Matrix(name, nodes);
        nodes.addAll(nodes);
        matrices.add(m);
        return m;
    }

    public void addMatrices(Matrix[] v) {
        for (Matrix m : v) {
            matrices.add(m);
        }
    }

    /**This method causes the NTNodes to be clustered using the LinLog Algorithm.
	 * It returns a HashMap mapping each NTNode to an integer depicting its cluster.
	 * The created matrices are returned but not added to the global matrix list.As
	 * soon as the nodes are added to the new matrix, they are not longer aware of 
	 * their old matrix.
	 * Use this method as an alternative to <code>addMatrix(String name, Vector<NTNode> nodes)</code>
	 **/
    public Matrix[] createMatricesByClustering(Collection<NTNode> nodes, List<LinLogEdge> edges) {
        LinLogOptimizerModularity llalgo = new LinLogOptimizerModularity();
        ArrayList<LinLogNode> llNodes = new ArrayList<LinLogNode>();
        llNodes.addAll(nodes);
        Map<LinLogNode, Integer> clusterMap = llalgo.execute(llNodes, edges, false);
        HashMap<Integer, Matrix> newMatrices = new HashMap<Integer, Matrix>();
        for (LinLogNode lln : clusterMap.keySet()) {
            NTNode n = (NTNode) lln;
            int cluster = clusterMap.get(lln);
            Matrix m = newMatrices.get(cluster);
            if (m == null) {
                m = new Matrix("[" + cluster + "]", new Vector<NTNode>());
                newMatrices.put(cluster, m);
            }
            m.addNode(n);
            n.setMatrix(m);
        }
        return newMatrices.values().toArray(new Matrix[0]);
    }

    /** Method is used if inputfile is passed directly to zvtm-ontotrix*/
    public NTEdge addEdge(NTNode tail, NTNode head) {
        return addEdge(tail, head, ProjectColors.INTRA_COLOR_DEFAULT, null);
    }

    public NTEdge addEdge(NTNode tail, NTNode head, Color c, Object owner) {
        NTEdge e = new NTEdge(tail, head, c);
        e.setOwner(owner);
        tail.addOutgoingEdge(e);
        head.addIncomingEdge(e);
        return e;
    }

    public void createVisualisation(final VirtualSpace vs) {
        this.vs = vs;
        for (Matrix m : matrices) {
            m.createGraphics(0, 0, vs);
        }
        layoutMatrices(matrices);
    }

    /**
     * Lays out a set of matrices.
     * 
     * @param matrices
     * @author benjamin.bach@inria.fr
     */
    public void layoutMatrices(Vector<Matrix> matrices) {
        Map<Matrix, Map<Matrix, Double>> llg = new HashMap<Matrix, Map<Matrix, Double>>();
        HashMap<Matrix, Object> orphanMatrices = new HashMap();
        for (Matrix m : matrices) {
            orphanMatrices.put(m, null);
        }
        for (Matrix matrix : matrices) {
            for (Matrix matrix2 : matrices) {
                if (matrix != matrix2 && matrix.isConnectedTo(matrix2)) {
                    if (llg.get(matrix) == null) {
                        llg.put(matrix, new HashMap<Matrix, Double>());
                        orphanMatrices.remove(matrix);
                    }
                    llg.get(matrix).put(matrix2, new Double(matrix.nodes.size()));
                }
            }
        }
        for (Matrix m : orphanMatrices.keySet()) {
            llg.put(m, new HashMap<Matrix, Double>());
            llg.get(m).put(llg.keySet().iterator().next(), new Double(0.1));
        }
        llg = makeSymmetricGraph(llg);
        Map<Matrix, Node> matrixToLLNode = makeNodes(llg);
        List<Node> llnodes = new ArrayList<Node>(matrixToLLNode.values());
        List<Edge> lledges = makeEdges(llg, matrixToLLNode);
        final Map<Node, double[]> nodeToPosition = makeInitialPositions(llnodes);
        new MinimizerBarnesHut(llnodes, lledges, REPU_EXPONENT, ATTR_EXPONENT, GRAV_FACTOR).minimizeEnergy(nodeToPosition, LINLOG_ITERATIONS);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (Node node : nodeToPosition.keySet()) {
                    double[] position = nodeToPosition.get(node);
                    node.getMatrix().move(position[0] * SCALE, position[1] * SCALE);
                }
            }
        });
    }

    /** Finish creating the visualization.
	 *  Make sure the view has been painted once so that we have access to VText bounding boxes
	 *  before instantiating the remaining graphical elements
	 */
    public void finishCreateViz(VirtualSpace vs) {
        for (Matrix m : matrices) {
            m.adjustEdgeAppearance();
            m.performEdgeAppearanceChange();
        }
        for (Matrix m : matrices) {
            m.finishCreateNodeGraphics(vs);
        }
        for (Matrix m : matrices) {
            m.createEdgeGraphics(vs);
        }
        Collections.sort(matrices, new MatrixSizeComparator());
        for (Matrix m : matrices) {
            m.onTop(vs);
        }
    }

    private static Map<Matrix, Map<Matrix, Double>> makeSymmetricGraph(Map<Matrix, Map<Matrix, Double>> graph) {
        Map<Matrix, Map<Matrix, Double>> result = new HashMap<Matrix, Map<Matrix, Double>>();
        for (Matrix m1 : graph.keySet()) {
            for (Matrix m2 : graph.get(m1).keySet()) {
                double weight = graph.get(m1).get(m2);
                double revWeight = 0.0f;
                if (graph.get(m2) != null && graph.get(m2).get(m1) != null) {
                    revWeight = graph.get(m2).get(m1);
                }
                if (result.get(m1) == null) {
                    result.put(m1, new HashMap<Matrix, Double>());
                }
                result.get(m1).put(m2, weight + revWeight);
                if (result.get(m2) == null) {
                    result.put(m2, new HashMap<Matrix, Double>());
                }
                result.get(m2).put(m1, weight + revWeight);
            }
        }
        return result;
    }

    private static Map<Matrix, Node> makeNodes(Map<Matrix, Map<Matrix, Double>> graph) {
        Map<Matrix, Node> result = new HashMap<Matrix, Node>();
        for (Matrix m : graph.keySet()) {
            double nodeWeight = 0.0;
            for (double edgeWeight : graph.get(m).values()) {
                nodeWeight += edgeWeight;
            }
            result.put(m, new Node(m, nodeWeight));
        }
        return result;
    }

    private static List<Edge> makeEdges(Map<Matrix, Map<Matrix, Double>> graph, Map<Matrix, Node> matrixToLLNode) {
        List<Edge> result = new ArrayList<Edge>();
        for (Matrix m1 : graph.keySet()) {
            for (Matrix m2 : graph.get(m1).keySet()) {
                Node m1Node = matrixToLLNode.get(m1);
                Node m2Node = matrixToLLNode.get(m2);
                double weight = graph.get(m1).get(m2);
                result.add(new Edge(m1Node, m2Node, weight));
            }
        }
        return result;
    }

    private static Map<Node, double[]> makeInitialPositions(List<Node> nodes) {
        Map<Node, double[]> result = new HashMap<Node, double[]>();
        for (Node node : nodes) {
            double[] position = { Math.random() - 0.5, Math.random() - 0.5, 0.0 };
            result.put(node, position);
        }
        return result;
    }

    /**Causes the passed NTEdges to be "invible". Internally it is 
     * actually called the fade() or the show() method of the
     * NTEdgeAppearance class.
     * */
    public void setEdgesFaded(ArrayList<LinLogEdge> fadedEdges) {
        for (LinLogEdge lle : fadedEdges) {
            ((NTEdge) lle).setFaded();
        }
    }

    public void setEdgesVisible(ArrayList<LinLogEdge> visibleEdges) {
        for (LinLogEdge lle : visibleEdges) {
            ((NTEdge) lle).setVisible();
        }
    }

    public void cleanMatrices(AnimationManager am) {
        System.out.println("Matrices to be cleaned: " + matrices.size());
        for (Matrix m : matrices) {
            System.out.println("Matrix cleaned!");
            m.cleanGraphics(am);
            m.cleanNodeGraphics();
        }
        matrices.clear();
    }

    public void highlightNodeContext(NTNode n, boolean context) {
        Matrix m = n.getMatrix();
        m.resetGrid();
        m.highlightGrid(n, n, ProjectColors.HIGHLIGHT_GRID[ProjectColors.COLOR_SCHEME]);
        highlightedNodes.add(n);
        n.setNewInteractionState(IA_STATE_HIGHLIGHT, true, true);
        n.perfomStateChange();
        if (context) {
            HashSet<NTNode> highlightedNodes = new HashSet<NTNode>();
            HashSet<NTEdge> highlightedEdges = new HashSet<NTEdge>();
            for (NTEdge e : n.getOutgoingEdges()) {
                e.getHead().setNewInteractionState(NodeTrixViz.IA_STATE_RELATED, true, true);
                e.performInteractionStateChange();
                highlightedEdges.add(e);
                highlightedNodes.add(e.getHead());
            }
            for (NTEdge e : n.getIncomingEdges()) {
                e.getTail().setNewInteractionState(NodeTrixViz.IA_STATE_RELATED, true, true);
                e.performInteractionStateChange();
                highlightedEdges.add(e);
                highlightedNodes.add(e.getTail());
            }
            Matrix mRel;
            for (NTNode nRel : highlightedNodes) {
                mRel = nRel.getMatrix();
                nRel.perfomStateChange();
            }
            for (NTEdge e : highlightedEdges) {
                e.setNewInteractionState(IA_STATE_HIGHLIGHT);
                e.performInteractionStateChange();
            }
            this.highlightedEdges.addAll(highlightedEdges);
            this.highlightedNodes.addAll(highlightedNodes);
        }
    }

    public void resetAllContext() {
        for (NTNode n : highlightedNodes) {
            n.setNewInteractionState(IA_STATE_DEFAULT, true, true);
            n.perfomStateChange();
            n.getMatrix().resetGrid();
        }
        highlightedNodes.clear();
        for (NTEdge e : highlightedEdges) {
            e.setNewInteractionState(IA_STATE_DEFAULT);
            e.performInteractionStateChange();
        }
        highlightedEdges.clear();
    }

    public void highlightEdgeContext(NTEdge e) {
        System.out.println("-- highlight edge context -- ");
        highlightedEdges.add(e);
        e.setNewInteractionState(NodeTrixViz.IA_STATE_HIGHLIGHT_OUTGOING);
        e.performInteractionStateChange();
        NTNode head = e.getHead();
        highlightedNodes.add(head);
        NTNode tail = e.getTail();
        highlightedNodes.add(tail);
        head.setNewInteractionState(NodeTrixViz.IA_STATE_HIGHLIGHT, true, true);
        head.perfomStateChange();
        tail.setNewInteractionState(NodeTrixViz.IA_STATE_HIGHLIGHT, true, true);
        tail.perfomStateChange();
        if (e.isIntraEdge()) {
            tail.getMatrix().highlightGrid(tail, head, ProjectColors.HIGHLIGHT_GRID[ProjectColors.COLOR_SCHEME]);
        }
    }

    public void splitMatrices(AnimationManager am) {
        Vector<Matrix> newMatrices = new Vector<Matrix>();
        Vector<Matrix> toRemove = new Vector<Matrix>();
        for (Matrix m : matrices) {
            Vector<Matrix> currentNew = m.splitMatrix(am);
            if (currentNew == null) continue;
            newMatrices.addAll(currentNew);
            toRemove.add(m);
        }
        for (Matrix m : toRemove) {
            matrices.remove(m);
        }
        matrices.addAll(newMatrices);
        reorderMatricesCMK();
    }

    /** Causes matrices to merge according their name. So matrices with 
     * */
    public void mergeMatrices(final VirtualSpace vs, final AnimationManager am) {
        final HashMap<String, Vector<Matrix>> mergeMap = new HashMap<String, Vector<Matrix>>();
        for (Matrix m : matrices) {
            String name = m.getName();
            if (!mergeMap.containsKey(name)) {
                mergeMap.put(name, new Vector<Matrix>());
            }
            mergeMap.get(name).add(m);
        }
        for (Entry<String, Vector<Matrix>> entry : mergeMap.entrySet()) {
            Vector<Matrix> mergeMatrices = entry.getValue();
            if (mergeMatrices.size() < 2) continue;
            Matrix firstMatrix = mergeMatrices.firstElement();
            double xStart = firstMatrix.getPosition().x - firstMatrix.getBackgroundWidth() / 2;
            double yStart = firstMatrix.getPosition().y + firstMatrix.getBackgroundWidth() / 2;
            double offset = 0;
            for (Matrix m : mergeMatrices) {
                offset += m.getBackgroundWidth() / 2;
                m.move(xStart + offset - m.getPosition().x, yStart - offset - m.getPosition().y);
                offset += m.getBackgroundWidth() / 2;
            }
        }
        System.out.println("> Matrices before merge: " + matrices.size());
        final Vector<Matrix> newMatrices = new Vector<Matrix>();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                int clusterNumber = 0;
                for (Entry<String, Vector<Matrix>> entry : mergeMap.entrySet()) {
                    Vector<Matrix> mergeMatrices = entry.getValue();
                    Matrix newMatrix = new Matrix(entry.getKey(), new Vector<NTNode>());
                    clusterNumber++;
                    newMatrices.add(newMatrix);
                    for (Matrix m : mergeMatrices) {
                        for (NTNode n : m.nodes) {
                            newMatrix.addNode(n);
                            n.setMatrix(newMatrix);
                        }
                        m.cleanNodeGraphics();
                    }
                    Matrix firstMatrix = mergeMatrices.firstElement();
                    Matrix lastMatrix = mergeMatrices.lastElement();
                    double xCentre = (firstMatrix.getPosition().x - firstMatrix.getBackgroundWidth()) + (lastMatrix.getPosition().x + lastMatrix.getBackgroundWidth());
                    double yCentre = firstMatrix.getPosition().y + firstMatrix.getBackgroundWidth() + (lastMatrix.getPosition().x - lastMatrix.getBackgroundWidth());
                    for (Matrix m : mergeMatrices) {
                        xCentre += m.getPosition().x;
                        yCentre += m.getPosition().y;
                    }
                    xCentre /= (mergeMatrices.size() + 2);
                    yCentre /= (mergeMatrices.size() + 2);
                    newMatrix.createGraphics(xCentre, yCentre, vs);
                    newMatrix.finishCreateNodeGraphics(vs);
                    for (Matrix m : mergeMatrices) {
                        m.onTop(vs);
                        int xNew = (int) (m.getPosition().x - (m.getBackgroundWidth() + m.getMaxLabelWidth()));
                        int yNew = (int) (m.getPosition().y + m.getBackgroundWidth() + m.getMaxLabelWidth());
                    }
                    for (Matrix m : mergeMatrices) {
                        m.cleanGraphics(am);
                    }
                    System.out.println("> Remove all matrices");
                    matrices.removeAll(mergeMatrices);
                    System.out.println("> Matrices left after removal: " + matrices.size());
                }
                for (Matrix newMatrix : newMatrices) {
                    newMatrix.adjustEdgeAppearance();
                    newMatrix.performEdgeAppearanceChange();
                    newMatrix.createEdgeGraphics(vs);
                    newMatrix.onTop(vs);
                    matrices.add(newMatrix);
                }
                layoutMatrices(matrices);
                System.out.println("> Matrices left after merge: " + matrices.size());
            }
        });
    }

    /**
     * Iterates over all matrices and group their nodes according to their assigned
     * groupname.
     */
    public void regroupMatrices() {
        for (Matrix m : matrices) {
            m.cleanGroupLabels();
            m.group();
        }
    }

    public void reorderMatricesCMK() {
        for (Matrix m : matrices) {
            m.reorderCutHillMcKee();
        }
    }

    public HashSet<NTNode> getNodes() {
        return nodes;
    }

    public HashSet<NTEdge> getEdges() {
        return edges;
    }

    public Vector<Matrix> getMatrices() {
        return this.matrices;
    }
}
