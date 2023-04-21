package org.jjsip.sip.header;

/** SIP Header Proxy-Require */
public class ProxyRequireHeader extends OptionHeader {

    public ProxyRequireHeader(String option) {
        super(SipHeaderType.PROXY_REQUIRE, option);
    }

    public ProxyRequireHeader(Header hd) {
        super(hd);
    }
}
