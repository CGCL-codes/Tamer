package org.mobicents.protocols.ss7.sccp.impl.message;

import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpAddressedMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public abstract class SccpAddressedMessageImpl extends SccpMessageImpl implements SccpAddressedMessage {

    protected SccpAddress calledParty;

    protected SccpAddress callingParty;

    protected HopCounterImpl hopCounter;

    protected SccpAddressedMessageImpl(SccpStackImpl sccpStackImpl, int type, int outgoingSls, int localSsn, SccpAddress calledParty, SccpAddress callingParty, HopCounter hopCounter) {
        super(sccpStackImpl, type, outgoingSls, localSsn);
        this.calledParty = calledParty;
        this.callingParty = callingParty;
        this.hopCounter = (HopCounterImpl) hopCounter;
    }

    protected SccpAddressedMessageImpl(SccpStackImpl sccpStackImpl, int type, int incomingOpc, int incomingDpc, int incomingSls) {
        super(sccpStackImpl, type, incomingOpc, incomingDpc, incomingSls);
    }

    @Override
    public SccpAddress getCalledPartyAddress() {
        return calledParty;
    }

    @Override
    public void setCalledPartyAddress(SccpAddress calledParty) {
        this.calledParty = calledParty;
    }

    @Override
    public SccpAddress getCallingPartyAddress() {
        return callingParty;
    }

    @Override
    public void setCallingPartyAddress(SccpAddress callingParty) {
        this.callingParty = callingParty;
    }

    @Override
    public HopCounter getHopCounter() {
        return hopCounter;
    }

    @Override
    public void setHopCounter(HopCounter hopCounter) {
        this.hopCounter = (HopCounterImpl) hopCounter;
    }

    @Override
    public boolean reduceHopCounter() {
        if (this.hopCounter != null) {
            int val = this.hopCounter.getValue();
            if (--val <= 0) {
                val = 0;
            }
            this.hopCounter.setValue(val);
            if (val == 0) return false;
        }
        return true;
    }
}
