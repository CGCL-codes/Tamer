package com.jeantessier.classreader;

import junit.framework.TestCase;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestXMLPrinterEscaping extends TestCase {

    private XMLPrinter printer;

    protected void setUp() throws Exception {
        super.setUp();
        printer = new XMLPrinter(new PrintWriter(new StringWriter()));
    }

    public void testEscapeXMLCharacters_normalCharacters() {
        String testText = "abcdef";
        String expectedText = "abcdef";
        String actualText = printer.escapeXMLCharacters(testText);
        assertEquals("text", expectedText, actualText);
    }

    public void testEscapeXMLCharacters_entities() {
        String testText = "<abc>";
        String expectedText = "&lt;abc&gt;";
        String actualText = printer.escapeXMLCharacters(testText);
        assertEquals("text", expectedText, actualText);
    }

    public void testEscapeXMLCharacters_lowValueCharacters() {
        String testText = "";
        String expectedText = "<![CDATA[&#x5;]]>";
        String actualText = printer.escapeXMLCharacters(testText);
        assertEquals("text", expectedText, actualText);
    }

    public void testEscapeXMLCharacters_highValueCharacters() {
        String testText = "";
        String expectedText = "<![CDATA[&#x80;]]>";
        String actualText = printer.escapeXMLCharacters(testText);
        assertEquals("text", expectedText, actualText);
    }

    public void testEscapeXMLCharacters_veryHighValueCharacters() {
        String testText = "ÿ";
        String expectedText = "<![CDATA[&#xFF;]]>";
        String actualText = printer.escapeXMLCharacters(testText);
        assertEquals("text", expectedText, actualText);
    }
}
