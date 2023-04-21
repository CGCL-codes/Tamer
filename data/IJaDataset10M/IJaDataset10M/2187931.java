package repast.simphony.util;

import junit.framework.TestCase;
import org.apache.velocity.VelocityContext;
import repast.simphony.TestUtils;
import repast.simphony.util.VelocityUtils;
import java.util.ArrayList;

/**
 * Tests for the DataSetHandler class.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class VelocityUtilsTester extends TestCase {

    public void testEvaluate() {
        String baseString = null;
        String expectedResultString = null;
        VelocityContext context = new VelocityContext();
        baseString = "$Var1";
        context.put("Var1", new Integer(999));
        expectedResultString = "999";
        assertEquals(expectedResultString, VelocityUtils.evaluate(context, baseString, "errorString"));
        context = new VelocityContext();
        baseString = "${Var1}";
        context.put("Var1", new Integer(999));
        expectedResultString = "999";
        assertEquals(expectedResultString, VelocityUtils.evaluate(context, baseString, "errorString"));
        context = new VelocityContext();
        baseString = "${Var1}, $Repast";
        context.put("Var1", new Integer(999));
        context.put("Repast", "rpst");
        expectedResultString = "999, rpst";
        assertEquals(expectedResultString, VelocityUtils.evaluate(context, baseString, "errorString"));
    }

    public void testEvaluateError() {
        assertEquals("errorString", VelocityUtils.evaluate(new VelocityContext(), null, "errorString"));
    }

    public void testGetTemplateVarNames() {
        ArrayList<String> expected = new ArrayList<String>();
        String varString = "${Var1}";
        expected.add("Var1");
        assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils.getTemplateVarNames(varString)));
        expected = new ArrayList<String>();
        varString = "$Var1";
        expected.add("Var1");
        assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils.getTemplateVarNames(varString)));
        expected = new ArrayList<String>();
        varString = "$Var1, ${repast}";
        expected.add("Var1");
        expected.add("repast");
        assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils.getTemplateVarNames(varString)));
    }
}
