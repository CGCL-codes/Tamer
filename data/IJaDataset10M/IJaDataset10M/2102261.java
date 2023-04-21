package sf2.view.impl.paxos.nop.msg;

import java.io.Serializable;
import sf2.view.impl.paxos.PaxosEvent;

public class PaxosResponse implements PaxosEvent, Serializable {

    private static final long serialVersionUID = 4177108705987389617L;

    protected byte[] key;

    protected long reqId;

    protected Object ret;

    public PaxosResponse(byte[] key, long reqId, Object ret) {
        this.key = key;
        this.reqId = reqId;
        this.ret = ret;
    }

    public byte[] getKey() {
        return key;
    }

    public long getRequestId() {
        return reqId;
    }

    public Object getResult() {
        return ret;
    }
}
