package org.boticelli.plugin.twitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.boticelli.Bot;
import org.boticelli.auth.UserAction;
import org.boticelli.auth.UserActionExecutor;
import org.boticelli.dao.TwitterDAO;
import org.boticelli.model.TwitterAuth;
import org.boticelli.model.TwitterEntry;
import org.boticelli.plugin.HelpfulBoticelliPlugin;
import org.boticelli.plugin.PluginResult;
import org.boticelli.plugin.dist.AbstractBotAwarePlugin;
import org.boticelli.plugin.dist.ResponseMode;
import org.boticelli.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.commands.MessageCommand;

public class TwitterPlugin extends AbstractBotAwarePlugin implements HelpfulBoticelliPlugin {

    private static Logger log = LoggerFactory.getLogger(TwitterPlugin.class);

    private static final String TWEET_COMMAND_PREFIX = "!tweet ";

    private static final String FOLLOW_COMMAND_PREFIX = "!follow ";

    private static final String FOLLOWING_COMMAND_PREFIX = "!following";

    private static final String UNFOLLOW_COMMAND_PREFIX = "!unfollow ";

    private static final String DEL_TWEET_COMMAND_PREFIX = "!deltweet ";

    private static final int MILLIS_PER_MINUTE = 60 * 1000;

    private int updateIntervalMillis = 3 * MILLIS_PER_MINUTE;

    private TwitterDAO twitterDAO;

    private long lastChecked = System.currentTimeMillis() - updateIntervalMillis + 10000;

    private TwitterAPI api;

    static final Pattern QUOTE_PATTERN = Pattern.compile("^(\\s*<.*>|\\s*\\*).*");

    private ShortURLResolver shortURLResolver;

    private UserActionExecutor userActionExecutor;

    @Required
    public void setUserActionExecutor(UserActionExecutor userActionExecutor) {
        this.userActionExecutor = userActionExecutor;
    }

    @Required
    public void setTwitterAPI(TwitterAPI twitterAPIImpl) {
        this.api = twitterAPIImpl;
    }

    public void setShortURLResolver(ShortURLResolver shortURLResolver) {
        this.shortURLResolver = shortURLResolver;
    }

    @Required
    public void setTwitterDAO(TwitterDAO twitterDAO) {
        this.twitterDAO = twitterDAO;
    }

    /**
     * Sets the interval in minutes for which to fetch tweets.
     * 
     * @param updateInterval
     */
    public void setUpdateInterval(int updateInterval) {
        this.updateIntervalMillis = updateInterval * MILLIS_PER_MINUTE;
    }

    public String getHelpName() {
        return "tweet";
    }

    public String helpText(Bot bot, List<String> args) {
        return "Sends or receives tweets from twitter via twitter account for the bot.\n" + "\002!tweet <message>\002 to make me tweet something citing your name if the message is no IRC quote.\n" + "\002!follow user\002 to make me follow someone.\n" + "\002!unfollow <user>\\002 to make me stop following someone.\n" + "\002!following\002 to list the twitter user I currently follow.\n" + "\002!deltweet <id>\002 to list the twitter user I currently follow.\n";
    }

