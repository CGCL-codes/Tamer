package org.databene.commons.converter;

import java.util.Locale;
import org.databene.commons.LocaleUtil;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link NumberParser}.<br/>
 * <br/>
 * Created at 22.03.2009 08:02:42
 * @since 0.4.9
 * @author Volker Bergmann
 */
public class NumberFormatterTest extends AbstractConverterTest {

    public NumberFormatterTest() {
        super(NumberFormatter.class);
    }

    @Test
    public void testEmpty() {
        NumberFormatter converter = new NumberFormatter();
        assertEquals("", converter.convert(null));
    }

    @Test
    public void testIntegralNumber() {
        NumberFormatter converter = new NumberFormatter();
        assertEquals("-1", converter.convert(-1));
        assertEquals("1000", converter.convert(1000));
    }

    @Test
    public void testConvert_US() {
        LocaleUtil.runInLocale(Locale.US, new Runnable() {

            public void run() {
                checkConversions();
            }
        });
    }

    @Test
    public void testConvert_DE() {
        LocaleUtil.runInLocale(Locale.GERMANY, new Runnable() {

            public void run() {
                checkConversions();
            }
        });
    }

    void checkConversions() {
        NumberFormatter converter = new NumberFormatter();
        assertEquals("0", converter.convert(0.));
        assertEquals("1000", converter.convert(1000.));
        converter.setPattern("0.00");
        assertEquals("0.00", converter.convert(0.));
        converter.setDecimalSeparator(',');
        assertEquals("0,00", converter.convert(0.));
        converter.setPattern("#,##0");
        assertEquals("1,000", converter.convert(1000.));
        converter.setPattern("#,##0.00");
        converter.setGroupingSeparator('.');
        assertEquals("1.000,00", converter.convert(1000.));
    }
}
