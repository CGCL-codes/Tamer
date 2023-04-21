package kr.ac.ssu.imc.whitehole.report.designer.dialogs;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class RDDBInfoTreeRenderer extends DefaultTreeCellRenderer {

    ImageIcon keyFieldIcon;

    ImageIcon columnIcon;

    ImageIcon tableIcon;

    ImageIcon databaseIcon;

    String[] sTableList;

    String[][] sKeyFieldList;

    public RDDBInfoTreeRenderer(String[] tableList, String[][] keyFieldList) {
        keyFieldIcon = new ImageIcon("icons/rviewer/arrow_right16.gif");
        columnIcon = new ImageIcon("icons/menus/column.gif");
        tableIcon = new ImageIcon("icons/menus/table.gif");
        databaseIcon = new ImageIcon("icons/menus/database.gif");
        sTableList = tableList;
        sKeyFieldList = keyFieldList;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (leaf) {
            if (isKeyField(value)) setIcon(keyFieldIcon); else setIcon(columnIcon);
        } else setIcon(tableIcon);
        if (isRoot(value)) setIcon(databaseIcon);
        return this;
    }

    protected boolean isRoot(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.isRoot()) return true; else return false;
    }

    protected boolean isKeyField(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.isLeaf()) {
            for (int i = 0; i < sTableList.length; i++) {
                if (sTableList[i].equals(node.getParent().toString())) {
                    if (sKeyFieldList[i] != null) {
                        for (int j = 0; j < sKeyFieldList[i].length; j++) {
                            if (sKeyFieldList[i][j].equals(node.toString())) return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
