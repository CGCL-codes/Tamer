package net.rpm.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RpmServiceImpl extends RemoteServiceServlet {

    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}
