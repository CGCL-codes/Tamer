package org.boticelli.plugin.dist;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.boticelli.Bot;
import org.boticelli.auth.UserAction;
import org.boticelli.auth.UserActionExecutor;
import org.boticelli.dao.SeenDAO;
import org.boticelli.dao.UserDAO;
import org.boticelli.model.SeenEntry;
import org.boticelli.model.User;
import org.boticelli.plugin.HelpfulBoticelliPlugin;
import org.boticelli.plugin.PluginResult;
import org.boticelli.util.Util;
import org.springframework.beans.factory.annotation.Required;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.clientstate.Channel;
import f00f.net.irc.martyr.clientstate.Member;
import f00f.net.irc.martyr.commands.MessageCommand;
import f00f.net.irc.martyr.util.FullNick;

public class SeenPlugin implements HelpfulBoticelliPlugin {

    private static final int MILLIS_PER_HOUR = 60 * 60 * 1000;

    protected static Logger log = Logger.getLogger(SeenPlugin.class);

    private int syncInterval = 60;

    private SeenDAO seenDAO;

    private UserDAO userDAO;

    private Map<String, SeenEntry> seenCache = new HashMap<String, SeenEntry>();

    private long lastSync;

    private UserActionExecutor userActionExecutor;

    private List<String> availableTimeZoneIds;

    @Required
    public void setUserActionExecutor(UserActionExecutor userActionExecutor) {
        this.userActionExecutor = userActionExecutor;
    }

    @Required
    public void setSeenDAO(SeenDAO seenDAO) {
        this.seenDAO = seenDAO;
    }

    @Required
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Sets the interval in seconds after which the SeenPlugin synchronizes with
     * the database.
     *
     * @param syncInterval
     */
    public void setSyncInterval(int syncInterval) {
        this.syncInterval = syncInterval;
    }

    public String getHelpName() {
        return "seen";
    }

    public String helpText(Bot bot, List<String> args) {
        return "\002!seen nick [timezone]\002 to tells you when the nick was last seen in the given timezone and what the last message was.\nWorks both in the channel and as private message.\nthe timezone is remembered for registered users";
    }

    public boolean supports(Class<? extends InCommand> inCommandType) {
        return true;
    }

