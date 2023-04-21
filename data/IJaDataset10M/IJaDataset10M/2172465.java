package org.das2.components.treetable;

import javax.swing.tree.TreeNode;

/**
 *
 * @author  eew
 */
public interface TreeTableNode extends TreeNode {

    boolean isCellEditable(int columnIndex);

    Object getValueAt(int columnIndex);

    void setValueAt(Object value, int columnIndex);

    Class getColumnClass(int columnIndex);

    int getColumnCount();

    String getColumnName(int columnIndex);
}
