package org.tockit.crepe.gui.eventhandlers;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import org.tockit.cgs.model.ConceptualGraph;
import org.tockit.cgs.model.Instance;
import org.tockit.cgs.model.Link;
import org.tockit.cgs.model.Node;
import org.tockit.cgs.model.Relation;
import org.tockit.cgs.model.Type;
import org.tockit.crepe.gui.datatransfer.CGFlavors;
import org.tockit.crepe.view.GraphView;
import org.tockit.crepe.view.LineView;
import org.tockit.crepe.view.LinkView;
import org.tockit.crepe.view.NodeView;

public class GraphViewDragHandler implements DropTargetListener {

    private final GraphView graphView;

    private ConceptualGraph graph;

    public GraphViewDragHandler(GraphView graphView, ConceptualGraph graph) {
        this.graphView = graphView;
        this.graph = graph;
    }

    public void setGraph(ConceptualGraph graph) {
        this.graph = graph;
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.isDataFlavorSupported(CGFlavors.TypeFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else if (dtde.isDataFlavorSupported(CGFlavors.RelationFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else if (dtde.isDataFlavorSupported(CGFlavors.InstanceFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    /**
     * @todo refactor, reuse
     */
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable transferable = dtde.getTransferable();
            if (transferable.isDataFlavorSupported(CGFlavors.TypeFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Type type = (Type) transferable.getTransferData(CGFlavors.TypeFlavor);
                Node newNode = new Node(this.graph.getKnowledgeBase(), type, null, null);
                Point screenPos = dtde.getLocation();
                Point2D canvasPos = this.graphView.getCanvasCoordinates(screenPos);
                newNode.setPosition(canvasPos.getX(), canvasPos.getY());
                this.graph.addNode(newNode);
                NodeView newView = new NodeView(newNode);
                this.graphView.addCanvasItem(newView);
                this.graphView.repaint();
                dtde.getDropTargetContext().dropComplete(true);
            } else if (transferable.isDataFlavorSupported(CGFlavors.RelationFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Point screenPos = dtde.getLocation();
                Point2D canvasPos = this.graphView.getCanvasCoordinates(screenPos);
                double xPos = canvasPos.getX();
                double yPos = canvasPos.getY();
                Relation relation = (Relation) transferable.getTransferData(CGFlavors.RelationFlavor);
                Type[] signature = relation.getSignature();
                Node[] references = new Node[relation.getArity()];
                for (int j = 0; j < relation.getArity(); j++) {
                    Node node = new Node(this.graph.getKnowledgeBase(), signature[j], null, null);
                    references[j] = node;
                    this.graph.addNode(node);
                }
                Link link = new Link(this.graph.getKnowledgeBase(), relation, references);
                this.graph.addLink(link);
                LinkView linkView = new LinkView(link);
                linkView.setPosition(new Point2D.Double(xPos, yPos));
                for (int i = 0; i < references.length; i++) {
                    Node node = references[i];
                    NodeView nodeView = new NodeView(node);
                    this.graphView.addCanvasItem(new LineView(linkView, nodeView, i + 1));
                    if (!node.hasPosition()) {
                        double angle = 2 * Math.PI * i / references.length;
                        double nodeX = xPos + GraphView.LINK_LAYOUT_RADIUS * Math.sin(angle);
                        double nodeY = yPos + GraphView.LINK_LAYOUT_RADIUS * Math.cos(angle) - Math.abs(GraphView.LINK_LAYOUT_RADIUS * Math.sin(angle) / 3);
                        nodeView.setPosition(new Point2D.Double(nodeX, nodeY));
                    }
                    this.graphView.addCanvasItem(nodeView);
                }
                this.graphView.addCanvasItem(linkView);
                this.graphView.repaint();
                dtde.getDropTargetContext().dropComplete(true);
            } else if (transferable.isDataFlavorSupported(CGFlavors.InstanceFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Instance instance = (Instance) transferable.getTransferData(CGFlavors.InstanceFlavor);
                Node newNode = new Node(this.graph.getKnowledgeBase(), instance.getType(), instance, null);
                Point screenPos = dtde.getLocation();
                Point2D canvasPos = this.graphView.getCanvasCoordinates(screenPos);
                newNode.setPosition(canvasPos.getX(), canvasPos.getY());
                this.graph.addNode(newNode);
                NodeView newView = new NodeView(newNode);
                this.graphView.addCanvasItem(newView);
                this.graphView.repaint();
                dtde.getDropTargetContext().dropComplete(true);
            } else {
                dtde.rejectDrop();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            dtde.rejectDrop();
        } catch (UnsupportedFlavorException ufException) {
            ufException.printStackTrace();
            dtde.rejectDrop();
        }
    }
}
