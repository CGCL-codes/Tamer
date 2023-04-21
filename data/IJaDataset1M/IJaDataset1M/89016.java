package org.paradise.dms.dao.impl;

import java.sql.*;
import org.apache.log4j.Logger;
import org.paradise.dms.pojo.BaseException;

/**
 * 管理数据库连接池：取得连接，关闭连接
 *
 *
 */
public class ConnectionManager {

    private static Logger log = Logger.getLogger(ConnectionManager.class);

    /**
	 * Description:关闭指定的连接
	 * 
	 * @param conn
	 *            需要关闭的连接
	 */
    public static void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
	 * Description:关闭指定的数据集
	 * 
	 * @param rs
	 *            需要关闭的数据集
	 */
    public static void closeRS(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
	 * Description:关闭指定的Statement
	 * 
	 * @param statement
	 *            需要关闭的Statement
	 */
    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
	 * Description:关闭指定的PreparedStatement
	 * @param pstmt 需要关闭的PreparedStatement
	 */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
	 * Description:取得数据库连接。暂时不用数据库连接池，故name参数不用。
	 * 
	 * @param name 暂时不用，
	 */
    public static Connection getConn(String name) throws BaseException {
        Connection conn = null;
        try {
            String mySqlDriver = "org.gjt.mm.mysql.Driver";
            String url = "jdbc:mysql://localhost/dms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
            Class.forName(mySqlDriver);
            conn = DriverManager.getConnection(url, "root", "udbvj");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return conn;
    }
}
