package com.lts.swing.treetable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * A DefaultTreeModel that can display column data for the various nodes in 
 * the tree.
 * 
 * <P/>
 * This is an abstract class.  To be instantiable, the following methods must
 * be defined in a subclass:
 * <UL>
 * <LI/>getColumnCount
 * <LI/>getColumnName
 * <LI/>getValueAt
 * <LI/>isCellEditable
 * <LI/>setValueAt
 * </UL>
 * 
 * <P/>
 * This approach assumes that, for a particular tree node, the object can 
 * display column information about that node.  For example, if you have a 
 * tree that shows departments in a company, with leaf nodes being the 
 * employees, then the model might know that the first column is always 
 * the last name of the employee, the next column is the first name, etc.
 * 
 * <P/>
 * The class assumes that the class for all columns is string.  If other types
 * are desired, then the getColumnClass method should be overridden.
 * 
 * @author cnh
 */
public abstract class SimpleTreeTableModel extends DefaultTreeModel implements TreeTableModel {

    public abstract String[] getColumnNames();

    public SimpleTreeTableModel() {
        super(new DefaultMutableTreeNode());
    }

    public SimpleTreeTableModel(DefaultMutableTreeNode root) {
        super(root);
    }

    public Class getColumnClass(int column) {
        if (0 == column) return TreeTableModel.class; else return String.class;
    }

    public int getColumnCount() {
        return getColumnNames().length;
    }

    public String getColumnName(int index) {
        return getColumnNames()[index];
    }
}
