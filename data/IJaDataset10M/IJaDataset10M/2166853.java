package g4mfs.impl.org.peertrust.tnviz.app;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.Enumeration;
import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import g4mfs.impl.org.peertrust.net.Answer;
import g4mfs.impl.org.peertrust.net.Query;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TNSeqDiagramm.java,v 1.1 2005/11/30 10:35:09 ionut_con Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:09 $
 * by $Author: ionut_con $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
public class TNSeqDiagramm {

    private JGraph graph;

    private GraphModel model;

    private Hashtable graphElements;

    private final int NODE_HEIGHT = 40;

    private final int SEQ_START_DISTANCE_X = 30;

    private final int SEQ_START_DISTANCE_Y = 50;

    private final int SEQ_DISTANCE_Y = 40;

    private int SEQ_DISTANCE_X = 30;

    private int lastY;

    private int lastX;

    private Graphics graphics;

    private Vector graphPath;

    private Vector nodes;

    private Hashtable nodesInvisible;

    public TNSeqDiagramm(Graphics graphics) {
        graph = new JGraph();
        model = new DefaultGraphModel();
        graphElements = new Hashtable();
        lastY = SEQ_START_DISTANCE_Y;
        lastX = SEQ_START_DISTANCE_X;
        graphPath = new Vector();
        nodes = new Vector();
        nodesInvisible = new Hashtable();
        graph.setModel(model);
        graph.setGraphLayoutCache(new GraphLayoutCache(model, new DefaultCellViewFactory(), true));
        this.graphics = graphics;
    }

    public void wipeGraph() {
        graph = new JGraph();
        model = new DefaultGraphModel();
        graphElements = new Hashtable();
        lastY = SEQ_START_DISTANCE_Y;
        lastX = SEQ_START_DISTANCE_X;
        graphPath = new Vector();
        nodes = new Vector();
        nodesInvisible = new Hashtable();
        graph.setModel(model);
        graph.setGraphLayoutCache(new GraphLayoutCache(model, new DefaultCellViewFactory(), true));
    }

    public void addQuery(Query query) {
        if (query.getSource() == null || query.getTarget() == null) {
            return;
        }
        String sourceAddress = query.getSource().getAddress();
        String sourceAlias = query.getSource().getAlias();
        int sourcePort = query.getSource().getPort();
        String sourceIdentifier = sourceAlias + ":" + sourceAddress + ":" + sourcePort;
        String targetAddress = query.getTarget().getAddress();
        String targetAlias = query.getTarget().getAlias();
        int targetPort = query.getTarget().getPort();
        String targetIdentifier = targetAlias + ":" + targetAddress + ":" + targetPort;
        String goal = query.getGoal();
        long reqQueryId = query.getReqQueryId();
        TNNode source = null;
        TNNode target = null;
        if (graphElements.containsKey("node:" + sourceIdentifier)) {
            source = (TNNode) graphElements.get("node:" + sourceIdentifier);
        } else {
            String id = createNode(sourceAlias, sourceAddress, sourceAlias, sourcePort);
            source = getNode(id);
        }
        if (graphElements.containsKey("node:" + targetIdentifier)) {
            target = (TNNode) graphElements.get("node:" + targetIdentifier);
        } else {
            String id = createNode(targetAlias, targetAddress, targetAlias, targetPort);
            target = getNode(id);
        }
        connectNodes(source, target, goal + " ?", goal, reqQueryId, true, false, -1, "");
    }

    public void addAnswer(Answer answer) {
        if (answer.getSource() == null || answer.getTarget() == null) {
            return;
        }
        String sourceAddress = answer.getSource().getAddress();
        String sourceAlias = answer.getSource().getAlias();
        int sourcePort = answer.getSource().getPort();
        String sourceIdentifier = sourceAlias + ":" + sourceAddress + ":" + sourcePort;
        String targetAddress = answer.getTarget().getAddress();
        String targetAlias = answer.getTarget().getAlias();
        int targetPort = answer.getTarget().getPort();
        String targetIdentifier = targetAlias + ":" + targetAddress + ":" + targetPort;
        String goal = answer.getGoal();
        long reqQueryId = answer.getReqQueryId();
        int status = answer.getStatus();
        String proof = answer.getProof();
        TNNode source = null;
        TNNode target = null;
        if (graphElements.containsKey("node:" + sourceIdentifier)) {
            source = (TNNode) graphElements.get("node:" + sourceIdentifier);
        } else {
            String id = createNode(sourceAlias, sourceAddress, sourceAlias, sourcePort);
            source = getNode(id);
        }
        if (graphElements.containsKey("node:" + targetIdentifier)) {
            target = (TNNode) graphElements.get("node:" + targetIdentifier);
        } else {
            String id = createNode(targetAlias, targetAddress, targetAlias, targetPort);
            target = getNode(id);
        }
        connectNodes(source, target, goal, goal, reqQueryId, false, true, status, proof);
    }

