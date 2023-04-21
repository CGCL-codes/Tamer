package saadadb.admintool.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import saadadb.util.Messenger;

/**
 * @author laurentmichel
 * * @version $Id: SaadaTransferHandler.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public abstract class SaadaTransferHandler extends TransferHandler {

    protected abstract String exportString(JComponent c);

    protected abstract void importString(JComponent c, String str);

    protected abstract void cleanup(JComponent c, boolean remove);

    protected Transferable createTransferable(JComponent c) {
        return new StringSelection(exportString(c));
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                String str = (String) t.getTransferData(DataFlavor.stringFlavor);
                importString(c, str);
                return true;
            } catch (UnsupportedFlavorException ufe) {
                Messenger.printStackTrace(ufe);
            } catch (IOException ioe) {
                Messenger.printStackTrace(ioe);
            }
        }
        return false;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            String rep_class = flavors[i].getRepresentationClass().getName();
            if (rep_class.equals("java.lang.String")) {
                return true;
            }
        }
        return false;
    }
}
