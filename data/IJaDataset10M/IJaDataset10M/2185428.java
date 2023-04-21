package net.sourceforge.jtds.jdbc;

import java.sql.*;

public class Microsoft7MetaData extends DatabaseMetaData {

    public static final String cvsVersion = "$Id: Microsoft7MetaData.java,v 1.1 2002-10-14 10:48:59 alin_sinpalean Exp $";

    protected Microsoft7MetaData(TdsConnection connection_, Tds tds_) throws SQLException {
        super(connection_, tds_);
        sysnameLength = 128;
    }

    /**
    * What's the maximum number of columns in a "GROUP BY" clause?
    *
    * @return max number of columns
    * @exception SQLException if a database-access error occurs.
    */
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 0;
    }

    /**
    * What's the maximum number of columns in an "ORDER BY" clause?
    *
    * @return max columns
    * @exception SQLException if a database-access error occurs.
    */
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 0;
    }

    /**
    * What's the maximum number of columns in a table?
    *
    * @return max columns
    * @exception SQLException if a database-access error occurs.
    */
    public int getMaxColumnsInTable() throws SQLException {
        return 1024;
    }

    /**
    * What's the maximum length of a single row?
    *
    * @return max row size in bytes
    * @exception SQLException if a database-access error occurs.
    */
    public int getMaxRowSize() throws SQLException {
        return 8060;
    }

    /**
    * What's the maximum number of tables in a SELECT?
    *
    * @return the maximum
    * @exception SQLException if a database-access error occurs.
    */
    public int getMaxTablesInSelect() throws SQLException {
        return 256;
    }

    /**
    * Is "ALTER TABLE" with drop column supported?
    *
    * @return true if so
    * @exception SQLException if a database-access error occurs.
    */
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return true;
    }
}
