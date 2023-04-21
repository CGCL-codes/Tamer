package org.specrunner.sql;

import javax.sql.DataSource;

/**
 * To perform a script a DataSource should be provided. Implement this interface
 * and add it to the specification as
 * <code>class="connection" provider='&lt;your class name&gt;'</code>.
 * 
 * @author Thiago Santos
 * 
 */
public interface IDataSourceProvider {

    /**
     * Returns the data source target for scripts execution.
     * 
     * @return The data source.
     */
    DataSource getDataSource();

    /**
     * Release data source resources.
     */
    void release();
}
