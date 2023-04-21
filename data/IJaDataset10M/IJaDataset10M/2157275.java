package cpdetector.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Locale;

/**
 * <p>
 * A dummy charset that indicates an unknown (undetected) Charset.
 * </p>
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class UnknownCharset extends Charset {

    public UnknownCharset(String canonicalName, String[] aliases) {
        super("unknown", aliases);
    }

    public boolean contains(Charset cs) {
        return false;
    }

    public CharsetDecoder newDecoder() {
        return null;
    }

    public CharsetEncoder newEncoder() {
        return null;
    }

    public String displayName() {
        return super.displayName();
    }

    public String displayName(Locale locale) {
        return super.displayName(locale);
    }
}
