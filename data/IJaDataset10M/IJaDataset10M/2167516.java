package uk.org.ogsadai.activity.relational;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

public class PipelinedTupleJoinActivityTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009";

    List<Object> mLeftStream;

    List<Object> mRightStream;

    String mConditionString;

    /**
     * Constructor.
     * 
     * @param name
     */
    public PipelinedTupleJoinActivityTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Random rnd = new Random(100);
        List<ColumnMetadata> cmdList1 = new ArrayList<ColumnMetadata>();
        cmdList1.add(new SimpleColumnMetadata("id", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList1.add(new SimpleColumnMetadata("value", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        List<ColumnMetadata> cmdList2 = new ArrayList<ColumnMetadata>();
        cmdList2.add(new SimpleColumnMetadata("id", "r2", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList2.add(new SimpleColumnMetadata("value", "r2", new ResourceID("r2"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        TupleMetadata md1 = new SimpleTupleMetadata(cmdList1);
        mLeftStream = new ArrayList<Object>();
        mLeftStream.add(ControlBlock.LIST_BEGIN);
        mLeftStream.add(new MetadataWrapper(md1));
        for (int i = 0; i < 100; i++) {
            mLeftStream.add(new SimpleTuple(Arrays.asList(new Object[] { Math.abs(rnd.nextInt(10)), rnd.nextDouble() })));
        }
        mLeftStream.add(ControlBlock.LIST_END);
        final TupleMetadata md2 = new SimpleTupleMetadata(cmdList2);
        mRightStream = new ArrayList<Object>();
        mRightStream.add(ControlBlock.LIST_BEGIN);
        mRightStream.add(new MetadataWrapper(md2));
        for (int i = 0; i < 100; i++) {
            mRightStream.add(new SimpleTuple(Arrays.asList(new Object[] { Math.abs(rnd.nextInt(10)), rnd.nextDouble() })));
        }
        mRightStream.add(ControlBlock.LIST_END);
        mConditionString = "r1.id = r2.id";
    }

    public void testSimple() throws Exception {
        PipelinedTupleJoinActivity activity = new PipelinedTupleJoinActivity();
        MockInputPipe dataInput1 = new MockInputPipe(mLeftStream.toArray());
        activity.addInput(PipelinedTupleJoinActivity.INPUT_DATA1, dataInput1);
        MockInputPipe dataInput2 = new MockInputPipe(mRightStream.toArray());
        activity.addInput(PipelinedTupleJoinActivity.INPUT_DATA2, dataInput2);
        MockInputPipe condition = new MockInputPipe(new Object[] { mConditionString });
        activity.addInput(PipelinedTupleJoinActivity.INPUT_CONDITION, condition);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        activity.addOutput(PipelinedTupleJoinActivity.OUTPUT_RESULT, output);
        output.displayToConsole(true);
        activity.preprocess();
        activity.process();
    }
}
