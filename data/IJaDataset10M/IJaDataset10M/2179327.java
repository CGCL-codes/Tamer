package websphinx.searchengine;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import websphinx.Link;
import websphinx.Page;
import websphinx.Pattern;
import websphinx.PatternMatcher;
import websphinx.Regexp;
import websphinx.Region;
import websphinx.Tagexp;

/**
 * <A href="http://www.google.com/">Google</a> search engine.
 * @author Justin Boitano
 */
public class Google implements SearchEngine {

    static Pattern patCount = new Regexp("</b> of approximately <b>\\d+,?(\\d+)</b> for <b>");

    static Pattern patNoHits = new Regexp("Your search did not produce any results");

    static Pattern patResult = new Tagexp("<p>(?{link}<a>(?{title})</a>)<font>" + "<BR>(?{description}.*?)<font color=green>");

    static Pattern patMoreLink = new Tagexp("<A HREF=/search?q=*><img><br><font>.*?</a>");

    /**
     * Classify a page.  Sets the following labels:
     * <TABLE>
     * <TR><TH>Name <TH>Type  <TH>Meaning
     * <TR><TD>searchengine.source <TD>Page label <TD>Google object that labeled the page
     * <TR><TD>searchengine.count <TD>Page field <TD>Number of results on page
     * <TR><TD>searchengine.results <TD>Page fields <TD>Array of results.  Each result region
     * contains subfields: rank, title, description, and link.
     * <TR><TD>searchengine.more-results <TD>Link label <TD>Link to a page containing more results.
     * </TABLE>
     */
    public void classify(Page page) {
        String title = page.getTitle();
        if (title != null && title.startsWith("Google Search:")) {
            page.setObjectLabel("searchengine.source", this);
            Region count = patCount.oneMatch(page);
            if (count != null) page.setField("searchengine.count", count.getField("0"));
            Region[] results = patResult.allMatches(page);
            SearchEngineResult[] ser = new SearchEngineResult[results.length];
            for (int i = 0; i < results.length; ++i) ser[i] = new SearchEngineResult(results[i]);
            page.setFields("searchengine.results", ser);
            PatternMatcher m = patMoreLink.match(page);
            while (m.hasMoreElements()) {
                Link link = (Link) m.nextMatch();
                link.setLabel("searchengine.more-results");
                link.setLabel("hyperlink");
            }
        }
    }

    /**
     * Priority of this classifier.
     */
    public static final float priority = 0.0F;

    /**
     * Get priority of this classifier.
     * @return priority.
     */
    public float getPriority() {
        return priority;
    }

    /**
     * Make a query URL for Google.
     * @param keywords list of keywords, separated by spaces
     * @return URL that submits the keywords to Google.
     */
    public URL makeQuery(String keywords) {
        try {
            return new URL("http://www.google.com/search?q=" + URLEncoder.encode(keywords));
        } catch (MalformedURLException e) {
            throw new RuntimeException("internal error");
        }
    }

    /**
     * Get number of results per page for this search engine.
     * @return typical number of results per page
     */
    public int getResultsPerPage() {
        return 10;
    }

    /**
     * Search Google.
     * @param keywords list of keywords, separated by spaces
     * @return enumeration of SearchEngineResults returned by an Google query constructed from the keywords.
     */
    public static Search search(String keywords) {
        return new Search(new Google(), keywords);
    }

    /**
     * Search Google.
     * @param keywords list of keywords, separated by spaces
     * @param maxResults maximum number of results to return
     * @return enumeration of SearchEngineResults returned by an Google query constructed from the keywords.
     * The enumeration yields at most maxResults objects.
     */
    public static Search search(String keywords, int maxResults) {
        return new Search(new Google(), keywords, maxResults);
    }
}
