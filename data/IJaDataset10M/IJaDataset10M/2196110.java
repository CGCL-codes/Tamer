package fr.umlv.jee.hibou.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import fr.umlv.jee.hibou.bdd.RSSFeedManager;
import fr.umlv.jee.hibou.bdd.exception.HibouTechnicalException;
import fr.umlv.jee.hibou.bdd.table.RSSFeed;

/**
 * This class provides methods to build a rss feed.
 * @author micka, alex, nak, matt
 *
 */
public class RSSFeedBuilder {

    private static SyndFeed createFeed() {
        SyndFeed feed = null;
        feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("Hibou rss feed");
        feed.setLink("Hibou.org");
        feed.setDescription("This feed have been created with rome");
        feed.setEncoding("ISO-8859-1");
        return feed;
    }

    private static SyndEntry createEntry(String title, String content, Date publicationDate) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(title);
        entry.setPublishedDate(publicationDate);
        SyndContent description = new SyndContentImpl();
        description.setValue(content);
        description.setType("text/html");
        entry.setDescription(description);
        return entry;
    }

    /**
	 * Creates a rss feed with data from bdd
	 * @param nbItems number of last item to add to rss feed
	 * @return Returns the rss feed as a String
	 * @throws HibouTechnicalException 
	 */
    public static String createFeedFromBdd(int nbItems) throws HibouTechnicalException {
        SyndFeed feed = createFeed();
        RSSFeedManager manager = new RSSFeedManager();
        List<RSSFeed> bddFeed = manager.getLastRSSItems(nbItems);
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (RSSFeed f : bddFeed) {
            SyndEntry entry = createEntry(f.getTitle(), f.getDescription(), f.getPublicationDate());
            entries.add(entry);
        }
        feed.setEntries(entries);
        SyndFeedOutput output = new SyndFeedOutput();
        String rssFeed = null;
        try {
            rssFeed = output.outputString(feed);
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return rssFeed;
    }
}
