package com.esa.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Pool {

    private static Pool instance = null;

    private int maxConnect = 100;

    private int normalConnect = 10;

    private String password = "900425";

    private String url = "jdbc:mysql://localhost:3306/esc";

    private String user = "root";

    private String driverName = "com.mysql.jdbc.Driver";

    Driver driver = null;

    DBConnectionPool pool = null;

    private Pool() {
        loadDrivers(driverName);
        createPool();
    }

    /**
  * װ�غ�ע������JDBC�����
  */
    private void loadDrivers(String dri) {
        String driverClassName = dri;
        try {
            driver = (Driver) Class.forName(driverClassName).newInstance();
            DriverManager.registerDriver(driver);
            System.out.println("�ɹ�ע��JDBC�����" + driverClassName);
        } catch (Exception e) {
            System.out.println("�޷�ע��JDBC�����:" + driverClassName + ",����:" + e);
        }
    }

    /**
     *  �������ӳ�
     */
    private void createPool() {
        pool = new DBConnectionPool(password, url, user, normalConnect, maxConnect);
        if (pool != null) {
            System.out.println("�������ӳسɹ�");
        } else {
            System.out.println("�������ӳ�ʧ��");
        }
    }

    /**
   *  ����Ψһʵ��
   */
    public static synchronized Pool getInstance() {
        if (instance == null) {
            instance = new Pool();
        }
        return instance;
    }

    /**
     * ���һ�����õ�����,���û���򴴽�һ������,��С�������������
*/
    public Connection getConnection() {
        if (pool != null) {
            return pool.getConnection();
        }
        return null;
    }

    /**
     *  ���һ������,��ʱ������
*/
    public Connection getConnection(long time) {
        if (pool != null) {
            return pool.getConnection(time);
        }
        return null;
    }

    /** �����Ӷ��󷵻ظ����ӳ�
     */
    public void freeConnection(Connection con) {
        if (pool != null) {
            pool.freeConnection(con);
        }
    }

    /** ���ص�ǰ����������
     */
    public int getnum() {
        return pool.getnum();
    }

    /**
     * ���ص�ǰ������
     */
    public int getnumActive() {
        return pool.getnumActive();
    }

    /**
     * �ر���������,������ע��
*/
    public synchronized void release() {
        pool.release();
        try {
            DriverManager.deregisterDriver(driver);
            System.out.println("����JDBC����� " + driver.getClass().getName());
        } catch (SQLException e) {
            System.out.println("�޷�����JDBC������ע��:" + driver.getClass().getName());
        }
    }
}
