package com.google.gwt.safehtml.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT Unit tests for {@link SafeUriHostedModeUtils}.
 */
public class GwtSafeUriHostedModeUtilsTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.gwt.safehtml.SafeHtmlTestsModule";
    }

    public void testIsValidUriCharset() {
        if (GWT.isProdMode()) {
            return;
        }
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(""));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset("blah"));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset("blah<>foo"));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset("blah%foo"));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset("blah%25foo"));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.CONSTANT_URL));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.MAILTO_URL));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.EMPTY_GIF_DATA_URL));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.LONG_DATA_URL));
        assertTrue(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.JAVASCRIPT_URL));
        assertFalse(SafeUriHostedModeUtils.isValidUriCharset(GwtUriUtilsTest.INVALID_URL_UNPAIRED_SURROGATE));
    }

    public void testMaybeCheckValidUri() {
        if (GWT.isProdMode()) {
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.INVALID_URL_UNPAIRED_SURROGATE);
        } else {
            SafeUriHostedModeUtils.maybeCheckValidUri("");
            SafeUriHostedModeUtils.maybeCheckValidUri("blah");
            SafeUriHostedModeUtils.maybeCheckValidUri("blah<>foo");
            SafeUriHostedModeUtils.maybeCheckValidUri("blah%foo");
            SafeUriHostedModeUtils.maybeCheckValidUri("blah%25foo");
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.CONSTANT_URL);
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.MAILTO_URL);
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.EMPTY_GIF_DATA_URL);
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.LONG_DATA_URL);
            SafeUriHostedModeUtils.maybeCheckValidUri(GwtUriUtilsTest.JAVASCRIPT_URL);
            assertCheckValidUriFails(GwtUriUtilsTest.INVALID_URL_UNPAIRED_SURROGATE);
            assertCheckValidUriFails("http://");
            if (GWT.isClient()) {
                SafeUriHostedModeUtils.maybeCheckValidUri(GWT.getModuleBaseURL());
                SafeUriHostedModeUtils.maybeCheckValidUri(GWT.getHostPageBaseURL());
            }
        }
    }

    private void assertCheckValidUriFails(String uri) {
        try {
            SafeUriHostedModeUtils.maybeCheckValidUri(uri);
        } catch (IllegalArgumentException e) {
            return;
        } catch (AssertionError e) {
            return;
        }
        fail("maybeCheckValidUri failed to throw exception for: " + uri);
    }
}
