package uk.org.ogsadai.physicalschema;

import junit.framework.Test;
import junit.framework.TestSuite;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.test.TestPropertyNotFoundException;

/**
 * Test suite for {@link
 * uk.org.ogsadai.physicalschema.DatabasePhysicalSchemaProvider}
 * database-specific implementations.
 * <code>SQLResilientActivity</code> test class. This class expects  
 * test properties to be provided in a file whose location is
 * specified in a system property,
 * <code>ogsadai.test.properties</code>. The following properties need
 * to be provided:
 * <ul>
 * <li>
 * <code>jdbc.connection.url</code> - URL of the relational database
 * exposed by the above resource. 
 * </li>
 * <li>
 * <code>jdbc.driver.class</code> - JDBC driver class name.
 * </li>
 * <li>
 * <code>jdbc.user.name</code> - user name for above URL.
 * </li>
 * <li>
 * <code>jdbc.password</code> - password for above user name.
 * </li>
 * <li>
 * <code>jdbc.physicalschemaprovider.test.case</code> - name of
 * test case class that contains the tests ro be run.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class PhysicalSchemaProviderTest extends TestSuite {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** Test case class property name. */
    private static final String TEST_CASE = "jdbc.physicalschemaprovider.test.case";

    /**
     * {@inheritDoc}
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public static Test suite() throws Exception {
        TestProperties properties = new TestProperties();
        TestSuite suite = null;
        try {
            String testCase = properties.getProperty(TEST_CASE);
            Class testClass = Class.forName(testCase);
            suite = new TestSuite(testClass);
        } catch (TestPropertyNotFoundException e) {
            suite = new TestSuite();
        }
        return suite;
    }

    /**
     * Runs the test suite.
     * 
     * @param args
     *    Unused.
     */
    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
    }
}
