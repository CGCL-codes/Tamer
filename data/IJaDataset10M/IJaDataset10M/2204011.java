package up2p.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import up2p.core.DefaultWebAdapter;
import up2p.core.LocationEntry;
import up2p.core.WebAdapter;
import up2p.search.SearchQuery;
import up2p.search.SearchResponse;
import up2p.xml.TransformerHelper;

/**
 * Constructs a search query from the given HTML form request and dispatches the
 * search to the peer network layer. Search results are stored in the
 * application and the user is forwarded to a JSP to display the results.
 * 
 * <p>
 * Expects the following parameters:
 * <ul>
 * <li>{@link up2p.servlet.HttpParams#UP2P_COMMUNITY
 * up2p.servlet.HttpParams.UP2P_COMMUNITY} - the name of the community to
 * search
 * <li>{@link up2p.servlet.HttpParams#UP2P_MAX_RESULTS
 * up2p.servlet.HttpParams.UP2P_MAX_RESULTS} - optionally specifies the
 * maximum number of results to return in a search
 * <li>All other parameters not starting with <code>up2p:</code> are
 * interpreted as the parameter name being a simple XPath and the parameter
 * value as the value to match. All XPath/value pairs are ANDed together and
 * sent to {@link up2p.servlet.XPathSearchServlet XPathSearchServlet}.
 * </ul>
 * 
 * @see <a href="http://www.w3.org/TR/1999/REC-xpath-19991116">XML Path Language
 * (XPath) Version 1.0</a>
 * @see up2p.servlet.XPathSearchServlet
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public class SearchServlet extends AbstractWebAdapterServlet {

    private static final String MODE = "search";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter(HttpParams.UP2P_LAUNCH_SEARCH) != null) {
            launchSearch(req, resp);
        } else {
            getSearchResults(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        launchSearch(req, resp);
    }

    protected void getSearchResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initSession(request);
        Document resultsDom = TransformerHelper.newDocument();
        Element root = resultsDom.createElement("results");
        resultsDom.appendChild(root);
        root.setAttribute("up2pDefault", "true");
        Map<String, String> queryMap = adapter.getLastQuery();
        if (!queryMap.isEmpty()) {
            Element queryParams = resultsDom.createElement("queryParams");
            root.appendChild(queryParams);
            Set<String> queryXPaths = queryMap.keySet();
            for (String queryXPath : queryXPaths) {
                Element queryTerm = resultsDom.createElement("queryTerm");
                queryTerm.setAttribute("xpath", queryXPath);
                queryTerm.setAttribute("value", (String) queryMap.get(queryXPath));
                queryParams.appendChild(queryTerm);
            }
        }
        SearchResponse[] results = adapter.getSearchResults();
        for (SearchResponse result : results) {
            Element resource = resultsDom.createElement("resource");
            root.appendChild(resource);
            String resultTitle = result.getTitle();
            if (resultTitle == null || resultTitle.length() == 0) {
                resultTitle = result.getFileName();
                if (resultTitle == null) resultTitle = "[No Title]";
            }
            Element title = resultsDom.createElement("title");
            title.appendChild(resultsDom.createTextNode(resultTitle));
            resource.appendChild(title);
            Element resId = resultsDom.createElement("resId");
            resId.appendChild(resultsDom.createTextNode(result.getId()));
            resource.appendChild(resId);
            Element comId = resultsDom.createElement("comId");
            comId.appendChild(resultsDom.createTextNode(result.getCommunityId()));
            resource.appendChild(comId);
            Element filename = resultsDom.createElement("filename");
            filename.appendChild(resultsDom.createTextNode(result.getFileName()));
            resource.appendChild(filename);
            Element sources = resultsDom.createElement("sources");
            sources.setAttribute("local", Boolean.toString(result.foundLocally()));
            sources.setAttribute("downloading", Boolean.toString(result.isDownloading()));
            sources.setAttribute("number", Integer.toString(result.getLocationCount()));
            resource.appendChild(sources);
            for (LocationEntry location : result.getLocations()) {
                Element source = resultsDom.createElement("source");
                sources.appendChild(source);
                source.setAttribute("peer", location.getLocationString());
                String jaccard = location.getTrustMetric("Jaccard Distance");
                if (jaccard != null) {
                    source.setAttribute("jaccard", jaccard);
                }
                String netHops = location.getTrustMetric("Network Distance");
                if (netHops != null) {
                    source.setAttribute("netHops", netHops);
                }
                String neighbours = location.getTrustMetric("Network Neighbours");
                if (neighbours != null) {
                    source.setAttribute("neighbours", neighbours);
                }
            }
            if (result.getResourceDOM() != null) {
                try {
                    Source source = new DOMSource(result.getResourceDOM());
                    File xslFile = new File(adapter.getRootPath() + File.separator + "result-dom-transform.xsl");
                    Result resultDom = new DOMResult(resource);
                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    Transformer domTransformer = tFactory.newTransformer(new StreamSource(xslFile));
                    domTransformer.transform(source, resultDom);
                } catch (Exception e) {
                    LOG.error("SearchServlet: Error using XSLT to transform search result DOM.");
                }
            }
        }
        response.setContentType("text/xml");
        boolean customXslTransform = false;
        if (request.getParameter(HttpParams.UP2P_SKIP_XSL) == null) {
            try {
                String currentCommunity = null;
                if (session.getAttribute(AbstractWebAdapterServlet.CURRENT_COMMUNITY_ID) != null) {
                    currentCommunity = (String) session.getAttribute(AbstractWebAdapterServlet.CURRENT_COMMUNITY_ID);
                } else {
                    for (SearchResponse result : results) {
                        if (currentCommunity == null) {
                            currentCommunity = result.getCommunityId();
                        } else if (!currentCommunity.equals(result.getCommunityId())) {
                            currentCommunity = null;
                            break;
                        }
                    }
                }
                if (currentCommunity != null) {
                    File customXsl = adapter.getStyleSheet(currentCommunity, "RESULT");
                    if (customXsl != null) {
                        Source customSource = new DOMSource(resultsDom);
                        Document processedResultsDom = TransformerHelper.newDocument();
                        TransformerFactory tFactory = TransformerFactory.newInstance();
                        Transformer domTransformer = tFactory.newTransformer(new StreamSource(customXsl));
                        domTransformer.setParameter("up2p-community-id", currentCommunity);
                        File dir = new File(DefaultWebAdapter.getStorageDirectory(currentCommunity));
                        domTransformer.setParameter("up2p-community-dir", dir.toURI().toURL().toExternalForm());
                        domTransformer.setParameter("up2p-root-community-id", adapter.getRootCommunityId());
                        domTransformer.setParameter("up2p-base-url", "/" + adapter.getUrlPrefix() + "/");
                        if (domTransformer.getOutputProperty("media-type") != null) {
                            response.setContentType(domTransformer.getOutputProperty("media-type"));
                        }
                        domTransformer.transform(customSource, new StreamResult(response.getOutputStream()));
                        resultsDom = processedResultsDom;
                        customXslTransform = true;
                    } else {
                    }
                } else {
                }
            } catch (Exception e) {
                LOG.error("SearchServlet: Error while performing custom XSL transform on search results.");
                e.printStackTrace();
            }
        } else {
        }
        if (!customXslTransform) {
            TransformerHelper.encodedTransform(resultsDom, WebAdapter.DEFAULT_ENCODING, response.getOutputStream(), true);
        }
    }

    /**
     * Collects the search terms and sends them to XPathSearchServlet.
     * 
     * @see javax.servlet.http.HttpServlet#service(HttpServletRequest,
     * HttpServletResponse)
     */
    protected void launchSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> resolveResIds = new ArrayList<String>();
        String communityId = request.getParameter(HttpParams.UP2P_COMMUNITY);
        if (communityId == null) {
            communityId = getCurrentCommunityId(request.getSession());
        } else if (!communityId.equals(getCurrentCommunityId(request.getSession()))) {
            LOG.debug("SearchServlet: Servicing a request outside the current community.");
            if (request.getParameter(HttpParams.UP2P_BACKGROUND_REQUEST) == null) {
                request.getSession().setAttribute(CURRENT_COMMUNITY_ID, communityId);
                adapter.addNotification("Search request changed current community to: " + adapter.RMgetCommunityTitle(communityId));
            }
        }
        if ((communityId == null) || communityId.length() == 0) {
            LOG.warn("SearchServlet Request is missing the community ID.");
            writeError(request, response, "<p><b>SearchServlet:</b> Request is missing the ID of the" + " community. This should be submitted by the search " + "form.</p>", MODE);
            return;
        }
        LOG.info("SearchServlet Search Request received in community " + communityId);
        boolean useOr = false;
        if (request.getParameter(HttpParams.UP2P_SEARCH_OPERATOR) != null && request.getParameter(HttpParams.UP2P_SEARCH_OPERATOR).equalsIgnoreCase("or")) {
            useOr = true;
            LOG.debug("SearchServlet: Combining search terms with OR.");
        } else {
            LOG.debug("SearchServlet: Combining search terms with AND.");
        }
        Map<String, String> queryMap = new HashMap<String, String>();
        SearchQuery query = new SearchQuery();
        Enumeration params = request.getParameterNames();
        if (!params.hasMoreElements()) {
            LOG.error("error! no parameters received");
        }
        while (params.hasMoreElements()) {
            String param = (String) params.nextElement();
            String value = request.getParameter(param);
            if (!param.startsWith("up2p:") && value.length() > 0) {
                LOG.debug("SearchServlet Search term XPath: " + param + " Value: " + value);
                if (useOr) {
                    query.addOrOperand(param, value);
                } else {
                    query.addAndOperand(param, value);
                }
                queryMap.put(param, value);
            } else if (param.equalsIgnoreCase(HttpParams.UP2P_RID_SEARCH)) {
                resolveResIds.add(value);
            } else if (param.equalsIgnoreCase(HttpParams.UP2P_EXACT_STRING_MATCH)) {
                LOG.info("SearchServlet: Exact string match requested.");
                query.setExactMatch(true);
            }
        }
        LOG.debug("SearchServlet Compiled query: " + query.getQuery());
        request.getSession(true).setAttribute(CURRENT_SEARCH_ID, query);
        adapter.setLastQuery(queryMap);
        int extent = HttpParams.UP2P_SEARCH_ALL;
        if (request.getParameter(HttpParams.UP2P_SEARCH_EXTENT) != null) {
            try {
                extent = Integer.parseInt(request.getParameter(HttpParams.UP2P_SEARCH_EXTENT));
            } catch (NumberFormatException e) {
            }
        }
        String maxResultStr = "";
        if (request.getParameter(HttpParams.UP2P_MAX_RESULTS) != null) maxResultStr = "&" + HttpParams.UP2P_MAX_RESULTS + "=" + request.getParameter(HttpParams.UP2P_MAX_RESULTS);
        String redirect = response.encodeURL("/XPathSearch?" + HttpParams.UP2P_COMMUNITY + "=" + communityId + "&" + HttpParams.UP2P_SEARCH_EXTENT + "=" + extent + "&" + HttpParams.UP2P_XPATH_SEARCH + "=" + URLEncoder.encode(query.getQuery(), "UTF-8")) + maxResultStr;
        for (String resolveResId : resolveResIds) {
            redirect += "&" + HttpParams.UP2P_RID_SEARCH + "=" + resolveResId;
        }
        RequestDispatcher rd = request.getRequestDispatcher(redirect);
        if (rd != null) {
            LOG.info("SearchServlet is forwarding to " + redirect);
            rd.forward(request, response);
        } else {
            LOG.error("SearchServlet Error getting request dispatcher.");
        }
    }
}
