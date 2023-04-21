package org.jmeld.ui;

import org.jmeld.ui.swing.table.*;
import org.jmeld.ui.util.*;
import org.jmeld.util.node.*;
import javax.swing.*;

public class VersionControlTreeTableModel extends JMTreeTableModel {

    private Column fileNameColumn;

    private Column statusColumn;

    public VersionControlTreeTableModel() {
        fileNameColumn = addColumn("fileName", null, "File", null, -1, false);
        statusColumn = addColumn("status", "Left", "Status", Icon.class, 12, false);
    }

    public Object getValueAt(Object objectNode, Column column) {
        UINode uiNode;
        JMDiffNode diffNode;
        VersionControlBaseNode vcbNode;
        uiNode = (UINode) objectNode;
        diffNode = uiNode.getDiffNode();
        if (column == fileNameColumn) {
            return uiNode.toString();
        }
        if (column == statusColumn) {
            vcbNode = (VersionControlBaseNode) diffNode.getBufferNodeLeft();
            if (vcbNode == null) {
                return "";
            }
            System.out.println(vcbNode.getEntry().getStatus().getIconName());
            return ImageUtil.getImageIcon("16x16/" + vcbNode.getEntry().getStatus().getIconName());
        }
        return null;
    }

    public void setValueAt(Object value, Object objectNode, Column column) {
    }
}
