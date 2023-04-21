package org.nexopenframework.management.jdbc3.monitor.jdbcx;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.nexopenframework.management.jdbc.monitor.jdbcx.AbstractDataSource;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of {@link ConnectionPoolDataSource} in order to deal with monitoring a real
 * {@link ConnectionPoolDataSource}.</p>
 * 
 * @see org.nexopenframework.management.jdbc.monitor.jdbcx.AbstractDataSource
 * @see org.nexopenframework.management.jdbc.monitor.jdbcx.AbstractDataSource#setRealDataSourceClassName(String)
 * @see javax.sql.ConnectionPoolDataSource
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public class MonitorableConnectionPoolDataSource extends AbstractDataSource<ConnectionPoolDataSource> implements ConnectionPoolDataSource {

    /**Serialization stuff*/
    private static final long serialVersionUID = 1L;

    /**Total number of statements the pool should keep open.*/
    private int maxStatements;

    /**Number of physical connections the pool should contain when it is created.*/
    private int initialPoolSize;

    /**Minimum number of physical connections the pool*/
    private int minPoolSize;

    /**Maximum number of physical connections the pool*/
    private int maxPoolSize;

    /**Number of seconds that a physical connection should remain unused in pool before it is closed*/
    private int maxIdleTime;

    /**Interval, in seconds, that the pool should wait before enforcing policy.*/
    private int propertyCycle;

    /**
	 * <p></p>
	 * 
	 * @see javax.sql.ConnectionPoolDataSource#getPooledConnection()
	 */
    public PooledConnection getPooledConnection() throws SQLException {
        return new MonitorablePooledConnection(this.getDataSource().getPooledConnection(), this.getUser());
    }

    public PooledConnection getPooledConnection(final String user, final String password) throws SQLException {
        return new MonitorablePooledConnection(this.getDataSource().getPooledConnection(user, password), user);
    }

    /**
	 * @return the maxStatements
	 */
    public int getMaxStatements() {
        return maxStatements;
    }

    /**
	 * @param maxStatements the maxStatements to set
	 */
    public void setMaxStatements(final int maxStatements) {
        this.maxStatements = maxStatements;
    }

    /**
	 * @return the initialPoolSize
	 */
    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    /**
	 * @param initialPoolSize the initialPoolSize to set
	 */
    public void setInitialPoolSize(final int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    /**
	 * @return the minPoolSize
	 */
    public int getMinPoolSize() {
        return minPoolSize;
    }

    /**
	 * @param minPoolSize the minPoolSize to set
	 */
    public void setMinPoolSize(final int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    /**
	 * @return the maxPoolSize
	 */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
	 * @param maxPoolSize the maxPoolSize to set
	 */
    public void setMaxPoolSize(final int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
	 * @return the maxIdleTime
	 */
    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
	 * @param maxIdleTime the maxIdleTime to set
	 */
    public void setMaxIdleTime(final int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    /**
	 * @return the propertyCycle
	 */
    public int getPropertyCycle() {
        return propertyCycle;
    }

    /**
	 * @param propertyCycle the propertyCycle to set
	 */
    public void setPropertyCycle(final int propertyCycle) {
        this.propertyCycle = propertyCycle;
    }

    @Override
    protected ConnectionPoolDataSource createDataSource() throws SQLException {
        final ConnectionPoolDataSource cpds = super.createDataSource();
        cpds.setLoginTimeout(this.loginTimeout);
        cpds.setLogWriter(this.logWriter);
        return cpds;
    }
}
