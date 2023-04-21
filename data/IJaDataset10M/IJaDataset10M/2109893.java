package org.plenaquest.client.editor.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Use to transfert a array of ItemNode in one step
 */
class ItemNodeS implements Transferable, Serializable {

    ItemNode[] nodes;

    /**
	 * Constructor makes the array
	 * @param nodes
	 */
    public ItemNodeS(ItemNode[] nodes) {
        this.nodes = nodes;
    }

    /**
	 * Return the array of ItemNode
	 * @return
	 */
    public ItemNode[] getItemNodes() {
        return this.nodes;
    }

    /**
	 * Use by Tranfer Handler
	 * return the transfered data
	 */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
        return this;
    }

    /**
	 * Use by Tranfer Handler
	 * @return a array of flavor
	 */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] fs = new DataFlavor[1];
        fs[0] = ItemNode.getItemFlavor();
        return fs;
    }

    /**
	 * Use by Transfer Handler 
	 * @return true if flavor is supported
	 */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor f = ItemNode.getItemFlavor();
        return f.equals(flavor);
    }
}
