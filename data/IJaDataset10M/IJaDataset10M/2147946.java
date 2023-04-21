package org.mobicents.ssf.servlet.mapping;

import javax.servlet.sip.SipApplicationSessionBindingEvent;
import org.mobicents.ssf.servlet.handler.support.SipHandlerWrapper;

public interface SipApplicationSessionAttributeResolver {

    SipHandlerWrapper getAttributeAddedHandler(SipApplicationSessionBindingEvent evt);

    SipHandlerWrapper getAttributeRemovedHandler(SipApplicationSessionBindingEvent evt);

    SipHandlerWrapper getAttributeReplacedHandler(SipApplicationSessionBindingEvent evt);
}
