package net.sf.transmorgify.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestTransmorgifyUtil {

    @Test
    public void xmlEncodeEmptyStringShouldDoNothing() {
        String emptyString = "";
        assertEquals(emptyString, TransmorgifyUtil.xmlEncode(emptyString));
    }

    @Test
    public void xmlEncodeNoSpecialCharactersShouldDoNothing() {
        String string = "blah blah 123";
        assertEquals(string, TransmorgifyUtil.xmlEncode(string));
    }

    @Test
    public void xmlEncodeGreaterThan() {
        assertEquals("x &#62; 54", TransmorgifyUtil.xmlEncode("x > 54"));
    }

    @Test
    public void xmlEncodeLessThan() {
        assertEquals("x &#60; 54", TransmorgifyUtil.xmlEncode("x < 54"));
    }

    @Test
    public void xmlEncodeAmpersand() {
        assertEquals("you &#38; me", TransmorgifyUtil.xmlEncode("you & me"));
    }
}
