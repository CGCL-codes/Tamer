package blue.ui.core.orchestra;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import blue.orchestra.Instrument;
import blue.orchestra.InstrumentCategory;

/**
 * @author Steven Yi
 */
public class TransferableInstrument implements Transferable {

    public static DataFlavor INSTR_FLAVOR = new DataFlavor(Instrument.class, "Blue Instrument");

    public static DataFlavor INSTR_CAT_FLAVOR = new DataFlavor(InstrumentCategory.class, "Instrument Category");

    private Object obj;

    DataFlavor flavors[];

    public TransferableInstrument(Object obj) {
        this.obj = obj;
        if (obj instanceof Instrument) {
            flavors = new DataFlavor[] { INSTR_FLAVOR };
        } else if (obj instanceof InstrumentCategory) {
            flavors = new DataFlavor[] { INSTR_CAT_FLAVOR };
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
