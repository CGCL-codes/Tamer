package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.ISqName;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;

/**
 * Test for ToYearMonthDurationBuiltin.
 */
public class ToYearMonthDurationBuiltinTest extends TestCase {

    private static final ITerm X = TERM.createVariable("X");

    private static final ITerm Y = TERM.createVariable("Y");

    public ToYearMonthDurationBuiltinTest(String name) {
        super(name);
    }

    public void testBase64() throws EvaluationException {
        fails(CONCRETE.createBase64Binary("c3VyZS4="));
    }

    public void testBoolean() throws EvaluationException {
        fails(CONCRETE.createBoolean(true));
    }

    public void testDate() throws EvaluationException {
        fails(CONCRETE.createDate(2009, 04, 27));
    }

    public void testDateTime() throws EvaluationException {
        fails(CONCRETE.createDateTime(2009, 04, 27, 10, 10, 0, -5, 0));
    }

    public void testDayTimeDuration() throws EvaluationException {
        equals(CONCRETE.createYearMonthDuration(true, 0, 0), CONCRETE.createDayTimeDuration(true, 2, 2, 3, 0));
    }

    public void testDecimal() throws EvaluationException {
        fails(CONCRETE.createDecimal(1.337));
    }

    public void testDouble() throws EvaluationException {
        fails(CONCRETE.createDouble(1.3));
    }

    public void testDuration() throws EvaluationException {
        equals(CONCRETE.createYearMonthDuration(true, 2, 1), CONCRETE.createDuration(true, 2, 1, 0, 5, 4, 2.3));
    }

    public void testFloat() throws EvaluationException {
        fails(CONCRETE.createFloat(1.3f));
    }

    public void testGDay() throws EvaluationException {
        fails(CONCRETE.createGDay(27));
    }

    public void testGMonth() throws EvaluationException {
        fails(CONCRETE.createGMonth(4));
    }

    public void testGMonthDay() throws EvaluationException {
        fails(CONCRETE.createGMonthDay(4, 27));
    }

    public void testGYear() throws EvaluationException {
        fails(CONCRETE.createGYear(2009));
    }

    public void testGYearMonth() throws EvaluationException {
        fails(CONCRETE.createGYearMonth(2009, 4));
    }

    public void testHexBinary() throws EvaluationException {
        fails(CONCRETE.createHexBinary("0FB7"));
    }

    public void testInteger() throws EvaluationException {
        fails(CONCRETE.createInteger(1337));
    }

    public void testIri() throws EvaluationException {
        fails(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"));
    }

    public void testSqName() throws EvaluationException {
        ISqName name = CONCRETE.createSqName(CONCRETE.createIri("http://www.w3.org/2002/07/owl#"), "owl");
        fails(name);
    }

    public void testText() throws EvaluationException {
        fails(CONCRETE.createPlainLiteral("Ein Text@de"));
    }

    public void testTime() throws EvaluationException {
        fails(CONCRETE.createTime(12, 45, 0, 0, 0));
    }

    public void testXMLLiteral() throws EvaluationException {
        fails(CONCRETE.createXMLLiteral("<quote>Bam!</quote>", "de"));
    }

    public void testYearMonthDuration() throws EvaluationException {
        equals(CONCRETE.createYearMonthDuration(true, 2009, 4), CONCRETE.createYearMonthDuration(true, 2009, 4));
    }

    private void equals(IYearMonthDuration expected, ITerm term) throws EvaluationException {
        ITuple expectedTuple = BASIC.createTuple(expected);
        ITuple actualTuple = compute(term);
        assertEquals(expectedTuple, actualTuple);
    }

    private void fails(ITerm term) throws EvaluationException {
        assertNull(compute(term));
    }

    private ITuple compute(ITerm term) throws EvaluationException {
        ToYearMonthDurationBuiltin builtin = new ToYearMonthDurationBuiltin(term, Y);
        ITuple arguments = BASIC.createTuple(X, Y);
        ITuple actualTuple = builtin.evaluate(arguments);
        return actualTuple;
    }
}
