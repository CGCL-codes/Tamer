package org.mobicents.servlet.sip.ctf.core.extension;

import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import org.jboss.weld.bootstrap.ContextHolder;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipApplicationSessionContext;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipApplicationSessionContextImpl;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipLiteral;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipSessionContext;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipSessionContextImpl;

public class SipServletsContextsExtension implements Extension {

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
    }
}
