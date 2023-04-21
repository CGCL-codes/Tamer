package org.datanucleus.store.rdbms.adapter;

import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;

/**
 * Adapter for spatially enabled databases.
 */
public interface SpatialRDBMSAdapter extends RDBMSAdapter {

    /** Key name for the srid extension. **/
    String SRID_EXTENSION_KEY = "spatial-srid";

    /** Key name for the dimension extension. **/
    String DIMENSION_EXTENSION_KEY = "spatial-dimension";

    /**
     * Checks whether the given column is geometry backed by the datastore.
     *
     * @param column Column to check
     * @return <code>true</code> if the given column is geometry backed, 
     *         <code>false</code> otherwise
     */
    boolean isGeometryColumn(Column column);

    /**
     * Returns the appropriate SQL statement to retrieve description of 
     * the Coordinate Reference System (CRS) with the given srid.
     *
     * @param table A table
     * @param srid The srid
     * @return SQL statement, <code>null</code> if not available 
     *         for the datastore
     */
    String getRetrieveCrsWktStatement(Table table, int srid);

    /**
     * Returns the appropriate SQL statement to retrieve the name of 
     * the Coordinate Reference System (CRS) with the given srid.
     *
     * @param table A table
     * @param srid The srid
     * @return SQL statement, <code>null</code> if not available 
     *         for the datastore
     */
    String getRetrieveCrsNameStatement(Table table, int srid);

    /**
     * Returns the appropriate SQL statement that calculates the bounds 
     * of all geometries in the given column.
     *
     * @param table The table
     * @param column The column
     * @return SQL statement, <code>null</code> if not available 
     *         for the datastore
     */
    String getCalculateBoundsStatement(Table table, Column column);
}
