package ps.app.ode.test;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import ps.app.ode.connection.ConnectionFactory;

/** teste */
public class AllTest {

    @Test
    public void ConnectionTest() {
        Connection con = ConnectionFactory.getOracleJDBCConnection();
        try {
            Assert.assertTrue("Connection opened", !con.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
