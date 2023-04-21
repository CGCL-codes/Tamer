package org.gvsig.gui.beans.swing.treeTable;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 */
public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

    private static final long serialVersionUID = 1017457033135612066L;

    private int visibleRow;

    private TreeTable treeTable;

    public TreeTableCellRenderer(TreeModel model, TreeTable treetable) {
        super(model);
        this.treeTable = treetable;
    }

    public TreeTableCellRenderer(TreeModel model, TreeTable treetable, TreeCellRenderer cellRenderer, TreeCellEditor cellEditor) {
        super(model);
        this.setCellRenderer(cellRenderer);
        this.setCellEditor(cellEditor);
        this.treeTable = treetable;
    }

    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, treeTable.getHeight());
    }

    public void paint(Graphics g) {
        g.translate(0, -visibleRow * getRowHeight());
        super.paint(g);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) setBackground(table.getSelectionBackground()); else setBackground(table.getBackground());
        visibleRow = row;
        return this;
    }
}
