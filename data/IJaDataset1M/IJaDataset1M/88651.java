package blue.udo;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Steven Yi
 */
public class TransferableUDO implements Transferable {

    public static DataFlavor UDO_FLAVOR = new DataFlavor(UserDefinedOpcode.class, "Blue UDO");

    public static DataFlavor UDO_CAT_FLAVOR = new DataFlavor(UDOCategory.class, "UDO Category");

    private Object obj;

    DataFlavor flavors[];

    public TransferableUDO(Object obj) {
        this.obj = obj;
        if (obj instanceof UserDefinedOpcode) {
            flavors = new DataFlavor[] { UDO_FLAVOR };
        } else if (obj instanceof UDOCategory) {
            flavors = new DataFlavor[] { UDO_CAT_FLAVOR };
        } else {
            flavors = new DataFlavor[0];
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavors.length == 1) {
            return flavors[0].getRepresentationClass() == flavor.getRepresentationClass();
        }
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return obj;
    }
}
