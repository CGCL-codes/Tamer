package com.elibera.m.io.msg;

import java.io.IOException;
import com.elibera.m.io.Server;

/**
 * @author meisi
 *
 */
public class ServerMsg extends Server {

    private boolean msgstarted = false;

    public void informError(Exception e) {
        if (!msgstarted) HelperMsgServer.MSG_START_ERROR = true;
    }

    public void doServerStarted() throws IOException {
        msgstarted = true;
        HelperMsgServer.doMsgServer(this);
    }

    public void informClosed() {
        HelperMsgServer.serverWasStarted = false;
    }

    ;
}
