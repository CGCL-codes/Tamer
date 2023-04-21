package org.jboss.arquillian.container.mobicents.servlet.sip.embedded_1.ar;

import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.spi.SipApplicationRouterProvider;

/**
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public class DummyRouterProvider extends SipApplicationRouterProvider {

    private final DummyRouter appRouter = new DummyRouter();

    @Override
    public SipApplicationRouter getSipApplicationRouter() {
        return appRouter;
    }
}
