package org.rip.ftp.client;

import org.rip.ftp.FtpConstants;
import org.rip.ftp.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbortCommand extends ClientCommand implements ResponseListener {

    private static Logger LOG = LoggerFactory.getLogger(AbortCommand.class);

    public String syntax() {
        return FtpConstants.ABOR;
    }

    @Override
    public void parseCommand(String line) throws ParseException {
    }

    @Override
    public void execute() {
        if (session.getState() != FtpClientState.READY) {
            LOG.info("client not in correct state");
            return;
        }
        String cmd = FtpConstants.ABOR + CRLF;
        session.sendCommand(cmd);
    }

    @Override
    public void processResponse(ClientResponse response) {
        switch(response.getResponse()) {
            case OpenNoTransfer:
            case ClosingDataConnection:
                session.closeDataConnection();
                session.removeListener(this);
                LOG.info("ok");
                break;
            case SyntaxErrorCommand:
            case SyntaxErrorParam:
            case CommandNotImplemented:
            case NotAvailable:
                LOG.error("ABOR error: {} {}", response.getCode(), response.getText());
                session.removeListener(this);
                break;
            case FileSent:
                session.closeDataConnection();
                session.removeListener(this);
                break;
            default:
                LOG.error("ABOR unexpected response {} {}", response.getCode(), response.getText());
                session.closeDataConnection();
                session.removeListener(this);
        }
    }
}
