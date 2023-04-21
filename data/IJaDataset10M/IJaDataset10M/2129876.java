package org.boticelli.plugin.dist;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.boticelli.Bot;
import org.boticelli.auth.UserAction;
import org.boticelli.auth.UserActionExecutor;
import org.boticelli.plugin.BotAware;
import org.boticelli.plugin.BoticelliPlugin;
import org.boticelli.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.commands.NoticeCommand;

/**
 * This is the default user action executor. It ensures that the actor of an
 * {@link UserAction} is indeed authorized to do this action. This means he
 * <ul>
 * <li>has to be joined in the channel where the bot is</li>
 * <li>has to be identified with NickServ</li>
 * </ul>
 * 
 * @author shelmberger
 */
public class AthemeNickServBasedUserActionExecutor extends AbstractUserActionExecutor implements UserActionExecutor, BoticelliPlugin, BotAware {

    private static Logger log = LoggerFactory.getLogger(AthemeNickServBasedUserActionExecutor.class);

    private static final Pattern ACC_PATTERN = Pattern.compile("^([^ ]*) ACC ([0-9]*).*");

    @Override
    protected AuthenticationResponse matchResponse(Bot bot, InCommand command) {
        try {
            NoticeCommand msg = (NoticeCommand) command;
            String notice = msg.getNotice();
            String user = msg.getFrom().getNick();
            if (user.equals(nickServNick)) {
                Matcher m = ACC_PATTERN.matcher(notice);
                if (m.matches()) {
                    String nick = m.group(1);
                    boolean registered = Integer.parseInt(m.group(2)) == 3;
                    UserAction action = actions.get(nick);
                    if (action != null) {
                        return new AuthenticationResponse(nick, registered);
                    } else {
                        log.warn("no action for nickserv response for user " + nick);
                    }
                }
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    @Override
    public boolean supports(Class<? extends InCommand> inCommandType) {
        return NoticeCommand.class.isAssignableFrom(inCommandType);
    }

    @Override
    protected void initiateAction(String nick, UserAction action) {
        getBot().sendMessage(nickServNick, "ACC " + nick);
    }
}
