package atp.view.dnd;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.util.TooManyListenersException;
import atp.MainFrame.ATPSaveTimer;
import atp.data.*;
import atp.view.helpers.*;

public class DNDComponent {

    private final Component assoComponent;

    private final boolean isDragComponent, isDropComponent;

    private DSourceListener dsl = null;

    private DragGestureListener dgl = null;

    private final DragGestureRecognizer dgr;

    private DragSourceMotionListener dsml = null;

    private DragSource ds = null;

    private DropTarget dt = null;

    private DropTargetListener dtl = null;

    private DataFlavor[] targetFlavors = null;

    private int targetDropActions;

    public DNDComponent(Component associateWith) {
        this.isDragComponent = true;
        this.isDropComponent = false;
        assoComponent = associateWith;
        DNDComponentConstruct(isDragComponent, isDropComponent, DnDConstants.ACTION_NONE, new DSourceMotionListener());
        if (isDragComponent == true) {
            dgl = new DGestureListener(this);
            dgr = ds.createDefaultDragGestureRecognizer(assoComponent, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
        } else {
            dgl = null;
            dgr = null;
        }
    }

    public DNDComponent(Component associateWith, boolean isDragComponent, boolean isDropComponent, int targetDropActions) {
        this.isDragComponent = isDragComponent;
        this.isDropComponent = isDropComponent;
        assoComponent = associateWith;
        DNDComponentConstruct(isDragComponent, isDropComponent, targetDropActions, new DSourceMotionListener());
        if (isDragComponent == true) {
            dgl = new DGestureListener(this);
            dgr = ds.createDefaultDragGestureRecognizer(assoComponent, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
        } else {
            dgl = null;
            dgr = null;
        }
    }

    public DNDComponent(Component associateWith, boolean isDragComponent, boolean isDropComponent, int targetDropActions, DragSourceMotionListener dsml) {
        this.isDragComponent = isDragComponent;
        this.isDropComponent = isDropComponent;
        assoComponent = associateWith;
        DNDComponentConstruct(isDragComponent, isDropComponent, targetDropActions, dsml);
        if (isDragComponent == true) {
            dgl = new DGestureListener(this);
            dgr = ds.createDefaultDragGestureRecognizer(assoComponent, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
        } else {
            dgl = null;
            dgr = null;
        }
    }

    public void DNDComponentConstruct(boolean isDragComponent, boolean isDropComponent, int targetDropActions, DragSourceMotionListener dsml) {
        if (isDragComponent == true) {
            ds = new DragSource();
            dsl = new DSourceListener();
            this.dsml = dsml;
            ds.addDragSourceMotionListener(dsml);
        }
        if (isDropComponent = true) {
            dt = new DropTarget(assoComponent, dtl);
            this.targetDropActions = targetDropActions;
        }
    }

    public Component getAssociatedComponent() {
        return assoComponent;
    }

    public DataFlavor[] getTargetFlavors() {
        return targetFlavors;
    }

    public void setAcceptedTargetFlavors(DataFlavor[] acceptedTragetFlavors) {
        targetFlavors = acceptedTragetFlavors;
    }

    public void setObjectCompatibleTargetFlavors(Object[] compatibleTo) {
        targetFlavors = new DataFlavor[compatibleTo.length];
        for (int i = 0; i < compatibleTo.length; i++) targetFlavors[i] = new DataFlavor(compatibleTo[i].getClass(), compatibleTo[i].getClass().toString());
    }

    public int getTargetDropActions() {
        return targetDropActions;
    }

    public void addDropTargetListener(DropTargetListener dtl) {
        try {
            dt.addDropTargetListener(dtl);
        } catch (TooManyListenersException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setDragSourceMotionListener(DragSourceMotionListener dsml) {
        this.dsml = dsml;
        this.ds.addDragSourceMotionListener(dsml);
    }

    public DragSourceMotionListener getDragSourceMotionListener() {
        return this.dsml;
    }

    public void addNewDragGestureRecognizer(DragGestureListener dgl) {
        ds.createDefaultDragGestureRecognizer(assoComponent, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
    }

    public void setNewDragGestureListener(DragGestureListener dgl) {
        dgr.removeDragGestureListener(this.dgl);
        this.dgl = dgl;
        try {
            dgr.addDragGestureListener(dgl);
        } catch (TooManyListenersException e) {
            System.err.println("Fehler: Zu viele DragGestureListeners sind im DragGestureRecognizer eingetragen!");
        }
    }

    public void setNewSourceActions(int sourceActions) {
        this.dgr.setSourceActions(sourceActions);
    }

    public Transferable getTransferable() {
        return new TransferableObject(((DNDReady) assoComponent).getTransferableDragObject(), ((DNDReady) assoComponent).getTransferableDragObjectsDataFlavor());
    }

    private class DSourceListener implements DragSourceListener {

        public void dragDropEnd(DragSourceDropEvent dsde) {
            DataBox.emptyDataBox();
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }

    private class DGestureListener implements DragGestureListener {

        private DNDComponent assoDNDComponent;

        public DGestureListener(DNDComponent assoDNDComponent) {
            this.assoDNDComponent = assoDNDComponent;
        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            try {
                dge.startDrag(new Cursor(Cursor.HAND_CURSOR), assoDNDComponent.getTransferable(), assoDNDComponent.dsl);
                ATPSaveTimer.closeSemaphore();
            } catch (InvalidDnDOperationException error) {
                System.err.println(error);
            }
        }
    }

    private class DSourceMotionListener implements DragSourceMotionListener {

        public void dragMouseMoved(DragSourceDragEvent dsde) {
        }
    }
}
