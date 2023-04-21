package org.xsocket.mail.smtp;

import java.io.IOException;
import org.xsocket.mail.CommandName;
import org.xsocket.stream.INonBlockingConnection;

@CommandName("NOOP")
final class SmtpNOOPCommand extends SmtpCommand {

    public SmtpNOOPCommand() {
    }

    @Override
    public void execute(INonBlockingConnection connection, ISmtpSession session, String argument) throws IOException {
        sendResponse(connection, "250 OK");
    }
}
