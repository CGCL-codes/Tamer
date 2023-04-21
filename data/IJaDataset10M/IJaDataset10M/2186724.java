package ifpe.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 *
 * @author Sostenes
 */
public class ConnectionProxy implements InvocationHandler {

    private static Connection connection;

    private ThreadLocalDataSource dataSource;

    private ConnectionProxy(Connection conn, ThreadLocalDataSource dataSource) {
        connection = conn;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(connection, args);
        if (method.getName().equals("close")) {
            dataSource.removeConnection();
        }
        return result;
    }

    public static Connection newInstance(Connection conn, ThreadLocalDataSource tlds) {
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[] { Connection.class }, new ConnectionProxy(conn, tlds));
    }
}
