package org.deri.iris.builtins.string;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * Test for TextFromStringLangBuiltin.
 */
public class TextFromStringLangBuiltinTest extends TestCase {

    private static final ITerm X = TERM.createVariable("X");

    private static final ITerm Y = TERM.createVariable("Y");

    private static final ITerm Z = TERM.createVariable("Z");

    public TextFromStringLangBuiltinTest(String name) {
        super(name);
    }

    public void testEvaluation() throws EvaluationException {
        ITerm expected = CONCRETE.createText("foobar", "de");
        check(expected, "foobar", "de");
        expected = CONCRETE.createText("foobar@de");
        check(expected, "foobar", "de");
    }

    private void check(ITerm expectedTerm, String text, String lang) throws EvaluationException {
        ITuple expectedTuple = BASIC.createTuple(expectedTerm);
        ITerm textTerm = TERM.createString(text);
        ITerm langTerm = TERM.createString(lang);
        ITuple arguments = BASIC.createTuple(X, Y, Z);
        TextFromStringLangBuiltin builtin = new TextFromStringLangBuiltin(textTerm, langTerm, Z);
        ITuple actualTuple = builtin.evaluate(arguments);
        assertEquals(expectedTuple, actualTuple);
    }
}
