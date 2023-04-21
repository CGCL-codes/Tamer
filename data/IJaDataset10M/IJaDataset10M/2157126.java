package com.sitechasia.webx.core.utils.context;

import junit.framework.TestCase;
import com.sitechasia.webx.core.utils.context.ApplicationContext;
import com.sitechasia.webx.core.utils.context.IContext;

public class ApplicationContextTest extends TestCase {

    public void testApplication() {
        IContext context = ApplicationContext.getInstance();
        context.setAttribute("key", "value");
        IContext context2 = ApplicationContext.getInstance();
        assertEquals("value", context2.getAttribute("key"));
    }
}
