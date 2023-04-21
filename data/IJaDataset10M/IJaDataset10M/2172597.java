package pl.kernelpanic.dbmonster.test;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import junit.framework.TestCase;
import pl.kernelpanic.dbmonster.DBMonster;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.DBCPConnectionProvider;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.schema.SchemaUtil;

/**
 * DBMonster test.
 *
 * @author Piotr Maj &lt;pm@jcake.com&gt;
 *
 * @version $Id: MySQLTest.java,v 1.1 2004/05/22 13:14:15 majek Exp $
 */
public class MySQLTest extends TestCase {

    private static final String TEST_NAME = "mysql";

    public final void testDBMonster() throws Exception {
        if (!Utils.performTest(TEST_NAME)) {
            Utils.skipTest(TEST_NAME);
            return;
        }
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1/dbmonster";
        String user = "dbmonster";
        String pass = "dbmonster";
        Properties props = new Properties();
        props.put("dbmonster.jdbc.driver", driver);
        props.put("dbmonster.jdbc.url", url);
        props.put("dbmonster.jdbc.username", user);
        props.put("dbmonster.jdbc.password", pass);
        props.put("dbmonster.max-tries", "1000");
        ConnectionProvider cp = new DBCPConnectionProvider(driver, url, user, pass);
        Connection conn = cp.getConnection();
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate("DROP TABLE DEFAULT_TEST");
        } catch (Exception e) {
        }
        try {
            stmt.executeUpdate("DROP TABLE USERS");
        } catch (Exception e) {
        }
        try {
            stmt.executeUpdate("DROP TABLE GROUPS");
        } catch (Exception e) {
        }
        try {
            stmt.executeUpdate("CREATE TABLE USERS (id int not null," + "login varchar(255) not null, id_group int)");
            stmt.executeUpdate("CREATE TABLE GROUPS (id int not null," + "name varchar(255) not null, description text)");
            stmt.executeUpdate("CREATE TABLE DEFAULT_TEST (id int8  not null auto_increment primary key," + "name varchar(255) not null)");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
        DBMonster dbm = new DBMonster();
        dbm.setConnectionProvider(cp);
        dbm.setProperties(props);
        String u = "/pl/kernelpanic/dbmonster/test/resources/dbmonster-schema.xml";
        URL ur = getClass().getResource(u);
        Schema schema = SchemaUtil.loadSchema(ur, dbm.getLogger());
        dbm.addSchema(schema);
        try {
            dbm.doTheJob();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
