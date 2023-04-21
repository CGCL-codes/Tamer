package uk.org.ogsadai.test.server.activity.transform;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityStatus;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.transform.CSVToTuple;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToCSV;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleUnionAll;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;
import uk.org.ogsadai.resource.request.RequestStatus;
import uk.org.ogsadai.test.server.ServerTestProperties;
import uk.org.ogsadai.test.server.TestServerProxyFactory;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Server tests for TupleUnionAll activity. This class expects  
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
 *
 * @author The OGSA-DAI Project Team.
 */
public class TupleUnionAllTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008-2010.";

    /** Test properties. */
    private final ServerTestProperties mProperties;

    /** Basic (unsecure) server. */
    private Server mServer;

    /** DRER to test. */
    private DataRequestExecutionResource mDRER;

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public TupleUnionAllTest(String name) throws Exception {
        super(name);
        mProperties = new ServerTestProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void setUp() throws Exception {
        mServer = TestServerProxyFactory.getServerProxy(mProperties);
        mDRER = mServer.getDataRequestExecutionResource(mProperties.getDRERID());
    }

    /**
     * Run test for command line.
     * 
     * @param args
     *     Not used.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TupleUnionAllTest.class);
    }

    /**
     * Tests a simple union of three lists.
     * 
     * @throws Exception
     */
    public void testSimpleUnion() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 3;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        List<String> data = new ArrayList<String>();
        StringBuffer[] buf = new StringBuffer[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            buf[i] = new StringBuffer();
        }
        for (int i = 0; i < 60; i++) {
            data.add(String.valueOf(i));
            buf[i % numberOfInputs].append(i);
            buf[i % numberOfInputs].append("\n");
        }
        for (int i = 0; i < numberOfInputs; i++) {
            CSVToTuple csv = new CSVToTuple();
            csv.addOutputTypesAsArray(outputTypes);
            csv.addData(new StringReader(buf[i].toString()));
            pipeline.add(csv);
            union.connectDataInput(i, csv.getResultOutput());
        }
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        RequestResource requestResource = null;
        try {
            requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            genericErrorHandler(e);
            throw e;
        }
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        String line;
        while ((line = resultReader.readLine()) != null) {
            assertTrue("Unexpected data value: " + line, data.remove(line));
        }
        assertTrue("Missing data values: " + data, data.isEmpty());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests union of 100 tuple lists of the same size.
     * 
     * @throws Exception
     */
    public void testManyProducers() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 100;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        List<String> data = new ArrayList<String>();
        StringBuffer[] buf = new StringBuffer[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            buf[i] = new StringBuffer();
        }
        for (int i = 0; i < 1000; i++) {
            data.add(String.valueOf(i));
            buf[i % numberOfInputs].append(i);
            buf[i % numberOfInputs].append("\n");
        }
        for (int i = 0; i < numberOfInputs; i++) {
            CSVToTuple csv = new CSVToTuple();
            csv.addOutputTypesAsArray(outputTypes);
            csv.addData(new StringReader(buf[i].toString()));
            pipeline.add(csv);
            union.connectDataInput(i, csv.getResultOutput());
        }
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        RequestResource requestResource = null;
        try {
            requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            genericErrorHandler(e);
            throw e;
        }
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        String line;
        while ((line = resultReader.readLine()) != null) {
            assertTrue("Unexpected data value: " + line, data.remove(line));
        }
        assertTrue("Missing data values: " + data, data.isEmpty());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests union of a long list and a shorter list.
     * 
     * @throws Exception
     */
    public void testLongListUnionShortList() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 2;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        List<String> data = new ArrayList<String>();
        StringBuffer[] buf = new StringBuffer[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            buf[i] = new StringBuffer();
        }
        for (int i = 0; i < 100; i++) {
            data.add(String.valueOf(i));
            buf[0].append(i);
            buf[0].append("\n");
        }
        for (int i = 0; i < 10; i++) {
            data.add(String.valueOf(i));
            buf[1].append(i);
            buf[1].append("\n");
        }
        for (int i = 0; i < numberOfInputs; i++) {
            CSVToTuple csv = new CSVToTuple();
            csv.addOutputTypesAsArray(outputTypes);
            csv.addData(new StringReader(buf[i].toString()));
            pipeline.add(csv);
            union.connectDataInput(i, csv.getResultOutput());
        }
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        RequestResource requestResource = null;
        try {
            requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            genericErrorHandler(e);
            throw e;
        }
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        String line;
        while ((line = resultReader.readLine()) != null) {
            assertTrue("Unexpected data value: " + line, data.remove(line));
        }
        assertTrue("Missing data values: " + data, data.isEmpty());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests a union of an empty list and a non-empty list.
     * 
     * @throws Exception
     */
    public void testEmptyListUnionNonEmptyList() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 2;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        List<String> data = new ArrayList<String>();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 20; i++) {
            data.add(String.valueOf(i));
            buf.append(i);
            buf.append("\n");
        }
        CSVToTuple csv1 = new CSVToTuple();
        csv1.addOutputTypesAsArray(outputTypes);
        csv1.addData(new StringReader(buf.toString()));
        pipeline.add(csv1);
        union.connectDataInput(0, csv1.getResultOutput());
        CSVToTuple csv2 = new CSVToTuple();
        csv2.addOutputTypesAsArray(outputTypes);
        csv2.addData(new StringReader(""));
        pipeline.add(csv2);
        union.connectDataInput(1, csv2.getResultOutput());
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        RequestResource requestResource = null;
        try {
            requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            genericErrorHandler(e);
            throw e;
        }
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        String line;
        while ((line = resultReader.readLine()) != null) {
            assertTrue("Unexpected data value: " + line, data.remove(line));
        }
        assertTrue("Missing data values: " + data, data.isEmpty());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests a union of two empty lists.
     * 
     * @throws Exception
     */
    public void testUnionEmptyLists() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 2;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        CSVToTuple csv1 = new CSVToTuple();
        csv1.addOutputTypesAsArray(outputTypes);
        csv1.addData(new StringReader(""));
        pipeline.add(csv1);
        union.connectDataInput(0, csv1.getResultOutput());
        CSVToTuple csv2 = new CSVToTuple();
        csv2.addOutputTypesAsArray(outputTypes);
        csv2.addData(new StringReader(""));
        pipeline.add(csv2);
        union.connectDataInput(1, csv2.getResultOutput());
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        RequestResource requestResource = null;
        try {
            requestResource = mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        } catch (RequestExecutionException e) {
            genericErrorHandler(e);
            throw e;
        }
        RequestStatus status = requestResource.getRequestStatus();
        assertEquals("Unexpected error when processing request,", RequestExecutionStatus.COMPLETED, status.getExecutionStatus());
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        assertNull(resultReader.readLine());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests two lists with different column types that cannot be resolved
     * into a common type.
     * 
     * @throws Exception
     *     If any problems arise.
     */
    public void testIrresolvableColumnTypes() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 2;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes1 = new int[] { TupleTypes._INT };
        CSVToTuple csv1 = new CSVToTuple();
        csv1.addOutputTypesAsArray(outputTypes1);
        csv1.addData(new StringReader(""));
        pipeline.add(csv1);
        union.connectDataInput(0, csv1.getResultOutput());
        int[] outputTypes2 = new int[] { TupleTypes._ODBLOB };
        CSVToTuple csv2 = new CSVToTuple();
        csv2.addOutputTypesAsArray(outputTypes2);
        csv2.addData(new StringReader(""));
        pipeline.add(csv2);
        union.connectDataInput(1, csv2.getResultOutput());
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        try {
            mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
            fail("Expected RequestExecutionException");
        } catch (RequestExecutionException e) {
        }
    }

    /**
     * Tests two lists with different column types that can be resolved
     * into a common type.
     * 
     * @throws Exception
     *     If any problems arise.
     */
    public void testResolvableColumnTypes() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 2;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes1 = new int[] { TupleTypes._INT };
        CSVToTuple csv1 = new CSVToTuple();
        csv1.addOutputTypesAsArray(outputTypes1);
        csv1.addData(new StringReader(""));
        pipeline.add(csv1);
        union.connectDataInput(0, csv1.getResultOutput());
        int[] outputTypes2 = new int[] { TupleTypes._STRING };
        CSVToTuple csv2 = new CSVToTuple();
        csv2.addOutputTypesAsArray(outputTypes2);
        csv2.addData(new StringReader(""));
        pipeline.add(csv2);
        union.connectDataInput(1, csv2.getResultOutput());
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
        assertTrue(toCSV.hasNextResult());
        BufferedReader resultReader = new BufferedReader(toCSV.nextResult());
        assertNull(resultReader.readLine());
        assertTrue(!toCSV.hasNextResult());
    }

    /**
     * Tests providing only one producer which should raise an error.
     * 
     * @throws Exception
     */
    public void testOneProducer() throws Exception {
        PipelineWorkflow pipeline = new PipelineWorkflow();
        int numberOfInputs = 1;
        TupleUnionAll union = new TupleUnionAll();
        union.setNumberOfInputs(numberOfInputs);
        pipeline.add(union);
        int[] outputTypes = new int[] { TupleTypes._INT };
        CSVToTuple csv1 = new CSVToTuple();
        csv1.addOutputTypesAsArray(outputTypes);
        csv1.addData(new StringReader(""));
        pipeline.add(csv1);
        union.connectDataInput(0, csv1.getResultOutput());
        TupleToCSV toCSV = new TupleToCSV();
        toCSV.connectDataInput(union.getDataOutput());
        pipeline.add(toCSV);
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(toCSV.getResultOutput());
        pipeline.add(deliver);
        try {
            mDRER.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
            fail("Incompatible lists exception expected.");
        } catch (RequestExecutionException e) {
            assertTrue(union.hasErrorMessages());
            assertEquals(ErrorID.ACTIVITY_HAS_WRONG_NUMBER_OF_INPUTS, e.getRequestResource().getLocalRequestStatus().getActivityProcessingStatus(union.getInstanceName()).getError().getErrorID());
        }
    }

    /**
     * Prints the request status which may contain clues as to what went wrong.
     * 
     * @param cause
     *            execution exception
     */
    private void genericErrorHandler(RequestExecutionException cause) {
        System.out.println("A problem has occured...");
        System.out.println(cause.getMessage());
        System.out.println("Request status:");
        System.out.println(cause.getRequestResource().getLocalRequestStatus());
    }
}
