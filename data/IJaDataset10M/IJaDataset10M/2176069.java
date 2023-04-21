package com.organic.maynard.outliner.menus.outline;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;
import com.organic.maynard.outliner.actions.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2004/02/02 10:17:42 $
 */
public class MoveLeftMenuItem extends AbstractOutlinerMenuItem implements ActionListener, GUITreeComponent {

    public void startSetup(Attributes atts) {
        super.startSetup(atts);
        addActionListener(this);
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
            LeftAction.moveLeftText(textArea, tree, layout);
        } else if (doc.tree.getComponentFocus() == OutlineLayoutManager.ICON) {
            LeftAction.moveLeft(textArea, tree, layout);
        }
    }
}
