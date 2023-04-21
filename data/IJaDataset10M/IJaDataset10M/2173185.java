package org.opencms.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * Tests for the OpenCms locale manager.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 6.0.0
 */
public class TestCmsLocaleManager extends TestCase {

    /**
     * Tests selection of the default locale.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testDefaultLocaleSelection() throws Exception {
        CmsLocaleManager localeManager = new CmsLocaleManager();
        List available = new ArrayList();
        localeManager.addDefaultLocale(Locale.US.toString());
        localeManager.addDefaultLocale(Locale.UK.toString());
        localeManager.addDefaultLocale(Locale.GERMANY.toString());
        localeManager.addDefaultLocale(Locale.ENGLISH.toString());
        localeManager.addDefaultLocale(Locale.GERMAN.toString());
        available.add(Locale.GERMAN);
        available.add(Locale.US);
        Locale result = localeManager.getBestMatchingLocale(Locale.GERMAN, localeManager.getDefaultLocales(), available);
        assertEquals(Locale.GERMAN, result);
        result = localeManager.getBestMatchingLocale(Locale.GERMANY, localeManager.getDefaultLocales(), available);
        assertEquals(Locale.GERMAN, result);
        result = localeManager.getBestMatchingLocale(Locale.FRENCH, localeManager.getDefaultLocales(), available);
        assertEquals(Locale.US, result);
    }
}
