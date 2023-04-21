package org.echarts.jain.sip.messages;

import javax.sip.message.Response;

/** Represents SIP 486 response.
 */
public class ErrorResponse486 extends ErrorResponse {

    static final String rcsid = "$Name:  $ $Id: ErrorResponse486.java,v 1.1 2007/07/18 21:19:25 venkita Exp $";

    public ErrorResponse486(Response res) {
        super(res);
    }
}
