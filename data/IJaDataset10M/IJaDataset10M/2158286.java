package net.sf.jolene.html;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class AttributesTest extends TestCase {

    private static final Logger log = LogManager.getLogger(AttributesTest.class);

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomething() {
        Attributes a = new Attributes();
        a.setAttribute("TeSt1", "1");
        a.setAttribute("test2", "2");
        a.setAttribute("TEST3", "3");
        log.info("Attributes: " + a);
        assertTrue("has TeSt1: ", a.hasAttribute("TEST1"));
        assertTrue("has test2: ", a.hasAttribute("TEST2"));
        assertTrue("has test3: ", a.hasAttribute("test3"));
        log.info("a.hasAttribute null " + a.hasAttribute(null));
        boolean cantSetNull = true;
        try {
            a.setAttribute(null, "cow");
            cantSetNull = false;
        } catch (Exception e) {
            cantSetNull = true;
        }
        assertTrue("cant set key null", cantSetNull);
        log.info("a.hasAttribute null " + a.hasAttribute(null));
        a.setAttribute("cow", null);
        assertNotSame("cow should not be null", a.getAttribute("cow"), null);
        assertEquals("cow should be blank", a.getAttribute("cow"), "");
    }
}
