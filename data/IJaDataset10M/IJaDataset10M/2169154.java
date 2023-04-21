package org.datanucleus.sql4o.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.datanucleus.sql4o.Sql4oTestBase;
import org.datanucleus.sql4o.model.Contact;
import junit.framework.Assert;
import com.db4o.query.Query;

/**
 * TODO Enable these tests when we have a server starting up etc
 */
public class JdbcTest extends Sql4oTestBase {

    public JdbcTest(String name) {
        super(name);
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection con = null;
        Class.forName("org.datanucleus.sql4o.jdbc.Db4oDriver");
        con = DriverManager.getConnection("jdbc:db4o:file:db4o.db", null, null);
        return con;
    }

    /**
     * - test query time vs normal soda query - test that correct number of results are returned - maybe correct value
     * too
     * @throws SQLException
     */
    public void testPerformanceVsSoda() throws SQLException, ClassNotFoundException {
        int sodaSize = 0;
        int sqlSize = 0;
        {
            Query q = oc.query();
            q.constrain(Contact.class);
            q.descend("name").constrain("contact 2");
            q.descend("category").constrain("friends");
            List results = q.execute();
            sodaSize = results.size();
            oc.close();
        }
        {
            Connection conn = getConnection();
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * FROM " + Contact.class.getName() + " c where " + "name = 'contact 2' and " + " category = 'friends'");
                while (rs.next()) {
                    sqlSize++;
                }
                rs.close();
                stmt.close();
            } finally {
                if (conn != null) conn.close();
            }
        }
        Assert.assertEquals(sodaSize, sqlSize);
    }
}
