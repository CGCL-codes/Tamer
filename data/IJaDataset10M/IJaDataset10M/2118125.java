package org.bcholmes.jmicro.util.text;

import java.util.Locale;
import org.bcholmes.jmicro.util.text.LocaleHelper;
import junit.framework.TestCase;

public class LocaleHelperTest extends TestCase {

    public void testShouldDecodeLocale() throws Exception {
        assertEquals("kreyol", new Locale("ht"), LocaleHelper.decode("ht"));
    }

    public void testShouldDecodeLocaleWithCountry() throws Exception {
        assertEquals("Canadian French", new Locale("fr", "CA"), LocaleHelper.decode("fr_CA"));
    }

    public void testShouldEncodeLocaleWithCountry() throws Exception {
        assertEquals("Canadian French", "fr_CA", LocaleHelper.encode(new Locale("fr", "CA")));
    }
}
