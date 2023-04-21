package org.freeworld.prilib.row;

import java.io.Serializable;

/**
 * This interface was developed to allow default value assignments to a table
 * row when its values are cleared to a 'null' state. This should occur when a
 * new row is created and when a row is explicitly cleared of values.
 * 
 * @author dchemko
 */
public interface DefaultableTableRow extends TableRow {

    /**
    * Fetches the appropriate default value that the specified column should
    * return. It should be of no surprise that a return type of null is
    * perfectly valid as a table row may not need an overriding default value
    * for all columns.
    * 
    * @param columnName - Column to nullify
    * @return - Value to be assigned into the column
    */
    public Serializable getDefaultValue(String columnName);
}
