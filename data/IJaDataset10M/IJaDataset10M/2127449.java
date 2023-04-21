package gov.nist.javax.sip.stack;

import org.mobicents.ha.javax.sip.ClusteredSipStack;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.message.SIPResponse;

/**
 * Extends the ConfirmedNoAppDataReplicationSipDialog class and also replicate the transaction application data when it changes
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public class ConfirmedReplicationSipDialog extends ConfirmedNoAppDataReplicationSipDialog {

    private static final long serialVersionUID = -779892668482217624L;

    public ConfirmedReplicationSipDialog(SIPTransaction transaction) {
        super(transaction);
    }

    public ConfirmedReplicationSipDialog(SIPClientTransaction transaction, SIPResponse sipResponse) {
        super(transaction, sipResponse);
    }

    public ConfirmedReplicationSipDialog(SipProviderImpl sipProvider, SIPResponse sipResponse) {
        super(sipProvider, sipResponse);
    }

    @Override
    public void setApplicationData(Object applicationData) {
        super.setApplicationData(applicationData);
        if (((ClusteredSipStack) getStack()).isReplicateApplicationData()) {
            replicateState();
        }
    }

    public Object getApplicationDataToReplicate() {
        if (((ClusteredSipStack) getStack()).isReplicateApplicationData()) {
            return getApplicationData();
        }
        return null;
    }

    @Override
    public void setApplicationDataToReplicate(Object appData) {
        super.setApplicationData(appData);
    }
}
