package org.mobicents.ssf.context.signal.spring;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionEvent;
import org.mobicents.ssf.util.AssertUtils;

/**
 * SipSessionのScopeに対応する属性情報です。
 * 
 * @author nisihara
 *
 */
public class SipSessionAttributes extends SpringSignalingAttributes {

    private SipSession session;

    private Object originalObject;

    /**
     * SipSessionEventから属性情報を生成します。
     * 
     * @param evt SipSessionEvent
     */
    public SipSessionAttributes(SipSessionEvent evt) {
        AssertUtils.notNull(evt, "SipSessionEvent must not be null.");
        this.session = evt.getSession();
        this.originalObject = evt;
    }

    /**
     * SipSessionから属性情報を生成します。
     * 
     * @param session SipSession
     */
    public SipSessionAttributes(SipSession session) {
        AssertUtils.notNull(session, "SipSession must not be null.");
        this.session = session;
        this.originalObject = session;
    }

    public SipApplicationSession getSipApplicationSession() {
        return this.session.getApplicationSession();
    }

    public SipServletMessage getSipServletMessage() {
        return null;
    }

    public SipSession getSipSession() {
        return this.session;
    }

    public Object getOriginalEvent() {
        return this.originalObject;
    }
}
