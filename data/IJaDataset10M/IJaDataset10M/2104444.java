package org.xito.dcf.dnd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import org.xito.dcf.*;

/**
 * << License Info Goes HERE >> All Rights Reserved.
 * DragListenerAdapter
 * Description:
 *
 * @author  $Author: drichan $
 * @version $Revision: 1.5 $
 * @since   $Date: 2007/11/28 03:52:39 $
 */
public class DragListenerAdapter implements DragGestureListener, DragSourceListener {

    private boolean dragStarted = true;

    private DragSourceListener dragSourceListener;

    private DCComponent component;

    private Point dragPoint;

    private Point startLoc;

    private Point lastLoc;

    private int startX;

    private int startY;

    int test = 0;

    EventQueue queue;

    Dimension dim;

    private Transferable transferable;

    private Image dragImage;

    /**
    * Create a DragListener. Used by sub classes
    */
    protected DragListenerAdapter() {
    }

    /**
    * Create a DragListener for a specific Object.
    * If a drag operation is detected then This data will be used for the Drag Operation
    * @param pTransferable that contains the Data
    * @param pDragImage image to display during drag
    * @param pListener listener that will be notified of DragSource events
    */
    public DragListenerAdapter(Transferable pTransferable, Image pDragImage, DragSourceListener pListener) {
        transferable = pTransferable;
        dragImage = pDragImage;
        dragSourceListener = pListener;
    }

    /**
    * Create a DragListener for a specific DC Component. If the DCComponent is
    * draggable then when a DragGesture occurs the Drag Operation will
    * automatically be started.
    * @param pComp the Component that we will listen for drag operations.
    */
    public DragListenerAdapter(DCComponent pComp) {
        this(pComp, null);
    }

    /**
    * Create a DragListener for a specific DC Component. If the DCComponent is
    * draggable then when a DragGesture occurs the Drag Operation will
    * automatically be started.
    * @param pComp the Component that we will listen for drag operations.
    * @param pListener listener that will be notified of DragSource events
    */
    public DragListenerAdapter(DCComponent pComp, DragSourceListener pListener) {
        component = pComp;
        dragSourceListener = (pListener == null) ? this : pListener;
    }

    /**
    * Set the Transferable object that will be used during DnD operations
    * @param pTransferable
    */
    public void setTransferable(Transferable pTransferable) {
        transferable = pTransferable;
    }

    /**
    * Set the DragImage that will be used during DnD operstions
    * @param pDragImage
    */
    public void setDragImage(Image pDragImage) {
        dragImage = pDragImage;
    }

    /**
    * Default implementation of Drag Recognized
    * This method will
    */
    public void dragGestureRecognized(DragGestureEvent pEvent) {
        if (component != null && component.isDraggable() == false) return; else if (component == null && transferable == null) return;
        if (SwingUtilities.isRightMouseButton((MouseEvent) pEvent.getTriggerEvent())) {
            return;
        }
        int _action = pEvent.getDragAction();
        Cursor _cursor = DragSource.DefaultMoveDrop;
        if (_action == DnDConstants.ACTION_COPY) {
            _cursor = DragSource.DefaultCopyDrop;
        } else if (_action == DnDConstants.ACTION_MOVE) {
            _cursor = DragSource.DefaultMoveDrop;
        }
        if (component != null) {
            Transferable tempTransferable = component.getTransferable(pEvent.getDragAction());
            if (tempTransferable != null) {
                pEvent.startDrag(_cursor, component.getComponentDragImage(), new Point(), tempTransferable, (dragSourceListener == null ? this : dragSourceListener));
            }
        } else {
            pEvent.startDrag(_cursor, dragImage, new Point(), transferable, (dragSourceListener == null ? this : dragSourceListener));
        }
    }

    /**
    * Fire DragOver event to associated DragSourceListener
    */
    public void dragOver(DragSourceDragEvent pEvent) {
        if (dragSourceListener != null && dragSourceListener != this) dragSourceListener.dragOver(pEvent);
    }

    /**
    * Fire DragExit event to associated DragSourceListener
    */
    public void dragExit(DragSourceEvent pEvent) {
        if (dragSourceListener != null && dragSourceListener != this) dragSourceListener.dragExit(pEvent);
    }

    /**
    * Fire DropActionChanged event to associated DragSourceListener
    */
    public void dropActionChanged(DragSourceDragEvent pEvent) {
        if (dragSourceListener != null && dragSourceListener != this) dragSourceListener.dropActionChanged(pEvent);
    }

    /**
    * Fire DragEnter event to associated DragSourceListener
    */
    public void dragEnter(DragSourceDragEvent pEvent) {
        if (dragSourceListener != null && dragSourceListener != this) dragSourceListener.dragEnter(pEvent);
    }

    /**
    * Fire DragDropEnd event to associated DragSourceListener
    */
    public void dragDropEnd(DragSourceDropEvent pEvent) {
        if (dragSourceListener != null && dragSourceListener != this) dragSourceListener.dragDropEnd(pEvent);
    }
}
