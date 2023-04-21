package com.ail.openquote.pageflow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.ail.core.Core;
import com.ail.core.XMLString;
import com.ail.coretest.CoreUserTestCase;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.PageFlow;
import com.ail.openquote.ui.util.QuotationContext;

/**
 */
public class TestHtmlRender extends CoreUserTestCase {

    private static final long serialVersionUID = 2030295330203910171L;

    /**
     * Constructs a test case with the given name.
     */
    public TestHtmlRender(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestHtmlRender.class);
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        super.setupSystemProperties();
        super.setCore(new Core(this));
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
    }

    public void testPageRender() throws Exception {
        XMLString instanceXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusInstance.xml"));
        Quotation instanceObj = (Quotation) super.getCore().fromXML(Quotation.class, instanceXml);
        assertNotNull(instanceObj);
        XMLString pageFlowXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusPageFlow.xml"));
        PageFlow pageFlowObj = super.getCore().fromXML(PageFlow.class, pageFlowXml);
        assertNotNull(pageFlowObj);
        StringWriter output = new StringWriter();
        PortletSession session = new MockPortletSession(instanceObj);
        RenderResponse response = new MockRenderResponse(Locale.UK, new PrintWriter(output));
        RenderRequest request = new MockRenderRequest(Locale.UK, session);
        QuotationContext.setRequest(request);
        QuotationContext.setQuotation(instanceObj);
        QuotationContext.setPageFlow(pageFlowObj);
        pageFlowObj.renderResponse(request, response, instanceObj);
        System.out.println(output.getBuffer());
    }
}
