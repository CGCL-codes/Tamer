package org.moonwave.dconfig.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.model.DConfigSelection;

/**
 *
 * @author Jonathan Luo
 *
 */
class TransferableTreeNode implements Transferable {

    public static final DataFlavor treePathFlavor = new DataFlavor(TreePath.class, "Tree Path");

    DataFlavor flavors[] = { DConfigSelection.dconfigFlavor, treePathFlavor };

    DConfig dcfgObj;

    TreePath treePath;

    public TransferableTreeNode(DConfig dcfgObj) {
        this.dcfgObj = dcfgObj;
    }

    public TransferableTreeNode(TreePath treePath) {
        this.treePath = treePath;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.getRepresentationClass() == DConfig.class) {
            return (Object) dcfgObj;
        } else if (flavor.getRepresentationClass() == TreePath.class) {
            return (Object) treePath;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
