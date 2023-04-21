package com.azureus.plugins.aztsearch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements support for the Vertor torrent search engine.
 * 
 * @version 0.4
 * @author Dalmazio Brisinda
 * 
 * <p>
 * This software is licensed under the 
 * <a href="http://creativecommons.org/licenses/GPL/2.0/">CC-GNU GPL.</a>
 */
public class TSVertorEngine extends TSEngine {

    private static final String VERTOR_URL = "http://www.vertor.com";

    private static final String VERTOR_SEARCH_URL = VERTOR_URL + "/index.php?mod=search&search=&words=%s&orderby=a.seeds&asc=0";

    private static final String VERTOR_NAME = "Vertor";

    private static final String VERTOR_DATE_RE = "\\<td[^\\>]*\\>\\s*(\\d{2})-(\\d{2})\\s*\\</td\\>";

    private static final String VERTOR_NAME_RE = "\\<td[^\\>]*\\>\\s*\\<a.*?href=\"([^\"]*)\".*?\\<a[^\"]+\"([^\"]*)\"\\>\\s*([^\\<]*)\\</a\\>\\s*\\</td\\>";

    private static final String VERTOR_CONTENT_RE = "\\<td[^\\>]*\\>.*?\\</td\\>";

    private static final String VERTOR_CATEGORY_RE = "\\<td[^\\>]*\\>\\s*\\<a[^\\>]+\\>([\\w\\s/]*)\\</a\\>\\s*\\</td\\>";

    private static final String VERTOR_SIZE_RE = "\\<td[^\\>]*\\>\\s*(\\d+)\\s*(\\w+)\\s*\\</td\\>";

    private static final String VERTOR_SEEDS_RE = "\\<td[^\\>]*\\>\\s*(\\d+)\\s*\\</td\\>";

    private static final String VERTOR_PEERS_RE = "\\<td[^\\>]*\\>\\s*(\\d+)\\s*\\</td\\>";

    private static final String VERTOR_FORMATTED_RE = "\\<tr[^\\>]*\\>\\s*%s\\s*%s\\s*%s\\s*%s\\s*%s\\s*%s\\s*%s\\s*\\</tr\\>";

    private static final String VERTOR_RE = String.format(VERTOR_FORMATTED_RE, VERTOR_DATE_RE, VERTOR_NAME_RE, VERTOR_CONTENT_RE, VERTOR_CATEGORY_RE, VERTOR_SIZE_RE, VERTOR_SEEDS_RE, VERTOR_PEERS_RE);

    private static final Pattern VERTOR_PATTERN = Pattern.compile(VERTOR_RE, Pattern.DOTALL);

    private static final String VERTOR_NEXTPAGE_FORMATTED_RE = "\\<a\\s+href=\"(http://www\\.vertor\\.com/index\\.php\\?mod=search&(?:\\w+=[\\w\\+\\.]*&)*?p=)(\\d+)\"\\>\\d+\\</a\\>";

    private static final String VERTOR_HTML_STRONG_FILTER_RE = "\\<(?:/)?strong[^\\>]*\\>";

    private static final String VERTOR_HTML_FILTER_RE = VERTOR_HTML_STRONG_FILTER_RE;

    /**
	 * Canonical size object to keep track of torrent file sizes for the
	 * Vertor torrent search engine.
	 */
    private class CanonicalSize {

        float size;

        String unit;

        /**
		 * Creates a canonical size object that holds numerical values for the 
		 * size that are always less than 1024. The size unit is adjusted
		 * accordingly. Note that we use 1024 as the factor for moving from one
		 * size unit to another.
		 * 
		 * @param theSize the numerical size
		 * @param theUnit the unit associated with the size as a string; one of "KB", "MB", "GB", or "TB"
		 */
        CanonicalSize(float theSize, String theUnit) {
            this.size = theSize;
            this.unit = theUnit;
            while (size >= 1024) {
                size /= 1024;
                if (unit.equalsIgnoreCase("KB")) unit = "MB"; else if (unit.equalsIgnoreCase("MB")) unit = "GB"; else if (unit.equalsIgnoreCase("GB")) unit = "TB";
            }
        }
    }

