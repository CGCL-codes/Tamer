package org.dspace.eperson;

import java.util.Vector;

/**
 * Exception indicating that an EPerson may not be deleted due to the presence
 * of the EPerson's ID in certain tables
 * 
 * @author Grace Carpenter
 */
public class EPersonDeletionException extends Exception {

    private Vector myTableList;

    /**
     * Create an empty EPersonDeletionException
     */
    public EPersonDeletionException() {
        super();
        myTableList = null;
    }

    /**
     * Create an EPersonDeletionException
     * 
     * @param tableList
     *            tables in which the eperson ID exists. An person cannot be
     *            deleted if it exists in these tables.
     *  
     */
    public EPersonDeletionException(Vector tableList) {
        super();
        myTableList = tableList;
    }

    /**
     * Return the list of offending tables.
     * 
     * @return The tables in which the eperson ID exists.
     */
    public Vector getTables() {
        return myTableList;
    }
}
