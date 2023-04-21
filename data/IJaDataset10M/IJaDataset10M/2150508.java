package org.ochnygosch.jIMAP.base.command;

import org.ochnygosch.jIMAP.IIMAPProtocol;
import org.ochnygosch.jIMAP.IMAPCommand;
import org.ochnygosch.jIMAP.IMAPProtocolException;
import org.ochnygosch.jIMAP.IMAPResponseCode;
import org.ochnygosch.jIMAP.internal.IMAPResponse;

public class NoopCommand extends IMAPCommand {

    private IIMAPProtocol prot;

    public NoopCommand(IIMAPProtocol prot) {
        super("NOOP");
        this.prot = prot;
    }

    @Override
    public IMAPResponse[] handleResponse(IMAPResponse[] resp) throws IMAPProtocolException {
        for (int i = 0; i < resp.length; i++) {
            IMAPResponse re = resp[i];
            if (re.isTagged() && this.getTag().equals(re.getTag())) {
                resp[i] = null;
                this.setResult(re.getType());
            }
        }
        return resp;
    }

    @Override
    public boolean canExecute() throws IMAPProtocolException {
        return true;
    }
}
