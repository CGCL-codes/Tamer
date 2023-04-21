package com.googlecode.yoohoo.io.socket.stream;

import com.googlecode.yoohoo.xmppcore.stream.INegotiationContext;
import com.googlecode.yoohoo.xmppcore.stream.IStreamNegotiant;

public abstract class AbstractStreamNegotiant implements IStreamNegotiant {

    protected static final Object STREAM_NEGOTIATION_PHASE_KEY = new Object();

    protected AbstractStreamNegotiant nextNegotiant;

    public AbstractStreamNegotiant getNextNegotiant() {
        return nextNegotiant;
    }

    @Override
    public void negotiate(INegotiationContext context, String message) {
        if (!isNegotiationDone(context)) {
            doNegotiate(context, message);
            return;
        }
        if (nextNegotiant != null) {
            nextNegotiant.negotiate(context, message);
        }
    }

    public void setNextNegotiant(AbstractStreamNegotiant nextNegotiant) {
        this.nextNegotiant = nextNegotiant;
    }

    public boolean isNegotiationDone(INegotiationContext context) {
        return getPhase() != getCurrentPhase(context);
    }

    protected void nextPhase(INegotiationContext context) {
        NegotiationPhase currentPhase = getCurrentPhase(context);
        if (currentPhase != null) {
            currentPhase = currentPhase.next();
            context.setAttribute(STREAM_NEGOTIATION_PHASE_KEY, currentPhase);
        }
    }

    protected NegotiationPhase getCurrentPhase(INegotiationContext context) {
        NegotiationPhase currentPhase = (NegotiationPhase) context.getAttribute(STREAM_NEGOTIATION_PHASE_KEY);
        if (currentPhase == null) {
            currentPhase = NegotiationPhase.INITIAL_STREAM;
            context.setAttribute(STREAM_NEGOTIATION_PHASE_KEY, currentPhase);
        }
        return currentPhase;
    }

    protected abstract NegotiationPhase getPhase();

    protected abstract void doNegotiate(INegotiationContext context, String message);
}
