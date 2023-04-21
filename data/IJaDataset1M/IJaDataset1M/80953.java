package uk.org.ogsadai.activity.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.authorization.NullSecurityContext;
import uk.org.ogsadai.database.jdbc.JDBCConnection;
import uk.org.ogsadai.database.jdbc.book.JDBCBookDataCreator;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResource;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.test.jdbc.TestJDBCConnectionProperties;
import uk.org.ogsadai.test.jdbc.TestJDBCDataResourceState;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * <code>SQLBulkLoadTupleActivity</code> test class. This class expects  
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
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class SQLBulkLoadTupleActivityTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Test table name. */
    private static final String TABLE = "SQLBLTATestTable";

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
    public SQLBulkLoadTupleActivityTest(final String name) throws Exception {
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
        junit.textui.TestRunner.run(SQLBulkLoadTupleActivityTest.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        mActivity = new SQLBulkLoadTupleActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("SQLBulkLoadTupleActivityTest"));
        ResourceActivity ra = (ResourceActivity) mActivity;
        ra.setTargetResourceAccessor(mResource.createResourceAccessor(new NullSecurityContext()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            mDataCreator.drop(mConnection.getConnection(), TABLE);
        } finally {
            mConnection.closeConnection();
        }
    }

    /**
     * Tests the main functionality by inserting two tuples.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testUpdateDatabaseWithParameters() throws Exception {
        int id = 45768;
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(id));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Integer[] { new Integer(1) });
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        mActivity.process();
        ResultSet results = mConnection.executeQuery("SELECT * FROM " + TABLE + " WHERE id=" + id);
        assertTrue(results.next());
        output.verify();
        assertEquals(id, results.getInt(1));
        assertEquals(list1.get(1), results.getString(2));
        assertNull(results.getObject(3));
        assertNull(results.getObject(4));
        assertTrue("No more rows expected", !results.next());
        results.close();
    }

    /**
     * Tests the case where the activity is processed with no output attached.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testMissingInputTableName() throws Exception {
        try {
            mActivity.process();
            fail("Expected an InvalidActivityInputsException to be raised.");
        } catch (InvalidActivityInputsException e) {
            assertEquals(1, e.getExpectedNumberOfInputs());
            assertEquals(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, e.getInputName());
        }
    }

    /**
     * Tests the case where the activity is processed with invalid table 
     * name. This will then lead to rollback.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testInvalidTableName() throws Exception {
        String tableName = "noSuchTable";
        MockInputPipe pipe = new MockInputPipe(new String[] { tableName });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(45762));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivitySQLUserException e) {
        }
        output.verify();
    }

    /**
     * Tests the case where the activity is processed with invalid last list. 
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testInvalidLastListFormat() throws Exception {
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        Object[] parameterTuples = new Object[3];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(50010));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an InvalidInputValueException to be raised.");
        } catch (InvalidInputValueException e) {
            assertEquals(SQLBulkLoadTupleActivity.INPUT_TUPLES, e.getParameters()[0]);
            output.verify();
        }
    }

    /**
     * Tests the case where the activity is processed with invalid list in
     * the case where a new list starts without the previous having terminated. 
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testInvalidNewListWithoutPreviousHavingTerminated() throws Exception {
        int id1 = 50010;
        int id2 = 50012;
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(id1));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        List<Object> list2 = new ArrayList<Object>();
        list2.add(new Integer(id2));
        list2.add("Jerry");
        list2.add(Null.getValue());
        list2.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_BEGIN;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof MalformedListBeginException);
            output.verify();
        }
    }

    /**
     * Tests the case where the table name data is of the wrong type.
     * 
     * @throws Exception
     *             if there is an unexpected problem.
     */
    public void testInvalidTableNameType() throws Exception {
        MockInputPipe pipe = new MockInputPipe(new Integer[] { new Integer(1) });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(45762));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an InvalidInputValueException to be raised.");
        } catch (InvalidInputValueException e) {
            assertEquals(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, e.getParameters()[0]);
            assertEquals(String.class.getName(), e.getParameters()[1]);
            assertEquals(Integer.class.getName(), e.getParameters()[2]);
            output.verify();
        }
    }

    /**
     * Tests the case where there's more data in the table name input than in
     * the tuples input.
     * 
     * @throws Exception
     *             if there is an unexpected problem.
     */
    public void testUnmatchedTableInput() throws Exception {
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE, TABLE });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(45762));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] { new Integer(1) });
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an UnmatchedActivityInputsException to be raised.");
        } catch (UnmatchedActivityInputsException e) {
            assertEquals(Collections.singletonList(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME), e.getParameters()[0]);
            assertEquals(Collections.singletonList(SQLBulkLoadTupleActivity.INPUT_TUPLES), e.getParameters()[1]);
            output.verify();
        }
    }

    /**
     * Tests an unmatched data input.
     * 
     * @throws Exception
     *             if there is an unexpected problem.
     */
    public void testUnmatchedTuplesInput() throws Exception {
        int id1 = 45768;
        int id2 = 45767;
        Object[] parameterTuples = new Object[8];
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(id1));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        List<Object> list2 = new ArrayList<Object>();
        list2.add(new Integer(id2));
        list2.add("Jerry");
        list2.add("South Bridge");
        list2.add("0000000000");
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        parameterTuples[4] = ControlBlock.LIST_BEGIN;
        parameterTuples[5] = metadata;
        parameterTuples[6] = new SimpleTuple(list2);
        parameterTuples[7] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Object[] { new Integer(1) });
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        try {
            mActivity.process();
            fail("Expected an UnmatchedActivityInputsException to be raised.");
        } catch (UnmatchedActivityInputsException e) {
            assertEquals(Collections.singletonList(SQLBulkLoadTupleActivity.INPUT_TUPLES), e.getParameters()[0]);
            assertEquals(Collections.singletonList(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME), e.getParameters()[1]);
            output.verify();
        }
    }

    /**
     * Tests the case where an activity is processed but the output pipe has
     * been closed by the consumer. This should cause the activity to close
     * early without raising an error.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivityWithEarlyClosure() throws Exception {
        int id = 45768;
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(id));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        MockOutputPipe output = new MockOutputPipe(new Integer[] {});
        output.closeForReading();
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        mActivity.addOutput(SQLBulkLoadTupleActivity.OUTPUT_RESULT, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests the case of no output provided
     * 
     * @throws Exception
     */
    public void testNoOutput() throws Exception {
        int id = 45768;
        MockInputPipe pipe = new MockInputPipe(new String[] { TABLE });
        Object[] parameterTuples = new Object[4];
        List<Object> list1 = new ArrayList<Object>();
        list1.add(new Integer(id));
        list1.add("Bugs Bunny");
        list1.add(Null.getValue());
        list1.add(Null.getValue());
        MetadataWrapper metadata = createMetadata();
        parameterTuples[0] = ControlBlock.LIST_BEGIN;
        parameterTuples[1] = metadata;
        parameterTuples[2] = new SimpleTuple(list1);
        parameterTuples[3] = ControlBlock.LIST_END;
        MockInputPipe parameters = new MockInputPipe(parameterTuples);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TABLE_NAME, pipe);
        mActivity.addInput(SQLBulkLoadTupleActivity.INPUT_TUPLES, parameters);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
        }
    }

    /**
     * Auxiliary method to create metadata
     * 
     * @return A new metadata block
     */
    private MetadataWrapper createMetadata() {
        List<Object> cdlist = new ArrayList<Object>();
        cdlist.add(new SimpleColumnMetadata("col1", TupleTypes._INT, 0, 0, 10));
        cdlist.add(new SimpleColumnMetadata("col2", TupleTypes._STRING, 0, 0, 10));
        cdlist.add(new SimpleColumnMetadata("col3", TupleTypes._STRING, 0, 0, 10));
        cdlist.add(new SimpleColumnMetadata("col4", TupleTypes._STRING, 0, 0, 10));
        SimpleTupleMetadata metadata = new SimpleTupleMetadata(cdlist);
        return new MetadataWrapper(metadata);
    }
}
