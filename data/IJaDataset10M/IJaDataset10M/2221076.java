package ca.sqlpower.wabit.swingui.olap;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OlapMetadataTransferable implements Transferable {

    private final Object[] transferData;

    /**
     * Data flavour that indicates a JVM-local reference to any object (because
     * olap4j metadata classes do not implement a common interface or extend a
     * more specific common base class).
     */
    public static final DataFlavor OLAP_ARRAY_FLAVOUR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=\"[Ljava.lang.Object;\"", "Local Object Array");

    /**
     * @param transferData
     */
    public OlapMetadataTransferable(Object[] transferData) {
        super();
        this.transferData = transferData;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor != OLAP_ARRAY_FLAVOUR) {
            throw new UnsupportedFlavorException(flavor);
        }
        return transferData;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { OLAP_ARRAY_FLAVOUR };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == OLAP_ARRAY_FLAVOUR;
    }
}
