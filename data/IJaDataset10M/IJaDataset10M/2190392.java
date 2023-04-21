package org.echarts.servlet.sip.messages;

import javax.servlet.sip.SipServletResponse;

/** Represents SIP 180 response.
 */
public class ProvisionalResponse180 extends UnreliableProvisionalResponse {

    static final String rcsid = "$Name:  $ $Id: ProvisionalResponse180.java 1871 2011-12-01 22:02:40Z prakashkolan $";

    public ProvisionalResponse180(SipServletResponse res) {
        super(res);
    }
}
