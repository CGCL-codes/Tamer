package com.dotmarketing.scheduler;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.quartz.SchedulerConfigException;
import org.quartz.impl.jdbcjobstore.JobStoreCMT;
import org.quartz.impl.jdbcjobstore.UpdateLockRowSemaphore;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.util.Logger;

public class DotJobStore extends JobStoreCMT {

    public static final String TX_DATA_SOURCE_PREFIX = "TxDataSource.";

    public static final String NON_TX_DATA_SOURCE_PREFIX = "NonTxDataSource.";

    private DataSource dataSource;

    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        this.dataSource = DbConnectionFactory.getDataSource();
        setDataSource(TX_DATA_SOURCE_PREFIX + getInstanceName());
        setDontSetAutoCommitFalse(true);
        DBConnectionManager.getInstance().addConnectionProvider(TX_DATA_SOURCE_PREFIX + getInstanceName(), new ConnectionProvider() {

            public Connection getConnection() throws SQLException {
                return DataSourceUtils.doGetConnection(dataSource);
            }

            public void shutdown() {
            }
        });
        final DataSource nonTxDataSourceToUse = this.dataSource;
        setNonManagedTXDataSource(NON_TX_DATA_SOURCE_PREFIX + getInstanceName());
        DBConnectionManager.getInstance().addConnectionProvider(NON_TX_DATA_SOURCE_PREFIX + getInstanceName(), new ConnectionProvider() {

            public Connection getConnection() throws SQLException {
                return nonTxDataSourceToUse.getConnection();
            }

            public void shutdown() {
            }
        });
        String dbType = DbConnectionFactory.getDBType();
        UpdateLockRowSemaphore sem = new UpdateLockRowSemaphore();
        sem.setUpdateLockRowSQL("UPDATE {0}LOCKS SET LOCK_NAME = LOCK_NAME WHERE LOCK_NAME = ?");
        sem.setTablePrefix("QRTZ_");
        if (dbType.equals(DbConnectionFactory.MYSQL)) {
            try {
                setDriverDelegateClass("org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
            } catch (Exception e) {
                Logger.info(this, e.getMessage());
            }
        } else if (dbType.equals(DbConnectionFactory.POSTGRESQL)) {
            try {
                setDriverDelegateClass("org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
            } catch (Exception e) {
                Logger.info(this, e.getMessage());
            }
        } else if (dbType.equals(DbConnectionFactory.MSSQL)) {
            try {
                setDriverDelegateClass("org.quartz.impl.jdbcjobstore.MSSQLDelegate");
                setLockHandler(sem);
            } catch (Exception e) {
                Logger.info(this, e.getMessage());
            }
        } else if (dbType.equals(DbConnectionFactory.ORACLE)) {
            try {
                setDriverDelegateClass("org.quartz.impl.jdbcjobstore.oracle.OracleDelegate");
                setLockHandler(sem);
            } catch (Exception e) {
                Logger.info(this, e.getMessage());
            }
        }
        super.initialize(loadHelper, signaler);
    }

    protected void closeConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.dataSource);
    }
}
