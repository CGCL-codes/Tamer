package com.google.code.ebmlviewer.viewer.compontents.tabs;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

final class TabTransferable implements Transferable {

    static final DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, null);

    private static final DataFlavor[] FLAVORS = { FLAVOR };

    private final int index;

    TabTransferable(int index) {
        this.index = index;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS.clone();
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (FLAVOR.equals(flavor)) {
            return index;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
