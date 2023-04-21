package org.zoolu.sip.header;

import java.util.Vector;

/** SIP Proxy-Authenticate header */
public class ProxyAuthenticateHeader extends WwwAuthenticateHeader {

    /** Creates a new ProxyAuthenticateHeader */
    public ProxyAuthenticateHeader(String hvalue) {
        super(hvalue);
        m_strName = SipHeaders.Proxy_Authenticate;
    }

    /** Creates a new ProxyAuthenticateHeader */
    public ProxyAuthenticateHeader(Header hd) {
        super(hd);
    }

    /** Creates a new ProxyAuthenticateHeader
     * specifing the <i>auth_scheme</i> and the vector of authentication parameters.
     * <p> <i>auth_param</i> is a vector of String of the form <i>parm_name</i> "=" <i>parm_value</i> */
    public ProxyAuthenticateHeader(String auth_scheme, Vector auth_params) {
        super(auth_scheme, auth_params);
        m_strName = SipHeaders.Proxy_Authenticate;
    }
}
