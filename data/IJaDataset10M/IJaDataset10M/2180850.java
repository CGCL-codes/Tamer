package de.huxhorn.lilith.swing.transfer;

import de.huxhorn.lilith.swing.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;

/**
 * This class is handling d&amp;d as expected by 1.6.
 * It supports dropping files on the whole frame instead of only on the desktop.
 */
public class MainFrameTransferHandler16 extends MainFrameTransferHandler {

    private static final long serialVersionUID = 8433120912443123761L;

    private final Logger logger = LoggerFactory.getLogger(MainFrameTransferHandler16.class);

    public MainFrameTransferHandler16(MainFrame mainFrame) {
        super(mainFrame);
    }

    public void attach() {
        super.attach();
        mainFrame.setTransferHandler(this);
        if (logger.isDebugEnabled()) logger.debug("Attached transfer handler to mainFrame.");
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        if ((COPY & support.getSourceDropActions()) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        Transferable t = support.getTransferable();
        return importData(t);
    }
}
