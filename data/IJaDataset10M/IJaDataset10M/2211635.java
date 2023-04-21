package java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author Jesse Rosenstock
 * @since 1.4
 */
public abstract class CharsetEncoder {

    private static final int STATE_RESET = 0;

    private static final int STATE_CODING = 1;

    private static final int STATE_END = 2;

    private static final int STATE_FLUSHED = 3;

    private static final byte[] DEFAULT_REPLACEMENT = { (byte) '?' };

    private final Charset charset;

    private final float averageBytesPerChar;

    private final float maxBytesPerChar;

    private byte[] replacement;

    private int state = STATE_RESET;

    private CodingErrorAction malformedInputAction = CodingErrorAction.REPORT;

    private CodingErrorAction unmappableCharacterAction = CodingErrorAction.REPORT;

    protected CharsetEncoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar) {
        this(cs, averageBytesPerChar, maxBytesPerChar, DEFAULT_REPLACEMENT);
    }

    protected CharsetEncoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar, byte[] replacement) {
        if (averageBytesPerChar <= 0.0f) throw new IllegalArgumentException("Non-positive averageBytesPerChar");
        if (maxBytesPerChar <= 0.0f) throw new IllegalArgumentException("Non-positive maxBytesPerChar");
        this.charset = cs;
        this.averageBytesPerChar = averageBytesPerChar;
        this.maxBytesPerChar = maxBytesPerChar;
        this.replacement = replacement;
        implReplaceWith(replacement);
    }

    public final float averageBytesPerChar() {
        return averageBytesPerChar;
    }

    public boolean canEncode(char c) {
        CharBuffer cb = CharBuffer.allocate(1).put(c);
        cb.flip();
        return canEncode(cb);
    }

    public boolean canEncode(CharSequence cs) {
        CharBuffer cb;
        if (cs instanceof CharBuffer) cb = ((CharBuffer) cs).duplicate(); else cb = CharBuffer.wrap(cs);
        return canEncode(cb);
    }

    private boolean canEncode(CharBuffer cb) {
        if (state == STATE_FLUSHED) reset(); else if (state != STATE_RESET) throw new IllegalStateException();
        CodingErrorAction oldMalformedInputAction = malformedInputAction;
        CodingErrorAction oldUnmappableCharacterAction = unmappableCharacterAction;
        try {
            if (oldMalformedInputAction != CodingErrorAction.REPORT) onMalformedInput(CodingErrorAction.REPORT);
            if (oldUnmappableCharacterAction != CodingErrorAction.REPORT) onUnmappableCharacter(CodingErrorAction.REPORT);
        } catch (Exception e) {
            return false;
        } finally {
            if (oldMalformedInputAction != CodingErrorAction.REPORT) onMalformedInput(oldMalformedInputAction);
            if (oldUnmappableCharacterAction != CodingErrorAction.REPORT) onUnmappableCharacter(oldUnmappableCharacterAction);
        }
        return true;
    }

    public final Charset charset() {
        return charset;
    }

    public final ByteBuffer encode(CharBuffer in) throws CharacterCodingException {
        if (state != STATE_RESET) throw new IllegalStateException();
        int remaining = in.remaining();
        int n = (int) (remaining * maxBytesPerChar());
        ByteBuffer out = ByteBuffer.allocate(n);
        if (remaining == 0) {
            state = STATE_FLUSHED;
            return out;
        }
        CoderResult cr = encode(in, out, true);
        if (cr.isError()) cr.throwException();
        cr = flush(out);
        if (cr.isError()) cr.throwException();
        out.flip();
        byte[] resized = new byte[out.remaining()];
        out.get(resized);
        return ByteBuffer.wrap(resized);
    }

    public final CoderResult encode(CharBuffer in, ByteBuffer out, boolean endOfInput) {
        int newState = endOfInput ? STATE_END : STATE_CODING;
        if (state != STATE_RESET && state != STATE_CODING && !(endOfInput && state == STATE_END)) throw new IllegalStateException();
        state = newState;
        for (; ; ) {
            CoderResult cr;
            try {
                cr = encodeLoop(in, out);
            } catch (RuntimeException e) {
                throw new CoderMalfunctionError(e);
            }
            if (cr.isOverflow()) return cr;
            if (cr.isUnderflow()) {
                if (endOfInput && in.hasRemaining()) cr = CoderResult.malformedForLength(in.remaining()); else return cr;
            }
            CodingErrorAction action = cr.isMalformed() ? malformedInputAction : unmappableCharacterAction;
            if (action == CodingErrorAction.REPORT) return cr;
            if (action == CodingErrorAction.REPLACE) {
                if (out.remaining() < replacement.length) return CoderResult.OVERFLOW;
                out.put(replacement);
            }
            in.position(in.position() + cr.length());
        }
    }

    protected abstract CoderResult encodeLoop(CharBuffer in, ByteBuffer out);

    public final CoderResult flush(ByteBuffer out) {
        if (state != STATE_RESET && state != STATE_END) throw new IllegalStateException();
        state = STATE_FLUSHED;
        return implFlush(out);
    }

    protected CoderResult implFlush(ByteBuffer out) {
        return CoderResult.UNDERFLOW;
    }

    protected void implOnMalformedInput(CodingErrorAction newAction) {
    }

    protected void implOnUnmappableCharacter(CodingErrorAction newAction) {
    }

    protected void implReplaceWith(byte[] newReplacement) {
    }

    protected void implReset() {
    }

    public boolean isLegalReplacement(byte[] replacement) {
        CharsetDecoder decoder = charset.newDecoder();
        ByteBuffer bb = ByteBuffer.wrap(replacement);
        CharBuffer cb = CharBuffer.allocate((int) (replacement.length * decoder.maxCharsPerByte()));
        return !decoder.decode(bb, cb, true).isError();
    }

    public CodingErrorAction malformedInputAction() {
        return malformedInputAction;
    }

    public final float maxBytesPerChar() {
        return maxBytesPerChar;
    }

    public final CharsetEncoder onMalformedInput(CodingErrorAction newAction) {
        if (newAction == null) throw new IllegalArgumentException("Null action");
        malformedInputAction = newAction;
        implOnMalformedInput(newAction);
        return this;
    }

    public CodingErrorAction unmappableCharacterAction() {
        return unmappableCharacterAction;
    }

    public final CharsetEncoder onUnmappableCharacter(CodingErrorAction newAction) {
        if (newAction == null) throw new IllegalArgumentException("Null action");
        unmappableCharacterAction = newAction;
        implOnUnmappableCharacter(newAction);
        return this;
    }

    public final byte[] replacement() {
        return replacement;
    }

    public final CharsetEncoder replaceWith(byte[] newReplacement) {
        if (newReplacement == null) throw new IllegalArgumentException("Null replacement");
        if (newReplacement.length == 0) throw new IllegalArgumentException("Empty replacement");
        if (!isLegalReplacement(newReplacement)) throw new IllegalArgumentException("Illegal replacement");
        this.replacement = newReplacement;
        implReplaceWith(newReplacement);
        return this;
    }

    public final CharsetEncoder reset() {
        state = STATE_RESET;
        implReset();
        return this;
    }
}
