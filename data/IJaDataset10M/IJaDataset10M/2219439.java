package net.sf.mogbox.pol.ffxi.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class ShiftJISFFXICharset extends Charset {

    private static final String NAME = "x-Shift_JIS:FFXI";

    private static final String[] ALIASES = { "Shift_JIS:FFXI", "shift_jis:ffxi", "SJIS:FFXI", "sjis:ffxi", "X-Shift_JIS:FFXI", "x-shift_jis:ffxi", "x-sjis:ffxi", "ms_kanji:ffxi", "csShiftJIS:FFXI" };

    public ShiftJISFFXICharset() {
        super(NAME, ALIASES);
    }

    @Override
    public boolean contains(Charset cs) {
        if (cs instanceof ShiftJISFFXICharset) return true;
        String name = cs.name();
        if (name.equals("US-ASCII")) return true;
        if (name.equals("Shift_JIS")) return true;
        if (name.equals("JIS_X0201")) return true;
        if (name.equals("x-JIS0208")) return true;
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new ShiftJISFFXICharsetDecoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new ShiftJISFFXICharsetEncoder(this);
    }
}
