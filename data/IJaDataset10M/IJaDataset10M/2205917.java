package org.mitre.rt.client.ui.transfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.Logger;
import org.mitre.rt.client.util.GlobalUITools;

/**
 * An implementation of <code>Transferable</code> used to move <code>MutableTreeNode</code>.
 * @author Reid Gilman (rgilman@mitre.org)
 */
public class NodeTransfer implements Transferable {

    public static DataFlavor NODE_FLAVOR;

    private static final Logger logger = Logger.getLogger(NodeTransfer.class.getPackage().getName());

    static {
        String nodeFlavorString = DataFlavor.javaJVMLocalObjectMimeType + ";class=javax.swing.tree.MutableTreeNode";
        DataFlavor nf = null;
        try {
            nf = new DataFlavor(nodeFlavorString);
        } catch (ClassNotFoundException ex) {
            logger.error("Unable to instantiate data flavors", ex);
            GlobalUITools.displayExceptionMessage(null, "Unable to instantiate data flavors", ex);
        } finally {
            NODE_FLAVOR = nf;
        }
    }

    private MutableTreeNode myNode;

    protected NodeTransfer(MutableTreeNode node) {
        myNode = node;
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = { NODE_FLAVOR };
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(NODE_FLAVOR);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return myNode;
    }
}