    public TNNode getNode(String id) {
        return (TNNode) graphElements.get(id);
    }

    private String createNode(Object object, String peerAddress, String peerAlias, int peerPort) {
        TNNode node = new TNNode(object, graph);
        setNodeInformation(node, object.toString(), "node:" + peerAlias + ":" + peerAddress + ":" + peerPort, peerAddress, peerAlias, peerPort);
        graphElements.put(node.getId(), node);
        nodes.add(node);
        Vector elements = new Vector();
        elements.add(node);
        nodesInvisible.put(node, elements);
        DefaultPort port = new DefaultPort();
        node.add(port);
        node.setPort(port);
        Map nodeAttributes = new Hashtable();
        if (nodes.size() != 1) {
            lastX = lastX + SEQ_DISTANCE_X;
        }
        Rectangle nodeBounds = new Rectangle(lastX, SEQ_START_DISTANCE_Y, node.getLabelWidth(), NODE_HEIGHT);
        node.setX(lastX);
        node.setY(SEQ_START_DISTANCE_Y);
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        GraphConstants.setMoveable(nodeAttributes, graphics.getNodeMovable());
        GraphConstants.setBendable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setSizeable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setEditable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setBorderColor(nodeAttributes, graphics.getNodeBorderColor());
        GraphConstants.setBackground(nodeAttributes, graphics.getNodeBackgroundColor());
        GraphConstants.setOpaque(nodeAttributes, true);
        Map attributes = new Hashtable();
        attributes.put(node, nodeAttributes);
        graph.getGraphLayoutCache().insert(new Object[] { node }, attributes, null, null, null);
        lastX += node.getLabelWidth();
        return node.getId();
    }

    private TNNode createInvisibleNode() {
        TNNode node = new TNNode("", graph);
        DefaultPort port = new DefaultPort();
        node.add(port);
        node.setPort(port);
        node.setInvisible(true);
        Map nodeAttributes = new Hashtable();
        Rectangle nodeBounds = new Rectangle(0, 0, 0, 0);
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        GraphConstants.setMoveable(nodeAttributes, graphics.getNodeMovable());
        GraphConstants.setBendable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setSizeable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setEditable(nodeAttributes, graphics.getNodeEditable());
        GraphConstants.setBorderColor(nodeAttributes, Color.WHITE);
        GraphConstants.setBackground(nodeAttributes, Color.WHITE);
        GraphConstants.setOpaque(nodeAttributes, true);
        Map attributes = new Hashtable();
        attributes.put(node, nodeAttributes);
        graph.getGraphLayoutCache().insert(new Object[] { node }, attributes, null, null, null);
        return node;
    }

    private String connectNodes(String nodeSource, String nodeTarget, Object object, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
        return connectNodes(getNode(nodeSource), getNode(nodeTarget), object, goal, reqQueryId, query, answer, status, proof);
    }

