package ifpe.dao;

import ifpe.datasource.DataSourceFactory;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Sostenes
 */
public abstract class Dao {

    private DataSource dataSource;

    protected Connection getConnection() throws SQLException {
        dataSource = DataSourceFactory.getInstance().getDataSource();
        return dataSource.getConnection();
    }
}
