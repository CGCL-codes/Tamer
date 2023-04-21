package org.gridbus.broker.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.gridbus.broker.gui.model.NodeAdapter;
import org.gridbus.broker.gui.util.BaseUtil;
import org.gridbus.broker.gui.util.MessageEvent;
import org.gridbus.broker.gui.util.MessageListener;
import org.gridbus.broker.gui.util.XmlSourceChangedEvent;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> NodeGraphView </node>
 */
public class NodeGraphView extends JPanel implements MessageListener {

    private Node root;

    private JGraph graph;

    private int count = 1;

    private String cellName = "";

    private static final double START_X = 30;

    private static final double START_Y = 30;

    private static final double SIZE_ELEMENT_X = 100;

    private static final double SIZE_ELEMENT_Y = 25;

    private static final double SIZE_ATTRIBUTE_X = 100;

    private static final double SIZE_ATTRIBUTE_Y = 25;

    private static final double SPACING = 20;

    private JTree xmlTree;

    public NodeGraphView(Node root) {
        this.root = root;
        init();
    }

    private void init() {
        GraphModel model = new DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory());
        graph = new JGraph(model, view);
        parseGraph(graph);
        graph.addGraphSelectionListener(new GraphSelectionListener() {

            public void valueChanged(GraphSelectionEvent event) {
                DefaultGraphCell cell = (DefaultGraphCell) event.getCell();
                if (count == 1 && event.isAddedCell()) {
                    count++;
                    cellName = ((DefaultGraphCell) event.getCell()).getUserObject().toString();
                } else if (count == 2 && event.isAddedCell()) {
                    NodeAdapter node = (NodeAdapter) ((DefaultGraphCell) event.getCell()).getUserObject();
                    if (cellName.equals(node.toString())) {
                        Node toEdit = node.getNode();
                        if (toEdit.getNodeType() == Node.ATTRIBUTE_NODE) {
                        } else if (toEdit.getNodeType() == Node.ELEMENT_NODE) {
                            if (BaseUtil.getSupportedNodeNames(toEdit).length >= 1) new NodeProcessFrame(xmlTree, toEdit).setVisible(true);
                        } else if (toEdit.getNodeType() == Node.TEXT_NODE) {
                            new TextEditor((NotificationTree) xmlTree, toEdit).setVisible(true);
                        }
                    }
                    cellName = "";
                    count = 1;
                } else {
                    count = 1;
                    cellName = "";
                }
            }
        });
        setLayout(new BorderLayout());
        add(new JScrollPane(graph), BorderLayout.CENTER);
    }

    private void parseGraph(JGraph graph) {
        List cells = new ArrayList();
        graph.setModel(new DefaultGraphModel());
        if (getRoot() == null) {
            return;
        }
        parseAll(getRoot(), cells, START_X, START_Y, null);
        graph.getGraphLayoutCache().insert(cells.toArray());
        graph.setGridEnabled(true);
        graph.setMoveable(false);
    }

    private double parseAll(Node node, List cells, double xPos, double yPos, DefaultGraphCell parentCell) {
        if (node == null) {
            return xPos;
        }
        DefaultGraphCell rootCell = createCell(node, new Rectangle2D.Double(xPos, yPos, SIZE_ELEMENT_X, SIZE_ELEMENT_Y));
        cells.add(rootCell);
        yPos = yPos + SIZE_ELEMENT_Y + SPACING;
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            xPos = parseAll(children.item(i), cells, xPos, yPos, rootCell);
            xPos = xPos + SIZE_ELEMENT_X + SPACING;
        }
        if (parentCell != null && rootCell != null) cells.add(createEdge(parentCell, rootCell));
        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                DefaultGraphCell attrCell = createCell(attrs.item(i), new Rectangle2D.Double(xPos, yPos, SIZE_ATTRIBUTE_X, SIZE_ATTRIBUTE_Y));
                xPos = xPos + SIZE_ATTRIBUTE_X + SPACING;
                cells.add(attrCell);
                cells.add(createEdge(rootCell, attrCell));
            }
            xPos = xPos - SIZE_ATTRIBUTE_X - SPACING;
        }
        return xPos;
    }

    private Node getRoot() {
        return root;
    }

    private void setRoot(Node root) {
        this.root = root;
    }

    private DefaultGraphCell createCell(Node node, Rectangle2D.Double bound) {
        DefaultGraphCell cell = new DefaultGraphCell(new NodeAdapter(node));
        Color color = Color.GRAY;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            color = Color.ORANGE;
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            color = Color.GREEN;
        } else {
            color = Color.BLUE;
        }
        GraphConstants.setGradientColor(cell.getAttributes(), color);
        GraphConstants.setBounds(cell.getAttributes(), bound);
        GraphConstants.setOpaque(cell.getAttributes(), true);
        cell.add(new DefaultPort());
        return cell;
    }

    private DefaultGraphCell createCell(Node node) {
        DefaultGraphCell cell = new DefaultGraphCell(new NodeAdapter(node));
        Color color = Color.GRAY;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            color = Color.ORANGE;
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            color = Color.GREEN;
        } else {
            color = Color.WHITE;
        }
        GraphConstants.setGradientColor(cell.getAttributes(), color);
        GraphConstants.setOpaque(cell.getAttributes(), true);
        cell.add(new DefaultPort());
        return cell;
    }

    private DefaultGraphCell createEdge(DefaultGraphCell source, DefaultGraphCell target) {
        DefaultEdge edge = new DefaultEdge();
        edge.setSource(source.getChildAt(0));
        edge.setTarget(target.getChildAt(0));
        int arrow = GraphConstants.ARROW_DIAMOND;
        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
        GraphConstants.setEndFill(edge.getAttributes(), true);
        GraphConstants.setLineStyle(edge.getAttributes(), GraphConstants.STYLE_SPLINE);
        return edge;
    }

    public void display(Node node, JTree xmlTree) {
        setTree(xmlTree);
        if (node != null && node.getOwnerDocument() != null) setRoot(node.getOwnerDocument().getDocumentElement());
        parseGraph(graph);
    }

    private void setTree(JTree xmlTree) {
        this.xmlTree = xmlTree;
    }

    public void notify(MessageEvent event) {
        if (event instanceof XmlSourceChangedEvent) {
            display((Node) event.getSource(), xmlTree);
        }
    }
}
