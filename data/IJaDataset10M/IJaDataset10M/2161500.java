package goldengate.ftp.core.command.extension;

import goldengate.common.command.ReplyCode;
import goldengate.common.command.exception.CommandAbstractException;
import goldengate.common.command.exception.Reply501Exception;
import goldengate.common.digest.FilesystemBasedDigest;
import goldengate.ftp.core.command.AbstractCommand;

/**
 * XMD5 command: takes a filename and returns the MD5 of the file
 *
 * @author Frederic Bregier
 *
 */
public class XMD5 extends AbstractCommand {

    public void exec() throws CommandAbstractException {
        if (!hasArg()) {
            invalidCurrentCommand();
            throw new Reply501Exception("Need a pathname as argument");
        }
        String filename = getArg();
        String crc = FilesystemBasedDigest.getHex(getSession().getDir().getMD5(filename));
        getSession().setReplyCode(ReplyCode.REPLY_250_REQUESTED_FILE_ACTION_OKAY, crc + " \"" + filename + "\" MD5");
    }
}
