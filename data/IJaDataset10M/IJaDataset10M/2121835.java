package pub.test;

import junit.framework.*;
import pub.db.ConnectionFactory;
import pub.db.ConnectionFactoryManager;
import java.sql.Connection;

/** Base class of tests that need a connection from an empty database.
    This just defines a setUp() and tearDown() that all subclasses
    should use.  All subclasses, then, should remember to do
    super.setUp() and super.tearDown() first when overriding these
    methods!
 */
public class DatabaseTestCase extends TestCase {

    public pub.db.PubConnection conn;

    public ConnectionFactory connFactory;

    public DatabaseTestCase() {
        super();
    }

    public DatabaseTestCase(String s) {
        super(s);
    }

    protected String MAINTENANCE_USER_ID = "1";

    /** Returns the filename where test data lives */
    protected String testDataPath(String s) {
        return pub.utils.PubProperties.getTestDataDirectory() + java.io.File.separator + s;
    }

    public void setUp() throws Exception {
        connFactory = ConnectionFactoryManager.getInstance().getTestingFactory();
        conn = connFactory.getReadWriteConnection();
    }

    public void tearDown() throws Exception {
        conn.freeConnection();
        connFactory.shutdown();
    }
}
