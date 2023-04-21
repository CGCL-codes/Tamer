package com.organic.maynard.outliner.menus.window;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.dom.*;
import com.organic.maynard.outliner.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.3 $, $Date: 2004/02/02 10:17:42 $
 */
public class WindowMenu extends AbstractOutlinerMenu implements DocumentRepositoryListener, ActionListener, GUITreeComponent {

    protected static int WINDOW_LIST_START = -1;

    protected static int indexOfOldSelection = -1;

    public void documentAdded(DocumentRepositoryEvent e) {
        setEnabled(true);
        OutlinerDocument document = (OutlinerDocument) e.getDocument();
        WindowMenuItem item = new WindowMenuItem(document.getTitle(), document);
        item.addActionListener(this);
        add(item);
    }

    public void documentRemoved(DocumentRepositoryEvent e) {
        OutlinerDocument document = (OutlinerDocument) e.getDocument();
        int index = getIndexOfDocument(document);
        WindowMenuItem item = (WindowMenuItem) getItem(index);
        remove(index);
        item.destroy();
        if (e.getDocument().getDocumentRepository().openDocumentCount() <= 0) {
            setEnabled(false);
        }
    }

    public void changedMostRecentDocumentTouched(DocumentRepositoryEvent e) {
        if (e.getDocument() != null) {
            if ((WindowMenu.indexOfOldSelection >= WindowMenu.WINDOW_LIST_START) && (WindowMenu.indexOfOldSelection < getItemCount())) {
                getItem(indexOfOldSelection).setSelected(false);
            }
            WindowMenu.indexOfOldSelection = getIndexOfDocument(e.getDocument());
            getItem(indexOfOldSelection).setSelected(true);
        }
    }

    private int getIndexOfDocument(Document doc) {
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item instanceof WindowMenuItem) {
                WindowMenuItem wmItem = (WindowMenuItem) item;
                if (doc == wmItem.doc) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void startSetup(Attributes atts) {
        super.startSetup(atts);
        Outliner.menuBar.windowMenu = this;
        setEnabled(false);
    }

    public void endSetup(Attributes atts) {
        WINDOW_LIST_START = getItemCount();
        Outliner.documents.addDocumentRepositoryListener(this);
    }

    public void updateWindow(OutlinerDocument doc) {
        int index = getIndexOfDocument(doc);
        Outliner.menuBar.windowMenu.getItem(index).setText(doc.getTitle());
    }

    public void actionPerformed(ActionEvent e) {
        changeToWindow(((WindowMenuItem) e.getSource()).doc);
    }

    public static void changeToWindow(OutlinerDocument doc) {
        if (doc != null) {
            try {
                if (doc.isIcon()) {
                    doc.setIcon(false);
                }
                doc.moveToFront();
                if (Outliner.desktop.isMaximized()) {
                    OutlinerDocument prevDoc = (OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched();
                    if (prevDoc != null && prevDoc != doc) {
                        OutlinerDesktopManager.activationBlock = true;
                        prevDoc.setMaximum(false);
                        prevDoc.setSelected(false);
                        OutlinerDesktopManager.activationBlock = false;
                    }
                    doc.setMaximum(true);
                }
                doc.setSelected(true);
            } catch (java.beans.PropertyVetoException pve) {
                pve.printStackTrace();
            }
            if (!doc.isVisible()) {
                doc.setVisible(true);
                doc.validate();
                doc.panel.layout.redraw();
            }
        }
    }
}
