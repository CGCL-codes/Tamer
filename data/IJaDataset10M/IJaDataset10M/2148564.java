package com.chalko.tools.rt;

import com.chalko.tools.utils.JDBC;

/**
 * Insert the type's description here.
 * @author: $Author: chalko $
 * @version: $Revision: 1.1 $
 */
public abstract class StoredProcedureWrapper {

    private java.lang.String datasource = null;

    /**
	 * StoredProcedureWrapper constructor comment.
	 */
    public StoredProcedureWrapper() {
        super();
        setDatasource(getDefaultDataSource());
    }

    /**
	 * Gets a connection using the datasource.
	 * The caller must close the connection.
	 * @return java.sql.Connection
	 */
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        return JDBC.getConnection(datasource);
    }

    /**
	 * The JNDI name of the DataSource.
	 * @return java.lang.String
	 */
    public java.lang.String getDatasource() {
        return datasource;
    }

    /**
	 * The data source to use by default.
	 * 
	 * @return java.lang.String
	 */
    public String getDefaultDataSource() {
        return "data";
    }

    /**
	 * Set the JNDI name of the DataSource
	 * @param newDatasource java.lang.String
	 */
    public void setDatasource(java.lang.String newDatasource) {
        datasource = newDatasource;
    }
}
