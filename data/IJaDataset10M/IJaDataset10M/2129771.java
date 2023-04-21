package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEMode;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the HtmlElement
 */
public class HtmlElementTestCase extends TestCaseAbstract {

    private MarinerPageContextMock pageContextMock;

    private MarinerRequestContextMock requestContextMock;

    protected void setUp() throws Exception {
        super.setUp();
        requestContextMock = new MarinerRequestContextMock("requestContext", expectations);
        pageContextMock = new MarinerPageContextMock("pageContext", expectations);
        requestContextMock.expects.getMarinerPageContext().returns(pageContextMock).any();
    }

    /**
     * test the html element sets the mode to xdime 2
     * @throws XDIMEException
     */
    public void testHtmlModeForXDIME2() throws XDIMEException {
        XDIMEContextInternal xdimeContext = (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance().createXDIMEContext();
        HtmlElement html = new HtmlElement(xdimeContext);
        pageContextMock.expects.initialisedCanvas().returns(false);
        xdimeContext.setInitialRequestContext(requestContextMock);
        pageContextMock.expects.getCurrentProject().returns(null).any();
        XDIMEResult result = html.callOpenOnProtocol(xdimeContext, null);
        assertTrue("xdime mode should be XDIMEMode.XDIME2", html.getXDIMEMode() == XDIMEMode.XDIME2);
    }

    /**
     * test the html element sets the mode to xdime CP
     * @throws XDIMEException
     */
    public void testHtmlModeForXDIMECP() throws XDIMEException {
        XDIMEContextInternal xdimeContext = (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance().createXDIMEContext();
        HtmlElement html = new HtmlElement(xdimeContext);
        pageContextMock.expects.initialisedCanvas().returns(true);
        xdimeContext.setInitialRequestContext(requestContextMock);
        XDIMEResult result = html.callOpenOnProtocol(xdimeContext, null);
        assertTrue("xdime mode should be XDIMEMode.XDIME1", html.getXDIMEMode() == XDIMEMode.XDIMECP);
    }
}
