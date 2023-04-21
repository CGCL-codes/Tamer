package de.mindmatters.faces.spring.context.viewhandler;

import de.mindmatters.faces.spring.test.AbstractJsfSpringContextTests;

public class ViewHandlerTest extends AbstractJsfSpringContextTests {

    protected String getResourceBasePath() {
        return "src-test/de/mindmatters/faces/spring/context/viewhandler";
    }

    public void testViewHandlers() {
        ViewHandler2 viewHandler2 = ViewHandler2.INSTANCE;
        assertNotNull(viewHandler2);
        assertNotNull(viewHandler2.getBeanFactory());
        assertNull(viewHandler2.getHandlerProperty());
        ViewHandler1 viewHandler1 = (ViewHandler1) viewHandler2.getDelegate();
        assertNotNull(viewHandler1);
        assertNotNull(viewHandler1.getBeanFactory());
        assertNotNull(viewHandler1.getDelegate());
        assertEquals("doHandle", viewHandler1.getHandlerProperty());
    }
}
