package com.mac.chriswjohnson.mazewars.internal;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: chrisj
 * Date: Aug 30, 2008
 * Time: 9:44:47 PM
 * To change this template use File | Settings | File Templates.
 */
final class WaitingConnection {

    private static final Logger Log = Logger.getLogger(WaitingConnection.class.getName());

    private final Object BlockingObj = new Object();

    private boolean Waiting = true;

    private final long StartMs = System.currentTimeMillis();

    private final HttpServletRequest Req;

    private final Player Player;

    private int HTTPStatusCode;

    private String ReplyStr;

    WaitingConnection(final HttpServletRequest Req, final Player P) {
        this.Req = Req;
        this.Player = P;
        P.queueStatusRequest(this);
    }

    void waitForConnectionToBeUsed() throws InterruptedException {
        if (!Waiting) Log.log(Level.FINE, "Waiting for status query from {0} to be used.", Player);
        synchronized (BlockingObj) {
            while (Waiting) BlockingObj.wait();
        }
    }

    HttpServletRequest getRequest() {
        return Req;
    }

    long getAgeInMs() {
        return System.currentTimeMillis() - StartMs;
    }

    Player getPlayer() {
        return Player;
    }

    int getHTTPStatusCode() {
        return HTTPStatusCode;
    }

    String getReplyStr() {
        return ReplyStr;
    }

    void done(final int HTTPStatusCode, final String ReplyStr) {
        this.HTTPStatusCode = HTTPStatusCode;
        this.ReplyStr = ReplyStr;
        this.Waiting = false;
        synchronized (BlockingObj) {
            BlockingObj.notifyAll();
        }
        Log.log(Level.FINE, "Status query from {0} has been used.", Player);
    }
}
