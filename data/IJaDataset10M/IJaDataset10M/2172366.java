package com.volantis.mcs.servlet;

import java.io.IOException;
import mock.javax.servlet.http.HttpServletResponseMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for EmptyBodyResponseWrapper class
 */
public class EmptyBodyResponseWrapperTestCase extends TestCaseAbstract {

    private HttpServletResponseMock responseMock;

    private EmptyBodyResponseWrapper testee;

    protected void setUp() throws Exception {
        super.setUp();
        responseMock = new HttpServletResponseMock("response", expectations);
        testee = new EmptyBodyResponseWrapper(responseMock);
    }

    /**
     * Test that header are correctly passed to the undlerlying response
     */
    public void testHeaders() {
        String headerName = "Header-Name";
        String headerValue = "Header value";
        responseMock.expects.addHeader(headerName, headerValue);
        testee.addHeader(headerName, headerValue);
    }

    /**
     * Test data written to the output stream do not reach the response,
     * but the content length is computed properly 
     */
    public void testWritingToOutputStream() throws IOException {
        testee.getOutputStream().write('a');
        testee.getOutputStream().write(new byte[] { 'b', 'c', 'd', 'e', 'f', 'g' });
        testee.getOutputStream().write(new byte[] { 'h', 'i', 'j', 'k' }, 1, 3);
        testee.getOutputStream().flush();
        responseMock.expects.setContentLength(10);
        testee.updateContentLength();
    }

    /**
     * Test data written to the print writer do not reach the response,
     * but the content length is computed properly 
     */
    public void testWritingToPrintWriter() throws IOException {
        testee.getWriter().print("12345");
        testee.getWriter().flush();
        responseMock.expects.setContentLength(5);
        testee.updateContentLength();
    }

    protected void tearDown() throws Exception {
        testee = null;
        responseMock = null;
        super.tearDown();
    }
}
