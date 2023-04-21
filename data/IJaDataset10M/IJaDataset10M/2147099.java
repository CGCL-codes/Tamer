package em.sm.util;

/** Encoders and decoders for java primitives and byte arrays.
 *
 * @author Eugene Morozov
 * @version 0.9.0
 */
public class EncDec {

    /** Fills the <code>length</code> bytes of the array
     * <code>bytes</code> starting at <code>bytesOffset</code> with the
     * <code>content</code> value.
     *
     * Note: there is no <code>Arrays</code> class in the J2ME, so fillBytes
     *      should be implemented.
     *
     * @param content The content to fill the byte array with.
     * @param bytes The bytes to be filled.
     * @param bytesOffset The offset
     * @param length The ammount of bytes to be filled
     */
    public static final void fillBytes(byte content, byte[] bytes, int bytesOffset, int length) {
        for (int i = 0; i < length; i++) {
            bytes[bytesOffset + i] = content;
        }
    }

    /** Encodes int to byte array using the given offset.
     *
     * @param integer The integer to be encoded.
     * @param bytes Thebyte array to encode to.
     * @param bytesOffset The offset to start the encoding.
     */
    public static final void encodeIntegerToBytes(int integer, byte[] bytes, int bytesOffset) {
        bytes[bytesOffset] = (byte) (integer >> 24);
        bytes[bytesOffset + 1] = (byte) (integer >> 16);
        bytes[bytesOffset + 2] = (byte) (integer >> 8);
        bytes[bytesOffset + 3] = (byte) (integer);
    }

    /** Encodes the <code>int[]</code> <code>into bytes[]<code>.
     *
     * @param integers The int array to be encoded.
     * @param integersOffset The int[] offset
     * @param length Number of items from int[] to be encoded.
     * @param bytes The byte[] to encode into.
     * @param bytesOffset The byte[] offset.
     */
    public static final void encodeIntegersToBytes(int[] integers, int integersOffset, int length, byte[] bytes, int bytesOffset) {
        for (int i = 0; i < length; i++) {
            encodeIntegerToBytes(integers[integersOffset + i], bytes, bytesOffset + (i << 2));
        }
    }

    /** Encodes the byte array to integer.
     * Note: implementation doesn't check for ArrayIndexOutOfBounds
     *
     * @param bytes The byte[] to be encoded.
     * @param bytesOffset The byte[] offset.
     * @return The int representation of byte[].
     */
    public static final int encodeBytesToInteger(byte[] bytes, int bytesOffset) {
        return ((bytes[bytesOffset]) << 24) + ((bytes[bytesOffset + 1] & 0xff) << 16) + ((bytes[bytesOffset + 2] & 0xff) << 8) + (bytes[bytesOffset + 3] & 0xff);
    }

    /** Encodes the byte[] to int[].
     *
     * @param bytes The byte[] to be encoded.
     * @param bytesOffset The byte[] offset.
     * @param integers The int[] to encode into.
     * @param integersOffset The int[] offset.
     * @param length The number of items of int[] to be encoded.
     */
    public static final void encodeBytesToIntegers(byte[] bytes, int bytesOffset, int[] integers, int integersOffset, int length) {
        for (int i = 0; i < length; i++) {
            integers[integersOffset + 1] = encodeBytesToInteger(bytes, bytesOffset + (i << 2));
        }
    }

    /** Encodes long to byte[].
     *
     * @param longValue The long to be encoded.
     * @param bytes The byte[] to encode into.
     * @param bytesOffset The byte[] offset.
     */
    public static final void encodeLongToBytes(long longValue, byte[] bytes, int bytesOffset) {
        bytes[bytesOffset] = (byte) (longValue >> 56);
        bytes[bytesOffset + 1] = (byte) (longValue >> 48);
        bytes[bytesOffset + 2] = (byte) (longValue >> 40);
        bytes[bytesOffset + 3] = (byte) (longValue >> 32);
        bytes[bytesOffset + 4] = (byte) (longValue >> 24);
        bytes[bytesOffset + 5] = (byte) (longValue >> 16);
        bytes[bytesOffset + 6] = (byte) (longValue >> 8);
        bytes[bytesOffset + 7] = (byte) (longValue);
    }

    /** Encodes byte[] to long.
     *
     * @param bytes The byte[] to be encoded.
     * @param bytesOffset The byte[] offset.
     * @return The encoded long value.
     */
    public static final long encodeBytesToLong(byte[] bytes, int bytesOffset) {
        return ((bytes[bytesOffset] & 0xffL) << 56) | ((bytes[bytesOffset + 1] & 0xffL) << 48) | ((bytes[bytesOffset + 2] & 0xffL) << 40) | ((bytes[bytesOffset + 3] & 0xffL) << 32) | ((bytes[bytesOffset + 4] & 0xffL) << 24) | ((bytes[bytesOffset + 5] & 0xffL) << 16) | ((bytes[bytesOffset + 6] & 0xffL) << 8) | (bytes[bytesOffset + 7] & 0xffL);
    }

    /** Encodes the bytes to the Hex String.
     * Used for the key convertion in the UI.
     *
     * @param bytes The byte[] to be encoded.
     * @return The encoded String.
     */
    public static final String encodeToHexString(byte[] bytes) {
        StringBuffer result = new StringBuffer(32);
        int bytesLength = bytes.length;
        for (int i = 0; i < bytesLength; i++) {
            result.append(Integer.toString((bytes[i] >> 4) & 0x0f, 16));
            result.append(Integer.toString(bytes[i] & 0x0f, 16));
        }
        return result.toString();
    }

    /** Decodes the Hex string into the byte array.
     * Used for the key conversion in the UI.
     *
     * @param string The String to be decoded.
     * @param bytes The byte[] to decode into.
     */
    public static final void decodeHexString(String string, byte[] bytes) {
        int length = Math.min(string.length(), bytes.length * 2);
        int index;
        byte charValue;
        for (int i = 0; i < length; i++) {
            index = i / 2;
            charValue = (byte) Character.digit(string.charAt(i), 16);
            bytes[index] = (byte) (bytes[index] | ((charValue < 0) ? 0 : charValue << (((i + 1) % 2) * 4)));
        }
    }

    /** Removes non-numric characters from the phone number.
     *
     * @param address The address string to process
     * @return The processed address string.
     */
    public static final String normalizeAddressString(String address) {
        int addressLength = address.length();
        StringBuffer resultStringBuffer = new StringBuffer(addressLength);
        char ch;
        for (int i = 0; i < addressLength; i++) {
            ch = address.charAt(i);
            if (Character.isDigit(ch)) {
                resultStringBuffer.append(ch);
            }
        }
        return resultStringBuffer.toString();
    }
}
