package org.fireflow.example.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hsqldb.Server;

public class HsqlListener implements ServletContextListener {

    public static void main(String args[]) {
        String classpath = URLUtil.getClassPath(HsqlListener.class);
        System.out.println(classpath);
    }

    public static final String DATABASE_NAME = "fireflow";

    public static final String DATABASE_PATH = "\\db\\";

    public static final int PORT = 9001;

    public static final Server server = new Server();

    /**
	 * context初始化时,将启动hsql服务.
	 * 
	 * @param sce
	 *            ServletContextEvent
	 */
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, String> dbConfig = getDBConfig(sce);
        server.setDatabaseName(0, dbConfig.get("dbname"));
        server.setDatabasePath(0, dbConfig.get("dbpath"));
        int port = PORT;
        try {
            port = Integer.parseInt(dbConfig.get("port"));
        } catch (Exception e) {
            port = PORT;
        }
        server.setPort(port);
        server.setSilent(true);
        server.start();
        System.out.println("数据库服务已启动");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }
    }

    private Map<String, String> getDBConfig(ServletContextEvent sce) {
        Map<String, String> dbConfig = new HashMap<String, String>();
        String classpath = URLUtil.getClassPath(this.getClass());
        String dbpath = sce.getServletContext().getInitParameter("dbpath");
        if (null == dbpath) {
            dbpath = DATABASE_PATH;
        }
        if (!dbpath.startsWith("\\") || !dbpath.startsWith("/")) {
            dbpath = "/" + dbpath;
        }
        if (!dbpath.endsWith("\\") || !dbpath.endsWith("/")) {
            dbpath = dbpath + "/";
        }
        String dbname = sce.getServletContext().getInitParameter("dbname");
        if (null == dbname) {
            dbpath = DATABASE_NAME;
        }
        String strPort = sce.getServletContext().getInitParameter("port");
        if (null == strPort) {
            strPort = PORT + "";
        }
        dbpath = classpath + dbpath + dbname;
        dbConfig.put("dbpath", dbpath);
        dbConfig.put("dbname", dbname);
        dbConfig.put("port", strPort);
        return dbConfig;
    }

    /**
	 * context销毁时.关闭数据库
	 * 
	 * @param sce
	 *            ServletContextEvent
	 */
    public void contextDestroyed(ServletContextEvent sce) {
        server.stop();
        System.out.println("数据库服务已结束");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }
    }
}
