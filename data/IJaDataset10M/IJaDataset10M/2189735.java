package lu.fisch.unimozer.copies;

import java.awt.datatransfer.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Object used during copy/paste and DnD operations to represent RTF text.
 * It can return the text being moved as either RTF or plain text.  This
 * class is basically the same as
 * <code>java.awt.datatransfer.StringSelection</code>, except that it can also
 * return the text as RTF.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RtfTransferable implements Transferable {

    /**
	 * The RTF data, in bytes (the RTF is 7-bit ascii).
	 */
    private byte[] data;

    /**
	 * The "flavors" the text can be returned as.
	 */
    private final DataFlavor[] FLAVORS = { new DataFlavor("text/rtf", "RTF"), DataFlavor.stringFlavor, DataFlavor.plainTextFlavor };

    /**
	 * Constructor.
	 *
	 * @param data The RTF data.
	 */
    public RtfTransferable(byte[] data) {
        this.data = data;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(FLAVORS[0])) {
            return new ByteArrayInputStream(data == null ? new byte[0] : data);
        } else if (flavor.equals(FLAVORS[1])) {
            return data == null ? "" : RtfToText.getPlainText(data);
        } else if (flavor.equals(FLAVORS[2])) {
            String text = "";
            if (data != null) {
                text = RtfToText.getPlainText(data);
            }
            return new StringReader(text);
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) FLAVORS.clone();
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < FLAVORS.length; i++) {
            if (flavor.equals(FLAVORS[i])) {
                return true;
            }
        }
        return false;
    }
}
