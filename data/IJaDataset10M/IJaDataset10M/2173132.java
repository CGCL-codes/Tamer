package org.gerhardb.lib.dirtree.filelist;

import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;

/**
 * DragGestureListener with just move and copy.
 */
public class LinklessDragListener implements DragGestureListener {

    DragSourceListener myDSL;

    Transferable myTransferable;

    DragSource myDragSource;

    public LinklessDragListener(DragSourceListener dsl, Transferable t, DragSource ds) {
        this.myDSL = dsl;
        this.myTransferable = t;
        this.myDragSource = ds;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        Cursor cursor = DragSource.DefaultLinkNoDrop;
        switch(dge.getDragAction()) {
            case DnDConstants.ACTION_MOVE:
                cursor = DragSource.DefaultMoveDrop;
                break;
            case DnDConstants.ACTION_COPY:
                cursor = DragSource.DefaultCopyDrop;
                break;
            default:
                return;
        }
        this.myDragSource.startDrag(dge, cursor, this.myTransferable, this.myDSL);
    }
}
