package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import ar.com.esaweb.PropertyLoader;
import ar.com.faelsoft.dbconnection.ConnectionManager;
import ar.com.faelsoft.dbconnection.ManagedDatasource;

public class AcquireConnectionTest {

    @Test
    public void acquireConnTest() throws SQLException {
        Properties properties = PropertyLoader.loadProperties("ar.com.esaweb.AppProps");
        Connection con = ConnectionManager.getConnection(properties);
        Assert.assertNotNull(con);
        con.close();
    }

    @Test
    public void multiAcquireConnTest() throws SQLException {
        Properties properties = PropertyLoader.loadProperties("ar.com.esaweb.AppProps");
        Connection con;
        for (int i = 0; i < 100; i++) {
            con = ConnectionManager.getConnection(properties);
            Assert.assertNotNull(con);
            con.close();
        }
    }

    @Test
    public void multiAcquireConnFromCustomDataSourceTest() throws SQLException {
        DataSource ds = new ManagedDatasource();
        Connection con;
        for (int i = 0; i < 100; i++) {
            con = ds.getConnection();
            Assert.assertNotNull(con);
            con.close();
        }
    }
}
