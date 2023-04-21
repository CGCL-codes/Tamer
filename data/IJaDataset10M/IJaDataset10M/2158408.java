package org.mobicents.ssf.flow.servlet.handler;

import org.mobicents.ssf.event.Event;
import org.mobicents.ssf.flow.event.SipFlowEvent;
import org.mobicents.ssf.flow.util.ContextLocalUtil;
import org.mobicents.ssf.servlet.handler.SipHandlerAdapter;

/**
 * <p>
 * SipFlowフレームワーク上で使用するためのSipHandlerAdapterです。
 * </p>
 * @see SipFlowHandler
 * @see SipFlowEvent
 * @author nisihara
 *
 */
public class SipFlowHandlerAdapter implements SipHandlerAdapter {

    public Object handle(Event evt, Object handler) {
        ContextLocalUtil.start();
        try {
            SipFlowHandler sfhandler = (SipFlowHandler) handler;
            sfhandler.handleSipFlowEvent((SipFlowEvent) evt);
            return null;
        } finally {
            ContextLocalUtil.end();
        }
    }

    public boolean supports(Object handler) {
        return (handler instanceof SipFlowHandler);
    }
}
