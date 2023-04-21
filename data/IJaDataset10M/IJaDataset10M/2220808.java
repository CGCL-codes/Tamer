package org.jjsip.sip.header;

/** SIP Header Unsupported */
public class UnsupportedHeader extends OptionHeader {

    public UnsupportedHeader(String option) {
        super(SipHeaderType.UNSUPPORTED, option);
    }

    public UnsupportedHeader(Header hd) {
        super(hd);
    }
}