    private boolean printedDisableMsg;

    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        if (!api.isConfigured() && !printedDisableMsg) {
            log.info("TwitterPlugin configured to user NONE. Disabling plugin..");
            printedDisableMsg = true;
        }
        if (api.isConfigured()) {
            handleCommands(bot, command);
            long now = System.currentTimeMillis();
            if (now - lastChecked > updateIntervalMillis) {
                log.debug("checking for new tweets on {}", command);
                lastChecked = now;
                checkForNewMessages(bot, getBot().getNick(), false);
            }
        }
        return PluginResult.NEXT;
    }

    private void handleCommands(Bot bot, InCommand command) {
        if (command instanceof MessageCommand) {
            MessageCommand msg = (MessageCommand) command;
            String nick = msg.getSource().getNick();
            String message = msg.getMessage();
            if (message.startsWith(TWEET_COMMAND_PREFIX)) {
                message = message.substring(TWEET_COMMAND_PREFIX.length()).trim();
                String response = sendTweet(bot, msg.getSource().getNick(), message, msg.isPrivateToUs(getBot().getState()));
                bot.respond(msg, response);
            } else if (message.startsWith(FOLLOW_COMMAND_PREFIX)) {
                List<String> args = Util.split(message.substring(FOLLOW_COMMAND_PREFIX.length()).trim(), " ");
                if (args.size() >= 1) {
                    String name = args.remove(0);
                    String desc = "";
                    if (args.size() > 0) {
                        desc = Util.join(args, " ");
                    }
                    String response = startToFollow(getBot().getNick(), name, desc);
                    bot.respond(msg, response);
                }
            } else if (message.startsWith(FOLLOWING_COMMAND_PREFIX)) {
                listFollowing(bot, nick, msg);
            } else if (message.startsWith(UNFOLLOW_COMMAND_PREFIX)) {
                String user = message.substring(UNFOLLOW_COMMAND_PREFIX.length()).trim();
                if (user.length() > 0 && user.indexOf(" ") == -1) {
                    String response = stopFollowing(getBot().getNick(), user);
                    bot.respond(msg, response);
                }
            } else if (message.startsWith(DEL_TWEET_COMMAND_PREFIX)) {
                String id = message.substring(DEL_TWEET_COMMAND_PREFIX.length()).trim();
                if (id.length() > 0 && id.indexOf(" ") == -1) {
                    String response = deleteTweet(getBot().getNick(), id);
                    bot.respond(msg, response);
                }
            }
        }
    }

    private String deleteTweet(String nick, String id) {
        try {
            TwitterEntry entry = getTwitterEntryFor(nick);
            Map<String, Object> status = api.deleteTweet(entry, id);
            if (status != null) {
                return "Deleted tweet with id " + id + ".";
            }
            return null;
        } catch (TwitterException e) {
            return e.getMessage();
        }
    }

    private void listFollowing(Bot bot, String nick, MessageCommand cmd) {
        TwitterEntry entry = getTwitterEntryFor(nick);
        List<String> followers = api.showFriends(entry);
        if (followers != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Currently following ");
            boolean first = true;
            for (Iterator<String> i = followers.iterator(); i.hasNext(); ) {
                String name = i.next();
                if (!first) {
                    sb.append(i.hasNext() ? ", " : " and ");
                }
                sb.append(name);
                first = false;
            }
            bot.respond(cmd, sb.toString());
        }
    }

    public boolean supports(Class<? extends InCommand> inCommandType) {
        return true;
    }

    private String startToFollow(String nick, String name, String desc) {
        try {
            TwitterEntry entry = getTwitterEntryFor(nick);
            Map<String, Object> user = api.befriend(entry, name);
            if (user != null) {
                if (desc.length() == 0) {
                    desc = (String) user.get("description");
                }
                return "Now following " + name + " ( " + desc + ")";
            }
            return null;
        } catch (TwitterException e) {
            return e.getMessage();
        }
    }

    private String stopFollowing(String nick, String name) {
        try {
            TwitterEntry entry = getTwitterEntryFor(nick);
            Map<String, Object> user = api.unfriend(entry, name);
            if (user != null) {
                return "Stopped following " + name + ".";
            }
            return null;
        } catch (TwitterException e) {
            return e.getMessage();
        }
    }

    private String sendTweet(Bot bot, String nick, String message, boolean privately) {
        try {
            TwitterEntry entry = getTwitterEntryFor(nick);
            String botNick = getBot().getNick();
            if (entry.getNick().equals(botNick) && !nick.equals(botNick)) {
                message = "<" + nick + "> " + message;
            }
            if (message.length() > 140) {
                throw new TweetTooLongException("Message too long. Cut to 140 chars it would end: '" + message.substring(120, 140) + "'");
            }
            TweetAction action = new TweetAction(entry, message, privately);
            if (entry.getNick().equals(botNick)) {
                action.execute(bot, botNick);
                return null;
            } else {
                userActionExecutor.execute(nick, action);
                return null;
            }
        } catch (TwitterException e) {
            return e.getMessage();
        }
    }

    private void checkForNewMessages(Bot bot, String nick, boolean priv) {
        TwitterEntry entry;
        try {
            entry = getTwitterEntryFor(nick);
        } catch (NoTwitterAuthorizationException e) {
            log.debug("not authorized in checkForNewMessages()");
            return;
        }
        List<Map<String, Object>> updates = api.getFriendsTimeLine(entry, entry.getLastTweetId());
        if (updates.size() > 0) {
            String id = updates.get(0).get("id").toString();
            entry.setLastTweetId(id);
            if (entry.getId() == null) {
                twitterDAO.create(entry);
            } else {
                twitterDAO.update(entry);
            }
        }
        List<String> tweetOut = new ArrayList<String>(updates.size());
        for (Map<String, Object> update : updates) {
            Map<String, Object> user = (Map<String, Object>) update.get("user");
            if (user != null) {
                Object text = update.get("text");
                if (text != null) {
                    tweetOut.add(0, "<[ " + user.get("screen_name") + " ]> " + unescape(text.toString()));
                }
            }
        }
        for (String tweet : tweetOut) {
            if (priv) {
                bot.respond(nick, priv, tweet);
            } else {
                bot.say(tweet);
            }
        }
    }

    private String unescape(String in) {
        in = in.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("\r", " ").replace("\n", " ").replace("  ", " ");
        if (shortURLResolver != null) {
            String out = shortURLResolver.resolveShortURL(in);
            log.debug("resolved '{}' to '{}'", in, out);
            return out;
        } else {
            return in;
        }
    }

    private TwitterEntry getTwitterEntryFor(String nick) {
        List<TwitterEntry> entries = twitterDAO.findByNick(nick);
        if (entries.size() != 0 && entries.get(0).getTwitterAuth() == TwitterAuth.OK) {
            return entries.get(0);
        } else {
            String botNick = getBot().getNick();
            if (nick.equals(botNick)) {
                throw new NoTwitterAuthorizationException("bot not authorized.");
            }
            return getTwitterEntryFor(botNick);
        }
    }

    public class TweetAction implements UserAction {

        private TwitterEntry entry;

        private String message;

        private boolean privately;

        public TweetAction(TwitterEntry entry, String message, boolean privately) {
            super();
            this.entry = entry;
            this.message = message;
            this.privately = privately;
        }

        @Override
        public void execute(Bot bot, String nick) {
            Map<String, Object> status = api.tweet(entry, message);
            if (status == null) {
                throw new TwitterException("No response.");
            }
            bot.respond(nick, privately, "Published tweet id " + status.get("id"));
        }

        @Override
        public String getDescription() {
            return "tweet your message.";
        }

        @Override
        public ResponseMode getResponseMode() {
            return ResponseMode.CHANNEL;
        }
    }
}
