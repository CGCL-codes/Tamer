package net.sf.asyncobjects.net.protocols.lines;

import java.io.UnsupportedEncodingException;
import net.sf.asyncobjects.io.BinaryData;

/**
 * This class provides utilities that work with raw line representations.
 * 
 * @author const
 */
public class LineUtils {

    /** name of US-ASCII encoding */
    private static final String US_ASCII = "US-ASCII";

    /** carrige return symbol */
    public static final byte CR = '\r';

    /** line feed symbol */
    public static final byte LF = '\n';

    /** line feed sequence */
    public static final BinaryData CRLF = BinaryData.fromBytes(CR, LF);

    /**
	 * Convert raw line representation to the string.
	 * 
	 * @param rawLine
	 *            a raw line
	 * @param encoding
	 *            an encoding
	 * @return A string line representation.
	 */
    public static String asStringLine(BinaryData rawLine, String encoding) {
        byte data[] = rawLine.toArray();
        int i;
        loop: for (i = data.length - 1; i > 0; i--) {
            switch(data[i]) {
                case CR:
                case LF:
                    break;
                default:
                    break loop;
            }
        }
        try {
            return new String(data, 0, i + 1, encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Invalid encoding: " + encoding);
        }
    }

    /**
	 * Convert line to ascii representaiton
	 * 
	 * @param rawLine
	 *            a raw line
	 * @return converted line
	 */
    public static String asASCIILine(BinaryData rawLine) {
        return asStringLine(rawLine, US_ASCII);
    }

    /**
	 * Convert string to ASCII data
	 * 
	 * @param text
	 *            a text to convert
	 * @return a binary data
	 */
    public static BinaryData toASCIIData(String text) {
        try {
            return BinaryData.fromBytes(text.getBytes(US_ASCII));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to find ascii encoding", ex);
        }
    }
}
