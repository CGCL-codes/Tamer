package org.openfast.session;

import junit.framework.TestCase;

public class FastServerTest extends TestCase {

    private FastServer server = new FastServer("test", new SessionControlProtocol_1_0(), new LocalEndpoint());

    public void testConstructWithNullFactory() {
        try {
            new FastServer(null, null, null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public void testSetErrorHandlerNull() {
        try {
            server.setErrorHandler(null);
            fail();
        } catch (NullPointerException e) {
        }
    }
}
