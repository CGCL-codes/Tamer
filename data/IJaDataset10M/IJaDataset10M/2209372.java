package com.umc.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.umc.UMCStatistics;
import com.umc.dao.DataAccessFactory;
import com.umc.dao.UMCDataAccessInterface;
import com.umc.helper.UMCParams;
import com.umc.online.AbstractWebSearchPlugin;
import com.yahoo.rest.RestClient;
import com.yahoo.rest.RestException;
import com.yahoo.search.SearchClient;
import com.yahoo.search.SearchException;
import com.yahoo.search.WebSearchRequest;
import com.yahoo.search.WebSearchResult;
import com.yahoo.search.WebSearchResults;
import com.yahoo.search.xmlparser.XmlParserWebSearchResults;
import com.yahoo.xml.XmlParser;

public class YahooWebSearch extends AbstractWebSearchPlugin {

    private static Logger log = Logger.getLogger("com.umc.plugin.websearch");

    private UMCDataAccessInterface dao = null;

    private static final String APPID_KEY = "appid";

    private String appId = "websearchappid";

    private static final String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

    private Hashtable<String, Object> result = null;

    /**URL*/
    public static final String KEY_URL = "url";

    public YahooWebSearch() {
        setPluginDBVersion(1);
        setPluginDescriptionEnglish("This plugin enables UMC to search on Yahoo");
        setPluginDescriptionFrench(null);
        setPluginDescriptionGerman("Mit diesem Plugin ist es möglich auf Yahoo zu suchen");
        setPluginNameEnglish("Yahoo-WebSearch-Plugin");
        setPluginNameFrench("Yahoo-WebSearch-Plugin");
        setPluginNameGerman("Yahoo-WebSearch-Plugin");
        setPluginVersion(0.1);
    }

    public Collection<String> searchFor(String aSearchItem) {
        return null;
    }

