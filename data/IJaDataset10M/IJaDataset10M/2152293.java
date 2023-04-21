package org.rip.ftp.client;

import org.apache.commons.lang.StringUtils;
import org.rip.ftp.FtpConstants;
import org.rip.ftp.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteCommand extends ClientCommand implements ResponseListener {

    private static Logger LOG = LoggerFactory.getLogger(SiteCommand.class);

    String question;

    public String syntax() {
        return "help [SP string]";
    }

    @Override
    public void parseCommand(String line) throws ParseException {
        String[] split = StringUtils.split(line);
        if (split.length > 1) {
            question = split[1];
        }
    }

    @Override
    public void execute() {
        if (!session.isReady()) {
            LOG.info("not open");
            return;
        }
        String toSend = FtpConstants.SITE;
        if (question != null) {
            toSend += SP + question;
        }
        toSend += CRLF;
        session.addListener(this);
        session.sendCommand(toSend);
    }

    @Override
    public void processResponse(ClientResponse response) {
        switch(response.getResponse()) {
            case OK:
            case NotImplementedOK:
                if (response.isExtended()) {
                    LOG.info(response.getExtendedText());
                } else {
                    LOG.info(response.getText());
                }
                LOG.info("ok");
                break;
            case SyntaxErrorCommand:
            case SyntaxErrorParam:
            case CommandNotImplemented:
            case ParamNotImplemented:
            case NotLoggedIn:
                LOG.info(response.getText());
                LOG.info("ok");
            case NotAvailable:
            default:
        }
        session.removeListener(this);
    }
}
