package org.mockftpserver.stub.command;

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandHandler;
import org.mockftpserver.core.command.InvocationRecord;
import org.mockftpserver.core.command.ReplyCodes;
import org.mockftpserver.core.session.Session;
import java.io.IOException;
import java.net.InetAddress;

/**
 * CommandHandler for the EPSV (Extended Address Passive Mode) command. Request the Session to switch
 * to passive data connection mode. Return a reply code of 229, along with response text of the form:
 * "<i>Entering Extended Passive Mode (|||PORT|)</i>", where <i>PORT</i> is the 16-bit TCP port
 * address of the data connection on the server to which the client must connect.
 * See RFC2428 for more information.
 * <p/>
 * Each invocation record stored by this CommandHandler contains no data elements.
 *
 * @author Chris Mair
 * @version $Revision: 230 $ - $Date: 2009-06-13 07:44:57 -0400 (Sat, 13 Jun 2009) $
 */
public class EpsvCommandHandler extends AbstractStubCommandHandler implements CommandHandler {

    /**
     * Constructor. Initialize the replyCode.
     */
    public EpsvCommandHandler() {
        setReplyCode(ReplyCodes.EPSV_OK);
    }

    /**
     * @throws java.io.IOException
     * @see org.mockftpserver.core.command.CommandHandler#handleCommand(org.mockftpserver.core.command.Command, org.mockftpserver.core.session.Session)
     */
    public void handleCommand(Command command, Session session, InvocationRecord invocationRecord) throws IOException {
        int port = session.switchToPassiveMode();
        InetAddress server = session.getServerHost();
        LOG.debug("server=" + server + " port=" + port);
        sendReply(session, Integer.toString(port));
    }
}
