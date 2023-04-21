package org.apache.xml.serialize;

import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Provides information about encodings. Depends on the Java runtime
 * to provides writers for the different encodings, but can be used
 * to override encoding names and provide the last printable character
 * for each encoding.
 *
 * @version $Id: Encodings.java 317383 2001-07-20 20:37:06Z elena $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 */
public class Encodings {

    /**
     * The last printable character for unknown encodings.
     */
    static final int DefaultLastPrintable = 0x7F;

    /**
     * @param encoding a MIME charset name, or null.
     */
    static EncodingInfo getEncodingInfo(String encoding) {
        if (encoding == null) return new EncodingInfo(null, DefaultLastPrintable);
        for (int i = 0; i < _encodings.length; i++) {
            if (_encodings[i].name.equalsIgnoreCase(encoding)) return _encodings[i];
        }
        return new SieveEncodingInfo(encoding, DefaultLastPrintable);
    }

    static final String JIS_DANGER_CHARS = "\\~¢£¥¬" + "—―‖…‾‾∥∯〜" + "＼～￠￡￢￣";

    /**
     * Constructs a list of all the supported encodings.
     */
    private static final EncodingInfo[] _encodings = new EncodingInfo[] { new EncodingInfo("ASCII", 0x7F), new EncodingInfo("US-ASCII", 0x7F), new EncodingInfo("ISO-8859-1", 0xFF), new EncodingInfo("ISO-8859-2", 0xFF), new EncodingInfo("ISO-8859-3", 0xFF), new EncodingInfo("ISO-8859-4", 0xFF), new EncodingInfo("ISO-8859-5", 0xFF), new EncodingInfo("ISO-8859-6", 0xFF), new EncodingInfo("ISO-8859-7", 0xFF), new EncodingInfo("ISO-8859-8", 0xFF), new EncodingInfo("ISO-8859-9", 0xFF), new EncodingInfo("UTF-8", "UTF8", 0x10FFFF), new SieveEncodingInfo("Shift_JIS", "SJIS", 0x7F, JIS_DANGER_CHARS), new SieveEncodingInfo("Windows-31J", "MS932", 0x7F, JIS_DANGER_CHARS), new SieveEncodingInfo("EUC-JP", null, 0x7F, JIS_DANGER_CHARS), new SieveEncodingInfo("ISO-2022-JP", null, 0x7F, JIS_DANGER_CHARS) };
}
