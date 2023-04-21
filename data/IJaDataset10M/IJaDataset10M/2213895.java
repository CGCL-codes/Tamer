package org.zkoss.zss.engine.fn;

import org.zkoss.xel.fn.CommonFns;
import junit.framework.TestCase;

public class TextDataFnsTest extends TestCase {

    public TextDataFnsTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTextAsc() {
    }

    public void testTextBahtText() {
    }

    public void testTextChar() {
        Double[] d = { new Double(33.0) };
        char e = '!';
        assertEquals(e, CommonFns.toChar(TextDataFns.textChar(d, null)));
    }

    public void testTextClean() {
    }

    public void testTextCode() {
        Character[] o = { new Character('A') };
        int e = 65;
        assertEquals(e, CommonFns.toNumber(TextDataFns.textCode(o, null)).intValue());
    }

    public void testTextConcatenate() {
        String[] s = { "Stream population for ", "brook trout", " ", "species", " is ", "32", "/mile" };
        String e = "Stream population for brook trout species is 32/mile";
        assertEquals(e, TextDataFns.textConcatenate(s, null));
    }

    public void testTextDollar() {
        Double[] d = { new Double(1234.567), new Double(2) };
        String e = "$1,234.57";
        assertEquals(e, TextDataFns.textDollar(d, null));
        Double[] d1 = { new Double(1234.567), new Double(-2) };
        String e1 = "$1,200";
        assertEquals(e1, TextDataFns.textDollar(d1, null));
        Double[] d2 = { new Double(-1234.567), new Double(-2) };
        String e2 = "$1,200";
        assertEquals(e2, TextDataFns.textDollar(d2, null));
        Double[] d3 = { new Double(-0.123), new Double(4) };
        String e3 = "$0.1230";
        assertEquals(e3, TextDataFns.textDollar(d3, null));
        Object[] d4 = { new Double(99.888) };
        String e4 = "$99.89";
        assertEquals(e4, TextDataFns.textDollar(d4, null));
    }

    public void testTextExact() {
        String[] s = { "test123", "test123" };
        boolean e = true;
        assertEquals(e, CommonFns.toBoolean(TextDataFns.textExact(s, null)));
        String[] s1 = { "test123", "tes t123" };
        boolean e1 = false;
        assertEquals(e1, CommonFns.toBoolean(TextDataFns.textExact(s1, null)));
        String[] s2 = { "test123", "teSt123" };
        boolean e2 = false;
        assertEquals(e2, CommonFns.toBoolean(TextDataFns.textExact(s2, null)));
    }

    public void testTextFind() {
        String[] o = { "M", "Miriam McGovern" };
        int e = 1;
        assertEquals(e, CommonFns.toInt(TextDataFns.textFind(o, null)));
        String[] o1 = { "m", "Miriam McGovern" };
        int e1 = 6;
        assertEquals(e1, CommonFns.toInt(TextDataFns.textFind(o1, null)));
        Object[] o2 = { "M", "Miriam McGovern", new Integer(3) };
        int e2 = 8;
        assertEquals(e2, CommonFns.toInt(TextDataFns.textFind(o2, null)));
    }

    public void testTextFindB() {
    }

    public void testTextFixed() {
        Double[] d = { new Double(1234.567), new Double(1) };
        String e = "1,234.6";
        assertEquals(e, TextDataFns.textFixed(d, null));
        Double[] d1 = { new Double(1234.567), new Double(-1) };
        String e1 = "1,230";
        assertEquals(e1, TextDataFns.textFixed(d1, null));
        Object[] o2 = { new Double(-1234.567), new Double(-1), "TRUE" };
        String e2 = "-1230";
        assertEquals(e2, TextDataFns.textFixed(o2, null));
        Double[] d3 = { new Double(44.332) };
        String e3 = "44.33";
        assertEquals(e3, TextDataFns.textFixed(d3, null));
        Double[] d4 = { new Double(44.335) };
        String e4 = "44.34";
        assertEquals(e4, TextDataFns.textFixed(d4, null));
    }

    public void testTextJis() {
    }

    public void testTextLeft() {
        Object[] o = { "Sale Price", new Integer(4) };
        String e = "Sale";
        assertEquals(e, TextDataFns.textLeft(o, null));
        String[] s1 = { "Sweden" };
        String e1 = "S";
        assertEquals(e1, TextDataFns.textLeft(s1, null));
    }

    public void testTextLeftB() {
    }

    public void testTextLen() {
        String[] s = { "Phoenix, AZ" };
        int e = 11;
        assertEquals(e, CommonFns.toInt(TextDataFns.textLen(s, null)));
        String[] s1 = { "Phoenix, AZ" };
        int e1 = 11;
        assertEquals(e1, CommonFns.toInt(TextDataFns.textLen(s1, null)));
        String[] s2 = { "   One  " };
        int e2 = 8;
        assertEquals(e2, CommonFns.toInt(TextDataFns.textLen(s2, null)));
    }

