package org.gerhardb.jibs.viewer.shows.group;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceListener;
import java.io.File;
import java.util.ArrayList;
import org.gerhardb.jibs.viewer.IFrame;
import org.gerhardb.lib.scroller.IScroll;

/**
 * GroupDrage
 */
public class GroupDrag implements Transferable, DragGestureListener {

    public static final DataFlavor GRPOUP_DRAG_DATA_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=org.gerhardb.jibs.viewer.shows.group.GroupDrag", "Local GroupDrag");

    protected DataFlavor[] myDataFlavors = { DataFlavor.javaFileListFlavor, GRPOUP_DRAG_DATA_FLAVOR };

    DragSource myDragSource = new DragSource();

    DragSourceListener myDragSourceListner = new DSListener();

    IFrame myTopFrame;

    ResizeImageComponent myImageComponent;

    public GroupDrag(ResizeImageComponent imageComp, IFrame frame) {
        this.myTopFrame = frame;
        this.myImageComponent = imageComp;
        DragGestureListener dgl = this;
        this.myDragSource.createDefaultDragGestureRecognizer(this.myImageComponent, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
    }

    public File getFileToTransfer() {
        return this.myImageComponent.myFile;
    }

    /**
	 * This is called from a drop.
	 */
    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (this.myImageComponent.myFile == null) {
            return null;
        }
        if (DataFlavor.javaFileListFlavor.equals(flavor)) {
            ArrayList<File> fileList = new ArrayList<File>(1);
            fileList.add(getFileToTransfer());
            return new ArrayList<File>(fileList);
        } else if (GRPOUP_DRAG_DATA_FLAVOR.equals(flavor)) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return this.myDataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < this.myDataFlavors.length; i++) {
            if (this.myDataFlavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        if (this.myImageComponent.isPointOnImage(dge.getDragOrigin())) {
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
            this.myDragSource.startDrag(dge, cursor, this, this.myDragSourceListner);
        }
    }

    /**
	 * This is all about controlling what happens while the drag is flying over the 
	 * drop zone.  It doesn't do anything while over the source zone.  Except
	 * the dropActionChanged which happens anywhere.
	 * @author Gerhard
	 *
	 */
    class DSListener extends DragSourceAdapter {

        @Override
        public void dropActionChanged(DragSourceDragEvent event) {
            System.out.println("GroupDrag.dropActionChanged");
            switch(event.getUserAction()) {
                case DnDConstants.ACTION_MOVE:
                    event.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
                    break;
                case DnDConstants.ACTION_COPY:
                    event.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
                    break;
                default:
            }
        }

        /**
		 * This is where we find out the drag worked!!!!
		 */
        @Override
        public void dragDropEnd(DragSourceDropEvent event) {
            if (event.getDropSuccess()) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        GroupDrag.this.myTopFrame.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        GroupDrag.this.myTopFrame.getScroller().reloadScroller(GroupDrag.this.myTopFrame.getScroller().getCurrentFile(), IScroll.KEEP_CACHE);
                        GroupDrag.this.myTopFrame.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                });
            }
        }
    }
}
