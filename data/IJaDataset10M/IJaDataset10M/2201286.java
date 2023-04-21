package org.ddsteps.dataset;

import java.io.Serializable;
import java.util.Iterator;

/**
 * An ordered collection of DataTable:s. Read-only interface.
 * 
 * @author Adam
 * @version $Id: DataSet.java,v 1.2 2006/01/31 10:24:10 marthursson Exp $
 */
public interface DataSet extends Serializable {

    /**
     * Gets all tables names. The order of the names will correspond to the
     * order of the tables.
     * 
     * @return Possibly empty, never null.
     */
    public abstract String[] getTableNames();

    /**
     * Get iterator over all tables. The natural order of the managed
     * DataTable:s will be reflected in the iterator.
     * 
     * @return Iterator of (@link DataTable), never null.
     */
    public abstract Iterator tableIterator();

    /**
     * Get table by name.
     * 
     * @param tableName
     * @return Null if not found
     */
    public abstract DataTable getTable(String tableName);
}
