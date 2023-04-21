package org.drftpd.sitebot;

import java.util.ArrayList;
import java.util.StringTokenizer;
import net.sf.drftpd.event.InviteEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.drftpd.GlobalContext;
import org.drftpd.commands.UserManagement;
import org.drftpd.plugins.SiteBot;
import org.drftpd.usermanager.NoSuchUserException;
import org.drftpd.usermanager.User;
import org.drftpd.usermanager.UserFileException;
import org.tanesha.replacer.ReplacerEnvironment;
import f00f.net.irc.martyr.commands.MessageCommand;

/**
 * @author mog
 * @version $Id: Invite.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class Invite extends IRCCommand {

    private static final Logger logger = Logger.getLogger(Invite.class);

    public Invite(GlobalContext gctx) {
        super(gctx);
    }

    public ArrayList<String> doInvite(String args, MessageCommand msgc) {
        ArrayList<String> out = new ArrayList<String>();
        ReplacerEnvironment env = new ReplacerEnvironment(SiteBot.GLOBAL_ENV);
        StringTokenizer st = new StringTokenizer(args);
        if (st.countTokens() < 2) return out;
        String username = st.nextToken();
        String password = st.nextToken();
        User user;
        try {
            user = getGlobalContext().getUserManager().getUserByName(username);
        } catch (NoSuchUserException e) {
            logger.log(Level.WARN, username + " " + e.getMessage(), e);
            return out;
        } catch (UserFileException e) {
            logger.log(Level.WARN, "", e);
            return out;
        }
        boolean success = user.checkPassword(password);
        getGlobalContext().dispatchFtpEvent(new InviteEvent(success ? "INVITE" : "BINVITE", msgc.getSource().getNick(), user));
        String ident = msgc.getSource().getNick() + "!" + msgc.getSource().getUser() + "@" + msgc.getSource().getHost();
        if (success) {
            logger.info("Invited \"" + ident + "\" as user " + user.getName());
            user.getKeyedMap().setObject(UserManagement.IRCIDENT, ident);
            try {
                user.commit();
            } catch (UserFileException e1) {
                logger.warn("Error saving userfile", e1);
            }
        } else {
            logger.log(Level.WARN, msgc.getSourceString() + " attempted invite with bad password: " + msgc);
        }
        return out;
    }
}
