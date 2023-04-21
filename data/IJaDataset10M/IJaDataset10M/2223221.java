package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.text.NumberFormat;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-07 19:48:36
 * @version 1.0
 * @since 1.0
 */
public class TestDecimalFormatter {

    @Before
    public void init() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @After
    public void destroy() {
        System.clearProperty("localeprovide.locale");
    }

    @Test
    public void simpleDecimalParseFormat() {
        String testDouble = "12 314,43";
        Formatter<Double> formatter = Formatter.getFormatter(Double.class);
        Double aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatEmptyCharacters() {
        String testDouble = "12 314,43 ";
        Formatter<Double> formatter = Formatter.getFormatter(Double.class);
        Double aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatDuplicateDecimalCharacters() {
        String testDouble = "12 314,43,";
        Formatter<Double> formatter = Formatter.getFormatter(Double.class);
        Double aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatBadPlacedGroupingSeparator() {
        String testDouble = "1 12 314,43";
        Formatter<Double> formatter = Formatter.getFormatter(Double.class);
        Double aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }

    @Test
    public void simpleDecimalParseFormatSeveralGroupingSeparator() {
        String testDouble = "11 123 314,43";
        Formatter<Double> formatter = Formatter.getFormatter(Double.class);
        Double aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }

    @Test
    public void testAllLocalesDecimalParseFormat() {
        Locale[] locales = NumberFormat.getAvailableLocales();
        double myNumber = -1234.56;
        System.clearProperty("localeprovide.locale");
        for (int i = 0; i < locales.length; ++i) {
            Locale.setDefault(locales[i]);
            Formatter<Double> formatter = Formatter.getFormatter(Double.class);
            String aStringDouble = formatter.convertToString(myNumber);
            char badchar = ' ';
            assertFalse("Language " + locales[i].getLanguage() + "_" + locales[i].getCountry(), aStringDouble.indexOf(badchar) != -1);
        }
    }
}
