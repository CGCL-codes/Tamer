package org.echarts.servlet.sip.messages;

import javax.servlet.sip.SipServletRequest;

/** Represents SIP INVITE request.
 */
public class Invite extends Request {

    static final String rcsid = "$Name:  $ $Id: Invite.java 596 2007-06-22 18:11:01Z yotommy $";

    public Invite(SipServletRequest r) {
        super(r);
    }
}
