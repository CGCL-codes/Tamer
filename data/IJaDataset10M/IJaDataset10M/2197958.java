package jaxlib.net.ntp;

import java.net.ProtocolException;
import jaxlib.array.CharArrays;
import jaxlib.lang.Chars;
import jaxlib.lang.Ints;
import jaxlib.lang.Longs;
import jaxlib.util.CheckArg;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: NtpPacket.java 2614 2008-06-04 11:57:08Z joerg_wassmer $
 */
public final class NtpPacket extends Object {

    private static final int VERSION = 3;

    private static final int MODE_INDEX = 0;

    private static final int VERSION_INDEX = 0;

    private static final int VERSION_SHIFT = 3;

    private static final int LI_INDEX = 0;

    private static final int LI_SHIFT = 6;

    private static final int STRATUM_INDEX = 1;

    private static final int POLL_INDEX = 2;

    private static final int PRECISION_INDEX = 3;

    private static final int ROOT_DELAY_INDEX = 4;

    private static final int ROOT_DISPERSION_INDEX = 8;

    private static final int REFERENCE_ID_INDEX = 12;

    private static final int REFERENCE_TIMESTAMP_INDEX = 16;

    private static final int ORIGINATE_TIMESTAMP_INDEX = 24;

    private static final int RECEIVE_TIMESTAMP_INDEX = 32;

    static final int TRANSMIT_TIMESTAMP_INDEX = 40;

    private static NtpTimestamp readTimestamp(final byte[] a, final int index) {
        final long ntp = Longs.fromBytes(a, index);
        return (ntp == 0) ? null : NtpTimestamp.ofNtp(ntp);
    }

    static final byte[] createRequest() {
        final byte[] a = new byte[48];
        a[MODE_INDEX] = (byte) (a[MODE_INDEX] & 0xf8 | NtpMode.CLIENT.ordinal() & 0x7);
        a[VERSION_INDEX] = (byte) (a[VERSION_INDEX] & 0xc7 | ((VERSION & 0x7) << VERSION_SHIFT));
        return a;
    }

    public final NtpLeapIndicator leapIndicator;

    public final NtpMode mode;

    /**
   * The originate time as defined in RFC-1305.
   */
    public final NtpTimestamp originTime;

    /**
   * Poll interval as defined in RFC-1305, which is an eight-bit
   * signed integer indicating the maximum interval between successive
   * messages, in seconds to the nearest power of two (e.g. value of six
   * indicates an interval of 64 seconds. The values that can appear in
   * this field range from NTP_MINPOLL to NTP_MAXPOLL inclusive.
   */
    public final byte poll;

    /**
   * The precision as defined in RFC-1305 encoded as an 8-bit signed
   * integer (seconds to nearest power of two).
   * Values normally range from -6 to -20.
   */
    public final byte precision;

    /**
   * Receive timestamp as defined in RFC-1305.
   */
    public final NtpTimestamp receiveTime;

    /***
   * Reference id as defined in RFC-1305, which is
   * an unsigned 32-bit integer whose value is dependent on several criteria.
   */
    public final int reference;

    /**
   * The reference time as defined in RFC-1305.
   */
    public final NtpTimestamp referenceTime;

    /***
   * Root delay as defined in RFC-1305, which is the total roundtrip delay
   * to the primary reference source, in seconds. Values can take positive and
   * negative values, depending on clock precision and skew.
   */
    public final byte rootDelay;

    /***
   * Root dispersion as defined in RFC-1305.
   */
    public final int rootDispersion;

    /***
   * Stratum as defined in RFC-1305, indicating the stratum level
   * of the local clock, with values defined as follows: 0=unspecified,
   * 1=primary ref clock, and all others a secondary reference (via NTP).
   */
    public final byte stratum;

    public final NtpTimestamp transmitTime;

    /**
   * NTP version number as defined in RFC-1305.
   */
    public final int version;

