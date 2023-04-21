package org.fpse.server.handler.impl.smtp;

import java.io.IOException;
import org.fpse.server.Command;
import org.fpse.server.CommandFailedException;
import org.fpse.server.CommandStatus;
import org.fpse.server.ICommandHandler;
import org.fpse.server.ISession;
import org.fpse.server.SessionState;

/**
 * Created on Dec 16, 2006 9:39:25 AM by Ajay
 */
public class MailCommand implements ICommandHandler {

    public CommandStatus handleCommand(String tag, Command command, String[] arguments, ISession session) throws IOException, CommandFailedException {
        session.setState(session.getMajorState(), SessionState.MinorSessionState.MINOR_DATA_STATE);
        return CommandStatus.SUCCESS;
    }
}
