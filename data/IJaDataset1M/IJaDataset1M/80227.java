package javax.sip.header;

/**
 * The Unsupported header field lists the features not supported by the UAS.
 * If a server does not understand the option, it must respond by returning a
 * 420 (Bad Extension) Response and list those options it does not understand in
 * the UnsupportedHeader.
 *
 * @see SupportedHeader
 * @see RequireHeader
 * @author BEA Systems, NIST
 * @version 1.2
 */
public interface UnsupportedHeader extends OptionTag, Header {

    /**
     * Name of UnsupportedHeader
     */
    public static final String NAME = "Unsupported";
}
