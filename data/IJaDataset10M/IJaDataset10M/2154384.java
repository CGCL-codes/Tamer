package com.htdsoft.ihm;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ActionColler extends AbstractAction {

    public void actionPerformed(ActionEvent event) {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                @SuppressWarnings("unused") String txt = (String) t.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (UnsupportedFlavorException e1) {
        } catch (IOException e2) {
        }
    }
}
