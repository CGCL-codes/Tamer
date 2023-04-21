package com.organic.maynard.outliner.menus.edit;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.dom.*;
import com.organic.maynard.outliner.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;
import com.organic.maynard.outliner.actions.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2004/02/02 10:17:41 $
 */
public class CopyMenuItem extends AbstractOutlinerMenuItem implements TreeSelectionListener, DocumentRepositoryListener, ActionListener, GUITreeComponent {

    public void selectionChanged(TreeSelectionEvent e) {
        JoeTree tree = e.getTree();
        Document doc = tree.getDocument();
        if (doc == Outliner.documents.getMostRecentDocumentTouched()) {
            calculateEnabledState(tree);
        }
    }

    private void calculateEnabledState(JoeTree tree) {
        if (tree.getComponentFocus() == OutlineLayoutManager.ICON) {
            setEnabled(true);
        } else {
            if (tree.getCursorPosition() == tree.getCursorMarkPosition()) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }
    }

    public void documentAdded(DocumentRepositoryEvent e) {
    }

    public void documentRemoved(DocumentRepositoryEvent e) {
    }

    public void changedMostRecentDocumentTouched(DocumentRepositoryEvent e) {
        if (e.getDocument() == null) {
            setEnabled(false);
        } else {
            calculateEnabledState(e.getDocument().getTree());
        }
    }

    public void startSetup(Attributes atts) {
        super.startSetup(atts);
        setEnabled(false);
        addActionListener(this);
        Outliner.documents.addTreeSelectionListener(this);
        Outliner.documents.addDocumentRepositoryListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        OutlinerDocument doc = (OutlinerDocument) Outliner.documents.getMostRecentDocumentTouched();
        OutlinerCellRendererImpl textArea = doc.panel.layout.getUIComponent(doc.tree.getEditingNode());
        if (textArea == null) {
            return;
        }
        Node node = textArea.node;
        JoeTree tree = node.getTree();
        OutlineLayoutManager layout = tree.getDocument().panel.layout;
        if (doc.tree.getComponentFocus() == OutlineLayoutManager.TEXT) {
            CopyAction.copyText(textArea, tree, layout);
        } else if (doc.tree.getComponentFocus() == OutlineLayoutManager.ICON) {
            CopyAction.copy(tree, layout);
        }
    }
}
