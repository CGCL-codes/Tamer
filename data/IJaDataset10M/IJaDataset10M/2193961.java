package net.sf.myra.datamining.data;

import java.io.IOException;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import net.sf.myra.datamining.data.IntervalBuilder.Interval;
import net.sf.myra.datamining.io.ArffHelper;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2305 $ $Date:: 2010-08-06 18:58:46#$
 */
public class IntervalBuilderTest extends TestCase {

    /**
	 * The sample dataset used across the tests.
	 */
    private Dataset sample;

    /**
	 * The sample2 dataset used across the tests.
	 */
    private Dataset sample2;

    /**
	 * The sample3 dataset used across the tests.
	 */
    private Dataset sample3;

    @Override
    protected void setUp() throws Exception {
        try {
            sample = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example.arff")));
            sample2 = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example2.arff")));
            sample3 = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example-mixed.arff")));
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    public void testMdlEntropySingle1() {
        Metadata metadata = sample.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a1");
        IntervalBuilder builder = new MDLEntropyIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample.getInstances(), c);
        assertNotSame(Operator.IN_RANGE, interval.getOperator());
        assertEquals(20.5, interval.getValues()[0]);
    }

    public void testMdlEntropySingle2() {
        Metadata metadata = sample2.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a1");
        IntervalBuilder builder = new MDLEntropyIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample2.getInstances(), c);
        assertSame(Operator.IN_RANGE, interval.getOperator());
        assertEquals(40.5, interval.getValues()[0]);
        assertEquals(60.5, interval.getValues()[1]);
    }

    public void testMdlEntropySingle3() {
        Metadata metadata = sample.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a3");
        IntervalBuilder builder = new MDLEntropyIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample.getInstances(), c, "D");
        assertSame(Operator.IN_RANGE, interval.getOperator());
        assertEquals(40.5, interval.getValues()[0]);
        assertEquals(60.5, interval.getValues()[1]);
        c = (ContinuousAttribute) sample.getMetadata().get("a1");
        interval = builder.createSingle(sample.getInstances(), c, "C");
        assertSame(Operator.GREATER_THAN_EQUAL_TO, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
    }

    public void testEntropySingle1() {
        try {
            Dataset breast_w = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/breast-w.arff")));
            Metadata metadata = breast_w.getMetadata();
            ContinuousAttribute age = (ContinuousAttribute) metadata.get("Cell_Size_Uniformity");
            IntervalBuilder builder = new EntropyIntervalBuilder(metadata);
            assertEquals(2.5, builder.createSingle(breast_w.getInstances(), age).getValues()[0]);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testEntropySingle2() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/temperature-h.arff")));
            Metadata metadata = dataset.getMetadata();
            ContinuousAttribute temperature = (ContinuousAttribute) metadata.get("temperature");
            IntervalBuilder builder = new EntropyIntervalBuilder(metadata);
            assertEquals(9.5, builder.createSingle(dataset.getInstances(), temperature).getValues()[0]);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testEntropySingle3() {
        Metadata metadata = sample.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a3");
        IntervalBuilder builder = new EntropyIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample.getInstances(), c, "D");
        assertSame(Operator.LESS_THAN, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
        c = (ContinuousAttribute) sample.getMetadata().get("a1");
        interval = builder.createSingle(sample.getInstances(), c, "C");
        assertSame(Operator.GREATER_THAN_EQUAL_TO, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
    }

    public void testEntropySingle4() {
        Metadata metadata = sample.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a1");
        IntervalBuilder builder = new EntropyIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.LESS_THAN, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
        Label label = metadata.getLabel();
        sample.remove(sample.getInstances(label.toLabel("A", "C", "E")));
        interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.LESS_THAN, interval.getOperator());
        assertEquals(40.5, interval.getValues()[0]);
        sample.remove(sample.getInstances(label.toLabel("A", "C", "B")));
        interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.LESS_THAN, interval.getOperator());
        assertEquals(40.5, interval.getValues()[0]);
    }

    public void testDistanceSingle1() {
        Metadata metadata = sample.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a1");
        IntervalBuilder builder = new DistanceIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.GREATER_THAN_EQUAL_TO, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
        Label label = metadata.getLabel();
        sample.remove(sample.getInstances(label.toLabel("A", "C")));
        interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.LESS_THAN, interval.getOperator());
        assertEquals(60.5, interval.getValues()[0]);
        sample.remove(sample.getInstances(label.toLabel("A", "C", "E")));
        interval = builder.createSingle(sample.getInstances(), c);
        assertSame(Operator.GREATER_THAN_EQUAL_TO, interval.getOperator());
        assertEquals(70.5, interval.getValues()[0]);
    }

    public void testMdlDistanceSingle1() {
        Metadata metadata = sample3.getMetadata();
        ContinuousAttribute c = (ContinuousAttribute) metadata.get("a1");
        IntervalBuilder builder = new MDLDistanceIntervalBuilder(metadata);
        Interval interval = builder.createSingle(sample3.getInstances(), c);
        assertSame(Operator.IN_RANGE, interval.getOperator());
        assertEquals(20.5, interval.getValues()[0]);
        assertEquals(60.5, interval.getValues()[1]);
    }
}
