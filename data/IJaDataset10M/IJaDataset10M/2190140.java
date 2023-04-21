package org.mockftpserver.fake.command;

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.ReplyCodes;
import org.mockftpserver.core.session.Session;
import org.mockftpserver.core.session.SessionKeys;

/**
 * CommandHandler for the CWD command. Handler logic:
 * <ol>
 * <li>If the user has not logged in, then reply with 530</li>
 * <li>If the required pathname parameter is missing, then reply with 501 and terminate</li>
 * <li>If the pathname parameter does not specify an existing directory, then reply with 550 and terminate</li>
 * <li>If the current user does not have execute access to the directory, then reply with 550 and terminate</li>
 * <li>Otherwise, reply with 250 and change the current directory stored in the session</li>
 * </ol>
 * The supplied pathname may be absolute or relative to the current directory.
 *
 * @author Chris Mair
 * @version $Revision: 182 $ - $Date: 2008-11-30 21:37:49 -0500 (Sun, 30 Nov 2008) $
 */
public class CwdCommandHandler extends AbstractFakeCommandHandler {

    protected void handle(Command command, Session session) {
        verifyLoggedIn(session);
        String path = getRealPath(session, command.getRequiredParameter(0));
        this.replyCodeForFileSystemException = ReplyCodes.READ_FILE_ERROR;
        verifyFileSystemCondition(getFileSystem().exists(path), path, "filesystem.doesNotExist");
        verifyFileSystemCondition(getFileSystem().isDirectory(path), path, "filesystem.isNotADirectory");
        verifyExecutePermission(session, path);
        session.setAttribute(SessionKeys.CURRENT_DIRECTORY, path);
        sendReply(session, ReplyCodes.CWD_OK, "cwd", list(path));
    }
}
