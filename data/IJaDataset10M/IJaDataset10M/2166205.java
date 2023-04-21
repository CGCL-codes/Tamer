package ch.oxinia.webdav.davcommander;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * Title:       Property search model
 * Description: Datamodel to use in the ACL property dialog
 * Copyright:   Copyright (c) 2005 Regents of the University of California. All rights reserved.
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         11 Feb 2005
 */
public class ACLPropertySearchModel extends AbstractTableModel {

    /**
     * Constructor 
     */
    public ACLPropertySearchModel() {
        super();
        this.match = true;
    }

    /**
     * Constructor
     * 
     * @param match
     *      flag indicating if the match column is shown
     */
    public ACLPropertySearchModel(boolean match) {
        super();
        this.match = match;
    }

    /**
     *  Returns the name of a column.
     *
     * @param column
     *      the column being queried
     * @return
     *      a string containing the name of the column 
     */
    public String getColumnName(int column) {
        if (column < names.length) return names[column];
        return super.getColumnName(column);
    }

    /**
     *  Returns the class of the values shown in a column.
     *
     *  @param column
     *      the column being queried
     *  @return
     *      the Object.class
     */
    public Class getColumnClass(int column) {
        if (column < names.length) return types[column];
        return super.getColumnClass(column);
    }

    /**
     * Returns the number of columns.
     * 
     * @return
     *      the number of columns
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return match ? 2 : 1;
    }

    /**
     * Returns the number of rows.
     * Always returns at least 1 row, so that the list is shown.
     *  
     * @return
     *      the number of rows 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return (rows.size() > 1) ? rows.size() : 1;
    }

    /**
     * Returns the value at a specific cell in the table.
     * 
     * @param rowIndex
     *      the row position
     * @param columnIndex
     *      the column position
     * @return
     *      the value in the cell specified by the parameters 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= rows.size()) return null;
        ACLPropertySearchNode node = (ACLPropertySearchNode) rows.get(rowIndex);
        switch(columnIndex) {
            case 0:
                Vector props = node.getProperties();
                String retval = "";
                if (props != null) {
                    for (int i = 0; i < props.size(); i++) {
                        if (i > 0) retval += ", ";
                        String[] n = (String[]) props.get(i);
                        retval += n[0];
                    }
                }
                return retval;
            case 1:
                return node.getMatch();
            default:
                return null;
        }
    }

    /**
     * Returns the real number of rows.
     *  
     * @return
     *      the number of rows 
     * @see #getRowCount()
     */
    public int getRealRowCount() {
        return rows.size();
    }

    /**
     * Add a row to the data model.
     * 
     * @param properties
     *      the list of properties
     * @param match
     *      the match string
     */
    public void addRow(Vector properties, String _match) {
        int size = rows.size();
        ACLPropertySearchNode node = new ACLPropertySearchNode(properties, _match);
        rows.add(node);
        fireTableRowsInserted(size, size);
    }

    /**
     * Return an entry in the data model.
     *  
     * @param index
     *      the index of the entry to return
     * @return
     *      the selected entry
     */
    public ACLPropertySearchNode getRow(int index) {
        if (index < rows.size()) {
            return (ACLPropertySearchNode) rows.get(index);
        }
        return null;
    }

    /**
     * Remove an entry from the data model.
     *  
     * @param index
     *      the index of the entry to remove
     */
    public void removeRow(int index) {
        if (index < rows.size()) {
            rows.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    protected String[] names = { "Properties", "Match" };

    protected Class[] types = { String.class, String.class };

    protected Vector rows = new Vector();

    protected boolean match;
}
