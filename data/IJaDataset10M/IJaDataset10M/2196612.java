package com.w20e.socrates.util;

import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author dokter
 * Unit test for locale utility.
 */
public class TestLocaleUtility extends TestCase {

    public void testGetLocale() {
        String localeStr = null;
        assertNull(LocaleUtility.getLocale(localeStr));
        localeStr = "";
        assertNull(LocaleUtility.getLocale(localeStr));
        localeStr = "nl_";
        assertEquals(new Locale("nl"), LocaleUtility.getLocale(localeStr));
        localeStr = "nl_NL";
        assertEquals(LocaleUtility.getLocale(localeStr), new Locale("NL", "nl"));
        localeStr = "nl_NL_informal";
        assertEquals(LocaleUtility.getLocale(localeStr), new Locale("NL", "nl", "informal"));
    }
}
