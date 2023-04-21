package jbridge;

import java.util.HashMap;
import inou.net.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SessionManager {

    private IOverrideCall overrideCall;

    private Logger monitor = Logger.getLogger(this.getClass());

    private ObjectManager objectManager;

    private HashMap sessionTable = new HashMap();

    private HashMap threadTable = new HashMap();

    private int sessionId = 0;

    private Object sessionIdLock = new Object();

    public SessionManager(IOverrideCall or) {
        overrideCall = or;
    }

    public void init(ObjectManager om) {
        objectManager = om;
    }

    private Object createSessionId() {
        synchronized (sessionIdLock) {
            sessionId++;
            return "SID:" + sessionId;
        }
    }

    private Object getThreadId(Object sid) {
        Object tid = threadTable.get(Thread.currentThread());
        if (tid == null) {
            return sid;
        }
        return tid;
    }

    public Object overrideCall(Object proxyId, String method, Object[] args, Class returnTyep) throws Exception {
        args = objectManager.obj2ids(args);
        Object sid = createSessionId();
        Object tid = getThreadId(sid);
        Thread ct = Thread.currentThread();
        Utils.writeArray(monitor, Level.DEBUG, new Object[] { "SessionManager  SID:", sid.toString(), "  TID:", tid.toString(), " [", ct.getName(), " in ", ct.getThreadGroup().getName(), "]" });
        try {
            CallSession session = new CallSession(sid, proxyId, method, args, returnTyep);
            sessionTable.put(sid, session);
            threadTable.put(ct, tid);
            return objectManager.id2obj(session.overrideCall(overrideCall));
        } finally {
            sessionTable.remove(sid);
        }
    }

    public Object sessionCall(Object sid, ISessionProcedure p) throws Exception {
        CallSession s = getSession(sid);
        return s.sessionCall(p);
    }

    private CallSession getSession(Object sid) {
        return (CallSession) sessionTable.get(sid);
    }
}
