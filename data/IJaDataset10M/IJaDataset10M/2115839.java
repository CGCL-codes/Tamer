package test.hudson.zipscript;

import hudson.zipscript.ZipEngine;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.exception.ParseException;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class MathTestCase extends TestCase {

    public void testNoContextExpressions() throws Exception {
        assertEquals(new Integer(8), eval("4+3*2-2"));
        assertEquals(new Long(20), eval("(4+3*2l-2)+6*2"));
        assertEquals(new Float(2), eval("(20-2*2f-2)-6*2"));
        assertEquals(new Double(10), eval("(20+2*2-2)-6d*2"));
        assertEquals(new Float(5), eval("10/2"));
        assertEquals(new Double(5), eval("10d/2"));
        assertEquals(new Double(5.5), eval("11d/2"));
    }

    public void testContextExpressions() throws Exception {
        Map context = new HashMap();
        context.put("num1", new Integer(10));
        context.put("num2", new Double(2));
        assertEquals(new Double(5), eval("num1/num2", context));
        assertEquals(new Double(5), eval(" num1 / num2 ", context));
        assertEquals(new Double(5), eval("${num1}/${num2}", context));
        assertEquals(new Double(5), eval(" ${num1} / ${num2} ", context));
        assertEquals(new Integer(20), eval("${num1+10}", context));
    }

    private Object eval(String s) throws ParseException, ExecutionException {
        return eval(s, null);
    }

    private Object eval(String s, Object context) throws ParseException, ExecutionException {
        return ZipEngine.createInstance().getEvaluator(s).objectValue(context);
    }
}