    /**
	 * Constructs a new Vertor search engine.
	 * 
	 * @param controller the main controller for the plugin arbitrating between
	 * the model (search results) and the GUI view.
	 * 
	 * @param resultsManager the search results manager for all searches.
	 */
    public TSVertorEngine(TSController controller, TSSearchResultsManager resultsManager) {
        super(controller, resultsManager);
    }

    /**
	 * Perform the search by sending the query string out to the Vertor
	 * torrent site, collecting, parsing, and organizing the results.
	 * 
	 * @param query the search string. It is assumed that the search string is
	 * trimmed of all whitespace, and all word delimiting spaces are replaced
	 * with "+" to facilitate URL search engine submission.
	 */
    public void performSearch(String query) {
        super.performSearch(query);
        try {
            String html = this.getHtmlPage(new URL(String.format(VERTOR_SEARCH_URL, query)));
            if (html == null) {
                String message = "Vertor: Error getting HTML.";
                System.err.println(message);
                TSMainViewPlugin.getLoggerChannel().log(message);
                return;
            }
            html = html.replaceAll(VERTOR_HTML_FILTER_RE, "");
            Matcher torrentMatcher = VERTOR_PATTERN.matcher(html);
            int endOffset = this.extractResults(torrentMatcher);
            if (terminateSearch) return;
            Pattern nextPagePattern = Pattern.compile(VERTOR_NEXTPAGE_FORMATTED_RE, Pattern.DOTALL);
            Matcher nextPageMatcher = nextPagePattern.matcher(html);
            nextPageMatcher.region(endOffset, nextPageMatcher.regionEnd());
            ArrayList<URL> pageList = new ArrayList<URL>();
            int nextPage;
            while (nextPageMatcher.find()) {
                nextPage = Integer.parseInt(nextPageMatcher.group(2));
                if (nextPage == pageList.size() + 1) {
                    pageList.add(new URL(nextPageMatcher.group(1) + nextPageMatcher.group(2)));
                } else {
                    for (int i = pageList.size() + 1; i <= nextPage; i++) pageList.add(new URL(nextPageMatcher.group(1) + i));
                    break;
                }
            }
            for (URL url : pageList) {
                html = this.getHtmlPage(url);
                if (html == null) {
                    String message = "Vertor: Error getting HTML.";
                    System.err.println(message);
                    TSMainViewPlugin.getLoggerChannel().log(message);
                    return;
                }
                html = html.replaceAll(VERTOR_HTML_FILTER_RE, "");
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
        Calendar now = Calendar.getInstance();
        debugMatchCount = 0;
        while (m.find()) {
            end = m.end();
            int seeds = Integer.parseInt(m.group(9));
            int peers = Integer.parseInt(m.group(10));
            if (!controller.includeZeroSeedTorrents() && seeds <= 0) {
                terminateSearch = true;
                break;
            }
            if (!controller.includeDeadTorrents() && seeds <= 0 && peers <= 0) {
                terminateSearch = true;
                break;
            }
            int year = now.get(Calendar.YEAR);
            GregorianCalendar cal = new GregorianCalendar(year, Integer.parseInt(m.group(1)) - 1, Integer.parseInt(m.group(2)));
            if (cal.after(now)) cal.roll(Calendar.YEAR, false);
            URL downloadLink = null, pageLink = null;
            try {
                downloadLink = new URL(m.group(3));
                pageLink = new URL(m.group(4));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            CanonicalSize canonSize = new CanonicalSize(Float.parseFloat(m.group(7)), m.group(8));
            TSSearchItem item = new TSSearchItem(cal.getTime(), m.group(6).toLowerCase(), m.group(5), pageLink, downloadLink, canonSize.size, canonSize.unit, seeds, peers, VERTOR_NAME);
            resultsManager.addToSearchResults(item);
            if (++resultsCount >= controller.maxResultsPerEngine()) {
                terminateSearch = true;
                break;
            }
        }
        return end;
    }
}
