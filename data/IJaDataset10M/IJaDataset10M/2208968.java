package kaliko.gui;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import kaliko.UIPlayer;

abstract class UITilePanel extends TilePanel implements DropTargetListener, DragSourceListener, DragGestureListener {

    private String KALIKO_ROOT = "E:\\Sgnop\\Kaliko\\";

    public UITilePanel() {
        dragSource = null;
        new DropTarget(this, this);
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, 2, this);
        if (dragCursor == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image ii = tk.getImage(KALIKO_ROOT + "HexCur.png");
            dragCursor = tk.createCustomCursor(ii, new Point(12, 8), "DragTile");
        }
    }

    public void setPlayer(UIPlayer p) {
        _uip = p;
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent) {
    }

    public void dragExit(DropTargetEvent droptargetevent) {
    }

    public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
    }

    public void dragDropEnd(DragSourceDropEvent e) {
        localDrag = false;
    }

    public void dragOver(DragSourceDragEvent e) {
        DragSourceContext context = e.getDragSourceContext();
        context.setCursor(null);
        context.setCursor(dragCursor);
    }

    public void dragExit(DragSourceEvent e) {
        e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    public void dragEnter(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dropActionChanged(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dragGestureRecognized(DragGestureEvent event) {
        if (!_uip.isActive()) return;
        if (_uip.getTile() != null) {
            event.startDrag(DragSource.DefaultMoveNoDrop, _moot, this);
            localDrag = true;
        }
    }

    public abstract void drop(DropTargetDropEvent droptargetdropevent);

    public abstract void dragOver(DropTargetDragEvent droptargetdragevent);

    protected static Cursor dragCursor = null;

    protected static boolean localDrag = false;

    protected DragSource dragSource;

    protected UIPlayer _uip;

    private static final StringSelection _moot = new StringSelection("");
}
