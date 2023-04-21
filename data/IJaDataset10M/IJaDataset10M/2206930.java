package com.dayspringtech.daylisp;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;
import java.io.StringReader;

/**
 * EvalTestCase.java
 *
 * @author Matthew Denson - ae6up at users.sourceforge.net
 */
public abstract class EvalTestCase extends TestCase {

    public EvalTestCase() {
        super();
    }

    public EvalTestCase(String string) {
        super(string);
    }

    protected Environment env = Primitive.installPrimitives(new Environment(true));

    protected InputStream createInputStream(String s) {
        return new InputStream(new StringReader(s));
    }

    protected Object readForm(String s) {
        try {
            return (new InputStream(new StringReader(s))).read();
        } catch (ParseException e) {
            throw new AssertionFailedError("Could not read " + s);
        }
    }

    protected Object evalForm(String s) throws Exception {
        return SpecialOperators.EVAL.apply(readForm(s), env);
    }

    protected void assertEval(String expected, String form) {
        try {
            if (expected != null) assertEquals(readForm(expected), evalForm(form)); else assertNull(evalForm(form));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionFailedError("Could not evaluate " + form);
        }
    }
}