    public boolean isOnline(Bot bot, String nick) {
        Channel channel = bot.getState().getChannel(bot.getChannelName());
        for (Enumeration e = channel.getMembers(); e.hasMoreElements(); ) {
            Member member = (Member) e.nextElement();
            if (member.getNick().getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        long now = System.currentTimeMillis();
        if (now - lastSync > syncInterval * 1000l) {
            synchWithDB();
            lastSync = now;
        }
        if (command instanceof MessageCommand) {
            MessageCommand messageCommand = (MessageCommand) command;
            String message = messageCommand.getMessage();
            String nick = messageCommand.getSource().getNick();
            String lowerNick = nick.toLowerCase();
            if (!messageCommand.isPrivateToUs(bot.getState())) {
                SeenEntry seen = seenCache.get(lowerNick);
                if (seen == null) {
                    seen = new SeenEntry();
                    seen.setNick(nick);
                }
                seen.setLastSeen(new Timestamp(System.currentTimeMillis()));
                seenCache.put(lowerNick, seen);
                String msg;
                if (message.length() > 255) {
                    msg = message.substring(0, 255);
                } else {
                    msg = message;
                }
                seen.setLastMessage(msg);
            }
            final String seenPrefix = "!seen ";
            if (message.startsWith(seenPrefix)) {
                String out = handleSeen(bot, message.substring(seenPrefix.length()).trim(), messageCommand.getSource());
                if (messageCommand.isPrivateToUs(bot.getState())) {
                    bot.sendMessage(messageCommand.getSource(), out);
                } else {
                    bot.say(out);
                }
            }
        }
        return PluginResult.NEXT;
    }

    private void synchWithDB() {
        try {
            for (Map.Entry<String, SeenEntry> e : seenCache.entrySet()) {
                String name = e.getKey().toLowerCase();
                SeenEntry seen = e.getValue();
                Timestamp lastSeen = seen.getLastSeen();
                if (lastSeen == null) {
                    log.debug("lastSeen == null on " + seen);
                }
                if (lastSeen != null && lastSeen.getTime() > lastSync) {
                    if (seen.getId() == null) {
                        SeenEntry dbSeen = seenDAO.getUniqueByFieldIgnoreCase("nick", seen.getNick());
                        if (dbSeen != null) {
                            dbSeen.setLastSeen(lastSeen);
                            String message = seen.getLastMessage();
                            if (message.length() > 255) {
                                message = message.substring(0, 255);
                            }
                            dbSeen.setLastMessage(message);
                            seenDAO.update(dbSeen);
                            seenCache.put(name, dbSeen);
                        } else {
                            seenDAO.create(seen);
                        }
                    } else {
                        seenDAO.update(seen);
                    }
                }
            }
        } catch (RuntimeException e) {
            log.error("Error synching seen entries to the database, clearing seen cache", e);
            seenCache.clear();
        }
    }

    private String handleSeen(Bot bot, String args, FullNick fullNick) {
        List<String> words = Util.splitWords(args);
        if (words.size() == 0) {
            return "uh?";
        }
        String nick = words.remove(0);
        String actor = fullNick.getNick();
        User actorUser = userDAO.getUniqueByField("name", actor);
        TimeZone timeZone = TimeZone.getDefault();
        if (words.size() > 0) {
            if (availableTimeZoneIds == null) {
                availableTimeZoneIds = Arrays.asList(TimeZone.getAvailableIDs());
            }
            String id = Util.joinWords(words);
            if (availableTimeZoneIds.contains(id)) {
                timeZone = TimeZone.getTimeZone(id);
            } else {
                bot.say("I don't know the time zone \"" + id + "\"");
            }
        } else {
            if (actorUser != null) {
                timeZone = actorUser.getTimeZoneObject();
            }
        }
        String lowerNick = nick.toLowerCase();
        if (nick.equalsIgnoreCase(bot.getNick())) {
            return ("Yes, I'm here.");
        } else {
            SeenEntry seen = seenCache.get(lowerNick);
            if (seen == null) {
                seen = seenDAO.getUniqueByFieldIgnoreCase("nick", nick);
                if (seen != null) {
                    seenCache.put(lowerNick, seen);
                }
            }
            if (actorUser != null) {
                userActionExecutor.execute(actor, new SetUserTimeZoneAction(actorUser, timeZone.getID()));
            }
            if (seen != null) {
                DateFormat format = null;
                int tzOffset = timeZone.getOffset(System.currentTimeMillis());
                if (log.isDebugEnabled()) {
                    log.debug("time zone offset : " + tzOffset);
                }
                if (tzOffset <= -5 * MILLIS_PER_HOUR && tzOffset >= -8 * MILLIS_PER_HOUR) {
                    format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
                } else {
                    format = new SimpleDateFormat("dd.MMM.yyyy, HH:mm:ss");
                }
                format.setTimeZone(timeZone);
                String lastSeen = format.format(seen.getLastSeen());
                if (isOnline(bot, nick)) {
                    return (nick + " is currently online and last spoke on " + lastSeen + ", saying " + seen.getLastMessage());
                } else {
                    return (nick + " was last seen on " + lastSeen + ", saying " + seen.getLastMessage());
                }
            } else {
                return ("I know nothing about " + nick + ".");
            }
        }
    }

    private class SetUserTimeZoneAction implements UserAction {

        private String timeZoneId;

        private User user;

        public SetUserTimeZoneAction(User user, String timeZoneId) {
            this.user = user;
            this.timeZoneId = timeZoneId;
        }

        public void execute(Bot bot, String nick) {
            user.setTimeZone(timeZoneId);
            userDAO.update(user);
        }

        public String getDescription() {
            return null;
        }

        public ResponseMode getResponseMode() {
            return ResponseMode.SILENT;
        }
    }
}
