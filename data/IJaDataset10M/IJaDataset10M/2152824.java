package net.jsrb.runtime.impl.endpoint.sysvipc;

import java.util.*;
import net.jsrb.rtl.RtlException;
import net.jsrb.rtl.ipc.msg;
import net.jsrb.runtime.endpoint.*;

public class SysvipcEndpointSession implements ServerEndpointSession {

    private Object attach;

    private int idi;

    int serverPid;

    SessionType sessionType;

    private int nextsn;

    /**
	 * this msg is created in parent reactor thread
	 */
    msg msg;

    private List<msg> referencedMsgs = new LinkedList<msg>();

    /**
     * Create a duplicated msgqueue from other thread, 
     * and hold the reference to prevent gc. 
     */
    public msg dupMsg() throws RtlException {
        msg result = msg.dup();
        referencedMsgs.add(result);
        return result;
    }

    int snnext() {
        return ++nextsn;
    }

    public SysvipcEndpointSession(int id, msg msg) {
        idi = id;
        this.msg = msg;
    }

    public int getSessionId() {
        return idi;
    }

    public void setAttach(Object attachment) {
        this.attach = attachment;
    }

    public Object getAttach() {
        return attach;
    }

    public int getServerPid() {
        return serverPid;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public String toString() {
        return "SysvEndpointSession[id=0x" + Integer.toHexString(idi) + ",svrpid=" + serverPid + ",type=" + sessionType + ",msg=" + msg + "]";
    }

    public EndpointReadWriter getReadWriter() {
        return null;
    }
}