    private String connectNodes(TNNode nodeSource, TNNode nodeTarget, Object object, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
        int labelWidth = graph.getFontMetrics(graph.getFont()).stringWidth(object.toString()) + 10;
        if ((nodeTarget.getX() > nodeSource.getX()) && (nodeTarget.getX() - nodeSource.getX()) < labelWidth) {
            SEQ_DISTANCE_X = labelWidth;
            repositionNodes();
        } else if ((nodeSource.getX() > nodeTarget.getX()) && (nodeSource.getX() - nodeTarget.getX()) < labelWidth) {
            SEQ_DISTANCE_X = labelWidth;
            repositionNodes();
        }
        if (lastY == SEQ_START_DISTANCE_Y) {
            lastY = lastY + SEQ_DISTANCE_Y + NODE_HEIGHT;
        } else {
            lastY = lastY + SEQ_DISTANCE_Y;
        }
        int newY = lastY;
        DefaultPort portSource = (DefaultPort) nodeSource.getPort();
        DefaultPort portTarget = (DefaultPort) nodeTarget.getPort();
        TNNode lastInvisibleNodeSource;
        TNNode lastInvisibleNodeTarget;
        Vector sourceElements = (Vector) nodesInvisible.get(nodeSource);
        lastInvisibleNodeSource = (TNNode) sourceElements.lastElement();
        Vector targetElements = (Vector) nodesInvisible.get(nodeTarget);
        lastInvisibleNodeTarget = (TNNode) targetElements.lastElement();
        TNEdge invisibleEdgeSource = new TNEdge("");
        TNEdge invisibleEdgeTarget = new TNEdge("");
        Map edgeInvisibleSourceAttributes = new Hashtable();
        int arrowInvisible = GraphConstants.ARROW_NONE;
        GraphConstants.setLineEnd(edgeInvisibleSourceAttributes, arrowInvisible);
        GraphConstants.setEndFill(edgeInvisibleSourceAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeInvisibleSourceAttributes, false);
        GraphConstants.setMoveable(edgeInvisibleSourceAttributes, graphics.getEdgeMovable());
        GraphConstants.setConnectable(edgeInvisibleSourceAttributes, graphics.getEdgeMovable());
        GraphConstants.setDisconnectable(edgeInvisibleSourceAttributes, graphics.getEdgeMovable());
        GraphConstants.setBendable(edgeInvisibleSourceAttributes, graphics.getEdgeMovable());
        GraphConstants.setSizeable(edgeInvisibleSourceAttributes, graphics.getEdgeMovable());
        GraphConstants.setEditable(edgeInvisibleSourceAttributes, graphics.getEdgeEditable());
        GraphConstants.setLineColor(edgeInvisibleSourceAttributes, graphics.getEdgeColor());
        Map edgeInvisibleTargetAttributes = new Hashtable();
        GraphConstants.setLineEnd(edgeInvisibleTargetAttributes, arrowInvisible);
        GraphConstants.setEndFill(edgeInvisibleTargetAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeInvisibleTargetAttributes, false);
        GraphConstants.setMoveable(edgeInvisibleTargetAttributes, graphics.getEdgeMovable());
        GraphConstants.setConnectable(edgeInvisibleTargetAttributes, graphics.getEdgeMovable());
        GraphConstants.setDisconnectable(edgeInvisibleTargetAttributes, graphics.getEdgeMovable());
        GraphConstants.setBendable(edgeInvisibleTargetAttributes, graphics.getEdgeMovable());
        GraphConstants.setSizeable(edgeInvisibleTargetAttributes, graphics.getEdgeMovable());
        GraphConstants.setEditable(edgeInvisibleTargetAttributes, graphics.getEdgeEditable());
        GraphConstants.setLineColor(edgeInvisibleTargetAttributes, graphics.getEdgeColor());
        TNEdge edge = new TNEdge(object);
        edge.setLabel(object.toString());
        Map edgeAttributes = new Hashtable();
        int arrow;
        if (query) {
            arrow = GraphConstants.ARROW_SIMPLE;
        } else {
            arrow = GraphConstants.ARROW_CLASSIC;
        }
        GraphConstants.setLineEnd(edgeAttributes, arrow);
        GraphConstants.setEndFill(edgeAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeAttributes, true);
        GraphConstants.setDashPattern(edgeAttributes, new float[] { 6 });
        GraphConstants.setMoveable(edgeAttributes, graphics.getEdgeMovable());
        GraphConstants.setConnectable(edgeAttributes, graphics.getEdgeMovable());
        GraphConstants.setDisconnectable(edgeAttributes, graphics.getEdgeMovable());
        GraphConstants.setBendable(edgeAttributes, graphics.getEdgeMovable());
        GraphConstants.setSizeable(edgeAttributes, graphics.getEdgeMovable());
        GraphConstants.setEditable(edgeAttributes, graphics.getEdgeEditable());
        GraphConstants.setLineColor(edgeAttributes, graphics.getEdgeColor());
        Map attributes = new Hashtable();
        attributes.put(invisibleEdgeSource, edgeInvisibleSourceAttributes);
        ConnectionSet cs = new ConnectionSet();
        TNNode invisibleNodeSource = createInvisibleNode();
        cs.connect(invisibleEdgeSource, lastInvisibleNodeSource.getPort(), invisibleNodeSource.getPort());
        sourceElements.add(invisibleNodeSource);
        nodesInvisible.put(nodeSource, sourceElements);
        positionInvisibleNode(invisibleNodeSource, nodeSource.getX() + (nodeSource.getLabelWidth() / 2), newY);
        graph.getGraphLayoutCache().insert(new Object[] { invisibleEdgeSource }, attributes, cs, null, null);
        attributes = new Hashtable();
        attributes.put(invisibleEdgeTarget, edgeInvisibleTargetAttributes);
        cs = new ConnectionSet();
        TNNode invisibleNodeTarget = createInvisibleNode();
        cs.connect(invisibleEdgeTarget, lastInvisibleNodeTarget.getPort(), invisibleNodeTarget.getPort());
        targetElements.add(invisibleNodeTarget);
        nodesInvisible.put(nodeTarget, targetElements);
        positionInvisibleNode(invisibleNodeTarget, nodeTarget.getX() + (nodeTarget.getLabelWidth() / 2), newY);
        graph.getGraphLayoutCache().insert(new Object[] { invisibleEdgeTarget }, attributes, cs, null, null);
        attributes = new Hashtable();
        attributes.put(edge, edgeAttributes);
        cs = new ConnectionSet();
        cs.connect(edge, invisibleNodeSource.getPort(), invisibleNodeTarget.getPort());
        graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes, cs, null, null);
        setEdgeInformation(edge, edge.getLabel(), "edge:" + reqQueryId + ":" + goal, goal, reqQueryId, query, answer, status, proof);
        graphPath.add(edge);
        graphElements.put(edge.getId(), edge);
        return edge.getId();
    }

    private void setNodeInformation(TNNode node, String title, String id, String peerAddress, String peerAlias, int peerPort) {
        node.setTitle(title);
        node.setId(id);
        node.setPeerAddress(peerAddress);
        node.setPeerAlias(peerAlias);
        node.setPeerPort(peerPort);
    }

    private void setEdgeInformation(TNEdge edge, String label, String id, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
        edge.setId(id);
        edge.setLabel(label);
        edge.setGoal(goal);
        edge.setReqQueryId(reqQueryId);
        edge.setQuery(query);
        edge.setAnswer(answer);
        edge.setStatus(status);
        edge.setProof(proof);
    }

    private void positionInvisibleNode(TNNode node, int x, int y) {
        Rectangle nodeBounds = new Rectangle(x, y, 0, 0);
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        node.setX(x);
        node.setY(y);
        refreshGraph();
    }

    private void positionNode(TNNode node, int x, int y) {
        Rectangle nodeBounds = new Rectangle(x, y, node.getLabelWidth(), NODE_HEIGHT);
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        node.setX(x);
        node.setY(y);
        refreshGraph();
    }

    private void repositionNodes() {
        lastX = SEQ_START_DISTANCE_X;
        for (Enumeration e1 = nodes.elements(); e1.hasMoreElements(); ) {
            TNNode node = (TNNode) e1.nextElement();
            if (!node.equals(nodes.firstElement())) {
                lastX += SEQ_DISTANCE_X;
            }
            positionNode(node, lastX, node.getY());
            Vector elements = (Vector) nodesInvisible.get(node);
            for (Enumeration e2 = elements.elements(); e2.hasMoreElements(); ) {
                TNNode invisibleNode = (TNNode) e2.nextElement();
                if (!node.equals(invisibleNode)) {
                    positionInvisibleNode(invisibleNode, node.getX() + (node.getLabelWidth() / 2), invisibleNode.getY());
                }
            }
        }
    }

    public JGraph getGraph() {
        return graph;
    }

    public void refreshGraph() {
        graph.getGraphLayoutCache().reload();
        graph.repaint();
    }

    public Vector getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(Vector graphPath) {
        this.graphPath = graphPath;
    }

    public Hashtable getGraphElements() {
        return graphElements;
    }

    public void setGraphElements(Hashtable graphElements) {
        this.graphElements = graphElements;
    }
}
