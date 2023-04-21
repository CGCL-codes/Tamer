package org.log5j.format;

import java.util.Properties;
import junit.framework.TestCase;
import org.log5j.Format;

/**
 * Tests the {@link XmlFormat} class.
 * 
 * @author Bruce Ashton
 * @date 2007-12-15
 */
public class XmlFormatTest extends TestCase {

    private Format format;

    public void setUp() {
        Properties properties = new Properties();
        format = new XmlFormat(properties);
    }

    public void testNull() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>null</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (Object[]) null);
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testArrayAloneButCast() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>a</log5j:value>\r?\n\r? +<log5j:value>b</log5j:value>\r?\n\r? +<log5j:value>c</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (Object) new String[] { "a", "b", "c" });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testArrayInList() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>a</log5j:value>\r?\n\r? +<log5j:value>b</log5j:value>\r?\n\r? +<log5j:value>c</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>d</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", new String[] { "a", "b", "c" }, "d");
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testArrayOfArrays() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>a</log5j:value>\r?\n\r? +<log5j:value>b</log5j:value>\r?\n\r? +<log5j:value>c</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>d</log5j:value>\r?\n\r? +<log5j:value>e</log5j:value>\r?\n\r? +<log5j:value>f</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>g</log5j:value>\r?\n\r? +<log5j:value>h</log5j:value>\r?\n\r? +<log5j:value>i</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (Object[]) new String[][] { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testArrayOfArraysCast() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>a</log5j:value>\r?\n\r? +<log5j:value>b</log5j:value>\r?\n\r? +<log5j:value>c</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>d</log5j:value>\r?\n\r? +<log5j:value>e</log5j:value>\r?\n\r? +<log5j:value>f</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>g</log5j:value>\r?\n\r? +<log5j:value>h</log5j:value>\r?\n\r? +<log5j:value>i</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>(?s).";
        String message = format.format("test", "TEST", (Object) new String[][] { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testBoolean() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>true</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", true);
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testByte() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (byte) 1);
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testByteAndString() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r? +<log5j:value>mix</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (byte) 1, "mix");
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    public void testDefaultArrayDepth() {
        String regex4 = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>test</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>(?s).";
        String regex5 = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\\[Ljava\\.lang\\.String;@\\w+</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>(?s).";
        String[][][][] array4 = { new String[][][] { new String[][] { new String[] { "test" } } } };
        String[][][][][] array5 = { new String[][][][] { new String[][][] { new String[][] { new String[] { "test" } } } } };
        String message = format.format("test", "TEST", (Object) array4);
        assertNotNull(message);
        assertTrue(message, message.matches(regex4));
        message = format.format("test", "TEST", (Object) array5);
        assertNotNull(message);
        assertTrue(message, message.matches(regex5));
    }

    /**
     * 
     */
    public void testExceptionFormatted() {
        Properties properties = new Properties();
        properties.setProperty("traceThrowable", "true");
        format = new XmlFormat(properties);
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:exception>\r?\n\r? +<log5j:message>test</log5j:message>\r?\n\r? +<log5j:stacktrace>org\\.log5j\\.format\\.XmlFormatTest\\.testExceptionFormatted\\(XmlFormatTest\\.java:\\d{1,3}\\)</log5j:stacktrace>\r?\n\r?(?s)( +<log5j:stacktrace>.*</log5j:stacktrace>\r?\n\r?)+ +</log5j:exception>\r?\n\r?</log5j:event>\r?\n\r?(?s).*";
        String message = format.format("test", "TEST", new Exception("test"));
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testExceptionUnFormatted() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>java\\.lang\\.Exception: test</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", new Exception("test"));
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testPrimitiveArrayAlone() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r? +<log5j:value>2</log5j:value>\r?\n\r? +<log5j:value>3</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", new int[] { 1, 2, 3 });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testPrimitiveArrayInList() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r? +<log5j:value>2</log5j:value>\r?\n\r? +<log5j:value>3</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>d</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", new int[] { 1, 2, 3 }, "d");
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testPrimitiveArrayOfArrays() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r? +<log5j:value>2</log5j:value>\r?\n\r? +<log5j:value>3</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>4</log5j:value>\r?\n\r? +<log5j:value>5</log5j:value>\r?\n\r? +<log5j:value>6</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>7</log5j:value>\r?\n\r? +<log5j:value>8</log5j:value>\r?\n\r? +<log5j:value>9</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (Object[]) new int[][] { new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 }, new int[] { 7, 8, 9 } });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    /**
     * 
     */
    public void testPrimitiveArrayOfArraysCast() {
        String regex = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[I\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>1</log5j:value>\r?\n\r? +<log5j:value>2</log5j:value>\r?\n\r? +<log5j:value>3</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>4</log5j:value>\r?\n\r? +<log5j:value>5</log5j:value>\r?\n\r? +<log5j:value>6</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"int\">\r?\n\r? +<log5j:value>7</log5j:value>\r?\n\r? +<log5j:value>8</log5j:value>\r?\n\r? +<log5j:value>9</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>\r?\n\r?";
        String message = format.format("test", "TEST", (Object) new int[][] { new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 }, new int[] { 7, 8, 9 } });
        assertNotNull(message);
        assertTrue(message, message.matches(regex));
    }

    public void testSpecifiedArrayDepth() {
        Properties properties = new Properties();
        properties.setProperty("arrayDepth", "2");
        format = new XmlFormat(properties);
        String regex4 = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"java\\.lang\\.String\">\r?\n\r? +<log5j:value>test</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>(?s).";
        String regex5 = "<log5j:event log=\"test\" level=\"TEST\" thread=\"main\" tstamp=\"20\\d{2}[0-1]\\d[0-3]\\dT[0-2]\\d[0-5]\\d[0-5]\\d.\\d{3}[-+][0-1]\\d00\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\r?\n\r? +<log5j:array class=\"\\[Ljava\\.lang\\.String;\">\r?\n\r? +<log5j:value>\\[Ljava\\.lang\\.String;@\\w+</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r? +</log5j:array>\r?\n\r? +</log5j:value>\r?\n\r?</log5j:event>(?s).";
        String[][] array2 = { new String[] { "test" } };
        String[][][] array3 = { new String[][] { new String[] { "test" } } };
        String message = format.format("test", "TEST", (Object) array2);
        assertNotNull(message);
        assertTrue(message, message.matches(regex4));
        message = format.format("test", "TEST", (Object) array3);
        assertNotNull(message);
        assertTrue(message, message.matches(regex5));
    }
}
