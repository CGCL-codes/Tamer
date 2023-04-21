package org.fantasy.common.db.pool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.olap4j.OlapConnection;

/**
 * 连接池测试
 * @author 王文成
 * @version 1.0
 * @since 2009-3-24
 */
public class ConnectionPoolTest {

    static DBOptions options = new DBOptions();

    ;

    static {
        String uri = "jdbc:xmla:Server=http://172.16.3.52/OLAP/msmdpump.dll;Catalog=XZ_SSAS";
        options.setDatabaseURL(uri);
    }

    /**
	 * 多线程测试
	 * 
	 * @throws Exception
	 */
    static void manyThread() throws Exception {
        Runnable run = new Runnable() {

            public void run() {
                OlapConnection conn = null;
                try {
                    conn = ConnectionPool.getOlapConnection(options);
                    Thread.sleep((long) (Math.random() * 5000));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(run, ("ThreadTest[" + i + "]"));
            Thread.sleep((long) (Math.random() * 100));
            thread.start();
        }
    }

    /**
	 * 单线程测试
	 */
    static void oneTest() {
        String ip = null;
        String dbName = null;
        String url = "jdbc:xmla:Server=http://" + ip + "/OLAP/msmdpump.dll;Catalog=" + dbName;
        DBOptions options = new DBOptions();
        options.setDatabaseURL(url);
        List<OlapConnection> list = new ArrayList<OlapConnection>();
        for (int i = 0; i < 30; i++) {
            try {
                OlapConnection conn = ConnectionPool.getOlapConnection(options);
                list.add(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (OlapConnection conn : list) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        manyThread();
    }
}
