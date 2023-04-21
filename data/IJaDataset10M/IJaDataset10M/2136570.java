package com.jawise.serviceadapter.test;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class TestNvpToSoapAdapter extends TestCase {

    protected TestFixture serviceAdapterTestFixture;

    private static final Logger logger = Logger.getLogger(TestNvpToSoapAdapter.class);

    private static final String SERVICE_URL = "http://localhost:8080/serviceadapter/process/adapter?userid=sathyan&service=RamsBookStoreService&messageconversion=booksearchconverter";

    private static final String SERVICE_URL2 = "http://localhost:8080/serviceadapter/process/adapter?userid=sathyan&service=RamsBookStoreService&messageconversion=bookpriceconverter";

    protected void setUp() throws Exception {
        super.setUp();
        serviceAdapterTestFixture = new TestFixture(this);
        serviceAdapterTestFixture.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        serviceAdapterTestFixture.tearDown();
        serviceAdapterTestFixture = null;
    }

    public void testBookSearch() {
        try {
            HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
            WebConversation wc = new WebConversation();
            PostMethodWebRequest postReq = new PostMethodWebRequest(SERVICE_URL);
            postReq.setParameter("Tittle", "");
            postReq.setParameter("Aurther", "");
            postReq.setParameter("Isbn", "1234-5678-6645-1");
            WebResponse res = null;
            try {
                res = wc.getResponse(postReq);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                fail(ex.getMessage());
            }
            logger.debug(res.getText());
            assertTrue(res.getResponseCode() == 200);
            Map<String, String> parseResponse = parseResponse(res.getText());
            assertTrue("1234-5678-6645-1".equals(parseResponse.get("ISBN")));
            assertTrue(parseResponse.get("Author") != null);
            assertTrue("Using XFire".equals(parseResponse.get("Title")));
            HttpUnitOptions.reset();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testBookSearchOptionalParamter() {
        try {
            HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
            WebConversation wc = new WebConversation();
            PostMethodWebRequest postReq = new PostMethodWebRequest(SERVICE_URL);
            postReq.setParameter("Tittle", "");
            postReq.setParameter("Aurther", "");
            WebResponse res = null;
            try {
                res = wc.getResponse(postReq);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                fail(ex.getMessage());
            }
            logger.debug(res.getText());
            assertTrue(res.getResponseCode() == 200);
            Map<String, String> parseResponse = parseResponse(res.getText());
            assertTrue(res.getText().indexOf(" messages part Isbn not optional") >= 0);
            HttpUnitOptions.reset();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testOptionParamter() {
        try {
            HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
            WebConversation wc = new WebConversation();
            PostMethodWebRequest postReq = new PostMethodWebRequest(SERVICE_URL2);
            postReq.setParameter("Isbn", "1234-5678-6645-1");
            WebResponse res = null;
            try {
                res = wc.getResponse(postReq);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                fail(ex.getMessage());
            }
            logger.debug(res.getText());
            assertTrue(res.getResponseCode() == 200);
            Map<String, String> parseResponse = parseResponse(res.getText());
            assertTrue("89.00".equals(parseResponse.get("Price")));
            HttpUnitOptions.reset();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testMissingNoneOptionalParmeter1() {
        try {
            HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
            WebConversation wc = new WebConversation();
            PostMethodWebRequest postReq = new PostMethodWebRequest(SERVICE_URL);
            postReq.setParameter("Tittle", "");
            postReq.setParameter("Aurther", "");
            WebResponse res = null;
            try {
                res = wc.getResponse(postReq);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                fail(ex.getMessage());
            }
            logger.debug(res.getText());
            assertTrue(res.getText().indexOf("message conversion exception, messages part Isbn not optional") >= 0);
            HttpUnitOptions.reset();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testInvalidRecursiveValues() {
    }

    public void testCallWithInvalidScript() {
        try {
            HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
            WebConversation wc = new WebConversation();
            PostMethodWebRequest postReq = new PostMethodWebRequest(SERVICE_URL2);
            postReq.setParameter("Isbn", "error");
            WebResponse res = null;
            try {
                res = wc.getResponse(postReq);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                fail(ex.getMessage());
            }
            logger.debug(res.getText());
            assertTrue(res.getResponseCode() == 200);
            assertTrue(res.getText().indexOf("error occurred evaluating the script") >= 0);
            assertTrue(res.getText().indexOf("Arithemetic Exception") >= 0);
            HttpUnitOptions.reset();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    protected Map<String, String> parseResponse(String text) {
        Map<String, String> res = new HashMap<String, String>();
        StringTokenizer tokens = new StringTokenizer(text, "&");
        while (tokens.hasMoreTokens()) {
            String t = tokens.nextToken();
            if (!t.isEmpty()) {
                try {
                    int midPos = t.indexOf("=");
                    String key = t.substring(0, midPos);
                    String val = t.substring(midPos + 1, t.length());
                    res.put(key, val);
                } catch (RuntimeException e) {
                }
            }
        }
        return res;
    }
}