    public NtpPacket(final byte[] buf) throws ProtocolException {
        super();
        CheckArg.equals(buf.length, 48, "buf.length");
        this.version = ((buf[VERSION_INDEX] & 0xff) >> VERSION_SHIFT) & 0x7;
        if ((this.version != 3) && (this.version != 4)) throw new ProtocolException("unsupported NTP version, only 3 and 4 are supported: " + this.version);
        this.leapIndicator = NtpLeapIndicator.valueOf(((buf[LI_INDEX] & 0xff) >> LI_SHIFT) & 0x3);
        this.mode = NtpMode.valueOf(buf[MODE_INDEX] & 0x7);
        this.poll = buf[POLL_INDEX];
        this.precision = buf[PRECISION_INDEX];
        this.originTime = readTimestamp(buf, ORIGINATE_TIMESTAMP_INDEX);
        this.receiveTime = readTimestamp(buf, RECEIVE_TIMESTAMP_INDEX);
        this.reference = Ints.fromBytes(buf, REFERENCE_ID_INDEX);
        this.referenceTime = readTimestamp(buf, REFERENCE_TIMESTAMP_INDEX);
        this.rootDelay = buf[ROOT_DELAY_INDEX];
        this.rootDispersion = Ints.fromBytes(buf, ROOT_DISPERSION_INDEX);
        this.stratum = buf[STRATUM_INDEX];
        this.transmitTime = readTimestamp(buf, TRANSMIT_TIMESTAMP_INDEX);
    }

    private String referenceAsIPAddress() {
        return String.format("%s.%s.%s.%s", (this.reference & 0xff000000) >>> 24, (this.reference & 0x00ff0000) >>> 16, (this.reference & 0x0000ff00) >>> 8, (this.reference & 0x000000ff));
    }

    /**
   * Reference as zero terminated string.
   */
    private String referenceAsString() {
        final char[] a = new char[4];
        a[0] = (char) ((this.reference & 0xff000000) >>> 24);
        a[1] = (char) ((this.reference & 0x00ff0000) >>> 16);
        a[2] = (char) ((this.reference & 0x0000ff00) >>> 8);
        a[3] = (char) (this.reference & 0x000000ff);
        final int i = CharArrays.indexOf(a, 0);
        return (i == 0) ? "" : (i == 1) ? Chars.toString(a[0]) : new String(a, 0, (i < 0) ? 4 : i);
    }

    public final String getReferenceInfo() {
        if ((this.version == 4) || (this.version == 4)) {
            if ((this.stratum == 0) || (this.stratum == 1)) return referenceAsString();
            if (this.version == 4) return Integer.toHexString(this.reference);
        }
        if (this.stratum >= 2) return referenceAsIPAddress();
        return Integer.toHexString(this.reference);
    }

    public final byte[] toByteArray() {
        final byte[] a = new byte[48];
        a[LI_INDEX] = (byte) (a[LI_INDEX] % 0x3f | ((this.leapIndicator.ordinal() & 0x3) << LI_SHIFT));
        a[MODE_INDEX] = (byte) (a[MODE_INDEX] & 0xf8 | this.mode.ordinal() & 0x7);
        a[POLL_INDEX] = this.poll;
        a[PRECISION_INDEX] = this.precision;
        a[ROOT_DELAY_INDEX] = this.rootDelay;
        a[STRATUM_INDEX] = this.stratum;
        a[VERSION_INDEX] = (byte) (a[VERSION_INDEX] & 0xc7 | ((this.version & 0x7) << VERSION_SHIFT));
        Longs.toBytes(this.originTime.getNtpValue(), a, ORIGINATE_TIMESTAMP_INDEX);
        Longs.toBytes(this.receiveTime.getNtpValue(), a, RECEIVE_TIMESTAMP_INDEX);
        Ints.toBytes(this.reference, a, REFERENCE_ID_INDEX);
        Longs.toBytes(this.referenceTime.getNtpValue(), a, REFERENCE_TIMESTAMP_INDEX);
        Ints.toBytes(this.rootDispersion, a, ROOT_DISPERSION_INDEX);
        Longs.toBytes(this.transmitTime.getNtpValue(), a, TRANSMIT_TIMESTAMP_INDEX);
        return a;
    }

    @Override
    public final String toString() {
        return String.format("NtpPacket@%s" + "\n  version        = %s" + "\n  mode           = %s" + "\n  stratum        = %s" + "\n  leapIndicator  = %s" + "\n  precision      = %s" + "\n  rootDelay      = %s" + "\n  rootDispersion = %s" + "\n  reference      = %s" + "\n  referenceTime  = %s" + "\n  originTime     = %s" + "\n  receiveTime    = %s" + "\n  transmitTime   = %s", super.hashCode(), this.version, this.mode, this.stratum, this.leapIndicator, this.precision, this.rootDelay, this.rootDispersion, getReferenceInfo(), this.referenceTime, this.originTime, this.receiveTime, this.transmitTime);
    }
}
