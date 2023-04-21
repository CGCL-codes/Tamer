package org.rip.ftp.client;

import org.apache.commons.lang.StringUtils;
import org.rip.ftp.FtpConstants;
import org.rip.ftp.FtpResponse;
import org.rip.ftp.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenCommand extends ClientCommand {

    private static Logger LOG = LoggerFactory.getLogger(OpenCommand.class);

    private String server;

    private String port;

    @Override
    public String syntax() {
        return "open <server> [port]";
    }

    @Override
    public void execute() {
        super.execute();
        LOG.trace("OpenCommand.open()");
        if (!session.isClosed()) {
            return;
        }
        response = session.open(server, port);
        switch(response.getResponse()) {
            case Ready:
                if (response.isExtended()) {
                    LOG.info(response.getExtendedText());
                } else {
                    LOG.info(response.getText());
                }
                LOG.info("ok");
                break;
            case ReadyIn:
                break;
            case NotAvailable:
                break;
            default:
                LOG.info("open unrecognized response:" + response.getCode());
        }
    }

    @Override
    public void parseCommand(String line) throws ParseException {
        line = line.trim();
        String[] split = StringUtils.split(line);
        if (split == null) {
            throw new ParseException("Invalid Open Command");
        }
        if (split.length < 2) {
            throw new ParseException("Invalid Open Command");
        }
        server = split[1];
        if (split.length > 2) {
            port = split[2];
        } else {
            port = FtpConstants.DEFAULT_PORT;
        }
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
