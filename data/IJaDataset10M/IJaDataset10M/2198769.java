package org.ztemplates.test.actions.urlhandler.form;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.ztemplates.actions.urlhandler.ZIUrlHandler;
import org.ztemplates.form.impl.ZFormWrapper;
import org.ztemplates.test.ZTestUrlHandlerFactory;
import org.ztemplates.test.mock.ZMock;
import org.ztemplates.test.mock.ZMockServiceRepository;
import org.ztemplates.test.mock.ZMockServletService;

public class FormTest extends TestCase {

    static Logger log = Logger.getLogger(FormTest.class);

    ZIUrlHandler up;

    protected void setUp() throws Exception {
        super.setUp();
        ZMock.setUp();
        up = ZTestUrlHandlerFactory.create(FormTest.class.getPackage().getName(), ZTestUrlHandlerFactory.defaultSecurityService);
    }

    protected void tearDown() throws Exception {
        up = null;
        super.tearDown();
    }

    public void testNames() throws Exception {
        Form form = new Form();
        ZMockServiceRepository repo = ZMock.getMock();
        repo.setServletService(new ZMockServletService());
        ZFormWrapper mirr = new ZFormWrapper(form);
        assertEquals("op1", form.getOp1().getName());
        assertEquals("prop1", form.getProp1().getName());
        assertEquals("topSection_field1", form.getTopSection().getField1().getName());
        assertEquals("topSection_field2", form.getTopSection().getField2().getName());
        assertEquals("topSection_op1", form.getTopSection().getOp1().getName());
    }

    public void testValidationParameterWithDot() throws Exception {
        Map<String, String[]> param = new HashMap<String, String[]>();
        try {
            FormAction obj = (FormAction) up.process("/act", param);
            fail();
        } catch (Exception e) {
        }
    }

    public void testParamPropParameterWithDot() throws Exception {
        Map<String, String[]> param = new HashMap<String, String[]>();
        param.put("form_topSection_field1", new String[] { "val1" });
        param.put("katzeko", new String[] { "val4" });
        try {
            FormAction obj = (FormAction) up.process("/act", param);
            fail();
        } catch (Exception e) {
        }
    }

    public void testPublicNames() throws Exception {
        FormWithPublicFields form = new FormWithPublicFields();
        ZMockServiceRepository repo = ZMock.getMock();
        repo.setServletService(new ZMockServletService());
        ZFormWrapper mirr = new ZFormWrapper(form);
        assertEquals(1, mirr.getOperations().size());
        assertEquals(1, mirr.getProperties().size());
        assertEquals(1, mirr.getForms().size());
    }
}
