package org.es.uma.XMLEditor.transfer;

import java.util.Arrays;
import java.util.Vector;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.es.uma.XMLEditor.gui.XmlTree;
import org.es.uma.XMLEditor.gui.XmlTreeNode;
import org.es.uma.XMLEditor.undo.CompoundEdit;
import org.es.uma.XMLEditor.xerces.TreeElement;

/**
 * this is an implementation of the interface DragGestureListener that
 * deals with the drag and drop recognizing in the XmlEditor.
 */
public class NavigatorDragGestureRecognizer implements java.awt.dnd.DragGestureListener {

    /**
     * the underlying tree
     */
    private JTree tree;

    /**
     * the drag source
     */
    private DragSource dragSource;

    /**
     * the underlying source listener
     */
    private NavigatorDragSourceListener NavigatorDragSourceListener;

    public NavigatorDragGestureRecognizer(JTree trItems, DragSource dragSource2) {
        super();
        this.tree = trItems;
        this.dragSource = dragSource2;
        NavigatorDragSourceListener = new NavigatorDragSourceListener(tree);
    }

    /**
     * A DragGestureRecognizer has detected a platform-dependent Drag and Drop
     * action initiating gesture and is notifying this Listener in order for
     * it to initiate the action for the user.
     * @param dge The DragGestureEvent describing the gesture that has
     * just occured
     */
    public void dragGestureRecognized(DragGestureEvent dge) {
        MouseEvent mouseEvent = (MouseEvent) dge.getTriggerEvent();
        if (mouseEvent.isPopupTrigger()) {
            return;
        }
        TreePath[] treePaths = tree.getSelectionPaths();
        TreePath treePath = tree.getClosestPathForLocation(mouseEvent.getX(), mouseEvent.getY());
        if (!(Arrays.asList(treePaths).contains(treePath))) {
            return;
        }
        Vector nodes = new Vector();
        try {
            Cursor cursor = DragSource.DefaultMoveNoDrop;
            switch(dge.getDragAction()) {
                case DnDConstants.ACTION_COPY:
                    cursor = DragSource.DefaultCopyDrop;
                    break;
                case DnDConstants.ACTION_MOVE:
                    cursor = DragSource.DefaultMoveDrop;
                    break;
                case DnDConstants.ACTION_LINK:
                    cursor = DragSource.DefaultLinkDrop;
                    break;
            }
            dragSource.startDrag(dge, cursor, new TransferableNode(nodes), NavigatorDragSourceListener);
        } catch (java.awt.dnd.InvalidDnDOperationException exception) {
        }
    }
}
