package br.com.devx.scenery;

import br.com.devx.scenery.parser.ParseException;
import br.com.devx.test.JUnitHelper;
import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

public class TemplateFormatStrategyTest extends TestCase {

    public TemplateFormatStrategyTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testMappedTypes() {
        TemplateFormatStrategy formatStrategy = new TemplateFormatStrategy();
        assertNull(formatStrategy.format(null, null));
        assertEquals("Hello, world!", formatStrategy.format(null, "Hello, world!"));
        assertEquals("123", formatStrategy.format(null, new Integer(123)));
        assertEquals(JUnitHelper.doubleString(456.0), formatStrategy.format(null, new Double(456.00)));
        assertEquals(JUnitHelper.doubleString(456.4), formatStrategy.format(null, new Double(456.40)));
        assertEquals(JUnitHelper.doubleString(456.45), formatStrategy.format(null, new Double(456.45)));
        assertEquals(JUnitHelper.doubleString(456.456), formatStrategy.format(null, new Double(456.456)));
        assertEquals(JUnitHelper.dateString(1972, 6, 20), formatStrategy.format(null, new java.sql.Date(new GregorianCalendar(1972, 6, 20).getTime().getTime())));
        assertEquals(JUnitHelper.dateString(1972, 6, 20, 2, 15, 4), formatStrategy.format(null, new java.sql.Timestamp(new GregorianCalendar(1972, 6, 20, 2, 15, 4).getTime().getTime())));
        assertEquals(JUnitHelper.dateString(1972, 6, 20), formatStrategy.format(null, new GregorianCalendar(1972, 6, 20).getTime()));
    }

    public void testUnmappedTypes() {
        TemplateFormatStrategy templateFormatStrategy = new TemplateFormatStrategy();
        HashMap value = new HashMap();
        assertSame(value, templateFormatStrategy.format("unammed", value));
    }

    public void testCustomTemplateFormatStrategy() {
        TemplateFormatStrategy customFormatStrategy = new CustomTemplateFormatStrategy();
        assertEquals("(int) 123", customFormatStrategy.format("unammed", new Integer(123)));
        Date dt = new GregorianCalendar(1972, 6, 20).getTime();
        assertEquals(customFormatStrategy.formatDateTime("unammed", dt), customFormatStrategy.format("unammed", dt));
        assertEquals(customFormatStrategy.formatNumber("unammed", new Double(123.456)), customFormatStrategy.format("unammed", new Double(123.456)));
        assertEquals(customFormatStrategy.formatNumber("percentField", new Double(1.235)), customFormatStrategy.format("percentField", new Double(1.23456)));
        assertEquals(customFormatStrategy.formatNumber("otherField", new Double(123.456)), customFormatStrategy.format("otherField", new Double(123.456)));
    }

