package com.tchepannou.rails.core.resource;

import com.tchepannou.rails.core.ObjectFactory;
import com.tchepannou.rails.mock.servlet.MockHttpServletResponse;
import java.io.InputStream;
import junit.framework.TestCase;

/**
 *
 * @author herve
 */
public class InputStreamResourceTest extends TestCase {

    public InputStreamResourceTest(String testName) {
        super(testName);
    }

    public void testOutput() throws Exception {
        InputStream in = ObjectFactory.createInputStream("test");
        InputStreamResource r = new InputStreamResource(in, "text/plain");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        r.output(resp);
        assertEquals("test", resp.getContentAsString());
    }
}
