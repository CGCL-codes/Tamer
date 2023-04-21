package org.datascooter.db.h2;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import org.datascooter.impl.ContextConnector;
import org.datascooter.inface.IContextConnector;
import org.datascooter.inface.IDataSource;
import org.h2.jdbcx.JdbcDataSource;

public class H2Conector extends ContextConnector {

    private JdbcDataSource jDBCdataSource;

    public H2Conector() {
        contextId = "h2";
    }

    public H2Conector(IDataSource source) throws SQLException {
        super(source);
    }

    @Override
    public boolean isSupportPagination() {
        return true;
    }

    @Override
    public ConnectionPoolDataSource createPooledDataSource() throws SQLException {
        if (jDBCdataSource == null) {
            jDBCdataSource = new org.h2.jdbcx.JdbcDataSource();
            jDBCdataSource.setURL(dataSource.getURL());
            jDBCdataSource.setUser(dataSource.getUser());
            jDBCdataSource.setPassword(dataSource.getPassword());
        }
        return jDBCdataSource;
    }

    @Override
    public IContextConnector copy(IDataSource source) throws SQLException {
        return new H2Conector(source);
    }
}
