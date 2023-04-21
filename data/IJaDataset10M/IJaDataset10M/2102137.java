package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * 2008-12-7
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class ParameterMetaTest {

    /**
	 * @param args
	 * @throws SQLException
	 */
    public static void main(String[] args) throws SQLException {
        Object[] params = new Object[] { "lisi", 100f };
        read("select * from user where name=? and  money > ?", params);
    }

    static void read(String sql, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++) {
                ps.setObject(i, params[i - 1]);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getDate("birthday") + "\t" + rs.getFloat("money"));
            }
        } finally {
            JdbcUtils.free(rs, ps, conn);
        }
    }
}
