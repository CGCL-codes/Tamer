package projectviewer.tree;

import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.InputEvent;
import javax.swing.JTree;
import org.gjt.sp.util.Log;
import projectviewer.ProjectFile;

/**
 * A tree that can handle drag and drop.
 */
public class ProjectTree extends JTree implements DragGestureListener, DragSourceListener {

    private DragSource dragSrc;

    /**
   * Create a new <code>ProjectTree</code>.
   */
    public ProjectTree() {
        super();
        putClientProperty("JTree.lineStyle", "Angled");
        dragSrc = new DragSource();
        DragGestureRecognizer dragRecognizer = dragSrc.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        dragRecognizer.setSourceActions(dragRecognizer.getSourceActions() & ~InputEvent.BUTTON3_MASK);
    }

    /**
   * Recognize a drag gesture.
   */
    public void dragGestureRecognized(DragGestureEvent evt) {
        Object node = getLastSelectedPathComponent();
        if (!(node instanceof ProjectFile)) return;
        evt.getDragSource().startDrag(evt, DragSource.DefaultMoveNoDrop, (Transferable) node, this);
    }

    /**
   * This method is invoked to signify that the Drag and Drop
   * operation is complete.
   */
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    /**
   * Called as the hotspot enters a platform dependent drop site.
   */
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    /**
   * Called as the hotspot moves over a platform dependent drop site.
   */
    public void dragOver(DragSourceDragEvent dsde) {
    }

    /**
   * Called when the user has modified the drop gesture.
   */
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
   * Called as the hotspot exits a platform dependent drop site.
   */
    public void dragExit(DragSourceEvent dsde) {
    }
}
