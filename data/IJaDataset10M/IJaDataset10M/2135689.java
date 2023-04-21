package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * 
 */
public class TCContinueRequestImpl extends DialogRequestImpl implements TCContinueRequest {

    private Byte qos;

    private SccpAddress originatingAddress;

    private ApplicationContextName applicationContextName;

    private UserInformation userInformation;

    TCContinueRequestImpl() {
        super(EventType.Continue);
    }

    public ApplicationContextName getApplicationContextName() {
        return applicationContextName;
    }

    public SccpAddress getOriginatingAddress() {
        return this.originatingAddress;
    }

    public Byte getQOS() {
        return this.qos;
    }

    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    public void setApplicationContextName(ApplicationContextName acn) {
        this.applicationContextName = acn;
    }

    public void setOriginatingAddress(SccpAddress dest) {
        this.originatingAddress = dest;
    }

    public void setQOS(Byte b) throws IllegalArgumentException {
        this.qos = b;
    }

    public void setUserInformation(UserInformation acn) {
        this.userInformation = acn;
    }
}
