package org.nexopenframework.modules.httpserver.resources;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * <p>NexOpen Framework Modules</p>
 *
 * <p>Implementation of {@link javax.activation.DataSource} for dealing with <code>C3P0</code> {@link DataSource}
 * provider.</p>
 * 
 * <p>Since <code>1.2.0</code> supports <b>JDBC 3.0/4.0 PreparedStatement Pooling</b> using 
 * <code>c3p0.pool.pstmt_max</code> and <code>c3p0.pool.pstmt_max_per_conn</code> parameters.</p>
 *
 * @see org.nexopenframework.modules.httpserver.resources.DataSourceProvider
 * @see com.mchange.v2.c3p0.ComboPooledDataSource
 * @see javax.sql.DataSource
 * @author Francesc Xavier Magdaleno
 * @version $Revision ,$Date 21/05/2009 14:21:19
 * @since 1.0.0
 */
public class C3P0DataSourceProvider implements DataSourceProvider {

    /**logging facility*/
    private static final Log logger = LogFactory.getLog(C3P0DataSourceProvider.class);

    /**
	 * <p>Creates a <code>c3p0</code> pooling DataSource thru properties specified.</p>
	 * 
	 * <p><code>Required</code> properties</p>
	 * 
	 * <ul>
	 *  <li><b>c3p0.driver.class</b> fully qualified class name of JDBC Driver</li>
	 *  <li><b>c3p0.driver.url</b> JDBC URL</li>
	 *  <li><b>c3p0.driver.user</b> user to connect to RDBMS</li>
	 *  <li><b>c3p0.driver.password</b> password to connect to RDBMS</li>
	 * </ul>
	 * 
	 * <p><code>Optional</code> properties</p>
	 * 
	 * <ul>
	 *  <li><b>c3p0.pool.max</b> Max pool size</li>
	 *  <li><b>c3p0.pool.min</b> Min pool size</li>
	 *  <li><b>c3p0.pool.initialSize</b> initial pool size</li>
	 *  <li><b>c3p0.pool.pstmt_max</b> JDBC's standard parameter for controlling statement pooling</li>
	 *  <li><b>c3p0.pool.pstmt_max_per_conn</b> how many statements each pooled Connection is allowed to own</li>
	 * </ul>
	 * 
	 * @see com.mchange.v2.c3p0.ComboPooledDataSource
	 * @see org.nexopenframework.modules.httpserver.resources.DataSourceProvider#createDataSource(java.util.Map)
	 */
    public DataSource createDataSource(final Properties properties) throws SQLException {
        final ComboPooledDataSource cpds = new ComboPooledDataSource();
        final String driverClass = properties.getProperty("c3p0.driver.class");
        try {
            cpds.setDriverClass(driverClass);
        } catch (final PropertyVetoException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Problems loading java.sql.Driver", e);
            }
            throw new SQLException("Problems adding java.sql.Driver [" + driverClass + "]");
        }
        cpds.setJdbcUrl(properties.getProperty("c3p0.jdbc.url"));
        cpds.setUser(properties.getProperty("c3p0.jdbc.user"));
        final String pwd = properties.getProperty("c3p0.jdbc.password");
        cpds.setPassword(pwd);
        if (properties.containsKey("c3p0.pool.max")) {
            cpds.setMaxPoolSize(Integer.parseInt(properties.getProperty("c3p0.pool.max", "25")));
        }
        if (properties.containsKey("c3p0.pool.min")) {
            cpds.setMinPoolSize(Integer.parseInt(properties.getProperty("c3p0.pool.min", "10")));
        }
        if (properties.containsKey("c3p0.pool.initialSize")) {
            cpds.setInitialPoolSize(Integer.parseInt(properties.getProperty("c3p0.pool.initialSize", "10")));
        }
        if (properties.containsKey("c3p0.pool.pstmt_max")) {
            cpds.setMaxStatements(Integer.parseInt(properties.getProperty("c3p0.pool.pstmt_max", "180")));
        }
        if (properties.containsKey("c3p0.pool.pstmt_max_per_conn")) {
            cpds.setMaxStatementsPerConnection(Integer.parseInt(properties.getProperty("c3p0.pool.pstmt_max_per_conn", "200")));
        }
        return cpds;
    }
}
