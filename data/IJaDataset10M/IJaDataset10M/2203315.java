package com.azureus.plugins.aztsearch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements support for the Fenopy torrent search engine.
 * 
 * @version 0.4
 * @author Dalmazio Brisinda
 * 
 * <p>
 * This software is licensed under the 
 * <a href="http://creativecommons.org/licenses/GPL/2.0/">CC-GNU GPL.</a>
 */
public class TSFenopyEngine extends TSEngine {

    private static final String FENOPY_URL = "http://fenopy.com/";

    private static final String FENOPY_SEARCH_URL = FENOPY_URL + "?keyword=%s&order=3&dead=checkbox";

    private static final String FENOPY_NAME = "Fenopy";

    private static final String FENOPY_TORRENT_RE = "\\<dt[^\\>]*\\>\\s*\\<a[^\"]+\"([^\"]*)\".+?\\</dt\\>";

    private static final String FENOPY_NAME_RE = "\\<dt[^\\>]*\\>\\s*\\<a[^\"]+\"([^\"]*)\"\\s+title=\"([^\"]*)\".+?\\</dt\\>";

    private static final String FENOPY_SIZE_RE = "\\<dt[^\\>]*\\>(\\d*\\.??\\d*)\\s+(\\w{2})s*\\</dt\\>";

    private static final String FENOPY_CATEGORY_RE = "\\<dt[^\\>]*\\>(\\w+)\\</dt\\>";

    private static final String FENOPY_SEEDS_RE = "\\<dt[^\\>]*\\>(\\d+)\\</dt\\>";

    private static final String FENOPY_PEERS_RE = "\\<dt[^\\>]*\\>(\\d+)\\</dt\\>";

    private static final String FENOPY_FORMATTED_RE = "\\<dl[^\\>]*\\>\\s*%s\\s*%s\\s*%s\\s*%s\\s*%s\\s*%s\\s*\\</dl\\>";

    private static final String FENOPY_RE = String.format(FENOPY_FORMATTED_RE, FENOPY_TORRENT_RE, FENOPY_NAME_RE, FENOPY_SIZE_RE, FENOPY_CATEGORY_RE, FENOPY_SEEDS_RE, FENOPY_PEERS_RE);

    private static final Pattern FENOPY_PATTERN = Pattern.compile(FENOPY_RE, Pattern.DOTALL);

    private static final String FENOPY_NEXTPAGE_RE = "\\<a\\s+href=\"(\\?keyword=[^\"]+)\"\\s*\\>\\s*\\d+\\s*\\</a\\>";

    /**
	 * Constructs a new Fenopy search engine.
	 * 
	 * @param controller the main controller for the plugin arbitrating between
	 * the model (search results) and the GUI view.
	 * 
	 * @param resultsManager the search results manager for all searches.
	 */
    public TSFenopyEngine(TSController controller, TSSearchResultsManager resultsManager) {
        super(controller, resultsManager);
    }

    /**
	 * Perform the search by sending the query string out to the Fenopy torrent site,
	 * collecting, parsing, and organizing the results.
	 * 
	 * @param query the search string. It is assumed that the search string is trimmed of
	 * all whitespace, and all word delimiting spaces are replaced with "+" to facilitate
	 * URL search engine submission.
	 */
    public void performSearch(String query) {
        super.performSearch(query);
        try {
            String html = this.getHtmlPage(new URL(String.format(FENOPY_SEARCH_URL, query)));
            if (html == null) {
                String message = "Fenopy: Error getting HTML.";
                System.err.println(message);
                TSMainViewPlugin.getLoggerChannel().log(message);
                return;
            }
            Matcher torrentMatcher = FENOPY_PATTERN.matcher(html);
            int endOffset = this.extractResults(torrentMatcher);
            if (terminateSearch) return;
            Pattern nextPagePattern = Pattern.compile(FENOPY_NEXTPAGE_RE, Pattern.DOTALL);
            Matcher nextPageMatcher = nextPagePattern.matcher(html);
            nextPageMatcher.region(endOffset, nextPageMatcher.regionEnd());
            ArrayList<URL> pageList = new ArrayList<URL>();
            while (nextPageMatcher.find()) {
                pageList.add(new URL(FENOPY_URL + nextPageMatcher.group(1).replaceAll(" ", "+")));
            }
            for (URL url : pageList) {
                html = this.getHtmlPage(url);
                if (html == null) {
                    String message = "Fenopy: Error getting HTML.";
                    System.err.println(message);
                    TSMainViewPlugin.getLoggerChannel().log(message);
                    return;
                }
                torrentMatcher.reset(html);
                this.extractResults(torrentMatcher);
                if (terminateSearch) return;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Extract the items from the matcher and populate the search results.
	 * 
	 * @param m the matcher.
	 * @return the offset after the last character of the last successful match.
	 */
    private int extractResults(Matcher m) {
        int end = 0;
        Date date = new Date(0);
        debugMatchCount = 0;
        while (m.find()) {
            end = m.end();
            int seeds = Integer.parseInt(m.group(7));
            int peers = Integer.parseInt(m.group(8));
            if (!controller.includeZeroSeedTorrents() && seeds <= 0) {
                terminateSearch = true;
                break;
            }
            if (!controller.includeDeadTorrents() && seeds <= 0 && peers <= 0) {
                terminateSearch = true;
                break;
            }
            URL downloadLink = null, pageLink = null;
            try {
                downloadLink = new URL(m.group(1));
                pageLink = new URL(m.group(2));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            float size = Float.parseFloat(m.group(4));
            TSSearchItem item = new TSSearchItem(date, m.group(6).toLowerCase(), m.group(3), pageLink, downloadLink, size, m.group(5), seeds, peers, FENOPY_NAME);
            resultsManager.addToSearchResults(item);
            if (++resultsCount >= controller.maxResultsPerEngine()) {
                terminateSearch = true;
                break;
            }
        }
        return end;
    }
}
