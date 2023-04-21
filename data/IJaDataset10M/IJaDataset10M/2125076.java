package org.gardel.service;

import junit.framework.TestCase;
import org.gardel.client.Client;
import org.gardel.service.impl.MockService;
import org.gardel.service.impl.ServiceFactory;
import org.gardel.service.impl.ServiceImpl;

public class ServiceTest extends TestCase {

    public void testGo() {
        Service s = new ServiceImpl();
        assertEquals(false, ((ServiceImpl) s).isGone());
        s.go();
        assertEquals(true, ((ServiceImpl) s).isGone());
    }

    public void testClient() {
        Service previous = ServiceFactory.getInstance();
        try {
            final MockService mock = new MockService();
            ServiceFactory.setInstance(mock);
            Client client = new Client();
            client.go();
            assertTrue(mock.isGone());
        } finally {
            ServiceFactory.setInstance(previous);
        }
    }

    public void testClientDi() {
        final MockService mock = new MockService();
        org.gardel.client.di.Client client = new org.gardel.client.di.Client(mock);
        client.go();
        assertTrue(mock.isGone());
    }
}
