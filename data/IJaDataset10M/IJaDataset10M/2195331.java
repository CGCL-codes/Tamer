package edu.upmc.opi.caBIG.caTIES.client.vr.utils.treeutils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 */
public class TransferableNode implements Transferable {

    /**
	 * Field nodes.
	 */
    List nodes;

    /**
	 * Field copyChildNodes.
	 */
    boolean copyChildNodes = false;

    /**
	 * @return Returns the copyChildNodes.
	 */
    public boolean getCopyChildNodes() {
        return copyChildNodes;
    }

    /**
	 * @param copyChildNodes The copyChildNodes to set.
	 */
    public void setCopyChildNodes(boolean copyChildNodes) {
        this.copyChildNodes = copyChildNodes;
    }

    /**
	 * Constructor for TransferableNode.
	 * @param nodes List
	 */
    public TransferableNode(List nodes) {
        super();
        this.nodes = nodes;
    }

    /**
	 * Field TREENODE_FLAVOR.
	 */
    public static final DataFlavor TREENODE_FLAVOR = new DataFlavor(DefaultMutableTreeNode.class, "Default Mutable Tree Node");

    /**
	   * Field flavors.
	   */
    static DataFlavor flavors[] = { TREENODE_FLAVOR };

    /**
	 * Method getTransferDataFlavors.
	 * @return DataFlavor[]
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
	 * Method isDataFlavorSupported.
	 * @param df DataFlavor
	 * @return boolean
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(DataFlavor)
	 */
    public boolean isDataFlavorSupported(DataFlavor df) {
        return (df.equals(TREENODE_FLAVOR));
    }

    /**
	 * Method getTransferData.
	 * @param df DataFlavor
	 * @return Object
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 * @see java.awt.datatransfer.Transferable#getTransferData(DataFlavor)
	 */
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (df.equals(TREENODE_FLAVOR)) {
            return nodes;
        } else throw new UnsupportedFlavorException(df);
    }
}
