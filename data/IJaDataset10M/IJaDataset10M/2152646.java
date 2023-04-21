package net.laubenberger.bogatyr.helper.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.UnsupportedEncodingException;
import net.laubenberger.bogatyr.AllBogatyrTests;
import net.laubenberger.bogatyr.helper.HelperArray;
import net.laubenberger.bogatyr.helper.HelperString;
import net.laubenberger.bogatyr.misc.Constants;
import org.junit.Test;

/**
 *  JUnit test for {@link EncoderBase64}
 *
 * @author Stefan Laubenberger
 * @version 20101227
 */
public class EncoderBase64Test {

    @Test
    public void testPassEncodeAndDecodeString() {
        String encoded = EncoderBase64.encode(HelperString.EMPTY_STRING);
        byte[] decoded = EncoderBase64.decode(encoded);
        try {
            assertEquals(HelperString.EMPTY_STRING, new String(decoded, Constants.ENCODING_UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        encoded = EncoderBase64.encode(AllBogatyrTests.DATA);
        decoded = EncoderBase64.decode(encoded);
        try {
            assertEquals(AllBogatyrTests.DATA, new String(decoded, Constants.ENCODING_UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPassEncodeAndDecodeByteArray() {
        char[] encoded = EncoderBase64.encode(HelperArray.EMPTY_ARRAY_BYTE);
        byte[] decoded = EncoderBase64.decode(encoded);
        assertArrayEquals(HelperArray.EMPTY_ARRAY_BYTE, decoded);
        encoded = EncoderBase64.encode(AllBogatyrTests.DATA.getBytes());
        decoded = EncoderBase64.decode(encoded);
        assertArrayEquals(AllBogatyrTests.DATA.getBytes(), decoded);
    }

    @Test
    public void testFailEncode() {
        try {
            EncoderBase64.encode((byte[]) null);
            fail("input is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.encode((String) null);
            fail("input is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.encode(null, Constants.ENCODING_UTF8);
            fail("input is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.encode(AllBogatyrTests.DATA, "blabla");
            fail("unsupported encoding");
        } catch (UnsupportedEncodingException e) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.encode(AllBogatyrTests.DATA, HelperString.EMPTY_STRING);
            fail("encoding is empty");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.encode(AllBogatyrTests.DATA, null);
            fail("encoding is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testFailDecode() {
        try {
            EncoderBase64.decode((char[]) null);
            fail("input is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            EncoderBase64.decode((String) null);
            fail("input is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
