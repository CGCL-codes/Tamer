package uk.org.ogsadai.test.server.activity.transform;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import junit.framework.TestCase;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.DataValueIterator;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.delivery.ReadFromDataSink;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToCSV;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.client.toolkit.messages.Message;
import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;
import uk.org.ogsadai.converters.csv.CSVConfigurator;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.database.jdbc.JDBCConnection;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;
import uk.org.ogsadai.test.jdbc.TestJDBCConnectionProperties;
import uk.org.ogsadai.test.server.ServerTestProperties;
import uk.org.ogsadai.test.server.TestServerProxyFactory;
import uk.org.ogsadai.test.server.activity.sql.JDBCServerTestConstants;

/**
 * Server tests for the TupleToCSV activity. This class expects  
 * test properties to be provided in a file whose location is
 * specified in a system property,
 * <code>ogsadai.test.properties</code>. The following properties need
 * to be provided:
 * <ul>
 * <li>General server test properties:
 * <ul>
 * <li>
 * <code>server.url</code> - server URL (depends on server type).
 * </li>
 * <li>
 * <code>server.proxy.factory</code> - name of class used to create
 * client toolkit proxty server (depends on server type).
 * </li>
 * <li>
 * <code>server.version</code> - server version ID (depends on server type). 
 * </li>
 * <li>
 * <code>server.drer.id</code> - DRER ID on test server.
 * </li>
 * <li>
 * <code>line.break</code> - line break sequence on server.
 * </li>
 * <li>
 * Additional properties may be required depending on the server type.
 * </li>
 * </ul>
 * </li>
 * <li>
 * Test-specific properties.
 * <ul>
 * <li>
 * <code>server.jdbc.resource.id</code> - ID of OGSA-DAI data
 * resource on server exposing a relational database. 
 * </li>
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
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class TupleToCSVTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Test table name. */
    private static final String TABLE = "TupleToCSVTestTable";

    /** Test string values. */
    private static final String[] TEST_ARRAY = { "TestAName", "44 Calder Street", "03403829" };

    /** Test integer value. */
    private static final int TEST_INT = 23;

    /** Test properties. */
    private final ServerTestProperties mProperties;

    /** Basic (unsecure) server. */
    private Server mServer;

    /** DRER to test. */
    private DataRequestExecutionResource mDRER;

    /** Connection to database. */
    private JDBCConnection mConnection;

    /** Resource ID. */
    private ResourceID mResourceID;

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public TupleToCSVTest(final String name) throws Exception {
        super(name);
        mProperties = new ServerTestProperties();
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *     Not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TupleToCSVTest.class);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        mResourceID = new ResourceID(mProperties.getProperty(JDBCServerTestConstants.ID));
        TestJDBCConnectionProperties connectionProperties = new TestJDBCConnectionProperties(mProperties);
        mConnection = new JDBCConnection(connectionProperties);
        mConnection.openConnection();
        Statement stmt = mConnection.getConnection().createStatement();
        PreparedStatement pStmt = mConnection.getConnection().prepareStatement("INSERT INTO " + TABLE + " VALUES (?,?,?,?);");
        String sCreateTableSQL = "CREATE TABLE  " + TABLE + "(id INTEGER, name VARCHAR(64), address VARCHAR(128), " + "phone VARCHAR(20));";
        stmt.executeUpdate(sCreateTableSQL);
        pStmt.setInt(1, TEST_INT);
        for (int i = 0; i < TEST_ARRAY.length; i++) {
            pStmt.setString(i + 2, TEST_ARRAY[i]);
        }
        pStmt.executeUpdate();
        mServer = TestServerProxyFactory.getServerProxy(mProperties);
        mDRER = mServer.getDataRequestExecutionResource(mProperties.getDRERID());
        stmt.close();
        pStmt.close();
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        try {
            mConnection.executeUpdate("drop table " + TABLE);
        } catch (SQLException e) {
        }
        mConnection.closeConnection();
    }

    /**
     * Test normal behaviour for an input/output using the default CSV
     * settings. 
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectDataInputDefaults() throws Exception {
        CSVConfigurator csvConf = new CSVConfigurator();
        csvConf.setMLineBreak(mProperties.getLineBreak());
        StringBuffer testExpectedBuff = new StringBuffer();
        testExpectedBuff.append(TEST_INT);
        for (int i = 0; i < TEST_ARRAY.length; i++) {
            testExpectedBuff.append(csvConf.getMDelimiter());
            testExpectedBuff.append(TEST_ARRAY[i]);
        }
        testExpectedBuff.append(csvConf.getMLineBreakChars());
        String testExpected = testExpectedBuff.toString();
        String strSQL = "Select * from " + TABLE + ";";
        SQLQuery sQuery = new SQLQuery();
        sQuery.setResourceID(mResourceID);
        sQuery.addExpression(strSQL);
        TupleToCSV ttCSV = new TupleToCSV();
        ttCSV.connectDataInput(sQuery.getDataOutput());
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(ttCSV.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(sQuery);
        pipeline.add(ttCSV);
        pipeline.add(deliverToRequestStatus);
        RequestResource requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        DataValueIterator dvi = deliverToRequestStatus.getDataValueIterator();
        assertTrue("Should have list Start", dvi.next() instanceof ListBegin);
        char[] receivedResult = dvi.nextAsCharArray();
        for (int i = 0; i < receivedResult.length; i++) {
            assertEquals("Character " + i, (int) testExpected.charAt(i), (int) receivedResult[i]);
        }
        assertTrue("Should have list Start", dvi.next() instanceof ListEnd);
    }

    /**
     * Test behaviour using a different delimiter.
     * 
     * @throws Exception if any problems arise.
     */
    public void testAddDelimiter() throws Exception {
        String changedDelimiter = "@";
        CSVConfigurator csvConf = new CSVConfigurator();
        csvConf.setMLineBreak(mProperties.getLineBreak());
        csvConf.setMDelimiter(changedDelimiter.charAt(0));
        String testHeadersFromCreate = "\"id\"@\"name\"@\"address\"@\"phone\"" + csvConf.getMLineBreakChars();
        StringBuffer testExpectedBuff = new StringBuffer();
        testExpectedBuff.append(TEST_INT);
        for (int i = 0; i < TEST_ARRAY.length; i++) {
            testExpectedBuff.append(csvConf.getMDelimiter());
            testExpectedBuff.append("\"");
            testExpectedBuff.append(TEST_ARRAY[i]);
            testExpectedBuff.append("\"");
        }
        testExpectedBuff.append(csvConf.getMLineBreakChars());
        String testExpected = testExpectedBuff.toString();
        String strSQL = "Select * from " + TABLE + ";";
        SQLQuery sQuery = new SQLQuery();
        sQuery.setResourceID(mResourceID);
        sQuery.addExpression(strSQL);
        TupleToCSV ttCSV = new TupleToCSV();
        ttCSV.addDelimiter(changedDelimiter);
        ttCSV.addEscapeFields(true);
        ttCSV.addIncludeHeader(true);
        ttCSV.addNullDataString("NULL");
        ttCSV.connectDataInput(sQuery.getDataOutput());
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(ttCSV.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(sQuery);
        pipeline.add(ttCSV);
        pipeline.add(deliverToRequestStatus);
        RequestResource requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        DataValueIterator dvi = deliverToRequestStatus.getDataValueIterator();
        assertTrue("Should have list Start", dvi.next() instanceof ListBegin);
        char[] receivedResult = dvi.nextAsCharArray();
        for (int i = 0; i < receivedResult.length; i++) {
            assertEquals("Received result header character " + i, (int) testHeadersFromCreate.charAt(i), (int) receivedResult[i]);
        }
        receivedResult = dvi.nextAsCharArray();
        for (int i = 0; i < receivedResult.length; i++) {
            assertEquals("Received result character " + i, (int) testExpected.charAt(i), (int) receivedResult[i]);
        }
        assertTrue("Should have list Start", dvi.next() instanceof ListEnd);
    }

    /**
     * Test behaviour using a different delimiter provided via another
     * activity.
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectDelimiter() throws Exception {
        String changedDelimiter = "@";
        CSVConfigurator csvConf = new CSVConfigurator();
        csvConf.setMLineBreak(mProperties.getLineBreak());
        csvConf.setMDelimiter(changedDelimiter.charAt(0));
        String testHeadersFromCreate = "\"id\"@\"name\"@\"address\"@\"phone\"" + csvConf.getMLineBreakChars();
        StringBuffer testExpectedBuff = new StringBuffer();
        testExpectedBuff.append(TEST_INT);
        for (int i = 0; i < TEST_ARRAY.length; i++) {
            testExpectedBuff.append(csvConf.getMDelimiter());
            testExpectedBuff.append("\"");
            testExpectedBuff.append(TEST_ARRAY[i]);
            testExpectedBuff.append("\"");
        }
        testExpectedBuff.append(csvConf.getMLineBreakChars());
        String testExpected = testExpectedBuff.toString();
        DataSinkResource dsr = ResourceFactory.createDataSink(mServer, mDRER);
        dsr.putValue(new StringData(changedDelimiter));
        dsr.close();
        ReadFromDataSink rDSR = new ReadFromDataSink();
        rDSR.setResourceID(dsr.getResourceID());
        String strSQL = "Select * from " + TABLE + ";";
        SQLQuery sQuery = new SQLQuery();
        sQuery.setResourceID(mResourceID);
        sQuery.addExpression(strSQL);
        TupleToCSV ttCSV = new TupleToCSV();
        ttCSV.connectDelimiterInput(rDSR.getOutput());
        ttCSV.addEscapeFields(true);
        ttCSV.addIncludeHeader(true);
        ttCSV.addNullDataString("NULL");
        ttCSV.connectDataInput(sQuery.getDataOutput());
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(ttCSV.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rDSR);
        pipeline.add(sQuery);
        pipeline.add(ttCSV);
        pipeline.add(deliverToRequestStatus);
        RequestResource requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        DataValueIterator dvi = deliverToRequestStatus.getDataValueIterator();
        assertTrue("Should have list Start", dvi.next() instanceof ListBegin);
        char[] receivedResult = dvi.nextAsCharArray();
        for (int i = 0; i < receivedResult.length; i++) {
            assertEquals("Received result header character " + i, (int) testHeadersFromCreate.charAt(i), (int) receivedResult[i]);
        }
        receivedResult = dvi.nextAsCharArray();
        for (int i = 0; i < receivedResult.length; i++) {
            assertEquals("Received result character " + i, (int) testExpected.charAt(i), (int) receivedResult[i]);
        }
        assertTrue("Should have list Start", dvi.next() instanceof ListEnd);
    }

    /**
     * Check when data input streams in an error.
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectDataInputError() throws Exception {
        String strSQL = "Select * " + TABLE + ";";
        SQLQuery sQuery = new SQLQuery();
        sQuery.setResourceID(mResourceID);
        sQuery.addExpression(strSQL);
        TupleToCSV ttCSV = new TupleToCSV();
        ttCSV.addDelimiter("@");
        ttCSV.addEscapeFields(true);
        ttCSV.addLineBreak("LFCR");
        ttCSV.addIncludeHeader(true);
        ttCSV.addNullDataString("NULL");
        ttCSV.connectDataInput(sQuery.getDataOutput());
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(ttCSV.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(sQuery);
        pipeline.add(ttCSV);
        pipeline.add(deliverToRequestStatus);
        try {
            mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            Message[] errors = ttCSV.getErrorMessages();
            assertEquals("Number of errors", 1, errors.length);
            assertEquals("Error type", ErrorID.PIPE_CLOSED_DUE_TO_PRODUCER_ERROR, errors[0].getID());
        }
    }

    /**
     * Check when delimiter input streams in an error.
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectDelimiterInputError() throws Exception {
        DataSinkResource dsr = ResourceFactory.createDataSink(mServer, mDRER);
        dsr.closeDueToError();
        ReadFromDataSink rDSR = new ReadFromDataSink();
        rDSR.setResourceID(dsr.getResourceID());
        String strSQL = "Select * from " + TABLE + ";";
        SQLQuery sQuery = new SQLQuery();
        sQuery.setResourceID(mResourceID);
        sQuery.addExpression(strSQL);
        TupleToCSV ttCSV = new TupleToCSV();
        ttCSV.connectDelimiterInput(rDSR.getOutput());
        ttCSV.addEscapeFields(true);
        ttCSV.addLineBreak("LFCR");
        ttCSV.addIncludeHeader(true);
        ttCSV.addNullDataString("NULL");
        ttCSV.connectDataInput(sQuery.getDataOutput());
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(ttCSV.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rDSR);
        pipeline.add(sQuery);
        pipeline.add(ttCSV);
        pipeline.add(deliverToRequestStatus);
        try {
            mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            Message[] errors = ttCSV.getErrorMessages();
            assertEquals("Number of errors", 1, errors.length);
            assertEquals("Error type", ErrorID.PIPE_CLOSED_DUE_TO_PRODUCER_ERROR, errors[0].getID());
        }
    }
}