    public void testFormatStrategyDescriptor() throws ParseException, IOException {
        String src = "bigNumber = 123456.789;\n" + "defaultNumber = 123.456;\n" + "integerNumber = 123.456;\n" + "currencyNumber = 123.456;\n" + "percentNumber = 123.456;\n" + "patternNumber = 123.456;\n" + "shortDate = 2002-10-20 15:30;\n" + "mediumDate = 2002-10-20 15:30;\n" + "longDate = 2002-10-20 15:30;\n" + "fullDate = 2002-10-20 15:30;\n" + "patternDate = 2002-10-20 15:30;\n" + "shortTime = 2002-10-20 15:30;\n" + "mediumTime = 2002-10-20 15:30;\n" + "longTime = 2002-10-20 15:30;\n" + "fullTime = 2002-10-20 15:30;\n" + "patternTime = 2002-10-20 15:30;\n" + "defaultDateTime = 2002-10-20 15:30;\n" + "shortDateTime = 2002-10-20 15:30;\n" + "mediumDateTime = 2002-10-20 15:30;\n" + "longDateTime = 2002-10-20 15:30;\n" + "fullDateTime = 2002-10-20 15:30;\n" + "patternDateTime = 2002-10-20 15:30;\n" + "choiceValue = 3;";
        String format = "integerNumber = integer\n" + "currencyNumber = currency\n" + "percentNumber = percent\n" + "patternNumber = 0.00E00\n" + "shortDate = short\n" + "mediumDate = medium\n" + "longDate = long\n" + "fullDate = full\n" + "patternDate = dd/MM yyyy\n" + "shortTime = ; short\n" + "mediumTime = ; medium\n" + "longTime = ; long\n" + "fullTime = ; full\n" + "patternTime = HH:mm:ss.SS\n" + "shortDateTime = short; short\n" + "mediumDateTime = short; medium\n" + "longDateTime = short; long\n" + "fullDateTime = long; full\n" + "patternDateTime = dd/MM yy HH:mm:ss.SS\n" + "choice/choiceValue = -1#is negative| 0#is zero or fraction | 1#is one |1.0<is 1+ |2#is two |2<is more than 2.\n";
        Properties formatProperties = new Properties();
        formatProperties.load(new ByteArrayInputStream(format.getBytes()));
        TemplateAdapter templateAdapter = TemplateAdapter.load(new StringReader(src));
        templateAdapter.setFormatStrategy(new TemplateFormatStrategy(formatProperties));
        assertEquals(NumberFormat.getInstance().format(123456.789), templateAdapter.adapt("bigNumber"));
        assertEquals(NumberFormat.getInstance().format(123.456), templateAdapter.adapt("defaultNumber"));
        assertEquals(fmt(123.456, "###"), templateAdapter.adapt("integerNumber"));
        assertEquals(NumberFormat.getCurrencyInstance().format(123.456), templateAdapter.adapt("currencyNumber"));
        assertEquals(NumberFormat.getPercentInstance().format(123.456), templateAdapter.adapt("percentNumber"));
        assertEquals(fmt(123.456, "0.00E00"), templateAdapter.adapt("patternNumber"));
        Date dt = new GregorianCalendar(2002, 9, 20, 15, 30).getTime();
        assertEquals(DateFormat.getDateInstance(DateFormat.SHORT).format(dt), templateAdapter.adapt("shortDate"));
        assertEquals(DateFormat.getDateInstance(DateFormat.MEDIUM).format(dt), templateAdapter.adapt("mediumDate"));
        assertEquals(DateFormat.getDateInstance(DateFormat.FULL).format(dt), templateAdapter.adapt("fullDate"));
        assertEquals(new SimpleDateFormat("dd/MM yyyy").format(dt), templateAdapter.adapt("patternDate"));
        assertEquals(DateFormat.getTimeInstance(DateFormat.SHORT).format(dt), templateAdapter.adapt("shortTime"));
        assertEquals(DateFormat.getTimeInstance(DateFormat.MEDIUM).format(dt), templateAdapter.adapt("mediumTime"));
        assertEquals(DateFormat.getTimeInstance(DateFormat.FULL).format(dt), templateAdapter.adapt("fullTime"));
        assertEquals(new SimpleDateFormat("HH:mm:ss.SS").format(dt), templateAdapter.adapt("patternTime"));
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT).format(dt), templateAdapter.adapt("defaultDateTime"));
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(dt), templateAdapter.adapt("shortDateTime"));
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(dt), templateAdapter.adapt("mediumDateTime"));
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.FULL).format(dt), templateAdapter.adapt("fullDateTime"));
        assertEquals(new SimpleDateFormat("dd/MM yy HH:mm:ss.SS").format(dt), templateAdapter.adapt("patternDateTime"));
        assertEquals("is more than 2.", templateAdapter.adapt("choiceValue"));
    }

    private String fmt(double value, String pattern) {
        NumberFormat nf = new DecimalFormat(pattern);
        String result = nf.format(value);
        return result;
    }

    private static class CustomTemplateFormatStrategy extends TemplateFormatStrategy {

        protected String formatNumber(String fieldName, Number value) {
            if (value instanceof Integer) {
                return "(int) " + super.formatNumber(fieldName, value);
            } else if ("percentField".equals(fieldName)) {
                NumberFormat nf = NumberFormat.getPercentInstance();
                nf.setMinimumFractionDigits(1);
                nf.setMaximumFractionDigits(1);
                return nf.format(value);
            } else {
                return super.formatNumber(fieldName, value);
            }
        }

        protected String formatDateTime(String fieldName, Date value) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
            return df.format(value);
        }
    }
}
