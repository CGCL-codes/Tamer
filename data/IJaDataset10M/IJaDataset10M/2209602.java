package org.horus.miniframewrk;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

final class JndiSource implements ConnectionSource {

    private final DataSource dataSource;

    JndiSource(String jndi) throws ConnectionSourceException {
        Context ctx;
        try {
            ctx = new InitialContext();
            dataSource = DataSource.class.cast(ctx.lookup(jndi));
        } catch (NamingException nex) {
            throw new ConnectionSourceException("Jndi name not found", nex);
        } catch (ClassCastException ccex) {
            throw new ConnectionSourceException("Jndi object is not a DataSource", ccex);
        } catch (Exception ex) {
            throw new ConnectionSourceException("Not expected exception creating JndiSource", ex);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
