package org.springframework.scheduling.quartz;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.quartz.SchedulerConfigException;
import org.quartz.impl.jdbcjobstore.JobStoreCMT;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Subclass of Quartz' JobStoreCMT class that delegates to a Spring-managed
 * DataSource instead of using a Quartz-managed connection pool. This JobStore
 * will be used if SchedulerFactoryBean's "dataSource" property is set.
 *
 * <p>Supports both transactional and non-transactional DataSource access.
 * With a non-XA DataSource and local Spring transactions, a single DataSource
 * argument is sufficient. In case of an XA DataSource and global JTA transactions,
 * SchedulerFactoryBean's "nonTransactionalDataSource" property should be set,
 * passing in a non-XA DataSource that will not participate in global transactions.
 *
 * <p>Operations performed by this JobStore will properly participate in any
 * kind of Spring-managed transaction, as it uses Spring's DataSourceUtils
 * connection handling methods that are aware of a current transaction.
 *
 * <p>Note that all Quartz Scheduler operations that affect the persistent
 * job store should usually be performed within active transaction, as they
 * assume to get proper locks etc.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see SchedulerFactoryBean#setDataSource
 * @see SchedulerFactoryBean#setNonTransactionalDataSource
 * @see org.springframework.jdbc.datasource.DataSourceUtils#doGetConnection
 * @see org.springframework.jdbc.datasource.DataSourceUtils#releaseConnection
 */
public class LocalDataSourceJobStore extends JobStoreCMT {

    /**
	 * Name used for the transactional ConnectionProvider for Quartz.
	 * This provider will delegate to the local Spring-managed DataSource.
	 * @see org.quartz.utils.DBConnectionManager#addConnectionProvider
	 * @see SchedulerFactoryBean#setDataSource
	 */
    public static final String TX_DATA_SOURCE_PREFIX = "springTxDataSource.";

    /**
	 * Name used for the non-transactional ConnectionProvider for Quartz.
	 * This provider will delegate to the local Spring-managed DataSource.
	 * @see org.quartz.utils.DBConnectionManager#addConnectionProvider
	 * @see SchedulerFactoryBean#setDataSource
	 */
    public static final String NON_TX_DATA_SOURCE_PREFIX = "springNonTxDataSource.";

    private DataSource dataSource;

    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        this.dataSource = SchedulerFactoryBean.getConfigTimeDataSource();
        if (this.dataSource == null) {
            throw new SchedulerConfigException("No local DataSource found for configuration - " + "dataSource property must be set on SchedulerFactoryBean");
        }
        setDataSource(TX_DATA_SOURCE_PREFIX + getInstanceName());
        setDontSetAutoCommitFalse(true);
        DBConnectionManager.getInstance().addConnectionProvider(TX_DATA_SOURCE_PREFIX + getInstanceName(), new ConnectionProvider() {

            public Connection getConnection() throws SQLException {
                return DataSourceUtils.doGetConnection(dataSource);
            }

            public void shutdown() {
            }
        });
        DataSource nonTxDataSource = SchedulerFactoryBean.getConfigTimeNonTransactionalDataSource();
        final DataSource nonTxDataSourceToUse = (nonTxDataSource != null ? nonTxDataSource : this.dataSource);
        setNonManagedTXDataSource(NON_TX_DATA_SOURCE_PREFIX + getInstanceName());
        DBConnectionManager.getInstance().addConnectionProvider(NON_TX_DATA_SOURCE_PREFIX + getInstanceName(), new ConnectionProvider() {

            public Connection getConnection() throws SQLException {
                return nonTxDataSourceToUse.getConnection();
            }

            public void shutdown() {
            }
        });
        super.initialize(loadHelper, signaler);
    }

    protected void closeConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.dataSource);
    }
}
