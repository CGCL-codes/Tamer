package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.ISqName;

/**
 * Test for ToIntegerBuiltin.
 */
public class ToIntegerBuiltinTest extends TestCase {

    private static final ITerm X = TERM.createVariable("X");

    private static final ITerm Y = TERM.createVariable("Y");

    public ToIntegerBuiltinTest(String name) {
        super(name);
    }

    public void testBase64() throws EvaluationException {
        fails(CONCRETE.createBase64Binary("c3VyZS4="));
    }

    public void testBoolean() throws EvaluationException {
        equals(0, CONCRETE.createBoolean(false));
        equals(1, CONCRETE.createBoolean(true));
    }

    public void testDate() throws EvaluationException {
        fails(CONCRETE.createDate(2009, 04, 27));
    }

    public void testDateTime() throws EvaluationException {
        fails(CONCRETE.createDateTime(2009, 04, 27, 10, 10, 0, -5, 0));
    }

    public void testDayTimeDuration() throws EvaluationException {
        fails(CONCRETE.createDayTimeDuration(false, 2, 2, 3, 0));
    }

    public void testDecimal() throws EvaluationException {
        equals(1, CONCRETE.createDecimal(1.337));
    }

    public void testDouble() throws EvaluationException {
    }

    public void testDuration() throws EvaluationException {
        fails(CONCRETE.createDuration(true, 2, 1, 0, 5, 4, 2.3));
    }

    public void testFloat() throws EvaluationException {
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
        equals(1337, CONCRETE.createInteger(1337));
    }

    public void testIri() throws EvaluationException {
        fails(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"));
    }

    public void testSqName() throws EvaluationException {
        ISqName name = CONCRETE.createSqName(CONCRETE.createIri("http://www.w3.org/2002/07/owl#"), "owl");
        fails(name);
    }

    public void testString() throws EvaluationException {
        equals(1, TERM.createString("1"));
        equals(1, TERM.createString("1.337"));
        fails(TERM.createString("abcd"));
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
        fails(CONCRETE.createYearMonthDuration(true, 2009, 4));
    }

    private void equals(int expected, ITerm term) throws EvaluationException {
        ITuple expectedTuple = BASIC.createTuple(CONCRETE.createInteger(expected));
        ITuple actualTuple = compute(term);
        assertEquals(expectedTuple, actualTuple);
    }

    private void fails(ITerm term) throws EvaluationException {
        assertNull(compute(term));
    }

    private ITuple compute(ITerm term) throws EvaluationException {
        ToIntegerBuiltin builtin = new ToIntegerBuiltin(term, Y);
        ITuple arguments = BASIC.createTuple(X, Y);
        ITuple actualTuple = builtin.evaluate(arguments);
        return actualTuple;
    }
}
