package com.microstream.lib.dbf;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * ������DBConnectionManager֧�ֶ�һ�������������ļ��������ݿ�����
 * �صķ���.�ͻ�������Ե���getInstance()�������ʱ����Ψһʵ��.
 */
public class DBConnectionManager {

    private static DBConnectionManager instance;

    private static int clients;

    private Vector drivers = new Vector();

    private PrintWriter log;

    private Hashtable pools = new Hashtable();

    /**
	 * ����Ψһʵ��.����ǵ�һ�ε��ô˷���,�򴴽�ʵ��
	 * 
	 * @return DBConnectionManager Ψһʵ�� 022
	 */
    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        clients++;
        return instance;
    }

    /**
	 * ��������˽���Է�ֹ������󴴽�����ʵ��
	 */
    private DBConnectionManager() {
        init();
    }

    /**
	 * �����Ӷ��󷵻ظ�������ָ�������ӳ�
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * @param con
	 *            ���Ӷ���
	 */
    public void freeConnection(String name, Connection con) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            pool.freeConnection(con);
        }
    }

    /**
	 * ���һ�����õ�(���е�)����.���û�п�������,������������С����������� ����,�򴴽�������������
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * @return Connection �������ӻ�null
	 */
    public Connection getConnection(String name) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection();
        }
        return null;
    }

    /**
	 * ���һ����������.��û�п�������,������������С���������������, �򴴽�������������.����,��ָ����ʱ���ڵȴ������߳��ͷ�����.
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @param time
	 *            �Ժ���Ƶĵȴ�ʱ��
	 * @return Connection �������ӻ�null
	 */
    public Connection getConnection(String name, long time) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection(time);
        }
        return null;
    }

    /**
	 * �ر���������,����������ע��
	 */
    public synchronized void release() {
        if (--clients != 0) {
            return;
        }
        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements()) {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
            pool.release();
        }
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements()) {
            Driver driver = (Driver) allDrivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log("����JDBC����� " + driver.getClass().getName() + "��ע��");
            } catch (SQLException e) {
                log(e, "�޷���������JDBC������ע��: " + driver.getClass().getName());
            }
        }
    }

    /**
	 * ���ָ�����Դ������ӳ�ʵ��.
	 * 
	 * @param props
	 *            ���ӳ�����
	 */
    private void createPools(Properties props) {
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".url")) {
                String poolName = name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(poolName + ".url");
                if (url == null) {
                    log("û��Ϊ���ӳ�" + poolName + "ָ��URL");
                    continue;
                }
                String user = props.getProperty(poolName + ".user");
                String password = props.getProperty(poolName + ".password");
                String maxconn = props.getProperty(poolName + ".maxconn", "0");
                int max;
                try {
                    max = Integer.valueOf(maxconn).intValue();
                } catch (NumberFormatException e) {
                    log("������������������: " + maxconn + " .���ӳ�: " + poolName);
                    max = 0;
                }
                DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, max);
                pools.put(poolName, pool);
                log("�ɹ��������ӳ�" + poolName);
            }
        }
    }

    /**
	 * ��ȡ������ɳ�ʼ��
	 */
    private void init() {
        InputStream is = getClass().getResourceAsStream("/db.properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
        } catch (Exception e) {
            System.err.println("���ܶ�ȡ�����ļ�. " + "��ȷ��db.properties��CLASSPATHָ����·����");
            return;
        }
        String logFile = dbProps.getProperty("logfile", "DBConnectionManager.log");
        try {
            log = new PrintWriter(new FileWriter(logFile, true), true);
        } catch (IOException e) {
            System.err.println("�޷�����־�ļ�: " + logFile);
            log = new PrintWriter(System.err);
        }
        loadDrivers(dbProps);
        createPools(dbProps);
    }

    /**
	 * װ�غ�ע������JDBC�����
	 * 
	 * @param props
	 *            ����
	 */
    private void loadDrivers(Properties props) {
        String driverClasses = props.getProperty("drivers");
        StringTokenizer st = new StringTokenizer(driverClasses);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();
            try {
                Driver driver = (Driver) Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
                drivers.addElement(driver);
                log("�ɹ�ע��JDBC�����" + driverClassName);
            } catch (Exception e) {
                log("�޷�ע��JDBC�����: " + driverClassName + ", ����: " + e);
            }
        }
    }

    /**
	 * ���ı���Ϣд����־�ļ�
	 */
    private void log(String msg) {
        log.println(new Date() + ": " + msg);
    }

    /**
	 * ���ı���Ϣ���쳣д����־�ļ�
	 */
    private void log(Throwable e, String msg) {
        log.println(new Date() + ": " + msg);
        e.printStackTrace(log);
    }

    /**
	 * ���ڲ��ඨ����һ�����ӳ�.���ܹ����Ҫ�󴴽�������,ֱ��Ԥ������ ��������Ϊֹ.�ڷ������Ӹ�ͻ�����֮ǰ,���ܹ���֤���ӵ���Ч��.
	 */
    class DBConnectionPool {

        private int checkedOut;

        private Vector freeConnections = new Vector();

        private int maxConn;

        private String name;

        private String password;

        private String URL;

        private String user;

        /**
		 * �����µ����ӳ�
		 * 
		 * @param name
		 *            ���ӳ�����
		 * @param URL
		 *            ��ݿ��JDBC URL
		 * @param user
		 *            ��ݿ��ʺ�,�� null
		 * @param password
		 *            ����,�� null
		 * @param maxConn
		 *            �����ӳ����?�������������
		 */
        public DBConnectionPool(String name, String URL, String user, String password, int maxConn) {
            this.name = name;
            this.URL = URL;
            this.user = user;
            this.password = password;
            this.maxConn = maxConn;
        }

        /**
		 * ������ʹ�õ����ӷ��ظ����ӳ�
		 * 
		 * @param con
		 *            �ͻ������ͷŵ�����
		 */
        public synchronized void freeConnection(Connection con) {
            freeConnections.addElement(con);
            checkedOut--;
            notifyAll();
        }

        /**
		 * �����ӳػ��һ����������.��û�п��е������ҵ�ǰ������С��������� ������,�򴴽�������.��ԭ���Ǽ�Ϊ���õ����Ӳ�����Ч,�������ɾ��֮,
		 * Ȼ��ݹ�����Լ��Գ����µĿ�������.
		 */
        public synchronized Connection getConnection() {
            Connection con = null;
            if (freeConnections.size() > 0) {
                con = (Connection) freeConnections.firstElement();
                freeConnections.removeElementAt(0);
                try {
                    if (con.isClosed()) {
                        log("�����ӳ�" + name + "ɾ��һ����Ч����");
                        con = getConnection();
                    }
                } catch (SQLException e) {
                    log("�����ӳ�" + name + "ɾ��һ����Ч����");
                    con = getConnection();
                }
            } else if (maxConn == 0 || checkedOut < maxConn) {
                con = newConnection();
            }
            if (con != null) {
                checkedOut++;
            }
            return con;
        }

        /**
		 * �����ӳػ�ȡ��������.����ָ���ͻ������ܹ��ȴ���ʱ�� �μ�ǰһ��getConnection()����.
		 * 
		 * @param timeout
		 *            �Ժ���Ƶĵȴ�ʱ������
		 */
        public synchronized Connection getConnection(long timeout) {
            long startTime = new Date().getTime();
            Connection con;
            while ((con = getConnection()) == null) {
                try {
                    wait(timeout);
                } catch (InterruptedException e) {
                }
                if ((new Date().getTime() - startTime) >= timeout) {
                    return null;
                }
            }
            return con;
        }

        /**
		 * �ر���������
		 */
        public synchronized void release() {
            Enumeration allConnections = freeConnections.elements();
            while (allConnections.hasMoreElements()) {
                Connection con = (Connection) allConnections.nextElement();
                try {
                    con.close();
                    log("�ر����ӳ�" + name + "�е�һ������");
                } catch (SQLException e) {
                    log(e, "�޷��ر����ӳ�" + name + "�е�����");
                }
            }
            freeConnections.removeAllElements();
        }

        /**
		 * �����µ�����
		 */
        private Connection newConnection() {
            Connection con = null;
            try {
                if (user == null) {
                    con = DriverManager.getConnection(URL);
                } else {
                    con = DriverManager.getConnection(URL, user, password);
                }
                log("���ӳ�" + name + "����һ���µ�����");
            } catch (SQLException e) {
                log(e, "�޷���������URL������: " + URL);
                return null;
            }
            return con;
        }
    }
}
