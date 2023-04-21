package siouxsie.mvc.load;

import java.io.InputStream;
import java.util.Iterator;
import siouxsie.mvc.ActionForward;
import siouxsie.mvc.ActionMapping;
import siouxsie.mvc.ExceptionHandlerMapping;
import siouxsie.mvc.MvcConfiguration;
import siouxsie.mvc.MvcException;
import siouxsie.mvc.SimpleViewHandler;
import siouxsie.mvc.TextViewMessagesHandler;
import junit.framework.TestCase;

public class DigesterLoaderTest extends TestCase {

    public void testDefault() throws Exception {
        ILoader loader = new DigesterLoader();
        InputStream in = getClass().getResourceAsStream("/siouxsie/mvc/load/test-load.xml");
        assertNotNull(in);
        MvcConfiguration config = loader.loadConfiguration(in);
        assertEquals(2, config.getExceptionHandlers().size());
        Iterator iter = config.getExceptionHandlers().iterator();
        ExceptionHandlerMapping exHandler = (ExceptionHandlerMapping) iter.next();
        assertEquals("java.lang.Exception", exHandler.getType());
        assertEquals("siouxsie.mvc.SimpleExceptionHandler", exHandler.getHandler());
        exHandler = (ExceptionHandlerMapping) iter.next();
        assertEquals("my.swing.app.ServiceException", exHandler.getType());
        assertEquals("siouxsie.mvc.SimpleServiceExceptionHandler", exHandler.getHandler());
        assertEquals(SimpleViewHandler.class, config.getViewHandler().getClass());
        assertEquals(TextViewMessagesHandler.class, config.getViewMessagesHandler().getClass());
        assertEquals("siouxsie.mvc.load.dummy", config.getMessageResources().getBundleClass());
        assertEquals("Hello brave new world!", config.getMessageResources().getMessage("hello.world"));
        assertEquals(2, config.getActionMappings().size());
        ActionMapping mapping = config.getActionMapping("action1");
        assertNotNull(mapping);
        assertEquals("action1", mapping.getName());
        assertEquals("test.ActionClass", mapping.getAction());
        assertEquals(2, mapping.getForwards().size());
        assertEquals(2, mapping.getRoles().length);
        ActionForward forward = mapping.findForward("forward1");
        assertEquals("forward1", forward.getName());
        assertEquals("classView1", forward.getView());
        assertEquals("viewValueClass", forward.getViewValueClass());
        forward = mapping.findForward("forward2");
        assertEquals("forward2", forward.getName());
        assertEquals("classView2", forward.getView());
        assertEquals("viewValueClass1", forward.getViewValueClass());
        mapping = config.getActionMapping("action2");
        assertNotNull(mapping);
        assertEquals("action2", mapping.getName());
        assertEquals("test.ActionClass2", mapping.getAction());
        assertEquals(1, mapping.getForwards().size());
        assertNull(mapping.getRoles());
        try {
            loader.loadConfiguration(null);
            fail("should have launched a mvc exception");
        } catch (Exception e) {
            assertEquals(MvcException.class, e.getClass());
        }
    }
}
