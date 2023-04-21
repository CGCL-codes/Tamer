package sf.net.algotrade.c2api.request;

import sf.net.algotrade.c2api.C2SignalEntryCommandEnum;
import sf.net.algotrade.c2api.C2SignalEntryCommandParamEnum;
import sf.net.algotrade.c2api.exceptions.BadlyFormedRequestError;

public class CancelOrderRequest extends TradeSignalRequest {

    private long signalid = -1;

    private String systemid;

    private String pw;

    public CancelOrderRequest() {
        super(C2SignalEntryCommandEnum.cancel);
    }

    public long getSignalid() {
        return signalid;
    }

    public void setSignalid(long signalid) {
        this.signalid = signalid;
        super.setSignalParameter(C2SignalEntryCommandParamEnum.signalid, signalid);
    }

    public String getSystemid() {
        return systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid;
        super.setSignalParameter(C2SignalEntryCommandParamEnum.systemid, systemid);
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
        super.setSignalParameter(C2SignalEntryCommandParamEnum.pw, pw);
    }

    @Override
    public BadlyFormedRequestError validate() {
        if (signalid == -1) return (new BadlyFormedRequestError("Signal ID not set"));
        if (systemid == null) return (new BadlyFormedRequestError("System ID not set"));
        if (pw == null) return (new BadlyFormedRequestError("Password not set"));
        return null;
    }
}
