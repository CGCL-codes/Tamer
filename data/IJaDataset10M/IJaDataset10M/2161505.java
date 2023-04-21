package com.michaelzanussi.genalyze.ui.models;

/**
 * This interface defines a simple table model for the GUI test driver. 
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (28 September 2006) 
 */
public interface SimpleTableModel {

    /**
     * Insert a row into the table using the passed Object.
     * 
     * @param object the Object to insert into the table.
     */
    public void insertRow(Object object);

    /**
     * Empty the table.
     */
    public void empty();

    /**
     * Returns the column size for the column indicated by the index.
     * 
     * @param index the desired column
     * @return the column size for the column indicated by the index.
     */
    public int getColumnSize(int index);
}
