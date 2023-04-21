package uk.org.ogsadai.activity.relational;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.Pipe;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.SimpleKeyValueProperties;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Test class for TupleSemiJoinActivity. Test data is obtain from the wikipedia 
 * entry on joins,
 * 
 * http://en.wikipedia.org/wiki/Relational_algebra#Semijoin_.28.E2.8B.89.29.28.E2.8B.8A.29
 * 
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class TupleSemiJoinActivityTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    List<Object> mLeftStream;

    List<Object> mRightStream;

    String mConditionString;

    SimpleKeyValueProperties mProperties;

    /**
     * Constructor.
     * 
     * @param name
     */
    public TupleSemiJoinActivityTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests a basic join.
     * 
     * @throws Exception
     */
    public void testJoin() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harry", new Integer(3415), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Sales", "Harriet" })));
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Production", "Charles" })));
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Tests a join with some null values on the left side.
     * 
     * @throws Exception
     */
    public void testJoinSomeNullOnLeft() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harry", new Integer(3415), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), Null.VALUE })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Sales", "Harriet" })));
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Production", "Charles" })));
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Test with empty table on the left.
     * 
     * @throws Exception
     */
    public void testJoinEmptyLeft() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Sales", "Harriet" })));
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Production", "Charles" })));
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Tests a join with some null values on the right side.
     * 
     * @throws Exception
     */
    public void testJoinSomeNullOnRight() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harry", new Integer(3415), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(new SimpleTuple(Arrays.asList(new Object[] { Null.VALUE, "Harriet" })));
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Production", "Charles" })));
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Tests a join with a disjunction condition.
     * 
     * @throws Exception
     */
    public void testJoinWithDisjunction() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harry", new Integer(3415), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Sales", "Harriet" })));
        right.add(new SimpleTuple(Arrays.asList(new Object[] { "Production", "Charles" })));
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname OR Employee.name='George'";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        expected.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Test with empty table on the right.
     * 
     * @throws Exception
     */
    public void testJoinEmptyRight() throws Exception {
        List<Object> left = new ArrayList<Object>();
        left.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(left);
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harry", new Integer(3415), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Sally", new Integer(2241), "Sales" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "George", new Integer(3401), "Finance" })));
        left.add(new SimpleTuple(Arrays.asList(new Object[] { "Harriet", new Integer(2202), "Production" })));
        left.add(ControlBlock.LIST_END);
        List<Object> right = new ArrayList<Object>();
        right.add(ControlBlock.LIST_BEGIN);
        addDeptMetaData(right);
        right.add(ControlBlock.LIST_END);
        String condition = "Employee.deptname = Dept.deptname";
        List<Object> expected = new ArrayList<Object>();
        expected.add(ControlBlock.LIST_BEGIN);
        addEmployeeMetaData(expected);
        expected.add(ControlBlock.LIST_END);
        MockInputPipe expectedOutput = new MockInputPipe(expected.toArray());
        MockInputPipe output = new MockInputPipe(runActivity(left, right, condition));
        verifyEquality(output, expectedOutput);
    }

    /**
     * Verifies the equality of two streams.
     * 
     * @param actualOutput
     * @param expectedOutput
     * @throws Exception
     */
    private void verifyEquality(Pipe actualOutput, Pipe expectedOutput) throws Exception {
        Object actual;
        Object expected;
        actual = actualOutput.read();
        expected = expectedOutput.read();
        assertEquals(expected, actual);
        actual = actualOutput.read();
        expected = expectedOutput.read();
        TupleMetadata actualMD = (TupleMetadata) ((MetadataWrapper) actual).getMetadata();
        TupleMetadata expectedMD = (TupleMetadata) ((MetadataWrapper) expected).getMetadata();
        assertEquals(expectedMD.getColumnCount(), actualMD.getColumnCount());
        for (int i = 0; i < expectedMD.getColumnCount(); i++) {
            ColumnMetadata actualCMD = actualMD.getColumnMetadata(i);
            ColumnMetadata expectedCMD = expectedMD.getColumnMetadata(i);
            assertEquals(expectedCMD.getName(), actualCMD.getName());
            assertEquals(expectedCMD.getType(), actualCMD.getType());
        }
        actual = actualOutput.read();
        expected = expectedOutput.read();
        while (!(expected instanceof ControlBlock)) {
            Tuple actualT = (Tuple) actual;
            Tuple expectedT = (Tuple) expected;
            for (int i = 0; i < expectedT.getColumnCount(); i++) {
                assertEquals(expectedT.getObject(i), actualT.getObject(i));
            }
            actual = actualOutput.read();
            expected = expectedOutput.read();
        }
        assertEquals(expected, actual);
    }

    /**
     * Runs the activity with the given inputs and returns the output stream.
     * 
     * @param left
     * @param right
     * @param condition
     * @return
     * @throws Exception
     */
    private MockOutputPipe runActivity(List<Object> left, List<Object> right, String condition) throws Exception {
        TupleSemiJoinActivity activity = prepareActivity(left, right, condition);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        activity.addOutput(TupleSemiJoinActivity.OUTPUT_RESULT, output);
        output.displayToConsole(true);
        activity.preprocess();
        activity.process();
        return output;
    }

    /**
     * Adds Employee metadata to a list.
     * 
     * @param list
     * @throws Exception
     */
    private void addEmployeeMetaData(List<Object> list) throws Exception {
        List<ColumnMetadata> cmdList = new ArrayList<ColumnMetadata>();
        cmdList.add(new SimpleColumnMetadata("name", "Employee", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._STRING, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList.add(new SimpleColumnMetadata("empid", "Employee", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList.add(new SimpleColumnMetadata("deptname", "Employee", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._STRING, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        TupleMetadata md = new SimpleTupleMetadata(cmdList);
        list.add(new MetadataWrapper(md));
    }

    /**
     * Adds Department metadata to a list.
     * 
     * @param list
     * @throws Exception
     */
    private void addDeptMetaData(List<Object> list) throws Exception {
        List<ColumnMetadata> cmdList = new ArrayList<ColumnMetadata>();
        cmdList.add(new SimpleColumnMetadata("deptname", "Dept", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._STRING, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList.add(new SimpleColumnMetadata("manager", "Dept", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._STRING, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        TupleMetadata md = new SimpleTupleMetadata(cmdList);
        list.add(new MetadataWrapper(md));
    }

    /**
     * Prepares activity with the given inputs.
     * 
     * @param left
     * @param right
     * @param condition
     * @return
     * @throws Exception
     */
    private TupleSemiJoinActivity prepareActivity(List<Object> left, List<Object> right, String condition) throws Exception {
        TupleSemiJoinActivity activity = new TupleSemiJoinActivity();
        MockInputPipe dataInput1 = new MockInputPipe(left.toArray());
        activity.addInput(TupleSemiJoinActivity.INPUT_DATA_1, dataInput1);
        MockInputPipe dataInput2 = new MockInputPipe(right.toArray());
        activity.addInput(TupleSemiJoinActivity.INPUT_DATA_2, dataInput2);
        MockInputPipe conditionInput = new MockInputPipe(new Object[] { condition });
        activity.addInput(TupleSemiJoinActivity.INPUT_CONDITION, conditionInput);
        KeyValueProperties properties = new SimpleKeyValueProperties();
        properties.put(new Key("join.implementation"), "uk.org.ogsadai.tuple.join.ProductJoin");
        activity.configureActivity(properties);
        return activity;
    }
}