    public String searchForOFDB(String aTitle, int aYear) {
        log.debug("Suche nach IMDB ID für " + aTitle);
        try {
            log.info("suche in Yahoo mit:" + aTitle);
            SearchClient client = new SearchClient(getAppID());
            String query = aTitle + " www.ofdb.de/film";
            WebSearchRequest request = new WebSearchRequest(URLEncoder.encode(query, "UTF-8"));
            request.setParameter("results", "100");
            request.setParameter("format", "html");
            request.setParameter("type", "all");
            request.setParameter("site", "www.ofdb.de");
            WebSearchResults isr = client.webSearch(request);
            WebSearchResult[] i = isr.listResults();
            Pattern p = null;
            Matcher m = null;
            String ofdbID = null;
            log.debug("Anzahl der OFDB ID Such-Ergebnisse für " + aTitle + ": " + i.length);
            String regExTitle = aTitle.toUpperCase();
            regExTitle = regExTitle.trim().replaceAll(" ", ".*.");
            regExTitle = ".*" + regExTitle + ".*";
            StringBuffer sb = null;
            for (int a = 0; a < i.length; a++) {
                sb = new StringBuffer();
                sb.append(i[a].getTitle());
                sb.append(i[a].getSummary());
                sb.append(i[a].getUrl());
                sb.append(i[a].getClickUrl());
                ofdbID = null;
                if (Pattern.matches(regExTitle, sb.toString().toUpperCase())) {
                    if (Pattern.matches(".*http://www.ofdb.de/film/[0-9]*.*", i[a].getUrl()) || Pattern.matches(".*http://www.ofdb.de/review/[0-9]*.*", i[a].getUrl())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getUrl());
                        m.find();
                        ofdbID = m.group();
                        if (ofdbID != null) {
                            ofdbID = ofdbID.substring(1, ofdbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + ofdbID + " (aus " + i[a].getUrl() + ")");
                            return ofdbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.ofdb.de/film/[0-9]*.*", i[a].getClickUrl()) || Pattern.matches(".*http://www.ofdb.de/review/[0-9]*.*", i[a].getClickUrl())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getClickUrl());
                        m.find();
                        ofdbID = m.group();
                        if (ofdbID != null) {
                            ofdbID = ofdbID.substring(1, ofdbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + ofdbID + " (aus " + i[a].getClickUrl() + ")");
                            return ofdbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.ofdb.de/film/[0-9]*.*", i[a].getSummary()) || Pattern.matches(".*http://www.ofdb.de/review/[0-9]*.*", i[a].getSummary())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getSummary());
                        m.find();
                        ofdbID = m.group();
                        if (ofdbID != null) {
                            ofdbID = ofdbID.substring(1, ofdbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + ofdbID + " (aus " + i[a].getSummary() + ")");
                            return ofdbID;
                        }
                    }
                }
            }
            return null;
        } catch (SearchException exc) {
            log.fatal("Such Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (IOException exc) {
            log.fatal("IO Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (Exception exc) {
            log.fatal("Allgemeiner Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        }
    }

    public String searchForIMDB(String aTitle, int aYear) {
        log.debug("Suche nach IMDB ID für " + aTitle);
        try {
            log.info("suche in Yahoo mit:" + aTitle);
            SearchClient client = new SearchClient(getAppID());
            String query = aTitle + " www.imdb.com/title";
            WebSearchRequest request = new WebSearchRequest(URLEncoder.encode(query, "UTF-8"));
            request.setParameter("results", "100");
            request.setParameter("format", "html");
            request.setParameter("type", "all");
            WebSearchResults isr = client.webSearch(request);
            WebSearchResult[] i = isr.listResults();
            Pattern p = null;
            Matcher m = null;
            String imdbID = null;
            log.debug("Anzahl der IMDB ID Such-Ergebnisse für " + aTitle + ": " + i.length);
            String regExTitle = aTitle.toUpperCase();
            regExTitle = regExTitle.trim().replaceAll(" ", ".*.");
            regExTitle = ".*" + regExTitle + ".*";
            StringBuffer sb = null;
            for (int a = 0; a < i.length; a++) {
                sb = new StringBuffer();
                sb.append(i[a].getTitle());
                sb.append(i[a].getSummary());
                sb.append(i[a].getUrl());
                sb.append(i[a].getClickUrl());
                imdbID = null;
                if (Pattern.matches(regExTitle, sb.toString().toUpperCase())) {
                    if (Pattern.matches(".*http://www.imdb.com/title/tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]*.*", i[a].getUrl())) {
                        p = Pattern.compile("tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
                        m = p.matcher(i[a].getUrl());
                        m.find();
                        imdbID = m.group();
                        if (imdbID != null && imdbID.length() == 9) {
                            log.debug("IMDB ID für " + aTitle + ": " + imdbID + " (aus " + i[a].getUrl() + ")");
                            return imdbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.imdb.com/title/tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]*.*", i[a].getClickUrl())) {
                        p = Pattern.compile("tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
                        m = p.matcher(i[a].getClickUrl());
                        m.find();
                        imdbID = m.group();
                        if (imdbID != null && imdbID.length() == 9) {
                            log.debug("IMDB ID für " + aTitle + ": " + imdbID + " (aus " + i[a].getClickUrl() + ")");
                            return imdbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.imdb.com/title/tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]*.*", i[a].getSummary())) {
                        p = Pattern.compile("tt[0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
                        m = p.matcher(i[a].getSummary());
                        m.find();
                        imdbID = m.group();
                        if (imdbID != null && imdbID.length() == 9) {
                            log.debug("IMDB ID für " + aTitle + ": " + imdbID + " (aus " + i[a].getSummary() + ")");
                            return imdbID;
                        }
                    }
                }
            }
            return null;
        } catch (SearchException exc) {
            log.fatal("Such Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (IOException exc) {
            log.fatal("IO Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (Exception exc) {
            log.fatal("Allgemeiner Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        }
    }

    public String searchForTheMovieDB(String aTitle, int aYear) {
        log.debug("Suche nach TheMovieDB ID für " + aTitle);
        try {
            log.info("suche in Yahoo mit:" + aTitle);
            SearchClient client = new SearchClient(getAppID());
            String query = aTitle + " www.themoviedb.org/movie";
            WebSearchRequest request = new WebSearchRequest(URLEncoder.encode(query, "UTF-8"));
            request.setParameter("results", "100");
            request.setParameter("format", "html");
            request.setParameter("type", "all");
            request.setParameter("site", "www.themoviedb.org");
            WebSearchResults isr = client.webSearch(request);
            WebSearchResult[] i = isr.listResults();
            Pattern p = null;
            Matcher m = null;
            String themoviedbID = null;
            log.debug("Anzahl der TheMovieDB ID Such-Ergebnisse für " + aTitle + ": " + i.length);
            String regExTitle = aTitle.toUpperCase();
            regExTitle = regExTitle.trim().replaceAll(" ", ".*.");
            regExTitle = ".*" + regExTitle + ".*";
            StringBuffer sb = null;
            for (int a = 0; a < i.length; a++) {
                sb = new StringBuffer();
                sb.append(i[a].getTitle());
                sb.append(i[a].getSummary());
                sb.append(i[a].getUrl());
                sb.append(i[a].getClickUrl());
                themoviedbID = null;
                if (Pattern.matches(regExTitle, sb.toString().toUpperCase())) {
                    if (Pattern.matches(".*http://www.themoviedb.org/movie/[0-9]*.*", i[a].getUrl())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getUrl());
                        m.find();
                        themoviedbID = m.group();
                        if (themoviedbID != null) {
                            themoviedbID = themoviedbID.substring(1, themoviedbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + themoviedbID + " (aus " + i[a].getUrl() + ")");
                            return themoviedbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.themoviedb.org/movie/[0-9]*.*", i[a].getClickUrl())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getClickUrl());
                        m.find();
                        themoviedbID = m.group();
                        if (themoviedbID != null) {
                            themoviedbID = themoviedbID.substring(1, themoviedbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + themoviedbID + " (aus " + i[a].getClickUrl() + ")");
                            return themoviedbID;
                        }
                    }
                    if (Pattern.matches(".*http://www.themoviedb.org/movie/[0-9]*.*", i[a].getSummary())) {
                        p = Pattern.compile("/[0-9].*,");
                        m = p.matcher(i[a].getSummary());
                        m.find();
                        themoviedbID = m.group();
                        if (themoviedbID != null) {
                            themoviedbID = themoviedbID.substring(1, themoviedbID.length() - 1);
                            log.debug("OFDB ID für " + aTitle + ": " + themoviedbID + " (aus " + i[a].getSummary() + ")");
                            return themoviedbID;
                        }
                    }
                }
            }
            return null;
        } catch (SearchException exc) {
            log.fatal("Such Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (IOException exc) {
            log.fatal("IO Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        } catch (Exception exc) {
            log.fatal("Allgemeiner Fehler in Yahoo WebSearch Plugin", exc);
            return null;
        }
    }

    /**
     * Searches the Yahoo database.
     *
     * @param request The request to search for.
     * @return Results of the search.
     * @throws IOException     Thrown if any network issues occur while making the call.
     * @throws SearchException Thrown if the request is invalid or if the service is malfunctioning.
     */
    public WebSearchResults webSearch(WebSearchRequest request) throws IOException, SearchException {
        request.getParameters().put(APPID_KEY, appId);
        Map results = executeAndParse(request.getRequestUrl(), request.getParameters());
        return new XmlParserWebSearchResults(results);
    }

    private Map executeAndParse(String serviceUrl, Map parameters) throws IOException, SearchException {
        XmlParser xmlParser = null;
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            xmlParser = new XmlParser();
            parser.parse(RestClient.call(serviceUrl, parameters), xmlParser);
        } catch (ParserConfigurationException e) {
            throw new com.yahoo.java.ExtendedError("XML parser not properly configured", e);
        } catch (SAXException e) {
            throw new SearchException("Error parsing XML response", e);
        } catch (RestException ye) {
            throw new SearchException("Error calling service\n" + new String(ye.getErrorMessage(), "UTF-8"), ye);
        }
        return xmlParser.getRoot();
    }

    private String getAppID() {
        String result = "";
        Random r = new Random();
        int counter = 1;
        while (counter < 10) {
            result += letters[r.nextInt(36)];
            counter++;
        }
        log.debug("Erzeugte Yahoo App ID = " + result);
        return result;
    }
}
