package poolman.tests.basic;

import com.codestudio.sql.PoolMan;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StringTests extends TestCase {

    private String selectSQL;

    private String insertSQL;

    private String deleteSQL;

    public StringTests(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(StringTests.class);
    }

    protected void setUp() {
        this.selectSQL = "select * from neville";
    }

    public void testDataSourceString() {
        DataSource ds = null;
        try {
            ds = getDefaultDataSource();
            String s = ds.toString();
            System.out.println(s);
            assertNotNull(s);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        }
    }

    public void testConnectionString() {
        Connection con = null;
        try {
            con = getConnection();
            String s = con.toString();
            System.out.println(s);
            assertNotNull(s);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void testStatementString() {
        Connection con = null;
        Statement s = null;
        try {
            con = getConnection();
            s = con.createStatement();
            String str = s.toString();
            System.out.println(str);
            assertNotNull(str);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            try {
                s.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void testPreparedStatementString() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("update neville set name = ? where id = 1");
            ps.setString(1, "stringtest");
            ps.execute();
            String str = ps.toString();
            System.out.println(str);
            assertNotNull(str);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void testCallableStatementString() {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = getConnection();
            cs = con.prepareCall("{call usp_poolmantest[(?,?)]}");
            cs.setInt(1, 1);
            cs.registerOutParameter(2, java.sql.Types.VARCHAR);
            String str = cs.toString();
            System.out.println(str);
            assertNotNull(str);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            try {
                cs.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void testResultSetString() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("select name from neville where id = ?");
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
            }
            String str = rs.toString();
            System.out.println(str);
            assertNotNull(str);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return getDefaultDataSource().getConnection();
    }

    private DataSource getDefaultDataSource() throws SQLException {
        DataSource defaultDS = PoolMan.getDataSource();
        return defaultDS;
    }
}
