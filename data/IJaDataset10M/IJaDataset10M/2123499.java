package javatest.proxyPatternTest;

import junit.framework.TestCase;

interface Connection {

    Object get();

    void set(Object x);

    void release();
}

class ConnectionImplementation implements Connection {

    public Object get() {
        return null;
    }

    public void set(Object s) {
    }

    public void release() {
    }
}

class ConnectionPool {

    private static PoolManager pool = new PoolManager();

    private ConnectionPool() {
    }

    public static void addConnections(int number) {
        for (int i = 0; i < number; i++) pool.add(new ConnectionImplementation());
    }

    public static Connection getConnection() {
        PoolManager.ReleasableReference rr = (PoolManager.ReleasableReference) pool.get();
        if (rr == null) return null;
        return new ConnectionProxy(rr);
    }

    private static class ConnectionProxy implements Connection {

        private PoolManager.ReleasableReference implementation;

        public ConnectionProxy(PoolManager.ReleasableReference rr) {
            implementation = rr;
        }

        public Object get() {
            return ((Connection) implementation.getReference()).get();
        }

        public void set(Object x) {
            ((Connection) implementation.getReference()).set(x);
        }

        public void release() {
            implementation.release();
        }
    }
}

public class ConnectionPoolProxyDemo extends TestCase {

    static {
        ConnectionPool.addConnections(5);
    }

    public void test() {
        Connection c = ConnectionPool.getConnection();
        c.set(new Object());
        c.get();
        c.release();
    }

    public void testDisable() {
        Connection c = ConnectionPool.getConnection();
        String s = null;
        c.set(new Object());
        c.get();
        c.release();
        try {
            c.get();
        } catch (Exception e) {
            s = e.getMessage();
            System.out.println(s);
        }
        assertEquals(s, "Tried to use reference after it was released");
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(ConnectionPoolProxyDemo.class);
    }
}
