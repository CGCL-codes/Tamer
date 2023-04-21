package org.ochnygosch.jIMAP.base.command;

import org.ochnygosch.jIMAP.IIMAPProtocol;
import org.ochnygosch.jIMAP.IMAPCommand;
import org.ochnygosch.jIMAP.IMAPProtocolException;
import org.ochnygosch.jIMAP.IMAPState;
import org.ochnygosch.jIMAP.base.response.ExpungeResponse;
import org.ochnygosch.jIMAP.internal.IMAPResponse;

public class ExpungeCommand extends IMAPCommand {

    private IIMAPProtocol prot;

    public ExpungeCommand(IIMAPProtocol prot) {
        super("EXPUNGE");
        this.prot = prot;
    }

    @Override
    public IMAPResponse[] handleResponse(IMAPResponse[] resp) throws IMAPProtocolException {
        for (int i = 0; i < resp.length; i++) {
            IMAPResponse re = resp[i];
            if (re.isTagged() && this.getTag().equals(re.getTag())) {
                resp[i] = null;
                this.setResult(re.getType());
            } else if (ExpungeResponse.canHandle(re)) {
                resp[i] = null;
                ExpungeResponse r = new ExpungeResponse();
                r.handleResponse(re);
                this.addResponse(r);
            } else {
                re.reset();
            }
        }
        return resp;
    }

    @Override
    public boolean canExecute() throws IMAPProtocolException {
        return this.prot.getState() == IMAPState.SELECTED;
    }
}
