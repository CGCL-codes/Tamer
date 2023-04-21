package com.tgjorgoski.rtfview.support;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.tgjorgoski.utils.ClipboardUtil;
import com.tgjorgoski.utils.IconResource;

public class ClipboardMenuFiller implements PopupMenuFiller {

    public void populateMenu(JPopupMenu populateThisMenu, MouseEvent e) {
        if (e.getComponent() instanceof JTextComponent) {
            final JTextComponent tComp = (JTextComponent) e.getComponent();
            boolean selectionPresent = false;
            if (tComp.getSelectionStart() != tComp.getSelectionEnd()) selectionPresent = true;
            JMenuItem itemCut = new JMenuItem("Cut");
            if (tComp.isEditable() && selectionPresent) {
                itemCut.setEnabled(true);
                itemCut.setAction(new AbstractAction("Cut") {

                    public void actionPerformed(ActionEvent e) {
                        tComp.cut();
                    }
                });
            } else {
                itemCut.setEnabled(false);
            }
            itemCut.setIcon(IconResource.getIconResource("editcut-20"));
            JMenuItem itemCopy = new JMenuItem("Copy");
            if (selectionPresent) {
                itemCopy.setEnabled(true);
                itemCopy.setAction(new AbstractAction("Copy") {

                    public void actionPerformed(ActionEvent e) {
                        tComp.copy();
                    }
                });
            } else {
                itemCopy.setEnabled(false);
            }
            itemCopy.setIcon(IconResource.getIconResource("editcopy-20"));
            JMenuItem itemPaste = new JMenuItem("Paste");
            itemPaste.setEnabled(false);
            Transferable tr = ClipboardUtil.getDefaultClipboard().getContents(this);
            if (tr != null) {
                boolean stringPresent = tr.isDataFlavorSupported(DataFlavor.stringFlavor);
                if (tComp.isEditable() && stringPresent) {
                    itemPaste.setAction(new AbstractAction("Paste") {

                        public void actionPerformed(ActionEvent e) {
                            tComp.paste();
                        }
                    });
                    itemPaste.setEnabled(true);
                }
            }
            itemPaste.setIcon(IconResource.getIconResource("editpaste-20"));
            populateThisMenu.add(itemCut);
            populateThisMenu.add(itemCopy);
            populateThisMenu.add(itemPaste);
        }
    }
}
