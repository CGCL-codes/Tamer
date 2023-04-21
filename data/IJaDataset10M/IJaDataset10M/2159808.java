package org.echarts.jain.sip.messages;

import javax.sip.message.Response;

public class FinalResponse extends TypedResponse {

    static final String rcsid = "$Name:  $ $Id: FinalResponse.java,v 1.1.1.1 2007/06/28 14:52:14 venkita Exp $";

    public FinalResponse(Response r) {
        super(r);
    }
}
