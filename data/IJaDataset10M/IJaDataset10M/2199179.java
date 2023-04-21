package com.handjoys.dbpool;

import java.sql.*;
import java.util.*;
import com.handjoys.logger.FileLogger;

/**
 * <b>数据库操作封装类,用于本包内部使用</b>
 * <p>数据库操作的封装类,封装的操作有:
 * <li>连接数据库</li>
 * <li>断开连接</li>
 * <li>执行无返回sql查询</li>
 * <li>执行有返回sql查询</li>
 * </p>
 * @author 汉娱网络.技术部.wanggl
 * @since 1.1
 */
public class DBOperator {

    /**
	 * <b>连接数据库</b>
	 * <p>连接数据库并把连接的句柄返回,失败的话,返回null</p>
	 * @param driverStr  连接驱动的字符串
	 * @param connectStr 连接的字符串
	 * @param dbUser     用户名
	 * @param dbPassword 密码
	 * @return 数据库的连接句柄
	 * @since 1.1
	 */
    public Connection connectDataBase(String driverStr, String connectStr, String dbUser, String dbPassword) {
        Connection con = null;
        try {
            Class.forName(driverStr);
            con = DriverManager.getConnection(connectStr + "?user=" + dbUser + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=utf-8");
            if (con != null) {
                FileLogger.debug("DB:" + connectStr + ", Successfully connected!");
            }
        } catch (ClassNotFoundException e) {
            FileLogger.error("we can not find the jdbc driver");
        } catch (SQLException e) {
            FileLogger.error("DB:" + connectStr + ", connected failed! Reason:" + e.getMessage());
            con = null;
        }
        return con;
    }

    /**
	 * <b>执行无返回查询</b>
	 * <p>执行诸如insert, update,delete 之类的查询</p>
	 * @param queryStr 要查询的语句
	 * @param con 连接句柄
	 * @return 查询影响条数
	 * @throws Exception
	 * @since 1.1
	 */
    public int update(String queryStr, Connection con) throws Exception {
        int ret = 0;
        PreparedStatement pstmt;
        if (con != null) {
            pstmt = con.prepareStatement(queryStr);
            try {
                FileLogger.debug("ExeSql: " + queryStr);
                ret = pstmt.executeUpdate();
            } catch (Exception e) {
                ret = 0;
                FileLogger.error("Execute Sql:" + queryStr + "\t [failed] reason:" + e.getMessage());
                throw (e);
            } finally {
                pstmt.close();
            }
        }
        return ret;
    }

    public int batchTransactionUpdate(List<String> queryStrLisyt, Connection con) throws Exception {
        int ret = 0;
        Statement stmt;
        if (con != null) {
            con.setAutoCommit(false);
            stmt = con.createStatement();
            try {
                stmt.executeUpdate("START TRANSACTION;");
                for (int i = 0; i < queryStrLisyt.size(); i++) {
                    stmt.addBatch(queryStrLisyt.get(i));
                }
                int[] updateCounts = stmt.executeBatch();
                for (int i = 0; i < updateCounts.length; i++) {
                    FileLogger.debug("batch update result:" + updateCounts[i] + ", Statement.SUCCESS_NO_INFO" + Statement.SUCCESS_NO_INFO);
                    if (updateCounts[i] == Statement.SUCCESS_NO_INFO || updateCounts[i] > 0) {
                        ret++;
                    } else if (updateCounts[i] == Statement.EXECUTE_FAILED) ;
                    {
                        throw new Exception("query failed, while process batch update");
                    }
                }
                con.commit();
            } catch (Exception e) {
                ret = 0;
                FileLogger.debug(e.getMessage());
                con.rollback();
            } finally {
                con.setAutoCommit(true);
                stmt.close();
            }
        }
        return ret;
    }

    /**
	 * <b>执行单条有返回语句</b>
	 * <p>执行select的单条查询,查询失败返回null</p>
	 * @param queryStr 查询语句
	 * @param con  连接句柄
	 * @return 查询结果集
	 */
    public List<HashMap> select(String queryStr, Connection con) throws Exception {
        List<HashMap> ret = new ArrayList<HashMap>();
        ResultSet rset = null;
        PreparedStatement stmt;
        ResultSetMetaData mData;
        int count = 0;
        if (con != null) {
            stmt = con.prepareStatement(queryStr);
            try {
                FileLogger.debug("ExeSql: " + queryStr);
                rset = stmt.executeQuery();
                FileLogger.debug("Excecute query: " + queryStr + "\t[ok]");
                mData = rset.getMetaData();
                count = mData.getColumnCount();
                while (rset.next()) {
                    HashMap<String, Object> tmpHashMap = new HashMap<String, Object>();
                    for (int i = 1; i <= count; i++) {
                        Object tmpobj = rset.getObject(i);
                        if (tmpobj == null) {
                            switch(mData.getColumnType(i)) {
                                case Types.BIGINT:
                                    tmpobj = -1L;
                                    break;
                                case Types.INTEGER:
                                case Types.SMALLINT:
                                    tmpobj = -1;
                                    break;
                                case Types.BIT:
                                    tmpobj = 0;
                                    break;
                                case Types.CHAR:
                                case Types.VARCHAR:
                                case Types.LONGVARCHAR:
                                case Types.CLOB:
                                    tmpobj = "";
                                    break;
                                case Types.TIME:
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    tmpobj = new java.util.Date();
                                    break;
                                case Types.BINARY:
                                case Types.LONGVARBINARY:
                                case Types.VARBINARY:
                                    tmpobj = new Object();
                                    break;
                                case Types.DECIMAL:
                                case Types.DOUBLE:
                                case Types.FLOAT:
                                case Types.NUMERIC:
                                    tmpobj = 0.0D;
                                    break;
                            }
                            tmpHashMap.put(mData.getColumnName(i), tmpobj);
                        } else {
                            tmpHashMap.put(mData.getColumnName(i), rset.getObject(i));
                        }
                    }
                    ret.add(tmpHashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileLogger.error("Excecute query: " + queryStr + "\t[failed] Reason:" + e.getMessage());
                ret = null;
                throw (e);
            } finally {
                stmt.close();
            }
        }
        return ret;
    }

    /**
	 * <b>关闭连接</b>
	 * <p>关闭传入的连接</p>
	 * @param con 连接句柄
	 */
    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                FileLogger.debug("A connection lost while closing it, please check com.handjoys.dbpool.DBOperator");
            }
        }
    }
}
