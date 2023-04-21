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
 * Test for IsNotDatatypeBuiltin.
 */
public class IsNotDatatypeBuiltinTest extends TestCase {

    private static final ITerm X = TERM.createVariable("X");

    private static final ITerm Y = TERM.createVariable("Y");

    public IsNotDatatypeBuiltinTest(String name) {
        super(name);
    }

    public void testBase64() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#base64Binary";
        check(false, CONCRETE.createBase64Binary(""), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testBoolean() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#boolean";
        check(false, CONCRETE.createBoolean(true), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDate() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#date";
        check(false, CONCRETE.createDate(2009, 04, 21), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDateTime() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#dateTime";
        check(false, CONCRETE.createDateTime(2009, 04, 21, 12, 31, 0, 0, 0), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDayTimeDuration() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#dayTimeDuration";
        check(false, CONCRETE.createDayTimeDuration(true, 21, 12, 21, 0), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDecimal() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#decimal";
        check(false, CONCRETE.createDecimal(1.337), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDouble() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#double";
        check(false, CONCRETE.createDouble(0.0), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testDuration() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#duration";
        check(false, CONCRETE.createDuration(true, 2, 1, 0, 5, 4, 2.3), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testFloat() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#float";
        check(false, CONCRETE.createFloat(0.0f), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testGDay() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#gDay";
        check(false, CONCRETE.createGDay(21), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testGMonth() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#gMonth";
        check(false, CONCRETE.createGMonth(4), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testGMonthDay() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#gMonthDay";
        check(false, CONCRETE.createGMonthDay(4, 21), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testGYear() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#gYear";
        check(false, CONCRETE.createGYear(2009), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testGYearMonth() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#gYearMonth";
        check(false, CONCRETE.createGYearMonth(2009, 4), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testHexBinary() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#hexBinary";
        check(false, CONCRETE.createHexBinary("0FB7"), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testInteger() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#integer";
        check(false, CONCRETE.createInteger(1337), iri);
        check(true, CONCRETE.createDouble(0.0), iri);
    }

    public void testIri() throws EvaluationException {
        String iri = "http://www.w3.org/2007/rif#iri";
        check(false, CONCRETE.createIri(iri), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testSqName() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#QName";
        ISqName name = CONCRETE.createSqName(CONCRETE.createIri("http://www.w3.org/2002/07/owl#"), "owl");
        check(true, name, iri);
        check(true, CONCRETE.createInteger(0), iri);
        iri = "http://www.wsmo.org/wsml/wsml-syntax#sQName";
        check(false, name, iri);
    }

    public void testPlainLiteral() throws EvaluationException {
        String iri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral";
        check(false, CONCRETE.createPlainLiteral("a text@en"), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testTime() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#time";
        check(false, CONCRETE.createTime(12, 45, 0, 0, 0), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testXMLLiteral() throws EvaluationException {
        String iri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral";
        check(false, CONCRETE.createXMLLiteral("<quote>Bam!</quote>"), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    public void testYearMonthDuration() throws EvaluationException {
        String iri = "http://www.w3.org/2001/XMLSchema#yearMonthDuration";
        check(false, CONCRETE.createYearMonthDuration(true, 2009, 4), iri);
        check(true, CONCRETE.createInteger(0), iri);
    }

    private void check(boolean expected, ITerm term, String datatypeIRI) throws EvaluationException {
        IsNotDatatypeBuiltin builtin = new IsNotDatatypeBuiltin(X, Y);
        ITerm datatypeIRITerm = CONCRETE.createIri(datatypeIRI);
        ITuple arguments = BASIC.createTuple(term, datatypeIRITerm);
        ITuple actualTuple = builtin.evaluate(arguments);
        if (expected) {
            assertNotNull(actualTuple);
        } else {
            assertNull(actualTuple);
        }
    }
}
