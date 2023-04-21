package test.org.mbari.jdo.castor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

public class CastorJDBCConnectionTest extends TestCase {

    private JDOManager jdoManager;

    /**
     * Constructor for JdoDatabaseFactoryTest.
     * @param arg0
     */
    public CastorJDBCConnectionTest(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        return new TestSuite(CastorJDBCConnectionTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public void testJDBCConnection() {
        for (int i = 0; i < 50; i++) {
            String server = null;
            try {
                Database database = fetchDatabase();
                database.begin();
                Connection connection = database.getJdbcConnection();
                DatabaseMetaData metadata = connection.getMetaData();
                server = metadata.getURL();
                database.commit();
                database.close();
            } catch (Exception e) {
                fail("JDBC usage caused an error: " + e.getClass() + "->" + e.getMessage());
            }
            assertNotNull("Failed to get db url", server);
        }
    }

    private Database fetchDatabase() throws MappingException, PersistenceException {
        if (jdoManager == null) {
            JDOManager.loadConfiguration("build/dist/conf/vars_database.INTERNAL.xml");
            jdoManager = JDOManager.createInstance("vars");
        }
        return jdoManager.getDatabase();
    }
}
