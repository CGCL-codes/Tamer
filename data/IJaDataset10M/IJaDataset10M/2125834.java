package uk.org.ogsadai.test.server.activity.xmldb;

import java.io.CharArrayReader;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.xmldb.api.base.Resource;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.delivery.ReadFromDataSink;
import uk.org.ogsadai.client.toolkit.activities.xmldb.AddDocuments;
import uk.org.ogsadai.client.toolkit.activities.xmldb.GetDocuments;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.client.toolkit.messages.Message;
import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;
import uk.org.ogsadai.data.CharData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.database.xmldb.XMLDBCollection;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;
import uk.org.ogsadai.resource.request.RequestStatus;
import uk.org.ogsadai.test.server.ServerTestProperties;
import uk.org.ogsadai.test.server.TestServerProxyFactory;
import uk.org.ogsadai.test.server.activity.xmldb.XMLDBServerTestConstants;
import uk.org.ogsadai.test.xmldb.TestXMLDBCollectionProperties;

/**
 * Server tests for the AddDocuments activity. This class expects  
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
 * Additional properties may be required depending on the server type.
 * </li>
 * </ul>
 * </li>
 * <li>
 * Test-specific properties.
 * <ul>
 * <li>
 * <code>server.xmldb.resource.id</code> - ID of OGSA-DAI data
 * resource on server exposing an XMLDB database. 
 * </li>
 * <li>
 * <code>xmldb.collection.uri</code> - URL of collection of the
 * XMLDB database exposed by the above resource.
 * </li>
 * <li>
 * <code>xmldb.driver.class</code> - XMLDB driver class name.
 * </li>
 * <li>
 * <code>xmldb.user.name</code> - user name for above URL.
 * </li>
 * <li>
 * <code>xmldb.password</code> - password for above user name.
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class AddDocumentsTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Test document name prefix. */
    private String DOC_NAME_PREFIX = "AddDocumentsTest-";

    /** Test document 1. */
    private String DOCUMENT1 = "<addDocumentsTest id=\"1\">\n" + "    <name>Debbie</name>\n" + "    <phone>123</phone>\n" + "</addDocumentsTest>";

    /** Test document 2. */
    private String DOCUMENT2 = "<addDocumentsTest id=\"2\">\n" + "    <name>Vicki</name>\n" + "    <phone>123</phone>\n" + "</addDocumentsTest>";

    /** Test document 3. */
    private String DOCUMENT3 = "<addDocumentsTest id=\"1\">\n" + "    <name>Maria</name>\n" + "    <phone>123</phone>\n" + "</addDocumentsTest>";

    /** Test data root collection name. */
    private static final String COLLECTION_NAME = "AddDocumentsTestCollection";

    /** Test properties. */
    private final ServerTestProperties mProperties;

    /** Basic (unsecure) server. */
    private Server mServer;

    /** DRER to test. */
    private DataRequestExecutionResource mDRER;

    /** Connection to database. */
    private XMLDBCollection mCollection;

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
    public AddDocumentsTest(String name) throws Exception {
        super(name);
        mProperties = new ServerTestProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void setUp() throws Exception {
        mResourceID = new ResourceID(mProperties.getProperty(XMLDBServerTestConstants.ID));
        TestXMLDBCollectionProperties collectionProperties = new TestXMLDBCollectionProperties(mProperties);
        mCollection = new XMLDBCollection(collectionProperties);
        mCollection.openCollection();
        mCollection.createSubcollection(null, COLLECTION_NAME);
        mServer = TestServerProxyFactory.getServerProxy(mProperties);
        mDRER = mServer.getDataRequestExecutionResource(mProperties.getDRERID());
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        try {
            mCollection.removeCollection(null, COLLECTION_NAME);
        } catch (Exception e) {
        }
        try {
            mCollection.removeResource(DOC_NAME_PREFIX + 1, null);
        } catch (Exception e) {
        }
        mCollection.closeCollection();
    }

    /**
     * Run test for command line.
     * 
     * @param args
     *     Not used.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AddDocumentsTest.class);
    }

    /**
     * Test to check that <code>addResourceId</code> and
     * <code>addData</code> work correctly.
     * 
     * @throws Exception if any problems arise.
     */
    public void testAddResourceIdAddData() throws Exception {
        AddDocuments addDocs = new AddDocuments();
        addDocs.addResourceId(new String(DOC_NAME_PREFIX + 1));
        addDocs.setResourceID(mResourceID);
        addDocs.addData(new CharArrayReader(DOCUMENT1.toCharArray()));
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 1, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 1, null);
            assertEquals("Document " + DOC_NAME_PREFIX + 1, DOCUMENT1, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test to check that <code>connectResourceIDInput</code> and
     * <code>addData</code> work correctly.
     *
     * @throws Exception if any problems arise.
     */
    public void testConnectResourceIDAddData() throws Exception {
        DataSinkResource dsr = ResourceFactory.createDataSink(mServer, mDRER);
        dsr.putValue(new StringData(new String(DOC_NAME_PREFIX + 1)));
        dsr.close();
        ReadFromDataSink rfds = new ReadFromDataSink();
        rfds.setResourceID(dsr.getResourceID());
        AddDocuments addDocs = new AddDocuments();
        addDocs.setResourceID(mResourceID);
        addDocs.connectResourceIdInput(rfds.getOutput());
        addDocs.addData(new CharArrayReader(DOCUMENT1.toCharArray()));
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rfds);
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 1, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 1, null);
            assertEquals("Document " + DOC_NAME_PREFIX + 1, DOCUMENT1, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test to check that <code>addResourceId</code> and
     * <code>connectData</code> work correctly. 
     * 
     * @throws Exception if any problems arise.
     */
    public void testAddResourceIdConnectData() throws Exception {
        DataSinkResource dsr = ResourceFactory.createDataSink(mServer, mDRER);
        dsr.putValue(ListBegin.VALUE);
        dsr.putValue(new CharData(DOCUMENT1.toCharArray()));
        dsr.putValue(ListEnd.VALUE);
        dsr.close();
        ReadFromDataSink rfds = new ReadFromDataSink();
        rfds.setResourceID(dsr.getResourceID());
        AddDocuments addDocs = new AddDocuments();
        addDocs.setResourceID(mResourceID);
        addDocs.connectDataInput(rfds.getOutput());
        addDocs.addResourceId(new String(DOC_NAME_PREFIX + 1));
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rfds);
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 1, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 1, null);
            assertEquals("Document " + DOC_NAME_PREFIX + 1, DOCUMENT1, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test to check that <code>connectResourceId</code> and 
     * <code>connectData</code> work correctly.
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectResourceIdConnectData() throws Exception {
        DataSinkResource dsr1 = ResourceFactory.createDataSink(mServer, mDRER);
        dsr1.putValue(new StringData(new String(DOC_NAME_PREFIX + 1)));
        dsr1.close();
        ReadFromDataSink rfds1 = new ReadFromDataSink();
        rfds1.setResourceID(dsr1.getResourceID());
        DataSinkResource dsr2 = ResourceFactory.createDataSink(mServer, mDRER);
        dsr2.putValue(ListBegin.VALUE);
        dsr2.putValue(new CharData(DOCUMENT1.toCharArray()));
        dsr2.putValue(ListEnd.VALUE);
        dsr2.close();
        ReadFromDataSink rfds2 = new ReadFromDataSink();
        rfds2.setResourceID(dsr2.getResourceID());
        AddDocuments addDocs = new AddDocuments();
        addDocs.setResourceID(mResourceID);
        addDocs.connectResourceIdInput(rfds1.getOutput());
        addDocs.connectDataInput(rfds2.getOutput());
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rfds1);
        pipeline.add(rfds2);
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 1, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 1, null);
            assertEquals("Document " + DOC_NAME_PREFIX + 1, DOCUMENT1, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test to check that <code>connectionCollectionInput</code> works
     * correctly for a valid subcollection.
     * 
     * @throws Exception if any problems arise.
     */
    public void testConnectCollection() throws Exception {
        DataSinkResource dsr = ResourceFactory.createDataSink(mServer, mDRER);
        dsr.putValue(new StringData(COLLECTION_NAME));
        dsr.close();
        ReadFromDataSink rfds = new ReadFromDataSink();
        rfds.setResourceID(dsr.getResourceID());
        AddDocuments addDocs = new AddDocuments();
        addDocs.setResourceID(mResourceID);
        addDocs.addResourceId(new String(DOC_NAME_PREFIX + 2));
        addDocs.addData(new CharArrayReader(DOCUMENT2.toCharArray()));
        addDocs.connectCollectionInput(rfds.getOutput());
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rfds);
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 2, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 2, COLLECTION_NAME);
            assertEquals("Document " + DOC_NAME_PREFIX + 2, DOCUMENT2, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test to check that <code>addCollectionInput</code> works
     * correctly for a valid subcollection.
     * 
     * @throws Exception if any problems arise.
     */
    public void testAddCollection() throws Exception {
        AddDocuments addDocs = new AddDocuments();
        addDocs.setResourceID(mResourceID);
        addDocs.addResourceId(new String(DOC_NAME_PREFIX + 2));
        addDocs.addCollection(COLLECTION_NAME);
        addDocs.addData(new CharArrayReader(DOCUMENT2.toCharArray()));
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 2, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 2, COLLECTION_NAME);
            assertEquals("Document " + DOC_NAME_PREFIX + 2, DOCUMENT2, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test correct errors are generated for invalid subcollection.
     * 
     * @throws Exception if any problems arise.
     */
    public void testInvalidCollection() throws Exception {
        GetDocuments getDocs = new GetDocuments();
        getDocs.addCollection("blkjldkzjslszjkd");
        getDocs.addResourceId(new String(DOC_NAME_PREFIX + 2));
        getDocs.setResourceID(mResourceID);
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(getDocs.getResultOutput());
        DeliverToRequestStatus deliverTorequestStatus2 = new DeliverToRequestStatus();
        deliverTorequestStatus2.connectInput(getDocs.getDataOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(getDocs);
        pipeline.add(deliverTorequestStatus1);
        pipeline.add(deliverTorequestStatus2);
        try {
            mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
            fail("Expected RequestExecutionException");
        } catch (RequestExecutionException e) {
            Message[] errors = getDocs.getErrorMessages();
            assertTrue("should have two errors", errors.length == 2);
            assertEquals("should have XMLDB unknown subcollection id", ErrorID.XMLDB_UNKNOWN_SUBCOLLECTION_ERROR, errors[0].getID());
            assertEquals("should have XMLDB unknown subcollection id", ErrorID.XMLDB_UNKNOWN_SUBCOLLECTION_ERROR, errors[1].getID());
        }
    }

    /**
     * Test existing documents can be overwritten.
     * 
     * @throws Exception if any problems arise.
     */
    public void testOverwiteExistingDoc() throws Exception {
        mCollection.addDocument(DOC_NAME_PREFIX + 1, null, DOCUMENT1);
        AddDocuments addDocs = new AddDocuments();
        addDocs.addResourceId(new String(DOC_NAME_PREFIX + 1));
        addDocs.setResourceID(mResourceID);
        addDocs.addData(new CharArrayReader(DOCUMENT3.toCharArray()));
        DeliverToRequestStatus deliverTorequestStatus1 = new DeliverToRequestStatus();
        deliverTorequestStatus1.connectInput(addDocs.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(addDocs);
        pipeline.add(deliverTorequestStatus1);
        RequestResource requestResource = null;
        requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        while (addDocs.hasNextResult()) {
            String expResId = addDocs.getNextResult();
            assertEquals("Document ID", DOC_NAME_PREFIX + 1, expResId);
            Resource res = mCollection.getResource(DOC_NAME_PREFIX + 1, null);
            assertEquals("Document " + DOC_NAME_PREFIX + 1, DOCUMENT3, res.getContent().toString());
        }
        assertTrue("No more documents", !addDocs.hasNextResult());
        try {
            addDocs.getNextResult();
            fail("Expected no more data");
        } catch (NoSuchElementException e) {
        }
    }
}
