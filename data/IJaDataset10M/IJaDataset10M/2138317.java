package cz.psika.numerist;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;

/**
 * Support for drag and drop actions.
 *
 * Dragging and dropping diagrams inside application tested.
 *
 * Nothing else is supported for now. But dragging and dropping textual data
 * from outside should be implemented.
 *
 * @author Tomas Psika
 */
public final class DragAndDropHandler {

    /**
     * Singleton instance.
     */
    private static DragAndDropHandler handler;

    /**
     * Parent window.
     */
    private static ApplicationWindow window;

    /**
     * Diagram.
     */
    private static DataFlavor diagramFlavor;

    /**
     * All supported data flavors.
     */
    private static final DataFlavor supportedFlavors[];

    /**
     * Source diagram canvas, used to locate source position when moving components.
     */
    private Diagram draggedDiagram;

    static {
        try {
            diagramFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=cz.psika." + Constants.APPNAME.toLowerCase() + ".Diagram");
        } catch (ClassNotFoundException ex) {
            System.err.println("static initialization of diagramcanvas data flavor failed");
        }
        supportedFlavors = new DataFlavor[] { diagramFlavor };
    }

    /**
     * Get single instance of drag and drop handler.
     *
     * @return singleton of type <code>DragAndDropHandler</code>
     */
    public static DragAndDropHandler getInstance() {
        if (handler == null) {
            if (window == null) window = ApplicationWindow.getInstance();
            handler = new DragAndDropHandler();
        }
        return handler;
    }

    /**
     * Constructor.
     */
    private DragAndDropHandler() {
        draggedDiagram = null;
    }

    /**
     * Handle dragging diagram.
     *
     * @param dge drag event
     */
    public void dragDiagram(DragGestureEvent dge) {
        draggedDiagram = (Diagram) dge.getComponent();
        draggedDiagram.refresh();
        Transferable transferable = new DiagramTransferable(draggedDiagram);
        dge.startDrag(null, transferable, draggedDiagram.getMouseInputHandler());
    }

    /**
     * Handle drop of diagram.
     *
     * @param dtde drop event
     */
    public void dropDiagram(DropTargetDropEvent dtde) {
        if (((Diagram) ((DropTarget) dtde.getSource()).getComponent()).equals(draggedDiagram)) {
            dtde.rejectDrop();
            return;
        }
        DiagramCanvasMemento transferedObject;
        try {
            if (dtde.isDataFlavorSupported(diagramFlavor)) transferedObject = (DiagramCanvasMemento) dtde.getTransferable().getTransferData(diagramFlavor); else {
                dtde.rejectDrop();
                return;
            }
        } catch (Exception e) {
            System.err.println("drag and drop failure: " + e.getMessage());
            dtde.rejectDrop();
            return;
        }
        int dropAction = dtde.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) dtde.rejectDrop(); else {
            Diagram targetDiagram;
            boolean moving = (dropAction & DnDConstants.ACTION_MOVE) == 0 ? false : true;
            dtde.acceptDrop(moving ? DnDConstants.ACTION_MOVE : DnDConstants.ACTION_COPY);
            if (!draggedDiagram.isThumbnail() && !moving) targetDiagram = Manager.getInstance().getDiagramCopy(transferedObject, Constants.FULL_DIAGRAM); else targetDiagram = Manager.getInstance().getDiagram(transferedObject, Constants.FULL_DIAGRAM);
            window.setDiagram(targetDiagram, window.getPosOfDiagram((Diagram) dtde.getDropTargetContext().getComponent(), Constants.INSIDE_MAIN_PANEL));
            if (moving) window.setDiagram(null, window.getPosOfDiagram(draggedDiagram, Constants.FULL_DIAGRAM), Constants.FULL_DIAGRAM);
            dtde.dropComplete(true);
        }
        draggedDiagram = null;
    }

    /**
     * Transferable object.
     *
     * Only DnD operations with diagrams supported.
     */
    public class DiagramTransferable implements Transferable {

        DiagramCanvasMemento memento;

        /**
         * Constructor fetches memento from the diagram and saves it for later use.
         *
         * @param diagramToTransfer
         */
        public DiagramTransferable(Diagram diagramToTransfer) {
            memento = (DiagramCanvasMemento) diagramToTransfer.createMemento();
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return supportedFlavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return (diagramFlavor.equals(flavor)) ? true : false;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return (diagramFlavor.equals(flavor)) ? memento : memento.getString();
        }
    }
}
