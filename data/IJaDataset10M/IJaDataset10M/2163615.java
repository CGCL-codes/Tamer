package uk.org.ogsadai.activity.sql;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.LiteralBlockReader;
import uk.org.ogsadai.activity.io.StringBlockWriter;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleLiteral;
import uk.org.ogsadai.authorization.NullSecurityContext;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.SimpleKeyValueProperties;
import uk.org.ogsadai.database.jdbc.JDBCConnection;
import uk.org.ogsadai.database.jdbc.book.JDBCBookDataCreator;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResource;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.test.jdbc.TestJDBCConnectionProperties;
import uk.org.ogsadai.test.jdbc.TestJDBCDataResourceState;

/**
 * Tests {@link
 * uk.org.ogsadai.activity.sql.ExtractPhysicalSchemaToXMLActivity}}. 
 * This class tests the proper operation of the activity but it does
 * not validate the results. Results are tested in test cases specific
 * to implementations of {@link
 * uk.org.ogsadai.physicalschema.DatabasePhysicalSchemaProvider}.
 * 
 * This class exopects test properties to be provided in a file whose 
 * location is specified in a system property,
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
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class ExtractPhysicalSchemaToXMLActivityTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** Test table name. */
    private static final String TABLE = "EPSTXMLATestTable";

    /** Test properties. */
    private final TestProperties mTestProperties;

    /** JDBC test data. */
    private JDBCBookDataCreator mDataCreator;

    /** Connection to database. */
    private JDBCConnection mConnection;

    /** Activity being tested. */
    private Activity mActivity;

    /** Target data resource. */
    private JDBCDataResource mResource;

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public ExtractPhysicalSchemaToXMLActivityTest(final String name) throws Exception {
        super(name);
        mTestProperties = new TestProperties();
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *     Not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ExtractPhysicalSchemaToXMLActivityTest.class);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestJDBCConnectionProperties connectionProperties = new TestJDBCConnectionProperties(mTestProperties);
        mConnection = new JDBCConnection(connectionProperties);
        mConnection.openConnection();
        mDataCreator = new JDBCBookDataCreator();
        mDataCreator.create(mConnection.getConnection(), TABLE);
        mDataCreator.populate(mConnection.getConnection(), TABLE, 10);
        TestJDBCDataResourceState state = new TestJDBCDataResourceState(connectionProperties);
        mResource = new JDBCDataResource();
        mResource.initialize(state);
        mActivity = new ExtractPhysicalSchemaToXMLActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("ExtractPhysicalSchemaToXMLActivityTest"));
        ResourceActivity ra = (ResourceActivity) mActivity;
        ra.setTargetResourceAccessor(mResource.createResourceAccessor(new NullSecurityContext()));
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        mDataCreator.drop(mConnection.getConnection(), TABLE);
        mConnection.closeConnection();
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testProcessActivity() throws Exception {
        ConfigurableActivity ca = (ConfigurableActivity) mActivity;
        KeyValueProperties props = new SimpleKeyValueProperties();
        props.put(ExtractPhysicalSchemaToXMLActivity.PHYSICAL_SCHEMA_PROVIDER_CLASS, TestPhysicalSchemaProvider.class.getName());
        ca.configureActivity(props);
        StringBlockWriter output = new StringBlockWriter();
        mActivity.addInput(ExtractPhysicalSchemaToXMLActivity.NAME_INPUT, new LiteralBlockReader(new SimpleLiteral(TABLE)));
        mActivity.addOutput(ExtractPhysicalSchemaToXMLActivity.RESULT_OUTPUT, output);
        mActivity.process();
        String actual = output.getOutputData();
        String actualConn = null;
        String actualPattern = null;
        Pattern p = Pattern.compile(".*<conn>(.*)</conn>.*<pattern>(.*)</pattern>.*");
        Matcher m = p.matcher(actual);
        if (m.matches()) {
            actualConn = m.group(1);
            actualPattern = m.group(2);
        }
        assertTrue("Connection", !"null".equals(actualConn.toString()));
        assertEquals("Table name pattern", TABLE, actualPattern);
    }

    /**
     * Tests if the proper exception is being thrown when non existing
     * class is specified as a physical schema provider.
     *
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testClassNotFoundException() throws Exception {
        ConfigurableActivity ca = (ConfigurableActivity) mActivity;
        KeyValueProperties props = new SimpleKeyValueProperties();
        props.put(ExtractPhysicalSchemaToXMLActivity.PHYSICAL_SCHEMA_PROVIDER_CLASS, "some.non.existing.class");
        ca.configureActivity(props);
        StringBlockWriter output = new StringBlockWriter();
        mActivity.addInput(ExtractPhysicalSchemaToXMLActivity.NAME_INPUT, new LiteralBlockReader(new SimpleLiteral(TABLE)));
        mActivity.addOutput(ExtractPhysicalSchemaToXMLActivity.RESULT_OUTPUT, output);
        try {
            mActivity.process();
            fail("ActivityProcessingException expected");
        } catch (ActivityProcessingException e) {
        } catch (Throwable e) {
            fail("ActivityProcessingException expected");
        }
    }
}
