package net.sf.jerkbot.plugins.feeds;

import jerklib.Channel;
import jerklib.Session;
import net.sf.jerkbot.bot.BotService;
import net.sf.jerkbot.util.IOUtil;
import org.apache.commons.feedparser.DefaultFeedParserListener;
import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         Feed Poller task
 * @version 0.0.1
 */
public class FeedPollerTask implements Runnable {

    /**
     * The Constant LOG.
     */
    private static final Logger Log = LoggerFactory.getLogger(FeedPollerTask.class.getName());

    private static final int MAX_FEEDS = 1;

    private static final int TITLE_MAX_LEN = 50;

    private static final String HASH_ALGORITHM = "MD5";

    private int counter = 0;

    /**
     * The last digest.
     */
    byte[] lastDigest = null;

    private String feedURL;

    private FeedParser parser;

    private BotService botService;

    /**
     * Create a new feed fetcher
     */
    public FeedPollerTask(BotService botService, String feedURL) {
        this.botService = botService;
        this.feedURL = feedURL;
        try {
            parser = FeedParserFactory.newFeedParser();
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
    }

    public void run() {
        counter = 0;
        Log.debug("Fetching news");
        Session session = botService.getSession();
        if (session == null) {
            Log.warn("No current IRC session");
            return;
        }
        final List<Channel> channels = session.getChannels();
        if (channels.isEmpty()) {
            Log.warn("No channel for the current IRC session");
            return;
        }
        if (StringUtils.isEmpty(feedURL)) {
            Log.warn("No feed provided");
            return;
        }
        Log.debug("Creating feedListener");
        FeedParserListener feedParserListener = new DefaultFeedParserListener() {

            public void onChannel(FeedParserState state, String title, String link, String description) throws FeedParserException {
                Log.debug("onChannel:" + title + "," + link + "," + description);
            }

            public void onItem(FeedParserState state, String title, String link, String description, String permalink) throws FeedParserException {
                if (counter >= MAX_FEEDS) {
                    throw new FeedPollerCancelException("Maximum number of items reached");
                }
                boolean canAnnounce = false;
                try {
                    if (lastDigest == null) {
                        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
                        md.update(title.getBytes());
                        lastDigest = md.digest();
                        canAnnounce = true;
                    } else {
                        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
                        md.update(title.getBytes());
                        byte[] currentDigest = md.digest();
                        if (!MessageDigest.isEqual(currentDigest, lastDigest)) {
                            lastDigest = currentDigest;
                            canAnnounce = true;
                        }
                    }
                    if (canAnnounce) {
                        String shortTitle = title;
                        if (shortTitle.length() > TITLE_MAX_LEN) {
                            shortTitle = shortTitle.substring(0, TITLE_MAX_LEN) + " ...";
                        }
                        String shortLink = IOUtil.getTinyUrl(link);
                        Log.debug("Link:" + shortLink);
                        for (Channel channel : channels) {
                            channel.say(String.format("%s, %s", shortTitle, shortLink));
                        }
                    }
                } catch (Exception e) {
                    throw new FeedParserException(e);
                }
                counter++;
            }

            public void onCreated(FeedParserState state, Date date) throws FeedParserException {
            }
        };
        if (parser != null) {
            InputStream is = null;
            try {
                Log.debug("Reading feedURL");
                is = new URL(feedURL).openStream();
                parser.parse(feedParserListener, is, feedURL);
                Log.debug("Parsing done");
            } catch (IOException ioe) {
                Log.error(ioe.getMessage(), ioe);
            } catch (FeedPollerCancelException fpce) {
            } catch (FeedParserException e) {
                for (Channel channel : channels) {
                    channel.say(e.getMessage());
                }
            } finally {
                IOUtil.closeQuietly(is);
            }
        } else {
            Log.warn("Wasn't able to create feed parser");
        }
    }
}
