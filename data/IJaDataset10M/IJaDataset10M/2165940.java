package org.form4j.appl.jdbc.dbspecifics;

/**
 * Interface obtain database specific stuff.
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.1 $ $Date: 2004/06/20 04:15:57 $
 **/
public interface DBSpecifics {

    /**
     * Get the db-specific sql query to obtain table names.
     * @return the db-specific sql query to obtain table names
     */
    public String getTableNamesSQL();

    /**
     * Get the db-specific sql query to obtain database names.
     * Create the <CODE><B>SELECT .... FROM ...</B></CODE> for SQL Queries.
     * <br clear="left"/>(Included here due to a silly bug within Oracle's updateable
     * resultset that accepts only aliased SelectFrom Parts).
     * @param columns column names to select (null selects all fields)
     * @param tableName the table  name
     * @return the db-specific sql query to obtain database names
     */
    public String getSelectFromPart(String[] columns, String tableName);
}
