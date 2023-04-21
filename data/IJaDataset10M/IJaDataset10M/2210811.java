package org.research.sdbx.text;

import java.text.Format;
import java.text.NumberFormat;
import junit.framework.TestCase;

public class StringFormatUnitTest extends TestCase {

    private String ssnSchema = "###-##-####";

    private String ssnFormatted = "123-45-6789";

    private String ssnFormattedBad = "123-45-2";

    private String ssnUnFormatted = "123456789";

    private String ssnUnFormattedBad = "1234567";

    private StringFormat sf;

    protected void setUp() throws Exception {
        sf = new StringFormat(ssnSchema);
    }

    public void testFormat() {
        String ssn = sf.format(this.ssnUnFormatted);
        assertEquals(this.ssnFormatted, ssn);
    }

    public void testUnformat() {
        String ssn = sf.unformat(this.ssnFormatted);
        assertEquals(this.ssnUnFormatted, ssn);
    }

    public void testFormatMultipleTimes() {
        testFormat();
        testFormat();
    }

    public void testFormatWithBadValue() {
        try {
            String ssn = sf.format(this.ssnUnFormattedBad);
        } catch (StringFormatException e) {
            assertEquals("Failed to format rawValue=" + this.ssnUnFormattedBad + " to schema=" + this.ssnSchema, e.getMessage());
        }
    }

    public void testIntToChar() throws Exception {
        int x = 65;
        String myString = NumberFormat.getInstance().format(x);
        System.out.println("" + myString);
    }
}
