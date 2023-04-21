package net.jsrb.runtime.impl.admin.cmd;

public class FailResponse implements CmdResponse {

    private static final long serialVersionUID = -6466259376766401525L;

    public FailResponse() {
    }

    public FailResponse(String traceMessage) {
        this.traceMessage = traceMessage;
    }

    public int id() {
        return REP_FAIL;
    }

    public int errorCode;

    public String traceMessage;

    public Throwable t;

    public String toString() {
        return "FailResponse[errorCode=" + errorCode + ",traceMessage=" + traceMessage + ",t=" + t + "]";
    }
}
