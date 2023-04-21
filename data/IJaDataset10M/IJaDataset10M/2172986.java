package gnu.beanfactory.jdbc;

import junit.framework.*;
import gnu.beanfactory.jdbc.*;
import gnu.beanfactory.setter.*;

/**
 * JUnit Test Case
 **/
public class ConnectionPoolTestCase extends TestCase {

    public ConnectionPoolTestCase(String name) {
        super(name);
    }

    public void testBasicConnectionPool() {
        BasicConnectionPool p = new BasicConnectionPool();
        assertTrue(p.getPoolName() != null);
    }
}
