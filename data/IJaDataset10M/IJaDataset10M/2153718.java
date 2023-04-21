package org.sd_network.vfsshell.command;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.sd_network.vfs.AuthenticationException;
import org.sd_network.vfs.VfsContext;
import org.sd_network.vfs.VfsIOException;
import org.sd_network.vfs.VfsService;
import org.sd_network.vfsshell.CommandHandlerBase;
import org.sd_network.vfsshell.Session;
import org.sd_network.vfsshell.util.ConsoleUtil;

public class Logout extends CommandHandlerBase {

    /** Logger. */
    private static final Logger _log = Logger.getLogger(Logout.class.getName());

    public void execute(String[] args) {
        Session session = Session.getInstance();
        String sessionID = session.getSessionID();
        if (sessionID == null) System.out.println("You have already logged out.");
        VfsService vfsService = VfsContext.getService();
        try {
            vfsService.logout(sessionID);
        } catch (VfsIOException e) {
            System.out.println("ERROR: some file session close failed.");
        }
        session.clearSessionID();
        System.out.println("You were logged out.");
    }
}
