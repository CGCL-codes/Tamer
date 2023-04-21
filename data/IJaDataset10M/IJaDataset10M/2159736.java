package joc.internal.compiler;

import static org.junit.Assert.assertEquals;
import joc.internal.compiler.NestedExp.CodeNestedExp;
import org.junit.Before;
import org.junit.Test;

public class NestedExpTest {

    private NestedExp nestedExp;

    private CodeNestedExp exp1;

    private CodeNestedExp exp2;

    @Before
    public void before() {
        nestedExp = new CodeNestedExp("nested");
        exp1 = new CodeNestedExp("abc");
        exp2 = new CodeNestedExp("def");
    }

    @Test
    public void testJoinStringDelimiter() throws Exception {
        assertEquals("abc, def", nestedExp.getCodeForValues(exp1, exp2));
    }

    @Test
    public void testJoinStringDelimiterInFront() throws Exception {
        assertEquals("(abc, def)", nestedExp.getCodeForParams(exp1, exp2));
    }
}
