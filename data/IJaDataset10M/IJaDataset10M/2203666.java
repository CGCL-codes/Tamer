package org.hip.kernel.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.impl.AbstractContext;
import org.hip.kernel.servlet.impl.ServletRequestHelper;
import org.hip.kernel.servlet.test.TestServletRequest.Builder;
import org.junit.Test;

/**
 * @author Luthiger
 * Created: 23.09.2010
 */
public class ServletRequestHelperTest {

    @Test
    public void testClassic() throws Exception {
        String[] lExpected = new String[] { "blue", "green", "red" };
        Builder lBuilder = new TestServletRequest.Builder();
        Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>();
        lParameters.put("first", new String[] { "done" });
        lParameters.put("second", new String[] { "again" });
        lParameters.put("multi", lExpected);
        lParameters.put(AbstractContext.REQUEST_TYPE, new String[] { "doTest" });
        HttpServletRequest lRequest = lBuilder.setMethod("POST").setParameters(lParameters).build();
        ServletRequestHelper lHelper = new ServletRequestHelper(lRequest);
        Context lContext = new TestContext();
        lHelper.addParametersToContext(lContext, AbstractContext.REQUEST_TYPE);
        assertTrue("contains requestType", lHelper.containsParameter(AbstractContext.REQUEST_TYPE));
        assertEquals("value requestType", "doTest", lHelper.getParameterValue(AbstractContext.REQUEST_TYPE));
        assertTrue("contains parameter", lHelper.containsParameter("first"));
        assertEquals("value parameter", "done", lHelper.getParameterValue("first"));
        assertFalse("contains not parameter", lHelper.containsParameter("something"));
        assertNull("value not parameter", lHelper.getParameterValue("something"));
        assertEquals("value parameter 1", "done", lContext.getParameterValue("first"));
        assertEquals("value parameter 2", "", lContext.getParameterValue("something"));
        assertEquals("value parameter 3", "", lContext.getParameterValue(AbstractContext.REQUEST_TYPE));
        String[] lValueArray = lContext.getParameterValueArray("multi");
        assertEquals("", lExpected.length, lValueArray.length);
        for (int i = 0; i < lExpected.length; i++) {
            assertEquals("array value " + i, lExpected[i], lValueArray[i]);
        }
    }
}
