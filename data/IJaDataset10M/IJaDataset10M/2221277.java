package snipsnap.util.jdbc;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * The UIDGenerator generates worldwide unique strings
 * for identifying purposes. The length of this
 * string is 55 chars.
 *
 * @author Frank Mueller
 * @version $Id: UIDGenerator.java,v 1.2 2003/12/11 13:36:54 leo Exp $
 */
public final class UIDGenerator {

    public static final int MAX_LENGTH = 55;

    private static final String ZEROS = "0000000000000";

    private static final int ZEROSLENGTH = ZEROS.length();

    private static long detail = normalize((new Random()).nextLong());

    /**
   * Generates a UID string.
   *
   * @param aCode A classification code.
   * @return The UID string.
   */
    public static final String generate(long aCode) {
        long code = normalize(aCode);
        long ip = getIP();
        long timestamp = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        buffer.append(createString(code));
        buffer.append("-");
        buffer.append(createString(ip));
        buffer.append("-");
        buffer.append(createString(timestamp));
        buffer.append("-");
        buffer.append(createString(detail));
        return buffer.toString().toUpperCase();
    }

    /**
   * Generates a UID string for a given class.
   *
   * @param aClass The class specifying the classification code.
   * @return The UID string.
   */
    public static final String generate(Class aClass) {
        return UIDGenerator.generate(aClass.hashCode());
    }

    /**
   * Gets the IP address of the local host as a long.
   *
   * @return The IP address as a long.
   */
    private static final long getIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            BigInteger bigIP = new BigInteger(localHost.getAddress());
            return normalize(bigIP.longValue());
        } catch (UnknownHostException uhex) {
            return 0;
        }
    }

    /**
   * Normalizes a long value.
   *
   * @param value The original value.
   * @return The normalized value.
   */
    private static final long normalize(long value) {
        if (value > 0) return value;
        if (value == Long.MIN_VALUE) return Long.MAX_VALUE; else return value * -1;
    }

    /**
   * Creates a fixed length string out of a long value.
   *
   * @param value The long value.
   * @return The fixed length string.
   */
    private static final String createString(long value) {
        String baseString = Long.toString(value, 36);
        String result = ZEROS.substring(0, ZEROSLENGTH - baseString.length()) + baseString;
        return result;
    }
}
