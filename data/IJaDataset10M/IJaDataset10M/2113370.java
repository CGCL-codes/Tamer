package net.sourceforge.domian.specification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class DateStringSpecificationTest {

    static final String nullString = null;

    static final String emptyString = "";

    static final String blankString = "   ";

    static final String whitespaceString = "\t\r\n";

    static final String nonNumericDateString1 = "ddMMyyyy";

    static final String nonNumericDateString2 = "01MM2007";

    static final String nonNumericDateString3 = "0101yyyy";

    static final String nonNumericDateString4 = "0a0b200c";

    static final String nonNumericDateString5 = "010b200c";

    static final String nonNumericDateString6 = "0a012007";

    static final String nonNumericDateString7 = "0101200c";

    static final String nonNumericDateString8 = "0101.2007";

    static final String tooShortDateString = "0101200";

    static final String tooLongDateString = "010120000";

    static final String highDateString1 = "32032007";

    static final String highDateString2 = "31042007";

    static final String highMonthString = "01132007";

    static final String illegalLeapYearDateString = "29022007";

    static final String okDateString = "01012007";

    @Test
    public void shouldNotAcceptNullDatePattern() {
        try {
            new DateStringSpecification(nullString);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Date pattern parameter cannot be null";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldNotAcceptEmptyDatePattern() {
        try {
            new DateStringSpecification(emptyString);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Date pattern parameter cannot be empty";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldNotAcceptBlankDatePattern() {
        try {
            new DateStringSpecification(blankString);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Date pattern parameter cannot be blank";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldNotAcceptIllegalDatePattern() {
        try {
            new DateStringSpecification("abc");
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "\"abc\" is not a valid date pattern";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testSpecificationType() {
        Specification<String> spec = new DateStringSpecification(okDateString);
        assertSame(String.class, spec.getType());
        assertSame(String.class, new DateStringSpecification(okDateString).getType());
    }

    @Test
    public void testGetDatePattern() {
        DateStringSpecification spec = new DateStringSpecification("ddMMyyyy");
        assertSame(String.class, spec.getType());
        assertEquals("ddMMyyyy", spec.getValue());
    }

    @Test
    public void testDateStringSpecification() {
        Specification<String> spec = new DateStringSpecification("ddMMyyyy");
        assertFalse(spec.isSatisfiedBy(nullString));
        assertFalse(spec.isSatisfiedBy(emptyString));
        assertFalse(spec.isSatisfiedBy(blankString));
        assertFalse(spec.isSatisfiedBy(whitespaceString));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString1));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString2));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString3));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString4));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString5));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString6));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString7));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString8));
        assertFalse(spec.isSatisfiedBy(tooShortDateString));
        assertFalse(spec.isSatisfiedBy(tooLongDateString));
        assertFalse(spec.isSatisfiedBy(highDateString1));
        assertFalse(spec.isSatisfiedBy(highDateString2));
        assertFalse(spec.isSatisfiedBy(highMonthString));
        assertFalse(spec.isSatisfiedBy(illegalLeapYearDateString));
        assertTrue(spec.isSatisfiedBy(okDateString));
    }

    @Test
    public void testDateStringSpecificationWithDatePatternContainingCharacters() {
        Specification<String> spec = new DateStringSpecification("yyyy.MM.dd");
        assertFalse(spec.isSatisfiedBy(nullString));
        assertFalse(spec.isSatisfiedBy(emptyString));
        assertFalse(spec.isSatisfiedBy(blankString));
        assertFalse(spec.isSatisfiedBy(whitespaceString));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString1));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString2));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString3));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString4));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString5));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString6));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString7));
        assertFalse(spec.isSatisfiedBy(nonNumericDateString8));
        assertFalse(spec.isSatisfiedBy(tooShortDateString));
        assertFalse(spec.isSatisfiedBy(tooLongDateString));
        assertFalse(spec.isSatisfiedBy(highDateString1));
        assertFalse(spec.isSatisfiedBy(highDateString2));
        assertFalse(spec.isSatisfiedBy(highMonthString));
        assertFalse(spec.isSatisfiedBy(illegalLeapYearDateString));
        assertTrue(spec.isSatisfiedBy("2007.10.25"));
        spec = new DateStringSpecification("yyyy-MM-dd");
        assertTrue(spec.isSatisfiedBy("2007-10-25"));
        spec = new DateStringSpecification("yyyy/MM/dd");
        assertTrue(spec.isSatisfiedBy("2007/10/25"));
    }

    @Test
    public void testDateStringSpecification_YearFrom1To9() {
        Specification<String> spec = new DateStringSpecification("y.MM.dd");
        assertFalse(spec.isSatisfiedBy("0.10.25"));
        assertTrue(spec.isSatisfiedBy("2.10.25"));
    }

    public void XtestDateStringSpecification_YearFrom10To99() {
        Specification<String> spec = new DateStringSpecification("yyyy/MM/dd");
        assertFalse(spec.isSatisfiedBy("99.10.25"));
        assertTrue(spec.isSatisfiedBy("99/10/25"));
        spec = new DateStringSpecification("yy/MM/dd");
        assertFalse(spec.isSatisfiedBy("99.10.25"));
        assertTrue(spec.isSatisfiedBy("99/10/25"));
        spec = new DateStringSpecification("yy/MM/dd");
        assertFalse(spec.isSatisfiedBy("0099.10.25"));
        assertTrue(spec.isSatisfiedBy("0099/10/25"));
    }

    @Test
    public void testDateStringSpecification_YearFrom100To999() {
        Specification<String> spec = new DateStringSpecification("yyy.MM.dd");
        assertTrue(spec.isSatisfiedBy("200.10.25"));
    }

    @Test
    public void testDateStringSpecification_YearFrom10000To99999() {
        Specification<String> spec = new DateStringSpecification("yyyyy-MM-dd");
        assertTrue(spec.isSatisfiedBy("10000-10-25"));
    }
}
