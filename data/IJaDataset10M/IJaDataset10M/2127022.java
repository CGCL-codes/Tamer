package com.google.gwt.i18n.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests the LocaleInfo class and the associated generator.
 */
public class LocaleInfoTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.gwt.i18n.I18NTest";
    }

    public void testAvailableLocales() {
        String[] locales = LocaleInfo.getAvailableLocaleNames();
        assertArrayEquals(new String[] { "default", "piglatin", "piglatin_UK", "piglatin_UK_WINDOWS" }, locales);
    }

    public void testCookieName() {
        String cookieName = LocaleInfo.getCurrentLocale().getLocaleCookieName();
        assertNull(cookieName);
    }

    public void testCurrentLocale() {
        String locale = LocaleInfo.getCurrentLocale().getLocaleName();
        assertEquals("piglatin_UK_WINDOWS", locale);
    }

    public void testNativeDisplayNames() {
        String displayName = LocaleInfo.getLocaleNativeDisplayName("en");
        assertNull(displayName);
        displayName = LocaleInfo.getLocaleNativeDisplayName("piglatin");
        assertEquals("Igpay Atinlay", displayName);
    }

    public void testQueryParam() {
        String queryParam = LocaleInfo.getCurrentLocale().getLocaleQueryParam();
        assertEquals("locale", queryParam);
    }

    public void testRTL() {
        boolean isRTL = LocaleInfo.getCurrentLocale().isRTL();
        assertFalse(isRTL);
        boolean hasRTL = LocaleInfo.hasAnyRTL();
        assertFalse(hasRTL);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
