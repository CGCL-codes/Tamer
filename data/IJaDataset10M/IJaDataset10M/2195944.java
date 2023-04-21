package com.j2xtreme.xbean.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2xtreme.xbean.PropertyAccessException;
import com.j2xtreme.xbean.ServiceNotFoundException;
import com.j2xtreme.xbean.ServicePropertyReference;
import junit.framework.TestCase;

/**
 * @author <SCRIPT language="javascript">eval(unescape('%64%6f%63%75%6d%65%6e%74%2e%77%72%69%74%65%28%27%3c%61%20%68%72%65%66%3d%22%6d%61%69%6c%74%6f%3a%72%6f%62%40%6a%32%78%74%72%65%6d%65%2e%63%6f%6d%22%3e%52%6f%62%20%53%63%68%6f%65%6e%69%6e%67%3c%2f%61%3e%27%29%3b'))</SCRIPT>
 * @version $Id: ServicePropertyReferenceTestCase.java,v 1.2 2005/01/02 21:36:59 rschoening Exp $
 */
public class ServicePropertyReferenceTestCase extends TestCase {

    static Log log = LogFactory.getLog(ServicePropertyReferenceTestCase.class);

    public void testMe() {
        ServicePropertyReference spr = new ServicePropertyReference("/TestBean", "stringProperty");
        assertEquals("DO_NOT_CHANGE_THIS_VALUE", spr.get());
        try {
            new ServicePropertyReference("/someinvalidservice", "testme").get();
            fail("Should have thrown ServiceNotFoundException");
        } catch (ServiceNotFoundException e) {
        } finally {
        }
        try {
            new ServicePropertyReference("/TestBean", "someInvalidProperty").get();
            fail("Expected PropertyAccessException");
        } catch (PropertyAccessException e) {
        }
        try {
            new ServicePropertyReference(null, "something").get();
            fail("expected ServiceNotFoundException");
        } catch (ServiceNotFoundException e) {
        }
        try {
            new ServicePropertyReference("/TestBean", null).get();
        } catch (PropertyAccessException e) {
        }
        try {
            new ServicePropertyReference("/TestBean", "privateProperty").get();
            fail("Expected PropertyAccessException");
        } catch (PropertyAccessException e) {
        }
    }
}
