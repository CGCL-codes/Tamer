package com.googlecode.api.ajaxsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.googlecode.api.ajaxsearch.cursor.GSearchCursor;
import com.googlecode.api.ajaxsearch.cursor.GSearchCursorPage;
import com.googlecode.api.ajaxsearch.response.GoogleSearchResponse;
import com.googlecode.api.ajaxsearch.response.GoogleSearchResult;

/**
 * @author alex.parvulescu
 * 
 */
public class GSearch {

    private String version = "1.0";

    private String referer = "http://www.mysite.com/index.html";

    private static final Log log = LogFactory.getLog(GSearch.class);

    public GSearch() {
    }

    public GoogleSearchResponse runSearch(String search) {
        try {
            String rawSearchResults = runRawSearch(search);
            return filterResults(rawSearchResults);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public String runRawSearch(final String search) throws IOException {
        if (search == null) {
            return null;
        }
        StringBuilder urlString = new StringBuilder("http://ajax.googleapis.com/ajax/services/search/web?");
        if (version != null) {
            urlString.append("v=");
            urlString.append(version);
            urlString.append("&");
        }
        urlString.append("q=");
        urlString.append(StringEscapeUtils.escapeHtml(search));
        URL url = new URL(urlString.toString());
        Proxy proxy = null;
        final URLConnection connection;
        if (proxy != null) {
            connection = url.openConnection(proxy);
        } else {
            connection = url.openConnection();
        }
        if (referer != null) {
            connection.addRequestProperty("Referer", referer);
        }
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private GoogleSearchResponse filterResults(String rawSearchResults) throws JSONException {
        final GoogleSearchResponse searchResult = new GoogleSearchResponse();
        final JSONObject json = new JSONObject(rawSearchResults);
        int responseStatus = json.getInt("responseStatus");
        searchResult.setResponseStatus(responseStatus);
        String responseDetails = json.getString("responseDetails");
        searchResult.setResponseDetails(responseDetails);
        final JSONObject responseData = json.getJSONObject("responseData");
        JSONArray results = responseData.getJSONArray("results");
        for (int i = 0, n = results.length(); i < n; i++) {
            final JSONObject row = results.getJSONObject(i);
            String gSearchResultClass = row.getString("GsearchResultClass");
            String unescapedUrl = row.getString("unescapedUrl");
            String url = row.getString("url");
            String visibleUrl = row.getString("visibleUrl");
            String cacheUrl = row.getString("cacheUrl");
            String title = row.getString("title");
            String titleNoFormatting = row.getString("titleNoFormatting");
            String content = row.getString("content");
            GoogleSearchResult entry = new GoogleSearchResult();
            entry.setCacheUrl(cacheUrl);
            entry.setContent(content);
            entry.setGSearchResultClass(gSearchResultClass);
            entry.setTitle(title);
            entry.setTitleNoFormatting(titleNoFormatting);
            entry.setUnescapedUrl(unescapedUrl);
            entry.setUrl(url);
            entry.setVisibleUrl(visibleUrl);
            searchResult.addResult(entry);
        }
        GSearchCursor gCursor = new GSearchCursor();
        final JSONObject cursor = responseData.getJSONObject("cursor");
        long estimatedResultCount = cursor.getLong("estimatedResultCount");
        gCursor.setEstimatedResultCount(estimatedResultCount);
        long currentPageIndex = cursor.getLong("currentPageIndex");
        gCursor.setCurrentPageIndex(currentPageIndex);
        String moreResultsUrl = cursor.getString("moreResultsUrl");
        gCursor.setMoreResultsUrl(moreResultsUrl);
        JSONArray pages = cursor.getJSONArray("pages");
        for (int i = 0, n = pages.length(); i < n; i++) {
            final JSONObject row = pages.getJSONObject(i);
            GSearchCursorPage page = new GSearchCursorPage(row.getInt("start"), row.getString("label"));
            gCursor.addPage(page);
        }
        searchResult.setCursor(gCursor);
        return searchResult;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
