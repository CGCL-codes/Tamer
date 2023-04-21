package info.metlos.jdc.nmdc.helper;

/**
 * Static helper class containing helper methods for formatting and de/encoding
 * messages.
 * 
 * @author metlos
 * 
 * @version $Id: MessageFormatUtil.java 237 2008-09-28 17:03:21Z metlos $
 */
public class MessageFormatUtil {

    private MessageFormatUtil() {
    }

    /**
	 * NMDC messages can contain parts that need to be escaped to be correctly
	 * handled:
	 * <ol>
	 * <li>$ -> &amp;#36;
	 * <li>| -> &amp;#124;
	 * <li>& -> &amp;amp;
	 * </ol>
	 * 
	 * @param string
	 *            the string to escape
	 * @return the escaped string
	 */
    public static String escape(String string) {
        return string.replace("&", "&amp;").replace("|", "&#124;").replace("$", "&#36;");
    }

    /**
	 * The reverse of the {@link #escape(String)} method.
	 * 
	 * @param string
	 *            the strign to unescape
	 * @return the unescaped string
	 */
    public static String unescape(String string) {
        return string.replace("&#36;", "$").replace("&#124;", "|").replace("&amp;", "&");
    }

    /**
	 * Searches for a byte in the byte array
	 * 
	 * @param b
	 *            the byte
	 * @param seq
	 *            the byte array
	 * @return index of the byte b or -1 if not found
	 */
    public static int indexOf(byte b, byte[] seq) {
        return MessageFormatUtil.indexOf(b, seq, 0, seq.length);
    }

    /**
	 * Searches for a byte in the byte array starting at position startAt
	 * 
	 * @param b
	 *            the byte
	 * @param seq
	 *            the byte array
	 * @param startAt
	 *            the position to start searching
	 * @return index of the byte b or -1 if not found
	 */
    public static int indexOf(byte b, byte[] seq, int startAt) {
        return MessageFormatUtil.indexOf(b, seq, startAt, seq.length);
    }

    /**
	 * Searches for a byte in the byte array between the two given indices.
	 * 
	 * @param b
	 *            the byte
	 * @param seq
	 *            the byte array
	 * @param startAt
	 *            the position to start searching
	 * @param endAt
	 *            the position to end the search before
	 * @return index of the byte b or -1 if not found
	 */
    public static int indexOf(byte b, byte[] seq, int startAt, int endAt) {
        for (int i = startAt; i < endAt; i++) {
            if (seq[i] == b) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Finds the first b1 or b2 in seq whichever comes first.
	 * 
	 * @param b1
	 *            the first byte to look for
	 * @param b2
	 *            the second byte to look for
	 * @param seq
	 *            the byte array to look in
	 * @param startAt
	 *            index to start searching the array from
	 * @param endAt
	 *            the index to finish searching the array before
	 * @return the index of first occurence of either b1 or b2 in seq or -1 if
	 *         not found
	 */
    public static int indexOf(byte b1, byte b2, byte[] seq, int startAt, int endAt) {
        for (int i = startAt; i < endAt; i++) {
            byte s = seq[i];
            if (s == b1 || s == b2) {
                return i;
            }
        }
        return -1;
    }
}
