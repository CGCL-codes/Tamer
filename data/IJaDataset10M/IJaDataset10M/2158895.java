package reclish.awt.datatrasfer;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A {@code FileSelection} object is very similar to a {@code StringSelection}: 
 * it can be used to transfer an {@code Image}, so it supports only 
 * {@code DataFlavor.imageFlavor} flavor.
 * 
 * @see DataFlavor#imageFlavor
 * @see java.awt.datatransfer.StringSelection
 * @author Valerio Del Bello (valeriodelbello[at]users[dot]sourceforge[dot]net)
 */
public class ImageSelection implements Transferable {

    /**
	 * {@code Image} to transfer.
	 */
    private Image image;

    /**
	 * Supported flavors (only {@code DataFlavor.imageFlavor}).
	 */
    private DataFlavor[] transferDataFlavors = { DataFlavor.imageFlavor };

    /**
	 * Creates a Transferable capable of transferring the specified 
	 * {@code Image}.
	 * 
	 * @param image {@code Image} to transfer
	 */
    public ImageSelection(Image image) {
        this.image = image;
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (!this.isDataFlavorSupported(dataFlavor)) {
            throw new UnsupportedFlavorException(dataFlavor);
        }
        return this.image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return transferDataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return this.transferDataFlavors[0].equals(dataFlavor);
    }
}
