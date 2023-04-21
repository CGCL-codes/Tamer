package tests.api.java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnmappableCharacterException;

/**
 * test case specific activity of iso-8859-1 charset encoder
 */
public class ISOCharsetEncoderTest extends CharsetEncoderTest {

    private static final Charset CS = Charset.forName("iso-8859-1");

    protected void setUp() throws Exception {
        cs = CS;
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCanEncodeCharSequence() {
        assertTrue(encoder.canEncode("w"));
        assertFalse(encoder.canEncode("슣"));
        assertFalse(encoder.canEncode("𐀀"));
        try {
            encoder.canEncode(null);
        } catch (NullPointerException e) {
        }
        assertTrue(encoder.canEncode(""));
    }

    public void testCanEncodeICUBug() {
        assertFalse(encoder.canEncode((char) '?'));
        assertFalse(encoder.canEncode((String) "?"));
    }

    public void testCanEncodechar() throws CharacterCodingException {
        assertTrue(encoder.canEncode('w'));
        assertFalse(encoder.canEncode('슣'));
    }

    public void testSpecificDefaultValue() {
        assertEquals(1, encoder.averageBytesPerChar(), 0.001);
        assertEquals(1, encoder.maxBytesPerChar(), 0.001);
    }

    CharBuffer getMalformedCharBuffer() {
        return CharBuffer.wrap("? buffer");
    }

    CharBuffer getUnmapCharBuffer() {
        return CharBuffer.wrap("𐀀 buffer");
    }

    CharBuffer getExceptionCharBuffer() {
        return null;
    }

    protected byte[] getIllegalByteArray() {
        return null;
    }

    public void testMultiStepEncode() throws CharacterCodingException {
        encoder.onMalformedInput(CodingErrorAction.REPORT);
        encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            encoder.encode(CharBuffer.wrap("𐀀"));
            fail("should unmappable");
        } catch (UnmappableCharacterException e) {
        }
        encoder.reset();
        ByteBuffer out = ByteBuffer.allocate(10);
        assertTrue(encoder.encode(CharBuffer.wrap("?"), out, true).isMalformed());
        encoder.flush(out);
        encoder.reset();
        out = ByteBuffer.allocate(10);
        assertSame(CoderResult.UNDERFLOW, encoder.encode(CharBuffer.wrap("?"), out, false));
        assertTrue(encoder.encode(CharBuffer.wrap("?"), out, true).isMalformed());
    }
}
