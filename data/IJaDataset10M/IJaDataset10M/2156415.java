package uk.org.ogsadai.tuple.join;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.aggregate.Max;
import uk.org.ogsadai.dqp.lqp.udf.repository.SimpleFunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.scalar.Floor;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Test class for ThetaJoin.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ThetaJoinTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    /**
     * Constructor.
     * 
     * @param name
     */
    public ThetaJoinTest(String name) {
        super(name);
    }

    public void testLessThan() throws Exception {
        ThetaJoin thetaJoin = new ThetaJoin();
        thetaJoin.storeRightTuples(false);
        thetaJoin.setCondition(getExpression("r1.id < r2.id"));
        configureMetadata(thetaJoin);
        thetaJoin.storeTuples(getStoredTuples());
        Iterable<Tuple> tuples = thetaJoin.join(createTuple(5, 20.0));
        checkResults(tuples, 0, 4);
    }

    public void testLessThanWithAnd() throws Exception {
        ThetaJoin thetaJoin = new ThetaJoin();
        thetaJoin.storeRightTuples(false);
        thetaJoin.setCondition(getExpression("r1.id < r2.id AND r1.id > r2.id - 3"));
        configureMetadata(thetaJoin);
        thetaJoin.storeTuples(getStoredTuples());
        Iterable<Tuple> tuples = thetaJoin.join(createTuple(5, 20.0));
        checkResults(tuples, 3, 4);
    }

    public void testLessThanWithAddition() throws Exception {
        ThetaJoin thetaJoin = new ThetaJoin();
        thetaJoin.storeRightTuples(false);
        thetaJoin.setCondition(getExpression("r1.id < r2.id + 2"));
        configureMetadata(thetaJoin);
        thetaJoin.storeTuples(getStoredTuples());
        Iterable<Tuple> tuples = thetaJoin.join(createTuple(5, 20.0));
        checkResults(tuples, 0, 6);
    }

    public void testLessOrEqualThan() throws Exception {
        ThetaJoin thetaJoin = new ThetaJoin();
        thetaJoin.storeRightTuples(false);
        thetaJoin.setCondition(getExpression("r1.id <= r2.id"));
        configureMetadata(thetaJoin);
        thetaJoin.storeTuples(getStoredTuples());
        Iterable<Tuple> tuples = thetaJoin.join(createTuple(5, 20.0));
        checkResults(tuples, 0, 5);
    }

    public void testFunction() throws Exception {
        String condition = "Floor(r1.value) = Floor(r2.value)";
        SimpleFunctionRepository repository = new SimpleFunctionRepository();
        repository.register(Floor.class);
        SQLQueryParser p = SQLQueryParser.getInstance();
        Expression expression = ExpressionFactory.buildExpression(p.parseSQLForCondition(condition), repository);
        ThetaJoin thetaJoin = new ThetaJoin();
        thetaJoin.storeRightTuples(false);
        thetaJoin.setCondition(expression);
        configureMetadata(thetaJoin);
        thetaJoin.storeTuples(getStoredTuples());
        Iterable<Tuple> tuples = thetaJoin.join(createTuple(5, 2.0));
        checkResults(tuples, 0, 0);
    }

    private void configureMetadata(ThetaJoin thetaJoin) throws Exception {
        List<ColumnMetadata> cmdList1 = new ArrayList<ColumnMetadata>();
        cmdList1.add(new SimpleColumnMetadata("id", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList1.add(new SimpleColumnMetadata("value", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        List<ColumnMetadata> cmdList2 = new ArrayList<ColumnMetadata>();
        cmdList2.add(new SimpleColumnMetadata("id", "r2", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList2.add(new SimpleColumnMetadata("value", "r2", new ResourceID("r2"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        TupleMetadata md1 = new SimpleTupleMetadata(cmdList1);
        TupleMetadata md2 = new SimpleTupleMetadata(cmdList2);
        thetaJoin.configure(md1, md2);
    }

    private Expression getExpression(String condition) throws Exception {
        SQLQueryParser p = SQLQueryParser.getInstance();
        return ExpressionFactory.buildExpression(p.parseSQLForCondition(condition), null);
    }

    private List<Tuple> getStoredTuples() {
        int ids[] = new int[] { 7, 0, 2, 1, 4, 3, 5, 6, 8, 9 };
        double values[] = new double[] { 0.1, 2.5, 45.0, -23.3, 7.66, 7.88, 3.5, 6.8, 9.2, 123.0 };
        List<Tuple> result = new LinkedList<Tuple>();
        for (int i = 0; i < ids.length; ++i) {
            result.add(createTuple(ids[i], values[i]));
        }
        return result;
    }

    private Tuple createTuple(int id, double value) {
        return new SimpleTuple(Arrays.<Object>asList(id, value));
    }

    private void checkResults(Iterable<Tuple> tuples, int min, int max) {
        int expected = min - 1;
        for (Tuple t : tuples) {
            ++expected;
            if (expected > max) Assert.fail("Got more results that expected");
            System.out.println(t);
            Assert.assertEquals(expected, t.getInt(0));
        }
        if (expected != max) Assert.fail("Did not get all the expected IDs");
    }
}
