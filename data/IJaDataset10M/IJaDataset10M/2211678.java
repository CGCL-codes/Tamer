package net.sourceforge.jfuss.network.rmi.server;

import net.sourceforge.jfuss.network.rmi.interfaces.TestRemoteInterfaces;

public class TestServer implements TestRemoteInterfaces {

    String response;

    TestServer() {
    }

    @Override
    public String getMessage() {
        return null;
    }
}
