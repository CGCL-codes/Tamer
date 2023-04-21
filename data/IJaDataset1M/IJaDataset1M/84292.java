package com.qbrowser.clipboard;

import com.qbrowser.QBrowserV2;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

/**
 *
 * @author takemura
 */
public class ClipBoardManager {

    public void copyToClipBoard(String target_string) {
        Clipboard systemcClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = new StringSelection(target_string);
        systemcClipboard.setContents(t, null);
    }

    public void clearClipBoard() {
        Clipboard systemcClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = new StringSelection("");
        systemcClipboard.setContents(t, null);
    }

    public String getClipBoardData() {
        final Clipboard systemcClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable clipboardContent = systemcClipboard.getContents(null);
        if (clipboardContent == null) {
        } else {
            if (clipboardContent.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    return (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception unsuppe) {
                    unsuppe.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean hasClipBoardValidData() {
        final Clipboard systemcClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable clipboardContent = systemcClipboard.getContents(null);
        if (clipboardContent == null) {
            return false;
        } else {
            if (clipboardContent.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    final String source_paths = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
                    if (source_paths != null && source_paths.indexOf(QBrowserV2.MAGIC_SEPARATOR) != -1) {
                        return true;
                    }
                } catch (Exception unsuppe) {
                    unsuppe.printStackTrace();
                }
                return false;
            } else {
                return false;
            }
        }
    }
}
