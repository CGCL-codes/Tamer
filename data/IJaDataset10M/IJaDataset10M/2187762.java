package org.mockftpserver.stub.command;

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandHandler;
import org.mockftpserver.core.command.InvocationRecord;
import org.mockftpserver.core.command.ReplyCodes;
import org.mockftpserver.core.session.Session;

/**
 * CommandHandler for the CDUP (Change To Parent Directory) command. Send back a reply code of 250.
 * <p>
 * Each invocation record stored by this CommandHandler contains no data elements.
 *
 * @author Chris Mair
 * @version $Revision: 204 $ - $Date: 2008-12-12 19:38:37 -0500 (Fri, 12 Dec 2008) $
 */
public class CdupCommandHandler extends AbstractStubCommandHandler implements CommandHandler {

    /**
     * Constructor. Initialize the replyCode.
     */
    public CdupCommandHandler() {
        setReplyCode(ReplyCodes.CDUP_OK);
    }

    public void handleCommand(Command command, Session session, InvocationRecord invocationRecord) {
        sendReply(session);
    }
}
