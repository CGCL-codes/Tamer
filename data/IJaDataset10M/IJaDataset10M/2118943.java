package org.jazzteam.edu.numberToString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import org.jazzteam.edu.numberToString.localization.ILanguageLocalizator;
import org.jazzteam.edu.numberToString.localization.LanguageLocalizatorFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
@RunWith(Parameterized.class)
public class ConverterTestToWords {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String LOCALE = "LOCALE";

    private final String path;

    public ConverterTestToWords(String path) {
        this.path = path;
    }

    @Test
    public void checkResult() throws Exception {
        final Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        final LocalizationSupportConverter converter = new LocalizationSupportConverter();
        final String locale = properties.getProperty(LOCALE);
        final ILanguageLocalizator localizator = LanguageLocalizatorFactory.getInstance().getLocalizator(new Locale(locale));
        converter.setLocalizator(localizator);
        final Set<String> names = properties.stringPropertyNames();
        final List<Throwable> throwables = new ArrayList<Throwable>();
        for (String name : names) {
            final String value = properties.getProperty(name);
            try {
                final long number = Long.parseLong(name);
                try {
                    assertEquals("Trying to convert " + number, converter.toWords(number), value);
                } catch (AssertionError a) {
                    throwables.add(a);
                }
            } catch (NumberFormatException nfe) {
            }
        }
        if (!throwables.isEmpty()) {
            String result = "Problems during checking test data " + path + LINE_SEPARATOR;
            for (Throwable throwable : throwables) {
                result += throwable.getMessage() + LINE_SEPARATOR;
            }
            fail(result);
        }
    }

    @Parameters
    public static Collection inputParams() throws Exception {
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        params.add(new Object[] { "test/testData/englishTestData.properties" });
        params.add(new Object[] { "test/testData/germanTestData.properties" });
        return params;
    }
}
