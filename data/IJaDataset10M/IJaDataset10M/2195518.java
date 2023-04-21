package org.stanwood.media.source.xbmc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stanwood.media.extensions.ExtensionException;
import org.stanwood.media.model.Mode;
import org.stanwood.media.source.SourceException;
import org.stanwood.media.util.Stream;
import org.stanwood.media.xml.XMLParser;
import org.stanwood.media.xml.XMLParserException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is used to handle the XML scraper files from XBMC.
 */
public class XBMCScraper extends XBMCExtension {

    private static final String ROOT_NODE_NAME = "scraper";

    private static final Log log = LogFactory.getLog(XBMCScraper.class);

    private Mode mode;

    private XBMCAddonManager addonMgr;

    /**
	 * Used to create the class and set the scraper file
	 * @param addon The addon been used
	 * @param point The addon extension point for this scraper
	 * @param mode The mode this scraper is to be used for
	 * @param scraperFile The XML scraper file
	 */
    public XBMCScraper(XBMCAddon addon, File scraperFile, String point, Mode mode) {
        super(addon, scraperFile, point);
        addonMgr = addon.getManager();
        this.mode = mode;
    }

    /**
	 * Get the mode of the scrper
	 * @return The scraper mode
	 */
    public Mode getMode() {
        return mode;
    }

