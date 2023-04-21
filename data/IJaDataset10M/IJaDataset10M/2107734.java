package org.mobicents.ssf.flow.event;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionEvent;
import org.mobicents.ssf.context.SignalingAttributes;
import org.mobicents.ssf.context.SipContextHolder;
import org.mobicents.ssf.event.EventType;

public class SipSessionFlowEvent extends SipFlowEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String sessionName;

    private SipApplicationSession appSession;

    public SipSessionFlowEvent(Object source, SipSessionEvent event, EventType type) {
        super(source, event, "sipSessionEvent", type);
        init(event.getSession());
        this.appSession = event.getSession().getApplicationSession();
    }

    public SipSessionFlowEvent(Object source, SipSession event, EventType type) {
        super(source, event, "sipSession", type);
        init(event);
        this.appSession = event.getApplicationSession();
    }

    public SipApplicationSession getApplicationSession() {
        return this.appSession;
    }

    private void init(SipSession session) {
        SignalingAttributes attr = SipContextHolder.currentSignalingAttributes();
        this.sessionName = attr.getSignalingName(session);
    }

    @Override
    public String getSessionName() {
        return this.sessionName;
    }
}
