package uk.org.ogsadai.expression;

import junit.framework.TestCase;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Tests comparison with corrupted data.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ComparisonWithCorruptedData extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** The metadata object */
    private TupleMetadata mMetadata;

    private Tuple mTuple;

    /**
     * Constructor.
     * 
     * @param name
     */
    public ComparisonWithCorruptedData(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mMetadata = ExpressionTestUtils.getAllTypeTupleMetadata();
        mTuple = ExpressionTestUtils.getAllTypeTuple();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * We treat corrupted data as unknown (or NULL) value.
     * 
     * @throws Exception
     */
    public void testCorrupted() throws Exception {
        String[] expressions = new String[] { "corruptIntStringT = intT", "corruptIntStringT > intT", "corruptIntStringT >= intT", "corruptIntStringT < intT", "corruptIntStringT <= intT", "corruptIntStringT <> intT" };
        for (String expr : expressions) {
            Expression ae = ExpressionFactory.buildExpression(SQLQueryParser.getInstance().parseSQLForCondition(expr), null);
            ae.configure(mMetadata);
            Boolean evaluation = ae.evaluate(mTuple);
            assertTrue("Expected null for " + expr, evaluation == null);
        }
    }

    /**
     * Valid strings should be converted and compared without any problems.
     * 
     * @throws Exception
     */
    public void testValidString() throws Exception {
        String[] expressions = new String[] { "intStringT = intT", "intStringT > intT - 1", "intStringT >= intT", "intStringT < intT + 1", "intStringT <= intT", "intStringT <> intT + 1" };
        for (String expr : expressions) {
            Expression ae = ExpressionFactory.buildExpression(SQLQueryParser.getInstance().parseSQLForCondition(expr), null);
            ae.configure(mMetadata);
            Boolean evaluation = ae.evaluate(mTuple);
            assertTrue("Expected true for " + expr + " but evaluation result was " + evaluation, evaluation);
        }
    }
}