    /**
	 * Used to get the URL which should be used to search
	 * @param searchTerm The search term to use
	 * @param year The year to search for the result or empty string for any
	 * @return The search URL XML result
	 * @throws XBMCException Thrown if their are any problems creating the search URL
	 */
    public Document getCreateSearchUrl(String searchTerm, String year) throws XBMCException {
        try {
            searchTerm = URLEncoder.encode(searchTerm, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new XBMCException(Messages.getString("XBMCScraper.UNABLE_ENCODE_SEARCH_TERM_URL"), e);
        }
        return executeFunctionByName("CreateSearchUrl", searchTerm, year);
    }

    /**
	 * This function is used to get a XML document of the search results. It takes as input the
	 * webpage downloaded from the URL obtained with the @{link getCreateSearchUrl(String,String)} call.
	 * @param rawHtml The raw search results
	 * @param searchTerm The term been searched for
	 * @return The search results as a XML document
	 * @throws XBMCException Thrown if their are any problems creating the search urlXML
	 */
    public Document getGetSearchResults(String rawHtml, String searchTerm) throws XBMCException {
        return executeFunctionByName("GetSearchResults", rawHtml, searchTerm);
    }

    /**
	 * Used to get the show/film details as a XML document. It takes as input the
	 * 1 or more webpages downloaded from the URL obtained with the @{link getCreateSearchUrl(String,String)} call.
	 * @param file the file that the details are been retrieved for, or NULL if this is not known
	 * @param contents A list of webpage contents
	 * @return The results as a XML document
	 * @throws XBMCException Thrown if their are any problems
	 */
    public Document getGetDetails(File file, String... contents) throws XBMCException {
        Document result = executeFunctionByName("GetDetails", contents);
        return result;
    }

    /**
	 * Used to get the episode guide URL
	 * @param rawHtml The show weppage
	 * @return The XML doc containg the URL
	 * @throws XBMCException Thrown if their are any problem
	 */
    public Document getEpisodeGuideUrl(String rawHtml) throws XBMCException {
        return executeFunctionByName("EpisodeGuideUrl", rawHtml);
    }

    /**
	 * Used to get a Show or film URL from the contents of a NFO file.
	 * @param contents The contents of a NFO file
	 * @return The XML containing a URL
	 * @throws XBMCException Thrown if their are any problems
	 */
    public boolean getNfoUrl(String contents) throws XBMCException {
        if (log.isDebugEnabled()) {
            log.debug("executing scraper function with name: NfoUrl");
        }
        try {
            Map<Integer, String> params = convertParams(contents);
            String result = executeXBMCScraperFunction("NfoUrl", params);
            if (log.isDebugEnabled()) {
                log.debug("Got result: " + result);
            }
            return (result != null && result.length() > 0);
        } catch (Exception e) {
            throw new XBMCException(MessageFormat.format(Messages.getString("XBMCScraper.UNABLE_EXECUTE_SCRAPER_FUNCTION"), "NfoUrl", getAddon().getId()), e);
        }
    }

    private Document executeFunctionByName(String funcName, String... contents) throws XBMCException {
        if (log.isDebugEnabled()) {
            log.debug("executing scraper function with name: " + funcName);
        }
        try {
            Map<Integer, String> params = convertParams(contents);
            String result = executeXBMCScraperFunction(funcName, params);
            if (log.isDebugEnabled()) {
                log.debug("Got result: " + result);
            }
            if (result == null) {
                result = "";
            }
            Document doc = XMLParser.strToDom(result);
            checkForError(doc, funcName);
            resolveElements(doc);
            return doc;
        } catch (Exception e) {
            throw new XBMCException(MessageFormat.format(Messages.getString("XBMCScraper.UNABLE_TO_EXECUTE_SCRAPER_FUNCTION"), funcName, getAddon().getId()), e);
        }
    }

    private void checkForError(Document doc, String functionName) throws XBMCException, XMLParserException {
        Node node = selectSingleNode(doc, "error/text()");
        if (node != null) {
            throw new XBMCException(MessageFormat.format(Messages.getString("XBMCScraper.UNABLE_TO_EXECUTE_FUNCTION"), functionName, node.getTextContent()));
        }
    }

    /**
	 * Used to get a list of the the episodes in a show
	 * @param html The contents of the episode list
	 * @param showURL The URL used to fetch show info
	 * @return The XML document containing the episode list
	 * @throws XBMCException Thrown if their are any problems
	 */
    public Document getGetEpisodeList(String html, URL showURL) throws XBMCException {
        return executeFunctionByName("GetEpisodeList", html, showURL.toExternalForm());
    }

    private void resolveElements(Document doc) throws XMLParserException, XBMCException, IOException, SourceException {
        for (Node node : selectNodeList(doc, "details/chain")) {
            resolveChainNodes(doc, (Element) node);
        }
        for (Node node : selectNodeList(doc, "details/url")) {
            resolveUrlNodes(doc, (Element) node);
        }
    }

    private Map<Integer, String> convertParams(String... contents) throws XBMCException {
        Map<Integer, String> params = new HashMap<Integer, String>();
        if (contents.length >= 9) {
            throw new XBMCException(Messages.getString("XBMCScraper.NOT_ALLOWED_MORE_9_PARAMS"));
        }
        for (int i = 0; i < contents.length; i++) {
            params.put(i + 1, contents[i]);
        }
        return params;
    }

    private void resolveChainNodes(Document doc, Element node) throws XMLParserException, XBMCException {
        String functionName = node.getAttribute("function");
        String param = node.getTextContent();
        Map<Integer, String> subParams = new HashMap<Integer, String>();
        subParams.put(1, param);
        Document results = strToDom(getAddon().executeFunction(functionName, subParams));
        Node parent = node.getParentNode();
        parent.removeChild(node);
        for (Node n : selectNodeList(results, "details/*")) {
            Node newNode = doc.importNode(n, true);
            parent.appendChild(newNode);
        }
    }

    private void resolveUrlNodes(final Document doc, final Element node) throws DOMException, XMLParserException, IOException, SourceException {
        final String functionName = node.getAttribute("function");
        try {
            final URL url = new URL(node.getTextContent());
            if (!functionName.equals("")) {
                StreamProcessor processor = new StreamProcessor(url.toExternalForm()) {

                    @Override
                    protected Stream getStream() throws ExtensionException, IOException {
                        return addonMgr.getStreamToURL(url);
                    }

                    @Override
                    public void processContents(String contents) throws SourceException {
                        Map<Integer, String> params = new HashMap<Integer, String>();
                        params.put(1, contents);
                        try {
                            String s = getAddon().executeFunction(functionName, params);
                            if (s == null) {
                                s = "";
                            }
                            Document results = strToDom(s);
                            Node parent = node.getParentNode();
                            parent.removeChild(node);
                            for (Node n : selectNodeList(results, "details/*")) {
                                Node newNode = doc.importNode(n, true);
                                parent.appendChild(newNode);
                            }
                        } catch (XMLParserException e) {
                            throw new SourceException(MessageFormat.format(Messages.getString("XBMCScraper.UNABLE_EXECUTE_SCRAPER_FUNC"), functionName));
                        }
                    }
                };
                processor.handleStream();
            }
        } catch (MalformedURLException e) {
            throw new XMLParserException(MessageFormat.format(Messages.getString("XBMCScraper.UNVALID_URL"), node.getTextContent()));
        }
    }

    /**
	 * Used to execute a scraper function
	 * @param functionName The name of the function to execute
	 * @param params Numbered parameters passed to the function
	 * @return The result retured by the function
	 */
    @Override
    public String executeXBMCScraperFunction(String functionName, Map<Integer, String> params) throws XBMCException, XMLParserException {
        Element functionNode = (Element) selectSingleNode(getDocument(), ROOT_NODE_NAME + "/" + functionName);
        if (functionNode == null) {
            throw new XBMCFunctionNotFoundException(MessageFormat.format(Messages.getString("XBMCScraper.UNABLE_FIND_SCRAPER_FUNCTION"), functionName));
        }
        return executeXBMCFunction(functionNode, params);
    }

    /**
	 * This method is used to check if the scraper can be used for the episode details URL
	 * @param url The NFO file
	 * @return True of the scraper is compatible with the URL
	 * @throws SourceException Thrown if their are any problems
	 */
    public boolean supportsURL(URL url) throws SourceException {
        return getNfoUrl(url.toExternalForm());
    }

    /**
	 * Used to get the details of the a episode. The <code>contents</code> argument contains the downloaded
	 * weppage of the episode details. The episode details URL is obtained by calling {@link #getGetEpisodeList(String, URL)}
	 * @param contents The downloaded HTML of the episode guide
	 * @param episodeId The ID of the episode to get the details for
	 * @return episode details XML
	 * @throws XBMCException Thrown if their are any problems
	 */
    public Document getGetEpisodeDetails(String contents, String episodeId) throws XBMCException {
        return executeFunctionByName("GetEpisodeDetails", contents, episodeId);
    }
}
