package org.rdv.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * A transferable for a list of channels.
 * 
 * @author Jason P. Hanley
 */
public class ChannelListTransferable implements Transferable {

    /** the data flavor */
    private final DataFlavor dataFlavor;

    /** the list of data files */
    private final List<String> channels;

    /**
   * Create a transferable from a list of channels.
   * 
   * @param channels  the list of channels
   */
    public ChannelListTransferable(List<String> channels) {
        this.channels = channels;
        dataFlavor = new ChannelListDataFlavor();
    }

    /**
   * Gets the transfer data.
   * 
   * @param df  the data flavor of the transferable
   */
    public Object getTransferData(DataFlavor df) throws IOException, UnsupportedFlavorException {
        if (!dataFlavor.match(df)) {
            throw new UnsupportedFlavorException(df);
        }
        return channels;
    }

    /**
   * Gets a list of data flavors this transferable supports.
   */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dataFlavors = { dataFlavor };
        return dataFlavors;
    }

    /**
   * Returns whether or not the specified data flavor is supported by this
   * transferable.
   * 
   * @param df  the data flavor to check
   */
    public boolean isDataFlavorSupported(DataFlavor df) {
        return dataFlavor.match(df);
    }
}
