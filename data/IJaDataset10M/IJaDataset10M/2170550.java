package net.sourceforge.squirrel_sql.fw.gui.action.wikiTable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * A custom transferable that will inform the system clipboard that the data
 * being transferred as "text/plain".
 * 
 * @author Stefan Willinger
 * 
 */
public class WikiTableSelection implements Transferable {

    private DataFlavor[] supportedFlavors = null;

    private String data = null;

    /**
	 * Default Constructor.
	 * A WikiTableSelection supports a {@link DataFlavor} <code>text/plain</code> 
	 * @throws ClassNotFoundException if the class for  {@link DataFlavor} doesn't exist. 
	 */
    public WikiTableSelection() throws ClassNotFoundException {
        super();
        DataFlavor plainFlavor = new DataFlavor("text/plain");
        supportedFlavors = new DataFlavor[] { plainFlavor };
    }

    /**
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    /**
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean result = false;
        for (DataFlavor f : supportedFlavors) {
            if (f.getMimeType().equals(flavor.getMimeType())) {
                result = true;
            }
        }
        return result;
    }

    /**
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return new ByteArrayInputStream(data.getBytes());
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    /**
	 * @param string
	 */
    public void setData(String string) {
        this.data = string;
    }
}
