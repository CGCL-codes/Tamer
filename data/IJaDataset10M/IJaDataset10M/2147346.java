package com.yerihyo.yeritools.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class FileTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private DataFlavor fileFlavor;

    protected String newline = "\n";

    public FileTransferHandler() {
        fileFlavor = DataFlavor.javaFileListFlavor;
    }

    protected void copyFile(File sourceFile, File targetFile) throws FileNotFoundException, IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(sourceFile).getChannel();
            outChannel = new FileOutputStream(targetFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean importData(JComponent c, Transferable t) {
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        try {
            if (hasFileFlavor(t.getTransferDataFlavors())) {
                List<?> files = (List<?>) t.getTransferData(fileFlavor);
                for (int i = 0; i < files.size(); i++) {
                }
                return true;
            }
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("importData: unsupported data flavor");
        } catch (IOException ieo) {
            System.out.println("importData: I/O exception");
        }
        return false;
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE) {
        }
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasFileFlavor(flavors)) {
            return true;
        }
        return false;
    }

    private boolean hasFileFlavor(DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (fileFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
}
