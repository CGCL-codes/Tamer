package net.java.sip.communicator.sip;

/**
 * <p>Title: SIP COMMUNICATOR-1.1</p>
 * <p>Description: JAIN-SIP-1.1 Audio/Video Phone Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Organisation: LSIIT Laboratory (http://lsiit.u-strasbg.fr) \nNetwork Research Team (http://www-r2.u-strasbg.fr))\nLouis Pasteur University - Strasbourg - France</p>
 * @author Emil Ivov
 * @version 1.1
 */
public class ErrorProcessing {

    private SipManager sipManCallback = null;

    public ErrorProcessing() {
    }

    ErrorProcessing(SipManager sipManCallback) {
        this.sipManCallback = sipManCallback;
    }

    void setSipManagerCallBack(SipManager sipManCallback) {
        this.sipManCallback = sipManCallback;
    }
}
