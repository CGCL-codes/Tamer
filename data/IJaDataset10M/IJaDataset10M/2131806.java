package org.racsor.jmeter.flex.messaging.swing.treetable;

import org.racsor.jmeter.flex.messaging.swing.AbstractTreeModel;

public abstract class AbstractTreeTableModel extends AbstractTreeModel implements TreeTableModel {

    public Class getColumnClass(int column) {
        return column == 0 ? TreeTableModel.class : Object.class;
    }

    /** By default, make the column with the Tree in it the only editable one. 
    *  Making this column editable causes the JTable to forward mouse 
    *  and keyboard events in the Tree column to the underlying JTree. 
    */
    public boolean isCellEditable(Object node, int column) {
        return getColumnClass(column) == TreeTableModel.class;
    }

    public void setValueAt(Object aValue, Object node, int column) {
    }
}
