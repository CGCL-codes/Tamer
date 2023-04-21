package org.pprun.common.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * This monitor performs a {@literal SQL} to check the status of the Database.
 * 
 * @author <a href="mailto:pizhigang@letv.com">pizhigang</a>
 */
public class DataBaseDependencyMonitorMXBeanImpl implements DependencyMonitorMXBean {

    private static final String TEST_SQL = "SELECT 0 FROM DUAL";

    private static final Logger log = LoggerFactory.getLogger(DataBaseDependencyMonitorMXBeanImpl.class);

    private int timeout;

    private DataSource dataSource;

    private String jdbcUrl;

    private String name;

    @Override
    public boolean isLiving() {
        boolean success = true;
        Statement statement = null;
        ResultSet result = null;
        Connection connection = null;
        log.debug("Starting to check {}", dataSource);
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.setQueryTimeout(timeout / 1000);
            statement.execute(TEST_SQL);
            result = statement.getResultSet();
            result.next();
            if (0 != result.getInt(1)) {
                success = false;
            }
        } catch (Throwable e) {
            log.warn("Exception while performing database check.", e);
            success = false;
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                log.warn("failed to close result set.", e);
                success = false;
            }
            try {
                if (null != statement) {
                    statement.close();
                }
            } catch (SQLException e) {
                log.warn("failed to close statement.", e);
                success = false;
            }
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.warn("failed to close database connection.", e);
                success = false;
            }
        }
        log.debug("check {} done.", dataSource);
        return success;
    }

    @Override
    public String asJson() throws Exception {
        ObjectMapper om = new ObjectMapper();
        return new StringBuilder("dependency.").append(name).append(": ").append(jdbcUrl).append(".isLiving=").append(om.writeValueAsString(this)).toString();
    }

    @Override
    public String toString() {
        return new StringBuilder("dependency.").append(name).append(": ").append(jdbcUrl).append(".isLiving=").append(isLiving()).toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Required
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @JsonIgnore
    @Required
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @JsonIgnore
    @Required
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
