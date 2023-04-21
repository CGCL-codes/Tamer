package net.sf.doolin.app.sc.engine.support;

import net.sf.doolin.app.sc.engine.ClientConnectionResult;
import net.sf.doolin.app.sc.engine.ClientConnectionResultType;
import net.sf.doolin.app.sc.engine.ClientID;

public class DefaultClientConnectionResult implements ClientConnectionResult {

    private static final long serialVersionUID = 1L;

    private final ClientConnectionResultType type;

    private final ClientID clientID;

    private final String errorMessage;

    public DefaultClientConnectionResult(ClientID cid) {
        this.type = ClientConnectionResultType.OK;
        this.clientID = cid;
        this.errorMessage = null;
    }

    public DefaultClientConnectionResult(String errorMessage) {
        this.type = ClientConnectionResultType.ERROR;
        this.clientID = null;
        this.errorMessage = errorMessage;
    }

    @Override
    public ClientID getClientID() {
        return this.clientID;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public ClientConnectionResultType getType() {
        return this.type;
    }
}
