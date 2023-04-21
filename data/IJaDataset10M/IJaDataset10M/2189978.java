package com.intel.gpe.client2.defaults.preferences;

/**
 * @version $Id: TableKey.java,v 1.1 2007/02/20 15:46:49 dizhigul Exp $
 * @author Denis Zhigula
 */
public class TableKey implements ITableKey {

    private String[] columnNames;

    private INode parent;

    private INode node;

    public TableKey(INode parent, INode node, String[] columnNames) {
        this.parent = parent;
        this.node = node;
        this.columnNames = columnNames;
    }

    public INode getParent() {
        return parent;
    }

    public String getName() {
        return node.getName();
    }

    public String[] getColumnNames() {
        return columnNames;
    }
}
