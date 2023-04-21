package dnl.games.stratego.server;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class CDC {

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

    protected static ByteBuffer encodeObject(Object obj) {
        return null;
    }

    /**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s
	 *            the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
    protected static ByteBuffer encodeString(String s) {
        try {
            return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new Error("Required character set " + MESSAGE_CHARSET + " not found", e);
        }
    }

    /**
	 * Decodes a message into a {@code String}.
	 * 
	 * @param message
	 *            the message to decode
	 * @return the decoded string
	 */
    protected static String decodeString(ByteBuffer message) {
        try {
            byte[] bytes = new byte[message.remaining()];
            message.get(bytes);
            return new String(bytes, MESSAGE_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new Error("Required character set " + MESSAGE_CHARSET + " not found", e);
        }
    }
}
