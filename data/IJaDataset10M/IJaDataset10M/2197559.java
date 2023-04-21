package com.vividsolutions.jump.io.datasource;

/**
 * A wrapper for a query string that attributes it with the DataSource
 * to apply it against.
 */
public class DataSourceQuery {

    private String name;

    /**
	 * Constructs a DataSourceQuery that wraps a query string
	 * (implementation-dependent) and a DataSource to apply it against.
	 * 
	 * @param query
	 *                  identifies the dataset; may take the form of a SQL statement,
	 *                  a table name, null (if there is only one dataset), or other
	 *                  format
	 * @param name
	 *                  will be used for the layer name
	 */
    public DataSourceQuery(DataSource dataSource, String query, String name) {
        this.dataSource = dataSource;
        this.query = query;
        this.name = name;
    }

    /**
     * Parameterless constructor called by Java2XML
     */
    public DataSourceQuery() {
    }

    private DataSource dataSource;

    private String query;

    /**
	 * Returns the DataSource against which to apply the
	 * (implementation-dependent) query string.
	 */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
	 * Returns the implementation-dependent query string wrapped by this
	 * DataSourceQuery
	 */
    public String getQuery() {
        return query;
    }

    /**
     * Returns the name of this DataSourceQuery, suitable for use as a layer name.
     */
    public String toString() {
        return name;
    }

    /**
     * Called by Java2XML
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Called by Java2XML
     */
    public void setQuery(String query) {
        this.query = query;
    }
}
