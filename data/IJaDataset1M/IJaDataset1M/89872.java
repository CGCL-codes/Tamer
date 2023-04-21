package org.vfsutils.ftpserver.ftplet;

import java.io.IOException;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFtplet extends DefaultFtplet {

    private final Logger LOG = LoggerFactory.getLogger(LoggingFtplet.class);

    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
        LOG.debug("connect");
        return null;
    }

    @Override
    public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {
        LOG.debug("disconnect");
        return null;
    }

    @Override
    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {
        LOG.debug((session.getUser() != null ? session.getUser().getName() : "") + " - after - " + ("PASS".equals(request.getCommand()) ? request.getCommand() : request.getRequestLine()));
        return null;
    }

    @Override
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
        LOG.debug((session.getUser() != null ? session.getUser().getName() : "") + " - before - " + ("PASS".equals(request.getCommand()) ? request.getCommand() : request.getRequestLine()));
        return null;
    }
}
