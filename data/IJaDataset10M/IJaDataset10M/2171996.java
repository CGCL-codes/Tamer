package org.rip.ftp.client;

import org.apache.commons.lang.StringUtils;
import org.rip.ftp.FtpConstants;
import org.rip.ftp.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassiveCommand extends ClientCommand implements ResponseListener {

    private static Logger LOG = LoggerFactory.getLogger(PassiveCommand.class);

    public String syntax() {
        return FtpConstants.PASV;
    }

    @Override
    public void parseCommand(String line) throws ParseException {
    }

    @Override
    public void execute() {
        if (!session.isReady() || !session.isDCClosed()) {
            LOG.info("Invalid state for PASV command");
            return;
        }
        String cmd = FtpConstants.PASV + CRLF;
        session.addListener(this);
        session.sendCommand(cmd);
    }

    private String[] parseResponse(String response) {
        String[] ret = new String[2];
        int start = response.indexOf('(');
        int end = response.indexOf(')', start + 1);
        if (end > 0) {
            String address = response.substring(start + 1, end);
            String[] split = StringUtils.split(address, ",");
            try {
                ret[0] = split[0] + DOT + split[1] + DOT + split[2] + DOT + split[3];
                int port = (Integer.parseInt(split[4]) * 256) + (Integer.parseInt(split[5]));
                ret[1] = Integer.toString(port);
            } catch (Exception e) {
                LOG.info("pasv unrecognized response:" + response);
            }
        }
        return ret;
    }

    @Override
    public void processResponse(ClientResponse r) {
        response = r;
        switch(response.getResponse()) {
            case EnteringPassiveMode:
                String[] vals = parseResponse(response.getText());
                session.openDataConnection(vals[0], vals[1]);
                session.removeListener(this);
                LOG.info("ok");
                break;
            case SyntaxErrorCommand:
            case SyntaxErrorParam:
            case CommandNotImplemented:
                LOG.info("Error with command:" + response.getText());
                session.removeListener(this);
                break;
            case NotAvailable:
                break;
            case NotLoggedIn:
                session.setState(FtpClientState.Opened);
                session.setDCState(FtpClientState.DC_CLOSED);
                session.removeListener(this);
                break;
            default:
                LOG.info("pasv unrecognized response:" + response.getCode());
                session.removeListener(this);
        }
    }
}
