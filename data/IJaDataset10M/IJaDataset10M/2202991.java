package org.boticelli.plugin.dist;

import java.util.List;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.log4j.Logger;
import org.boticelli.Bot;
import org.boticelli.auth.BoticelliUserDetails;
import org.boticelli.auth.UserAction;
import org.boticelli.auth.UserActionExecutor;
import org.boticelli.dao.UserDAO;
import org.boticelli.model.User;
import org.boticelli.plugin.HelpfulBoticelliPlugin;
import org.boticelli.plugin.PluginResult;
import org.boticelli.util.RandomUtil;
import org.springframework.beans.factory.annotation.Required;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.commands.MessageCommand;
import f00f.net.irc.martyr.commands.NoticeCommand;

public class AccountCreator implements HelpfulBoticelliPlugin {

    private static final String PASSWORD_COMMAND = "showmeyourbits";

    protected static Logger log = Logger.getLogger(AccountCreator.class);

    private PasswordEncoder passwordEncoder;

    private SaltSource saltSource;

    private UserDAO userDAO;

    private UserActionExecutor userActionExecutor;

    @Required
    public void setUserActionExecutor(UserActionExecutor userActionExecutor) {
        this.userActionExecutor = userActionExecutor;
    }

    @Required
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Required
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Required
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    public String getHelpName() {
        return PASSWORD_COMMAND;
    }

    public String helpText(Bot bot, List<String> args) {
        return "/msg " + bot.getNick() + " showmeyourbits to create a new password.";
    }

    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        if (command instanceof MessageCommand) {
            MessageCommand msg = (MessageCommand) command;
            if (msg.isPrivateToUs(bot.getState())) {
                String message = msg.getMessage();
                if (message.toLowerCase().startsWith(PASSWORD_COMMAND)) {
                    String password = null;
                    if (message.length() > PASSWORD_COMMAND.length()) {
                        password = message.substring(PASSWORD_COMMAND.length()).trim();
                        if (password.length() == 0) {
                            password = null;
                        }
                    }
                    userActionExecutor.execute(msg.getSource().getNick(), new CreateOrResetUserPasswordAction(password));
                }
            }
        }
        return PluginResult.NEXT;
    }

    public boolean supports(Class<? extends InCommand> inCommandType) {
        return MessageCommand.class.isAssignableFrom(inCommandType) || NoticeCommand.class.isAssignableFrom(inCommandType);
    }

    private class CreateOrResetUserPasswordAction implements UserAction {

        private String password;

        public CreateOrResetUserPasswordAction() {
            this(null);
        }

        public CreateOrResetUserPasswordAction(String password) {
            this.password = password;
        }

        public void execute(Bot bot, String nick) {
            if (password == null) {
                password = RandomUtil.createWord(10);
            }
            User u = userDAO.getUniqueByField("name", nick);
            boolean alreadyExists = u != null;
            if (!alreadyExists) {
                u = new User();
                u.setName(nick);
                u.setRoles("ROLE_USER");
            }
            String hash = passwordEncoder.encodePassword(password, saltSource.getSalt(new BoticelliUserDetails(u)));
            u.setPassword(hash);
            if (alreadyExists) {
                userDAO.update(u);
                bot.say("Agent " + u.getName() + " forgot his password but can now continue his investigations");
                bot.sendMessage(nick, "Your new password is: " + password);
            } else {
                userDAO.create(u);
                bot.sayAction("creates a new account for agent " + u.getName());
                bot.sendMessage(nick, "Your password is: " + password);
            }
        }

        public ResponseMode getResponseMode() {
            return ResponseMode.PRIVATE;
        }

        public void execute(Bot bot, String nick, ResponseMode mode) {
        }

        public String getDescription() {
            return "receive a (new) password";
        }
    }
}
