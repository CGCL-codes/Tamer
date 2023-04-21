package org.datanucleus.store.mapped.exceptions;

import org.datanucleus.exceptions.NucleusUserException;

/**
 * A <tt>ColumnDefinitionException</tt> is thrown if the settings of a
 * database column are incompatible with the data type of the object field
 * to which it is mapped.
 *
 * @see org.datanucleus.store.mapped.DatastoreField
 */
public class DatastoreFieldDefinitionException extends NucleusUserException {

    /**
     * Constructs a column definition exception with no specific detail message.
     */
    public DatastoreFieldDefinitionException() {
        super();
        setFatal();
    }

    /**
     * Constructs a column definition exception with the specified detail message.
     * @param msg the detail message
     */
    public DatastoreFieldDefinitionException(String msg) {
        super(msg);
        setFatal();
    }
}
