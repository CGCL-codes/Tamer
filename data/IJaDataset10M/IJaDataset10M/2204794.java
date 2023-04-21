package net.frontlinesms.ui.i18n;

import java.util.Locale;
import net.frontlinesms.junit.BaseTestCase;

/**
 * Unit tests for {@link CountryCallingCode}.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class CountryCallingCodeTest extends BaseTestCase {

    public void testValidity() {
        for (CountryCallingCode code : CountryCallingCode.values()) {
            assertTrue("Country code was made of characters other than digits: " + code + " (+" + code.getCountryCode() + "))", code.getCountryCode().matches("\\d+"));
        }
    }

    public void testIsInInternationalFormat() {
        assertTrue(CountryCallingCode.isInInternationalFormat("+15559999"));
        assertTrue(CountryCallingCode.isInInternationalFormat("+336123456789"));
        assertTrue(CountryCallingCode.isInInternationalFormat("+447762258741"));
        assertFalse(CountryCallingCode.isInInternationalFormat("0612215656"));
        assertFalse(CountryCallingCode.isInInternationalFormat("00336123456"));
        assertFalse(CountryCallingCode.isInInternationalFormat("+1-(555)-9999"));
        assertFalse(CountryCallingCode.isInInternationalFormat("+44(0)7762975852"));
    }

    public void testFormat() {
        assertEquals("+15559999", CountryCallingCode.format("1-(555)-9999", Locale.US.getCountry()));
        assertEquals("+44712345678", CountryCallingCode.format("0712345678", Locale.UK.getCountry()));
        assertEquals("+15559999", CountryCallingCode.format("555-9999", Locale.US.getCountry()));
        assertEquals("+336123456789", CountryCallingCode.format("06123456789", Locale.FRANCE.getCountry()));
    }
}
