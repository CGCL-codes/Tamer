package jcomicdownloader.tools;

import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

/**
 *
 * 監控系統剪貼簿
 */
public class SystemClipBoard implements ClipboardOwner {

    private Clipboard sysClipBoard;

    private Transferable clipcontent;

    private String clipString;

    public SystemClipBoard() {
        super();
        initialize();
        clipString = "";
    }

    private void initialize() {
        sysClipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipcontent = sysClipBoard.getContents(null);
        sysClipBoard.setContents(clipcontent, this);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    public String getClipString() {
        try {
            if (clipcontent.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                clipString = (String) clipcontent.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return clipString;
    }

    public static void main(String[] args) {
        SystemClipBoard thisClass = new SystemClipBoard();
    }
}
