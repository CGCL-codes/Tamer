package reclish.awt.datatrasfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A {@code FileSelection} object is very similar to a {@code StringSelection}: 
 * it can be used to transfer a {@code List<File>}, so it supports only 
 * {@code DataFlavor.javaFileListFlavor} flavor.
 * 
 * @see DataFlavor#javaFileListFlavor
 * @see java.awt.datatransfer.StringSelection
 * @author Valerio Del Bello (valeriodelbello[at]users[dot]sourceforge[dot]net)
 */
public class FileSelection implements Transferable {

    /**
	 * File list to transfer.
	 */
    private List<File> fileList;

    /**
	 * Supported flavors (only {@code DataFlavor.javaFileListFlavor}).
	 */
    private DataFlavor[] transferDataFlavors = { DataFlavor.javaFileListFlavor };

    /**
	 * Creates a Transferable capable of transferring the specified 
	 * {@code List<File>}.
	 * 
	 * @param fileList file list to transfer
	 */
    public FileSelection(List<File> fileList) {
        this.fileList = fileList;
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (!this.isDataFlavorSupported(dataFlavor)) {
            throw new UnsupportedFlavorException(dataFlavor);
        }
        return fileList;
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
