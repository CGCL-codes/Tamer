package org.echarts.test.sip.commands;

import javax.sip.SipFactory;
import javax.sip.address.URI;

/**
 * For internal use only
 */
public class CmdReferDialogInit extends CmdReferCommon {

    static final String rcsid = "$Name$ $Id$";

    private URI rUri;

    public CmdReferDialogInit(String rUri, String referTo, boolean suppressSub) throws Exception {
        super(referTo, suppressSub);
        this.rUri = (rUri == null) ? null : SipFactory.getInstance().createAddressFactory().createURI(rUri);
    }

    public URI getRUri() {
        return this.rUri;
    }

    public String getName() {
        return "ReferDialogInit";
    }
}
