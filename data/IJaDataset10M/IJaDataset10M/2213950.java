package com.simpleftp.ftp.server.command;

import com.simpleftp.ftp.server.objects.FtpReply;
import com.simpleftp.ftp.server.objects.FtpUserSession;
import com.simpleftp.ftp.server.utils.TextUtil;

public class AbstractPWDCommand extends AbstractCommand {

    AbstractPWDCommand(String cmd) {
        super(cmd);
    }

    public boolean execute(FtpUserSession session) {
        String pwd = session.getUser().getPwd();
        if (TextUtil.isEmpty(pwd)) {
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(550));
            return false;
        }
        session.getControlConnection().scheduleSend(FtpReply.getFtpReply(257, pwd + " is the pwd"));
        return true;
    }

    public void setParameter(String[] params) {
    }
}
