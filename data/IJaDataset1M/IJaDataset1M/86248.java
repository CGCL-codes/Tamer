package simplefix.quickfix;

import simplefix.FixVersion;

public class Session implements simplefix.Session {

    quickfix.Session _session;

    public Session(final quickfix.Session session) {
        super();
        _session = session;
    }

    public FixVersion getFixVersion() {
        return FixVersion.fromBeginString(_session.getSessionID().getBeginString());
    }

    public String getSenderCompID() {
        return _session.getSessionID().getSenderCompID();
    }

    public String getTargetCompID() {
        return _session.getSessionID().getTargetCompID();
    }

    public void sendAppMessage(final simplefix.Message msg) {
        quickfix.Message message;
        if (msg instanceof Message) {
            message = ((Message) msg)._msg;
        } else {
            return;
        }
        _session.send(message);
    }
}
