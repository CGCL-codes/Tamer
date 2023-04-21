package test.unit.purview.i18n;

import junit.framework.TestCase;
import com.pureperfect.purview.i18n.ExpressionEngine;
import com.pureperfect.purview.i18n.JEXLExpressionEngine;
import com.pureperfect.purview.i18n.ParseException;
import com.pureperfect.purview.net.InvalidEmailProblem;

/**
 * Test the JEXL expression engine.
 * 
 * @author J. Chris Folsom
 * @version 1.0
 * @since 1.0
 */
public class JEXLExpressionEngineTest extends TestCase {

    public void testEval() {
        final ExpressionEngine jexl = JEXLExpressionEngine.getDefaultInstance();
        final InvalidEmailProblem problem = new InvalidEmailProblem("instanceValue", null, null, "bademail");
        String result = jexl.eval("The bad email address was: ${problem.instance.length()}", problem);
        assertEquals("The bad email address was: 13", result);
        result = jexl.eval("The bad email address was: ${problem.value}the object was: ${problem.instance}", problem);
        assertEquals("The bad email address was: bademailthe object was: instanceValue", result);
        result = jexl.eval("oops: $problem.value", problem);
        assertEquals("oops: $problem.value", result);
        result = jexl.eval("oops: $problem.value}", problem);
        try {
            jexl.eval("oops: ${problem.value the object was: ${problem.instance}", problem);
            fail();
        } catch (final ParseException e) {
        }
        try {
            jexl.eval("oops: ${problem.value", problem);
            fail();
        } catch (final ParseException e) {
            assertTrue(e.getMessage().contains("${problem.value"));
        }
    }
}
