package org.tockit.crepe.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.dnd.DropTarget;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.tockit.canvas.Canvas;
import org.tockit.cgs.model.ConceptualGraph;
import org.tockit.cgs.model.Link;
import org.tockit.cgs.model.Node;
import org.tockit.crepe.gui.eventhandlers.GraphViewDragHandler;
import org.tockit.crepe.view.manipulators.LinkContextMenuHandler;
import org.tockit.crepe.view.manipulators.LinkMoveConnectedManipulator;
import org.tockit.crepe.view.manipulators.LinkMoveINCManipulator;
import org.tockit.crepe.view.manipulators.LinkMoveImmediateManipulator;
import org.tockit.crepe.view.manipulators.NodeContextMenuHandler;
import org.tockit.crepe.view.manipulators.NodeMoveManipulator;
import org.tockit.events.EventBroker;

public class GraphView extends Canvas {

    private ConceptualGraph graphShown;

    private Hashtable nodemap = new Hashtable();

    private Hashtable linkmap = new Hashtable();

    public static final int LINK_LAYOUT_RADIUS = 100;

    private GraphViewDragHandler dragHandler;

    public GraphView(EventBroker eventBroker) {
        super(eventBroker);
        getBackgroundItem().setPaint(getBackground());
        new NodeMoveManipulator(this, eventBroker);
        new LinkMoveImmediateManipulator(this, eventBroker);
        new LinkMoveINCManipulator(this, eventBroker);
        new LinkMoveConnectedManipulator(this, eventBroker);
        new NodeContextMenuHandler(this, eventBroker);
        new LinkContextMenuHandler(this, eventBroker);
        dragHandler = new GraphViewDragHandler(this, this.graphShown);
        new DropTarget(this, dragHandler);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(getBackground());
        g2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2d.setPaint(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintCanvas(g2d);
    }

    public void showGraph(ConceptualGraph graph) {
        this.clearCanvas();
        this.nodemap.clear();
        this.linkmap.clear();
        this.graphShown = graph;
        this.dragHandler.setGraph(graph);
        if (graph == null) {
            return;
        }
        fillCanvas();
        repaint();
    }

    private void fillCanvas() {
        Node[] nodes = graphShown.getNodes();
        List nodeViewsToAdd = new ArrayList();
        List newNodeViewsPlaced = new ArrayList();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (!nodemap.containsKey(node)) {
                NodeView nodeView = new NodeView(node);
                nodemap.put(node, nodeView);
                nodeViewsToAdd.add(nodeView);
            }
        }
        Link[] links = graphShown.getLinks();
        for (int i = 0; i < links.length; i++) {
            Link link = links[i];
            if (!linkmap.containsKey(link)) {
                LinkView linkView = new LinkView(link);
                double xPos = this.getWidth() / 2.0;
                double yPos = this.getHeight() / 2.0;
                if (!link.hasPosition()) {
                    linkView.setPosition(new Point2D.Double(xPos, yPos));
                }
                linkmap.put(link, linkView);
                Node[] references = link.getReferences();
                for (int j = 0; j < references.length; j++) {
                    Node node = references[j];
                    NodeView nodeView = (NodeView) nodemap.get(node);
                    if (!node.hasPosition()) {
                        double angle = 2 * Math.PI * j / references.length;
                        double nodeX = xPos + LINK_LAYOUT_RADIUS * Math.sin(angle);
                        double nodeY = yPos + LINK_LAYOUT_RADIUS * Math.cos(angle) - Math.abs(LINK_LAYOUT_RADIUS * Math.sin(angle) / 3);
                        nodeView.setPosition(new Point2D.Double(nodeX, nodeY));
                    }
                    newNodeViewsPlaced.add(nodeView);
                    this.addCanvasItem(new LineView(linkView, nodeView, j + 1));
                }
                this.addCanvasItem(linkView);
            }
        }
        for (Iterator iterator = nodeViewsToAdd.iterator(); iterator.hasNext(); ) {
            NodeView nodeView = (NodeView) iterator.next();
            if (!newNodeViewsPlaced.contains(nodeView)) {
                double nodeX = this.getWidth() / 2.0;
                double nodeY = this.getHeight() / 2.0;
                nodeView.setPosition(new Point2D.Double(nodeX, nodeY));
            }
            this.addCanvasItem(nodeView);
        }
    }

    public ConceptualGraph getGraphShown() {
        return graphShown;
    }

    public void updateContents() {
        fillCanvas();
        repaint();
    }
}
