package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;
import javax.swing.TransferHandler;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYViewTransferObject;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYSubgraphEventObserver extends SubgraphEventObserver<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    private ComponentView<?, ?, ?, ?, ?, ?, ?> selectedView;

    ;

    private PLAYHigraphView higraphView;

    private PLAYWholeGraph wholeGraph;

    private PLAYViewFactory viewFactory;

    private PLAYSubgraph subgraph;

    private PLAYNodeView lastHoverNodeView;

    private enum DropActionType {

        TAG_NEW, TAG_REPLACE, TAG_INSERT, NODE_DETACH, NODE_MOVE, NODE_INSERT, NODE_REPLACE, NODE_COPY, NODE_DELETE, NONE
    }

    ;

    private DropActionType dropAction;

    /**
     * @param higraphView
     * @param wholeGraph
     * @param viewFactory
     * @param subgraph
     */
    public PLAYSubgraphEventObserver(PLAYHigraphView higraphView, PLAYWholeGraph wholeGraph, PLAYViewFactory viewFactory, PLAYSubgraph subgraph) {
        super();
        this.higraphView = higraphView;
        this.wholeGraph = wholeGraph;
        this.viewFactory = viewFactory;
        this.subgraph = subgraph;
        this.dropAction = PLAYSubgraphEventObserver.DropActionType.NONE;
    }

    /** Called when the mouse is being moved with no buttons down. */
    public void movedOver(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, MouseEvent e) {
        if (this.lastHoverNodeView != null) {
            this.lastHoverNodeView.setFillColor(null);
        }
        if (stack != null && stack.size() > 0) {
            System.out.println("Moved over " + e.getX() + "-" + e.getY());
            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (stack.peek() instanceof PLAYNodeView) {
                PLAYNodeView nodeView = (PLAYNodeView) stack.peek();
                nodeView.setFillColor(Color.LIGHT_GRAY);
                this.lastHoverNodeView = nodeView;
            }
        } else {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
        }
        this.higraphView.refresh();
        this.higraphView.getDisplay().repaint();
    }

    /** Called when the mouse is being moved with one or more buttons down. */
    public void dragged(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, MouseEvent e) {
        System.out.println("Dragged " + e.getX() + "-" + e.getY());
    }

    /** Called when the mouse button has been pressed and released. */
    public void clickedOn(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, MouseEvent e) {
        System.out.println("Clicked on " + e.getX() + "-" + e.getY());
        if (this.higraphView.getCurrentNodeView() != null) {
            String text = this.higraphView.getCurrentNodeView().getLabel().getTheLabel();
            if (text.endsWith("|")) {
                this.higraphView.getCurrentNodeView().getLabel().setTheLabel(text.substring(0, text.length() - 1));
                this.higraphView.refresh();
                this.higraphView.getDisplay().repaint();
            }
        }
    }

    /** Called when a mouse button goes down. */
    public void pressedOn(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, MouseEvent e) {
        System.out.println("Pressed on " + e.getX() + "-" + e.getY());
        e.getComponent().setFocusable(false);
        this.selectedView = null;
        if (stack != null && stack.size() > 0) {
            this.selectedView = stack.peek();
            this.higraphView.setCurrentNodeView((PLAYNodeView) this.selectedView);
            if (this.selectedView instanceof NUMNodeView || this.selectedView instanceof BOOLNodeView || this.selectedView instanceof STRINGNodeView || this.selectedView instanceof VARNodeView) {
                if (e.getClickCount() == 2) {
                    if (this.selectedView instanceof BOOLNodeView) {
                        this.higraphView.getCurrentNodeView().getLabel().setTheLabel(Boolean.toString(!Boolean.parseBoolean(this.higraphView.getCurrentNodeView().getLabel().getTheLabel())));
                    } else {
                        this.higraphView.getCurrentNodeView().getLabel().setTheLabel(this.higraphView.getCurrentNodeView().getLabel().getTheLabel() + "|");
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    }
                    this.higraphView.refresh();
                    this.higraphView.getDisplay().repaint();
                }
            }
            e.getComponent().setFocusable(true);
            e.getComponent().requestFocusInWindow();
        }
        System.out.println("selectedView is " + selectedView);
    }

    /** Called when a mouse button goes up. */
    public void releasedOn(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, MouseEvent e) {
    }

    public int getSourceActions() {
        System.out.println("getSourceActions()");
        int result;
        if (this.selectedView == null) {
            result = TransferHandler.NONE;
        } else {
            result = TransferHandler.COPY_OR_MOVE;
        }
        System.out.println("...returns " + result);
        return result;
    }

    public Transferable createTransferable() {
        Transferable result;
        System.out.println("createTransferable");
        if (this.selectedView == null) result = null; else {
            if (this.selectedView instanceof PLAYDropZone) {
                PLAYDropZone dropZone = (PLAYDropZone) this.selectedView;
                result = new PLAYViewTransferObject(dropZone.getAssociatedComponent());
            } else {
                result = new PLAYViewTransferObject(this.selectedView);
            }
        }
        System.out.println("...returns " + result);
        return result;
    }

    public boolean canDropHere(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, TransferHandler.TransferSupport supportObj) {
        System.out.println("canDropHere()");
        boolean result = false;
        if (!supportObj.isDrop()) {
            return false;
        } else {
            supportObj.setShowDropLocation(true);
        }
        if (supportObj.getDropAction() == TransferHandler.NONE) {
            result = false;
        } else {
            for (DataFlavor dataFlavor : supportObj.getDataFlavors()) {
                System.out.println("...data flavor is " + dataFlavor);
            }
            if (supportObj.isDataFlavorSupported(PLAYViewTransferObject.TAG_DATAFLAVOR) || supportObj.isDataFlavorSupported(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR)) {
                PLAYNodeView targetNodeView = null;
                PLAYNode targetNode = null;
                PLAYDropZone targetDropZone = null;
                ComponentView<?, ?, ?, ?, ?, ?, ?> peekElement = null;
                try {
                    Object object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.TAG_DATAFLAVOR);
                    if (object != null) {
                        System.out.println("TAG_DATAFLAVOR");
                        PLAYTag currentTag = (PLAYTag) object;
                        if (stack.isEmpty()) {
                            result = true;
                            this.dropAction = PLAYSubgraphEventObserver.DropActionType.TAG_NEW;
                            System.out.println("TAG_NEW");
                        } else {
                            peekElement = stack.peek();
                            if (peekElement instanceof PLAYDropZone) {
                                targetDropZone = (PLAYDropZone) peekElement;
                                targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                                targetNode = targetNodeView.getNode();
                                result = targetNode.canInsertChild(targetNodeView.indexOfZones(targetDropZone), currentTag);
                                if (result) {
                                    this.dropAction = PLAYSubgraphEventObserver.DropActionType.TAG_INSERT;
                                    System.out.println("TAG_INSERT");
                                }
                            } else if (peekElement instanceof PLAYNodeView) {
                                targetNodeView = (PLAYNodeView) peekElement;
                                targetNode = targetNodeView.getNode();
                                result = targetNode.canReplace(currentTag);
                                if (result) {
                                    this.dropAction = PLAYSubgraphEventObserver.DropActionType.TAG_REPLACE;
                                    System.out.println("TAG_REPLACE");
                                }
                            } else {
                                result = false;
                            }
                        }
                    } else {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        if (object != null) {
                            System.out.println("COMPONENTVIEW_DATAFLAVOR");
                            PLAYNodeView currentNodeView = (PLAYNodeView) object;
                            PLAYNode currentNode = currentNodeView.getNode();
                            if (stack.isEmpty()) {
                                result = currentNodeView.getNode().canDetach();
                                if (result) {
                                    this.dropAction = PLAYSubgraphEventObserver.DropActionType.NODE_DETACH;
                                    System.out.println("NODE_DETACH");
                                }
                            } else {
                                peekElement = stack.peek();
                                if (peekElement instanceof PLAYDropZone) {
                                    targetDropZone = (PLAYDropZone) peekElement;
                                    targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                                    targetNode = targetNodeView.getNode();
                                    int dropZoneIndex = targetNodeView.indexOfZones(targetDropZone);
                                    if (supportObj.getUserDropAction() == TransferHandler.COPY) {
                                        result = targetNode.canInsertChild(dropZoneIndex, currentNode);
                                        if (result) {
                                            this.dropAction = PLAYSubgraphEventObserver.DropActionType.NODE_INSERT;
                                            System.out.println("NODE_INSERT");
                                        }
                                    } else if (supportObj.getUserDropAction() == TransferHandler.MOVE) {
                                        result = targetNode.canInsertChild(dropZoneIndex, currentNode) && currentNode.canDetach();
                                        if (result) {
                                            this.dropAction = PLAYSubgraphEventObserver.DropActionType.NODE_MOVE;
                                            System.out.println("NODE_MOVE");
                                        }
                                    }
                                } else if (peekElement instanceof PLAYNodeView) {
                                    targetNodeView = (PLAYNodeView) peekElement;
                                    targetNode = targetNodeView.getNode();
                                    if (supportObj.getUserDropAction() == TransferHandler.COPY) {
                                        result = targetNode.canReplace(currentNode);
                                        if (result) {
                                            this.dropAction = PLAYSubgraphEventObserver.DropActionType.NODE_COPY;
                                            System.out.println("NODE_COPY");
                                        }
                                    } else if (supportObj.getUserDropAction() == TransferHandler.MOVE) {
                                        result = targetNode.canReplace(currentNode) && currentNode.canDetach();
                                        if (result) {
                                            this.dropAction = PLAYSubgraphEventObserver.DropActionType.NODE_REPLACE;
                                            System.out.println("NODE_REPLACE");
                                        }
                                    }
                                } else {
                                    result = false;
                                }
                            }
                        } else {
                            result = false;
                        }
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                result = false;
            }
        }
        System.out.println("...returns " + result);
        if (!result) {
            this.dropAction = DropActionType.NONE;
        }
        return result;
    }

    public boolean importData(Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack, TransferHandler.TransferSupport supportObj) {
        System.out.println("Import data");
        if (!canDropHere(stack, supportObj)) {
            System.out.println("... returns false");
            return false;
        }
        Object object = null;
        PLAYTag currentTag = null;
        PLAYNodeView currentNodeView = null;
        PLAYNode currentNode = null;
        PLAYNodeView targetNodeView = null;
        PLAYNode targetNode = null;
        PLAYDropZone targetDropZone = null;
        int index = 0;
        try {
            switch(this.dropAction) {
                case TAG_NEW:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.TAG_DATAFLAVOR);
                        currentTag = (PLAYTag) object;
                        this.wholeGraph.makeRootNode(new PLAYPayload(currentTag.toString(), currentTag));
                        System.out.println("DOTAG_NEW");
                        break;
                    }
                case TAG_REPLACE:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.TAG_DATAFLAVOR);
                        currentTag = (PLAYTag) object;
                        targetNodeView = (PLAYNodeView) stack.peek();
                        targetNode = targetNodeView.getNode();
                        targetNode.replace(currentTag);
                        System.out.println("DOTAG_REPLACE");
                        break;
                    }
                case TAG_INSERT:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.TAG_DATAFLAVOR);
                        currentTag = (PLAYTag) object;
                        targetDropZone = (PLAYDropZone) stack.peek();
                        targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                        targetNode = targetNodeView.getNode();
                        index = targetNodeView.indexOfZones(targetDropZone);
                        targetNode.insertChild(index, currentTag);
                        System.out.println("DOTAG_INSERT" + targetNode.getNumberOfChildren() + "-" + index);
                        break;
                    }
                case NODE_DETACH:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        currentNodeView = (PLAYNodeView) object;
                        currentNode = currentNodeView.getNode();
                        currentNode.detach();
                        System.out.println("DONODE_DETACH");
                        break;
                    }
                case NODE_MOVE:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        currentNodeView = (PLAYNodeView) object;
                        currentNode = currentNodeView.getNode();
                        targetDropZone = (PLAYDropZone) stack.peek();
                        targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                        targetNode = targetNodeView.getNode();
                        currentNode.detach();
                        index = targetNodeView.indexOfZones(targetDropZone);
                        targetNode.insertChild(index, currentNode);
                        System.out.println("DONODE_MOVE" + targetNode.getNumberOfChildren() + "-" + index);
                        break;
                    }
                case NODE_INSERT:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        currentNodeView = (PLAYNodeView) object;
                        currentNode = currentNodeView.getNode();
                        targetDropZone = (PLAYDropZone) stack.peek();
                        targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                        targetNode = targetNodeView.getNode();
                        index = targetNodeView.indexOfZones(targetDropZone);
                        targetNode.insertChild(index, currentNode);
                        System.out.println("DONODE_INSERT" + targetNode.getNumberOfChildren() + "-" + index);
                        break;
                    }
                case NODE_REPLACE:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        currentNodeView = (PLAYNodeView) object;
                        currentNode = currentNodeView.getNode();
                        targetDropZone = (PLAYDropZone) stack.peek();
                        targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                        targetNode = targetNodeView.getNode();
                        currentNode.detach();
                        targetNode.replace(currentNode);
                        System.out.println("DONODE_REPLACE");
                        break;
                    }
                case NODE_COPY:
                    {
                        object = supportObj.getTransferable().getTransferData(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR);
                        currentNodeView = (PLAYNodeView) object;
                        currentNode = currentNodeView.getNode();
                        targetDropZone = (PLAYDropZone) stack.peek();
                        targetNodeView = (PLAYNodeView) targetDropZone.getAssociatedComponent();
                        targetNode = targetNodeView.getNode();
                        targetNode.replace(currentNode);
                        System.out.println("DONODE_REPLACE");
                        break;
                    }
                case NODE_DELETE:
                    {
                        System.out.println("DONODE_DELETE");
                        break;
                    }
                case NONE:
                    {
                        System.out.println("DONODE_DELETE");
                        break;
                    }
                default:
                    {
                        break;
                    }
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.higraphView.refresh();
        this.higraphView.getDisplay().repaint();
        System.out.println("... returns true");
        return true;
    }
}
