package sun.security.ssl;

/**
 * Type safe enum for an SSL/TLS protocol version. Instances are obtained
 * using the static factory methods or by referencing the static members
 * in this class. Member variables are final and can be accessed without
 * accessor methods.
 *
 * There is only ever one instance per supported protocol version, this
 * means == can be used for comparision instead of equals() if desired.
 *
 * Checks for a particular version number should generally take this form:
 *
 * if (protocolVersion.v >= ProtocolVersion.TLS10) {
 *   // TLS 1.0 code goes here
 * } else {
 *   // SSL 3.0 code here
 * }
 *
 * @author  Andreas Sterbenz
 * @since   1.4.1
 */
public final class ProtocolVersion {

    static final ProtocolVersion NONE = new ProtocolVersion(-1, "NONE");

    static final ProtocolVersion SSL20Hello = new ProtocolVersion(0x0002, "SSLv2Hello");

    static final ProtocolVersion SSL30 = new ProtocolVersion(0x0300, "SSLv3");

    static final ProtocolVersion TLS10 = new ProtocolVersion(0x0301, "TLSv1");

    static final ProtocolVersion TLS11 = new ProtocolVersion(0x0302, "TLSv1.1");

    private static final boolean FIPS = SunJSSE.isFIPS();

    static final ProtocolVersion MIN = FIPS ? TLS10 : SSL30;

    static final ProtocolVersion MAX = TLS10;

    static final ProtocolVersion DEFAULT = TLS10;

    static final ProtocolVersion DEFAULT_HELLO = FIPS ? TLS10 : SSL20Hello;

    public final int v;

    public final byte major, minor;

    final String name;

    private ProtocolVersion(int v, String name) {
        this.v = v;
        this.name = name;
        major = (byte) (v >>> 8);
        minor = (byte) (v & 0xff);
    }

    private static ProtocolVersion valueOf(int v) {
        if (v == SSL30.v) {
            return SSL30;
        } else if (v == TLS10.v) {
            return TLS10;
        } else if (v == TLS11.v) {
            return TLS11;
        } else if (v == SSL20Hello.v) {
            return SSL20Hello;
        } else {
            int major = (v >>> 8) & 0xff;
            int minor = v & 0xff;
            return new ProtocolVersion(v, "Unknown-" + major + "." + minor);
        }
    }

    /**
     * Return a ProtocolVersion with the specified major and minor version
     * numbers. Never throws exceptions.
     */
    public static ProtocolVersion valueOf(int major, int minor) {
        major &= 0xff;
        minor &= 0xff;
        int v = (major << 8) | minor;
        return valueOf(v);
    }

    /**
     * Return a ProtocolVersion for the given name.
     *
     * @exception IllegalArgumentException if name is null or does not
     * identify a supported protocol
     */
    static ProtocolVersion valueOf(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Protocol cannot be null");
        }
        if (FIPS) {
            if (name.equals(TLS10.name)) {
                return TLS10;
            } else {
                throw new IllegalArgumentException("Only TLS 1.0 allowed in FIPS mode");
            }
        }
        if (name.equals(SSL30.name)) {
            return SSL30;
        } else if (name.equals(TLS10.name)) {
            return TLS10;
        } else if (name.equals(SSL20Hello.name)) {
            return SSL20Hello;
        } else {
            throw new IllegalArgumentException(name);
        }
    }

    public String toString() {
        return name;
    }
}
