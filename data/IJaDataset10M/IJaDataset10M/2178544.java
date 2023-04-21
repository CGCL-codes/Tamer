package com.arthur.srv;

import junit.framework.TestCase;

public class TestServer extends TestCase {

    public void TestInitializing() {
        int port = 2021;
        Server srv = new Server(port);
        assertEquals(port, srv.getServerPort());
    }
}
