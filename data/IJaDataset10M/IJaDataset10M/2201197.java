package org.toolme.net;

import jmunit.framework.cldc10.AssertionFailedException;
import jmunit.framework.cldc10.TestCase;

/**
 * Test EasyHttpRequest.
 */
public class EasyHttpRequestTest extends TestCase {

    /**
	 * Number of tests.
	 */
    private static final int TESTCOUNT = 4;

    /**
	 * Initialise.
	 */
    public EasyHttpRequestTest() {
        super(TESTCOUNT, "EasyHttpRequestTest");
    }

    /**
	 * @param testNumber
	 *            current test number.
	 * @throws Throwable
	 *             test exception.
	 */
    public final void test(final int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testURLProperty();
                break;
            case 1:
                testContentProperty();
                break;
            case 2:
                testHeader();
                break;
            case 3:
                testMethod();
                break;
            default:
                break;
        }
    }

    /**
	 * Test of class EasyHttpRequest.
	 * Method.
	 * @throws AssertionFailedException
	 *             test exception.
	 */
    public final void testMethod() throws AssertionFailedException {
        String method = EasyHttpRequest.POST;
        EasyHttpRequest request = new EasyHttpRequest(null, method, null);
        assertSame(method, request.getHttpMethod());
        method = EasyHttpRequest.GET;
        request.setHttpMethod(method);
        assertSame(method, request.getHttpMethod());
    }

    /**
	 * Test of class EasyHttpRequest.
	 * Header.
	 * @throws AssertionFailedException
	 *             test exception.
	 */
    public final void testHeader() throws AssertionFailedException {
        EasyHttpRequest request = new EasyHttpRequest(null, null, null);
        String value = "value";
        request.setHeader("header", value);
        assertSame(value, request.getHeader("header"));
    }

    /**
	 * Test of class EasyHttpRequest.
	 * Content property.
	 * @throws AssertionFailedException
	 *             test exception.
	 */
    public final void testContentProperty() throws AssertionFailedException {
        byte[] content = new byte[1];
        EasyHttpRequest request = new EasyHttpRequest(null, null, content);
        assertSame(content, request.getRequestContent());
        content = new byte[2];
        request.setRequestContent(content);
        assertSame(content, request.getRequestContent());
    }

    /**
	 * Test of class EasyHttpRequest.
	 * URL property.
	 * @throws AssertionFailedException
	 *             test exception.
	 */
    public final void testURLProperty() throws AssertionFailedException {
        String url = "http://google.com";
        EasyHttpRequest request = new EasyHttpRequest(url, null, null);
        assertSame(url, request.getURL());
    }
}
