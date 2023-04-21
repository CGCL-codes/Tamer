package goldengate.ftp.core.command.access;

import goldengate.common.command.NextCommandReply;
import goldengate.common.command.exception.Reply421Exception;
import goldengate.common.command.exception.Reply501Exception;
import goldengate.common.command.exception.Reply502Exception;
import goldengate.common.command.exception.Reply530Exception;
import goldengate.ftp.core.command.AbstractCommand;

/**
 * ACCT command
 *
 * @author Frederic Bregier
 *
 */
public class ACCT extends AbstractCommand {

    public void exec() throws Reply501Exception, Reply421Exception, Reply530Exception, Reply502Exception {
        if (!hasArg()) {
            invalidCurrentCommand();
            throw new Reply501Exception("Need an account as argument");
        }
        String account = getArg();
        NextCommandReply nextCommandReply;
        try {
            nextCommandReply = getSession().getAuth().setAccount(account);
        } catch (Reply530Exception e) {
            getSession().reinitFtpAuth();
            throw e;
        }
        setExtraNextCommand(nextCommandReply.command);
        getSession().setReplyCode(nextCommandReply.reply, nextCommandReply.message);
    }
}
