package org.mockftpserver.stub.command;

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandHandler;
import org.mockftpserver.core.command.InvocationRecord;
import org.mockftpserver.core.command.ReplyCodes;
import org.mockftpserver.core.session.Session;

/**
 * CommandHandler for the RNTO (Rename To) command. Send back a reply code of 250.
 * <p/>
 * Each invocation record stored by this CommandHandler includes the following data element key/values:
 * <ul>
 * <li>{@link #PATHNAME_KEY} ("pathname") - the pathname of the file submitted on the invocation (the first command parameter)
 * </ul>
 *
 * @author Chris Mair
 * @version $Revision: 204 $ - $Date: 2008-12-12 19:38:37 -0500 (Fri, 12 Dec 2008) $
 */
public class RntoCommandHandler extends AbstractStubCommandHandler implements CommandHandler {

    public static final String PATHNAME_KEY = "pathname";

    /**
     * Constructor. Initialize the replyCode.
     */
    public RntoCommandHandler() {
        setReplyCode(ReplyCodes.RNTO_OK);
    }

    /**
     * @see org.mockftpserver.core.command.CommandHandler#handleCommand(org.mockftpserver.core.command.Command, org.mockftpserver.core.session.Session)
     */
    public void handleCommand(Command command, Session session, InvocationRecord invocationRecord) {
        invocationRecord.set(PATHNAME_KEY, command.getRequiredParameter(0));
        sendReply(session);
    }
}
