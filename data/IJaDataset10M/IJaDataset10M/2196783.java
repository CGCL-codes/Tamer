package org.vrspace.server;

import java.util.*;
import org.vrspace.util.*;

/**
*/
public class PipedConnection extends Connection implements Observer {

    protected PipedSession session;

    public PipedConnection(PipedSession session, String login, String password) throws ConnectionException {
        this.session = session;
        this.login = login;
        this.password = password;
        login();
    }

    public void update(Observable o, Object msg) {
        if (msg instanceof Request) {
            inQueue.add(msg);
        } else if (msg instanceof Throwable) {
            Logger.logError((Throwable) msg);
        } else {
            Logger.logError("Invalid message type: " + msg);
        }
    }

    public void login() throws ConnectionException {
        active = true;
        (new Thread(this, login + "@" + session)).start();
    }

    public void close() {
        active = false;
    }

    public String getHostName() {
        return session.getId();
    }

    /**
  Writes this line to the session
  */
    protected synchronized void write(String s) {
        try {
            while (outQueue.size() > 0) {
                String line = (String) outQueue.peek();
                session.update(this, line);
                outQueue.remove();
            }
            session.update(this, s);
        } catch (Throwable t) {
            Logger.logError(t);
        }
    }

    public Session getSession() {
        return session;
    }

    public void run() {
        while (active) {
            try {
                while (inQueue.size() == 0 || countObservers() == 0) {
                    Util.sleep(100);
                }
                Request line = (Request) inQueue.remove();
                setChanged();
                notifyObservers(line);
            } catch (Exception e) {
                Logger.logError(e);
            }
        }
    }
}
