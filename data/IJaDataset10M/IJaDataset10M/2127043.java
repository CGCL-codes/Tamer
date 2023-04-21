package org.t2framework.lucy.tx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import junit.framework.Assert;
import org.t2framework.lucy.annotation.core.Inject;

public class ServiceImpl implements Service {

    protected DataSource ds;

    public void begin() throws Exception {
        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS MOGE (ID INT PRIMARY KEY,NAME VARCHAR(255));");
            ps.execute();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public void exec() throws Exception {
        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO MOGE (ID,NAME)VALUES(1,'HOGE');");
            ps.execute();
            ps = con.prepareStatement("SELECT * FROM MOGE;");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Assert.assertEquals(1, rs.getInt("ID"));
                Assert.assertEquals("HOGE", rs.getString("NAME"));
            }
            throw new IllegalStateException();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public void proc() throws Exception {
        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM MOGE;");
            ResultSet rs = ps.executeQuery();
            Assert.assertFalse(rs.next());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Inject
    public void setDs(DataSource ds) {
        this.ds = ds;
    }
}