    public void testTextLenB() {
    }

    public void testTextLower() {
        String[] s = { "E. E. Cummings" };
        String e = "e. e. cummings";
        assertEquals(e, TextDataFns.textLower(s, null));
        String[] s1 = { "Apt. 2B" };
        String e1 = "apt. 2b";
        assertEquals(e1, TextDataFns.textLower(s1, null));
    }

    public void testTextMid() {
        Object[] o = { "Fluid Flow", new Integer(1), new Integer(5) };
        String e = "Fluid";
        assertEquals(e, TextDataFns.textMid(o, null));
        Object[] o1 = { "Fluid Flow", new Integer(7), new Integer(20) };
        String e1 = "Flow";
        assertEquals(e1, TextDataFns.textMid(o1, null));
        Object[] o2 = { "Fluid Flow", new Integer(20), new Integer(5) };
        String e2 = "";
        assertEquals(e2, TextDataFns.textMid(o2, null));
    }

    public void testTextMidB() {
    }

    public void testTextPhonetic() {
    }

    public void testTextProper() {
        String[] s = { "this is a TITLE" };
        String e = "This Is A Title";
        assertEquals(e, TextDataFns.textProper(s, null));
        String[] s1 = { "2-cent's worth" };
        String e1 = "2-Cent'S Worth";
        assertEquals(e1, TextDataFns.textProper(s1, null));
        String[] s2 = { "76BudGet" };
        String e2 = "76Budget";
        assertEquals(e2, TextDataFns.textProper(s2, null));
    }

    public void testTextReplace() {
        Object[] o = { "abcdefghijk", new Integer(6), new Integer(5), "*" };
        String e = "abcde*k";
        assertEquals(e, TextDataFns.textReplace(o, null));
        Object[] o1 = { "2009", new Integer(3), new Integer(2), "10" };
        String e1 = "2010";
        assertEquals(e1, TextDataFns.textReplace(o1, null));
        Object[] o2 = { "123456", new Integer(1), new Integer(3), "@" };
        String e2 = "@456";
        assertEquals(e2, TextDataFns.textReplace(o2, null));
    }

    public void testTextReplaceB() {
    }

    public void testTextRept() {
        Object[] o = { "*-", new Integer(3) };
        String e = "*-*-*-";
        assertEquals(e, TextDataFns.textRept(o, null));
        Object[] o1 = { "-", new Integer(10) };
        String e1 = "----------";
        assertEquals(e1, TextDataFns.textRept(o1, null));
    }

    public void testTextRight() {
        Object[] o = { "Sale Price", new Integer(5) };
        String e = "Price";
        assertEquals(e, TextDataFns.textRight(o, null));
        String[] s1 = { "Stock Number" };
        String e1 = "r";
        assertEquals(e1, TextDataFns.textRight(s1, null));
    }

    public void testTextRightB() {
    }

    public void testTextSearch() {
    }

    public void testTextSearchB() {
    }

    public void testTextSubstitute() {
        String[] s = { "Sales Data", "Sales", "Cost" };
        String e = "Cost Data";
        assertEquals(e, TextDataFns.textSubstitute(s, null));
        Object[] s1 = { "Quarter 1, 2008", "1", "2", new Integer(1) };
        String e1 = "Quarter 2, 2008";
        assertEquals(e1, TextDataFns.textSubstitute(s1, null));
        Object[] o2 = { "Quarter 1, 2011", "1", "2", new Integer(3) };
        String e2 = "Quarter 1, 2012";
        assertEquals(e2, TextDataFns.textSubstitute(o2, null));
    }

    public void testTextT() {
        String[] s = { "Rainfall" };
        String e = "Rainfall";
        assertEquals(e, TextDataFns.textT(s, null));
        Integer[] i1 = { new Integer(19) };
        String e1 = "";
        assertEquals(e1, TextDataFns.textT(i1, null));
        Boolean[] b2 = { new Boolean(true) };
        String e2 = "";
        assertEquals(e2, TextDataFns.textT(b2, null));
    }

    public void testTextText() {
        Object[] o = { new Double(2800), "$0.00" };
        String e = "$2800.00";
        assertEquals(e, TextDataFns.textText(o, null));
        Object[] o1 = { new Double(0.4), "0%" };
        String e1 = "40%";
        assertEquals(e1, TextDataFns.textText(o1, null));
    }

    public void testTextTrim() {
        String[] s = { "    First   Quarter Earnings   " };
        String e = "First Quarter Earnings";
        assertEquals(e, TextDataFns.textTrim(s, null));
    }

    public void testTextUpper() {
        String[] s = { "total" };
        String e = "TOTAL";
        assertEquals(e, TextDataFns.textUpper(s, null));
        String[] s1 = { "Yield" };
        String e1 = "YIELD";
        assertEquals(e1, TextDataFns.textUpper(s1, null));
    }

    public void testTextValue() {
    }
}
