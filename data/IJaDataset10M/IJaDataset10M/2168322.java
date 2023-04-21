package services.core.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class DatabaseConnectionProvider {

    public static Connection getConnection(String jndiName, String jdbcDriver, String connectionString, String username, String password) throws SQLException {
        Connection connection = null;
        try {
            JndiTemplate jndiTemplate = new JndiTemplate();
            DataSource jndiObject = (DataSource) jndiTemplate.lookup(jndiName, DataSource.class);
            if (null != jndiObject) {
                connection = ((DataSource) jndiObject).getConnection();
            }
        } catch (SQLException e) {
        } catch (NamingException e) {
        }
        if (null == connection && null != jdbcDriver && null != connectionString) {
            try {
                SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
                DataSource datasource = createPooledDatasourceConnection(jdbcDriver, connectionString, username, password);
                builder.bind(jndiName, datasource);
                connection = datasource.getConnection();
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null == connection) {
            throw new SQLException("Failed to retrieve database connection");
        } else {
            return connection;
        }
    }

    public static DataSource createPooledDatasourceConnection(String jdbcDriver, String connectionString, String username, String password) {
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = null;
        if (null != username) {
            connectionFactory = new DriverManagerConnectionFactory(connectionString, username, password);
        } else {
            connectionFactory = new DriverManagerConnectionFactory(connectionString, null);
        }
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
        return dataSource;
    }
}
