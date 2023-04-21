package org.mockftpserver.core.command;

import org.mockftpserver.core.session.Session;

/**
 * CommandHandler that encapsulates the sending of the reply when a requested command is not
 * recognized/supported. Send back a reply code of 502, indicating command not implemented.
 * <p>
 * Note that this is a "special" CommandHandler, in that it handles any unrecognized command,
 * rather than an explicit FTP command.
 * <p>
 * Each invocation record stored by this CommandHandler contains no data elements.
 *
 * @author Chris Mair
 * @version $Revision: 204 $ - $Date: 2008-12-12 19:38:37 -0500 (Fri, 12 Dec 2008) $
 */
public final class UnsupportedCommandHandler extends AbstractStaticReplyCommandHandler implements CommandHandler {

    /**
     * Constructor. Initiate the replyCode.
     */
    public UnsupportedCommandHandler() {
        setReplyCode(ReplyCodes.COMMAND_NOT_SUPPORTED);
    }

    /**
     * @see org.mockftpserver.core.command.AbstractTrackingCommandHandler#handleCommand(org.mockftpserver.core.command.Command, org.mockftpserver.core.session.Session, org.mockftpserver.core.command.InvocationRecord)
     */
    public void handleCommand(Command command, Session session, InvocationRecord invocationRecord) {
        LOG.warn("No CommandHandler is defined for command [" + command.getName() + "]");
        sendReply(session, command.getName());
    }
}
