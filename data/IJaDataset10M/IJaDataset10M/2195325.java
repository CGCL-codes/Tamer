package com.liusoft.util.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * �ӹ���ݿ����ӣ���ֹ����ĳЩ��ݿⲻ֧����������׳����쳣
 * @author Winter Lau
 */
public class _Connection implements InvocationHandler {

    private Class[] infs;

    private Connection conn;

    private boolean supportTransaction;

    private boolean coding;

    public _Connection(Connection conn, boolean coding) {
        this.conn = conn;
        this.coding = coding;
        DatabaseMetaData dm = null;
        try {
            dm = conn.getMetaData();
            supportTransaction = dm.supportsTransactions();
            infs = conn.getClass().getInterfaces();
            if (infs == null || infs.length == 0) infs = IC;
        } catch (Exception e) {
        }
    }

    /**
	 * ��ȡ����Ĵ���
	 * @return
	 */
    public Connection getConnection() {
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), infs, this);
    }

    void close() throws SQLException {
        conn.close();
    }

    public Object invoke(Object proxy, Method m, Object args[]) throws Throwable {
        String method = m.getName();
        if ((M_SETAUTOCOMMIT.equals(method) || M_COMMIT.equals(method) || M_ROLLBACK.equals(method)) && !isSupportTransaction()) return null;
        Object obj = null;
        try {
            obj = m.invoke(conn, args);
            if (CREATESTATEMENT.equals(method) || PREPAREDSTATEMENT.equals(method)) return (new _Statement((Statement) obj, coding)).getStatement();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
        return obj;
    }

    public boolean isSupportTransaction() {
        return supportTransaction;
    }

    private static final Class[] IC = new Class[] { Connection.class };

    private static final String PREPAREDSTATEMENT = "prepareStatement";

    private static final String CREATESTATEMENT = "createStatement";

    private static final String M_SETAUTOCOMMIT = "setAutoCommit";

    private static final String M_COMMIT = "commit";

    private static final String M_ROLLBACK = "rollback";
}
