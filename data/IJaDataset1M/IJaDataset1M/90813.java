package net.sourceforge.pebble.logging;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a referer URL along with a count of how many times
 * it has been refered from.
 *
 * @author    Simon Brown
 */
public class Referer extends CountedUrl {

    /** regular expression to pull out the query from a Google referer */
    private static final Pattern GOOGLE_QUERY_STRING_PATTERN = Pattern.compile("[?&]q=[^&]+&*");

    /** the prefix for all Google referers */
    private static final String GOOGLE_PREFIX = "http://www.google.";

    /** regular expression to pull out the query from a Google referer */
    private static final Pattern GOOGLE_IMAGES_QUERY_STRING_PATTERN = Pattern.compile("[?&]prev=[^&]+&*");

    /** the prefix for all Google referers */
    private static final String GOOGLE_IMAGES_PREFIX = "http://images.google.";

    /** regular expression to pull out the query from a Yahoo! referer */
    private static final Pattern YAHOO_QUERY_STRING_PATTERN = Pattern.compile("[?&]p=[^&]+&*");

    /** the prefix for all Google referers */
    private static final String YAHOO_PREFIX = "http://search.yahoo.";

    /** regular expression to pull out the query from an MSN referer */
    private static final Pattern MSN_QUERY_STRING_PATTERN = Pattern.compile("[?&]q=[^&]+&*");

    /** the prefix for all Google referers */
    private static final String MSN_PREFIX = "http://search.msn.";

    /** the prefix for all Google referers */
    private static final String MSN_BETA_PREFIX = "http://beta.search.msn.";

    /** pattern for java.blogs welcome page referers */
    private static final Pattern JAVABLOGS_WELCOME_PATTERN = Pattern.compile(".*javablogs.com/Welcome.*");

    /** pattern for java.blogs hot entries page referers */
    private static final Pattern JAVABLOGS_HOT_ENTRIES_PATTERN = Pattern.compile(".*javablogs.com/ViewHotBlogEntries.*");

    /**
   * Creates a new instance representing the specified url.
   *
   * @param url   the url as a String
   */
    public Referer(String url) {
        super(url);
    }

    protected void setUrl(String url) {
        super.setUrl(url);
        if (url == null || url.length() == 0) {
            setName("None");
        } else if (url.length() > GOOGLE_PREFIX.length() && url.substring(0, GOOGLE_PREFIX.length()).equalsIgnoreCase(GOOGLE_PREFIX)) {
            String query = extractQuery(GOOGLE_QUERY_STRING_PATTERN, url);
            setName("Google : " + query);
        } else if (url.length() > GOOGLE_IMAGES_PREFIX.length() && url.substring(0, GOOGLE_IMAGES_PREFIX.length()).equalsIgnoreCase(GOOGLE_IMAGES_PREFIX)) {
            String query = extractQuery(GOOGLE_IMAGES_QUERY_STRING_PATTERN, url);
            query = extractQuery(GOOGLE_QUERY_STRING_PATTERN, query);
            setName("Google Images : " + query);
        } else if (url.length() > YAHOO_PREFIX.length() && url.substring(0, YAHOO_PREFIX.length()).equalsIgnoreCase(YAHOO_PREFIX)) {
            String query = extractQuery(YAHOO_QUERY_STRING_PATTERN, url);
            setName("Yahoo! : " + query);
        } else if (url.length() > MSN_PREFIX.length() && url.substring(0, MSN_PREFIX.length()).equalsIgnoreCase(MSN_PREFIX)) {
            String query = extractQuery(MSN_QUERY_STRING_PATTERN, url);
            setName("MSN : " + query);
        } else if (url.length() > MSN_BETA_PREFIX.length() && url.substring(0, MSN_BETA_PREFIX.length()).equalsIgnoreCase(MSN_BETA_PREFIX)) {
            String query = extractQuery(MSN_QUERY_STRING_PATTERN, url);
            setName("MSN beta : " + query);
        } else if (JAVABLOGS_WELCOME_PATTERN.matcher(url).matches()) {
            setName("java.blogs : Welcome");
        } else if (JAVABLOGS_HOT_ENTRIES_PATTERN.matcher(url).matches()) {
            setName("java.blogs : Hot Entries");
        } else {
            setName(url);
        }
    }

    private String extractQuery(Pattern pattern, String url) {
        Matcher m = pattern.matcher(url);
        String query = "";
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            query = url.substring(start + 3, end);
            if (query.endsWith("&")) {
                query = query.substring(0, query.length() - 1);
            }
            try {
                query = URLDecoder.decode(query, "UTF-8");
            } catch (Exception e) {
            }
        }
        return query;
    }

    /**
   * Gets a regex expression that will filter out other referers with the same domain.
   *
   * @return  a regex as a String
   */
    public String getDomainFilter() {
        if (getUrl() == null) {
            return null;
        }
        int index = getUrl().indexOf("://");
        if (index == -1) {
            return getUrl();
        }
        String domainName = getUrl().substring(index + 3);
        index = domainName.indexOf("/");
        if (index > -1) {
            domainName = domainName.substring(0, index);
        }
        if (domainName.indexOf(":") > -1) {
            domainName = domainName.substring(0, domainName.indexOf(":"));
        }
        return ".*" + domainName + ".*";
    }
}
