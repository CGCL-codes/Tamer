package DE.FhG.IGD.semoa.envision.uihelper;

import java.io.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.text.*;
import DE.FhG.IGD.logging.*;
import DE.FhG.IGD.semoa.ui.*;

/**
 * This text field allows to drop objects with 
 * <code>HostSelection.DN_FLAVOR</code>.
 *
 * @see DE.FhG.IGD.semoa.ui.HostSelection
 */
public class DnTextField extends JTextField implements DropTargetListener {

    private static Logger log_ = LoggerFactory.getLogger("envision/uihelper");

    DropTarget dropTarget = null;

    public DnTextField() {
        super();
        initDropTarget();
    }

    public DnTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        initDropTarget();
    }

    public DnTextField(int columns) {
        super(columns);
        initDropTarget();
    }

    public DnTextField(String text) {
        super(text);
        initDropTarget();
    }

    public DnTextField(String text, int columns) {
        super(text, columns);
        initDropTarget();
    }

    private void initDropTarget() {
        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    public void drop(DropTargetDropEvent dtde) {
        Transferable tr;
        try {
            tr = dtde.getTransferable();
            if (dtde.isDataFlavorSupported(HostSelection.DN_FLAVOR) && (dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                setText(tr.getTransferData(HostSelection.DN_FLAVOR).toString());
                dtde.dropComplete(true);
            } else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor) && (dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                setText(tr.getTransferData(DataFlavor.stringFlavor).toString());
                dtde.dropComplete(true);
            } else {
                dtde.rejectDrop();
            }
        } catch (IOException io) {
            log_.caught(LogLevel.ERROR, "Error occured while dropping to DnTextField", io);
        } catch (UnsupportedFlavorException ufe) {
            log_.caught(LogLevel.ERROR, "Error occured while dropping to DnTextField", ufe);
        }
    }

    public void dragEnter(DropTargetDragEvent e) {
    }

    public void dragExit(DropTargetEvent e) {
    }

    public void dragOver(DropTargetDragEvent e) {
    }

    public void dropActionChanged(DropTargetDragEvent e) {
    }
}
