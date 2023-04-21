package com.simpleftp.ftp.server.client.command;

import com.simpleftp.ftp.server.command.AbstractCommand;
import com.simpleftp.ftp.server.command.ICommand;
import com.simpleftp.ftp.server.objects.FtpUserSession;

public class ClientEXITCommand extends AbstractCommand implements ICommand {

    public ClientEXITCommand(String cmd) {
        super(cmd);
    }

    public boolean execute(FtpUserSession session) {
        return false;
    }

    public void setParameter(String[] params) {
    }

    public String getHelp() {
        return "Exit the command prompt";
    }
}
