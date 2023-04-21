package sample.sidewiki;

import com.google.gdata.util.common.base.CharEscapers;
import com.google.gdata.client.sidewiki.SidewikiService;
import com.google.gdata.data.Content;
import com.google.gdata.data.IContent;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.sidewiki.SidewikiAuthor;
import com.google.gdata.data.sidewiki.SidewikiEntry;
import com.google.gdata.data.sidewiki.SidewikiEntryFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Demonstrates interactions with the Google Sidewiki Data API's entry feeds
 * using the Java client library.
 */
public class SidewikiEntryFeedDemo {

    private static final String SIDEWIKI_FEEDS_URL_BASE = "http://www.google.com/sidewiki/feeds";

    private static final String ENTRIES_FEEDS_URL_BASE = SIDEWIKI_FEEDS_URL_BASE + "/entries";

    private static final String AUTHORS_FEED_URL_SUFFIX_FORMAT = "/author/{author_id}/full";

    private static final String WEBPAGE_FEED_URL_SUFFIX_FORMAT = "/webpage/{url}/full";

    /**
   * Utility classes should not have a public or default constructor.
   */
    private SidewikiEntryFeedDemo() {
    }

    /**
   * Returns URL of the feed of entries written by author {@code authorId}.
   * 
   * @param authorId author Profile ID
   * @return URL of the feed of entries written by given author
   * @throws MalformedURLException if error happens during URL construction
   */
    private static URL getEntriesByAuthorFeedUrl(String authorId) throws MalformedURLException {
        return new URL(ENTRIES_FEEDS_URL_BASE + AUTHORS_FEED_URL_SUFFIX_FORMAT.replace("{author_id}", authorId));
    }

    /**
   * Returns URL of the feed of entries written for web page {@code webpage}.
   *
   * @param webpage unescaped web page URL
   * @return URL of the feed of entries written for given web page
   * @throws MalformedURLException if error happens during URL construction
   */
    private static URL getEntriesForWebpageFeedUrl(String webpage) throws MalformedURLException {
        return new URL(ENTRIES_FEEDS_URL_BASE + WEBPAGE_FEED_URL_SUFFIX_FORMAT.replace("{url}", CharEscapers.uriEscaper().escape(webpage)));
    }

    /**
   * Prints Sidewiki entries in the feed specified by the given URL.
   *
   * @param service an authenticated SidewikiService object
   * @param feedUrl the URL of a Sidewiki entry feed to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   * @return a set of author IDs who wrote entries found in given feed
   */
    private static Set<String> printSidewikiEntries(SidewikiService service, URL feedUrl) throws IOException, ServiceException {
        Set<String> authors = new HashSet<String>();
        SidewikiEntryFeed resultFeed = service.getFeed(feedUrl, SidewikiEntryFeed.class);
        System.out.println(resultFeed.getTitle().getPlainText());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            SidewikiEntry entry = resultFeed.getEntries().get(i);
            printEntry(entry);
            SidewikiAuthor author = (SidewikiAuthor) entry.getAuthors().get(0);
            authors.add(author.getResourceId().getValue());
        }
        return authors;
    }

    /**
   * Returns plain text representation of content {@code c}.
   *
   * @param c content object to convert to plain text
   * @return content string or "{@code null}" if {@code c} is {@code null}
   */
    private static String contentToString(Content c) {
        if (c == null) {
            return "null";
        }
        switch(c.getType()) {
            case IContent.Type.TEXT:
                return ((TextContent) c).getContent().getPlainText();
            default:
                return "(" + c.getType() + ")";
        }
    }

    /**
   * Prints Sidewiki entry to standard output, including its author,
   * title and content.
   *
   * @param entry Sidewiki entry to print
   */
    private static void printEntry(SidewikiEntry entry) {
        SidewikiAuthor author = (SidewikiAuthor) entry.getAuthors().get(0);
        System.out.println("Sidewiki entry by " + author.getName() + ": \"" + entry.getTitle().getPlainText() + "\" - \"" + contentToString(entry.getContent()) + "\"\n");
    }

    /**
   * Instantiates a SidewikiService object and uses the command line arguments
   * to authenticate. The SidewikiService object is used to demonstrate
   * interactions with the Google Sidewiki Data API's feeds.
   *
   * @param args must be length 3 and contain a valid username/password and web page url
   */
    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
            return;
        }
        String userName = args[0];
        String userPassword = args[1];
        String webpage = args[2];
        SidewikiService service = new SidewikiService("demo-SidewikiEntryFeedDemo-1");
        try {
            service.setUserCredentials(userName, userPassword);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("Entries for web page \"" + webpage + "\":");
            URL webpageFeedUrl = getEntriesForWebpageFeedUrl(webpage);
            Set<String> authorIds = printSidewikiEntries(service, webpageFeedUrl);
            for (String authorId : authorIds) {
                URL authorEntriesFeedUrl = getEntriesByAuthorFeedUrl(authorId);
                printSidewikiEntries(service, authorEntriesFeedUrl);
            }
            System.out.println("My Sidewiki entries:");
            URL myEntriesFeedUrl = getEntriesByAuthorFeedUrl("me");
            printSidewikiEntries(service, myEntriesFeedUrl);
        } catch (IOException e) {
            System.err.println("There was a problem communicating with the service.");
            e.printStackTrace();
        } catch (ServiceException e) {
            System.err.println("The server had a problem handling your request.");
            e.printStackTrace();
        }
    }

    /**
   * Prints the command line usage of this sample application.
   */
    private static void usage() {
        System.out.println("Syntax: SidewikiEntryFeedDemo <username> <password> <web page url>");
        System.out.println("\nPrints entries written for given web page. " + "The username and password are used for authentication.");
    }
}
