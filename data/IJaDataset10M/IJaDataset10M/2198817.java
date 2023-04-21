package org.atlantal.impl.portal.url;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.net.URLCodec;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.portal.url.AtlantalURI;

/**
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @version 1.0
 * @since Sep 30, 2004
 */
public final class AtlantalURIParser {

    private static final Logger LOGGER = Logger.getLogger(AtlantalURIInstance.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    /** The singleton parser instance. */
    private static final AtlantalURIParser PARSER = new AtlantalURIParser();

    private static final int EMPTY = AtlantalURI.EMPTY;

    private static final int NODE = AtlantalURI.NODE;

    private static final int FRAME = AtlantalURI.FRAME;

    private static final int ROOT = AtlantalURI.ROOT;

    private static final int CONTENT = AtlantalURI.CONTENT;

    private static final String STRING_NULL = null;

    private static final String DOT_SEP_SPLIT = "\\.";

    private static final String PATH_SEPARATOR = "-";

    private static final String INPATH_SEPARATOR = "~";

    private static final String INPATH_SUBSEPARATOR = ",";

    private static final char ACTION_SUFFIX = '$';

    private static final URLCodec URLCODEC = new URLCodec();

    /**
     * Private constructor that prevents external instantiation.
     */
    private AtlantalURIParser() {
    }

    /**
     * Returns the singleton parser instance.
     *
     * @return the singleton parser instance.
     */
    public static AtlantalURIParser getParser() {
        return PARSER;
    }

    /**
     * Parse a string uri to a portal URL.
     *
     * @param request
     *            the servlet request to parse.
     * @return the portal URL.
     */
    public AtlantalURI parse(HttpServletRequest request) {
        String scheme = request.getScheme();
        boolean secure = request.isSecure();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String queryString = request.getQueryString();
        AtlantalURIInstance uri = new AtlantalURIInstance(scheme, secure, serverName, port, contextPath, servletPath, queryString);
        parseServletPath(uri, servletPath);
        parseQueryString(uri, queryString);
        return uri;
    }

    private void parseServletPath(AtlantalURIInstance uri, String servletPath) {
        String[] pathItems = servletPath.split("/");
        String basePath = null;
        String newPath = null;
        String[] newPathTab;
        switch(pathItems.length) {
            case 2:
                newPathTab = pathItems[1].split(DOT_SEP_SPLIT);
                newPath = (newPathTab.length > 0) ? newPathTab[0] : null;
                break;
            case 3:
                basePath = pathItems[1];
                newPathTab = pathItems[2].split(DOT_SEP_SPLIT);
                newPath = (newPathTab.length > 0) ? newPathTab[0] : null;
                break;
            default:
                break;
        }
        if (basePath != null) {
            parseBasePath(uri, basePath);
        }
        if (newPath != null) {
            parseNewPath(uri, newPath);
        }
    }

    private void getRenderParameters(AtlantalURIInstance uri, String basePath) {
        String[] basePathTab = basePath.split(INPATH_SEPARATOR);
        int length = basePathTab.length;
        for (int i = 0; i < length; i++) {
            String[] ptlInfos = basePathTab[i].split(INPATH_SUBSEPARATOR);
            int infosLength = ptlInfos.length;
            if (infosLength > 0) {
                String ptlName = ptlInfos[0];
                Map params = new LinkedHashMap();
                for (int j = 1; j < infosLength; j += 2) {
                    params.put(ptlInfos[j], ptlInfos[j + 1]);
                }
                uri.setRenderParameters(ptlName, params);
            }
        }
    }

    private void parseBasePath(AtlantalURIInstance uri, String basepath) {
        String[] basePathTab = basepath.split(PATH_SEPARATOR);
        int length = basePathTab.length;
        if (length >= 2) {
            String[] tab = basePathTab[1].split(INPATH_SEPARATOR);
            String node = STRING_NULL;
            String frame = STRING_NULL;
            String rootLayout = STRING_NULL;
            String contentWindow = STRING_NULL;
            int code = Integer.parseInt(basePathTab[0]);
            switch(code) {
                case (NODE):
                    node = tab[0];
                    break;
                case (ROOT):
                    rootLayout = tab[0];
                    break;
                case (NODE | ROOT):
                    node = tab[0];
                    rootLayout = tab[1];
                    break;
                case (FRAME | ROOT):
                    frame = tab[0];
                    rootLayout = tab[1];
                    break;
                case (NODE | FRAME | ROOT):
                    node = tab[0];
                    frame = tab[1];
                    rootLayout = tab[2];
                    break;
                case (ROOT | CONTENT):
                    rootLayout = tab[0];
                    contentWindow = tab[1];
                    break;
                case (NODE | ROOT | CONTENT):
                    node = tab[0];
                    rootLayout = tab[1];
                    contentWindow = tab[2];
                    break;
                case (FRAME | ROOT | CONTENT):
                    frame = tab[0];
                    rootLayout = tab[1];
                    contentWindow = tab[2];
                    break;
                case (NODE | FRAME | ROOT | CONTENT):
                    node = tab[0];
                    frame = tab[1];
                    rootLayout = tab[2];
                    contentWindow = tab[3];
                    break;
                default:
                    break;
            }
            uri.setNode(node);
            uri.setFrame(frame);
            uri.setRootLayout(rootLayout);
            uri.setContentWindow(contentWindow);
            if (length == 3) {
                getRenderParameters(uri, basePathTab[2]);
            }
        }
    }

    private void getNewRenderAndActionParameters(AtlantalURIInstance uri, String newPath) {
        String[] newPathTab = newPath.split(INPATH_SEPARATOR);
        int length = newPathTab.length;
        for (int i = 0; i < length; i++) {
            String[] ptlInfos = newPathTab[i].split(INPATH_SUBSEPARATOR);
            int infosLength = ptlInfos.length;
            if (infosLength > 1) {
                String ptlName = ptlInfos[0].toLowerCase(Locale.getDefault());
                char last = ptlName.charAt(ptlName.length() - 1);
                boolean action = (last == ACTION_SUFFIX);
                if (action) {
                    ptlName = ptlName.substring(0, ptlName.length() - 1);
                }
                Map params = new LinkedHashMap();
                for (int j = 1; j < infosLength; j += 2) {
                    params.put(ptlInfos[j], ptlInfos[j + 1]);
                }
                if (action) {
                    uri.setActionParameters(ptlName, params);
                } else {
                    uri.setNewRenderParameters(ptlName, params);
                }
            }
        }
    }

    private void parseNewPath(AtlantalURIInstance uri, String newPath) {
        getNewRenderAndActionParameters(uri, newPath);
    }

    private void parseQueryString(AtlantalURIInstance uri, String queryString) {
        Map queryParameters = new LinkedHashMap();
        if (queryString != null) {
            String[] couples = queryString.split("&");
            for (int i = 0; i < couples.length; i++) {
                String[] keyvalue = couples[i].split("=");
                if (keyvalue.length == 2) {
                    queryParameters.put(keyvalue[0], keyvalue[1]);
                }
            }
        }
        uri.setQueryParameters(queryParameters);
    }

    /**
     * Converts a atlantal URI to a URI string.
     *
     * @param uri the portal URL to convert.
     * @return a URL string representing the portal URL.
     */
    public String toString(AtlantalURI uri) {
        return toString(uri, false);
    }

    /**
     * Converts a atlantal URI to a URI string.
     *
     * @param uri the portal URL to convert.
     * @param relative relative path of the URL
     * @return a URL string representing the portal URL.
     */
    public String toString(AtlantalURI uri, boolean relative) {
        return toString(uri, null, null, relative);
    }

    /**
     * Converts a atlantal URI to a URI string.
     *
     * @param uri the portal URL to convert.
     * @param newRenderParameters newRenderParameters
     * @param actionParameters actionParameters
     * @param relative relative path of the URL
     * @return a URL string representing the portal URL.
     */
    public String toString(AtlantalURI uri, Map newRenderParameters, Map actionParameters, boolean relative) {
        StringBuilder uriStr = new StringBuilder();
        toString(uriStr, uri, newRenderParameters, actionParameters, relative);
        return uriStr.toString();
    }

    /**
     * Converts a atlantal URI to a URI string.
     *
     * @param uriStr a string builder to append to.
     * @param uri the portal URL to convert.
     * @param newRenderParameters newRenderParameters
     * @param actionParameters actionParameters
     * @param relative relative path of the URL
     */
    public void toString(StringBuilder uriStr, AtlantalURI uri, Map newRenderParameters, Map actionParameters, boolean relative) {
        AtlantalURIInstance uriImpl = (AtlantalURIInstance) uri;
        if (newRenderParameters == null) {
            newRenderParameters = uriImpl.getNewRenderParameters();
        }
        if (actionParameters == null) {
            actionParameters = uriImpl.getActionParameters();
        }
        if (!relative) {
            uriStr.append(uriImpl.getContextPath());
            uriStr.append("/");
            toStringBasePath(uriImpl, uriStr);
            uriStr.append("/");
        }
        toStringNewRenderAndActionParams(uriImpl, uriStr, newRenderParameters, actionParameters);
        toStringQueryParams(uriImpl, uriStr);
    }

    /**
     * @param uri uri
     * @param uriStr uriStr
     */
    private static void toStringBasePath(AtlantalURIInstance uri, StringBuilder uriStr) {
        int code = EMPTY;
        if (uri.getNode() != null) {
            code |= NODE;
        }
        if (uri.getFrame() != null) {
            code |= FRAME;
        }
        if (uri.getRootLayout() != null) {
            code |= ROOT;
        }
        if (uri.getContentWindow() != null) {
            code |= CONTENT;
        }
        uriStr.append(code);
        uriStr.append(PATH_SEPARATOR);
        switch(code) {
            case (NODE):
                uriStr.append(uri.getNode());
                break;
            case (ROOT):
                uriStr.append(uri.getRootLayout());
                break;
            case (NODE | ROOT):
                uriStr.append(uri.getNode());
                uriStr.append(uri.getRootLayout());
                break;
            case (FRAME | ROOT):
                uriStr.append(uri.getFrame());
                uriStr.append(INPATH_SEPARATOR).append(uri.getRootLayout());
                break;
            case (NODE | FRAME | ROOT):
                uriStr.append(uri.getNode());
                uriStr.append(INPATH_SEPARATOR).append(uri.getFrame());
                uriStr.append(INPATH_SEPARATOR).append(uri.getRootLayout());
                break;
            case (ROOT | CONTENT):
                uriStr.append(uri.getRootLayout());
                uriStr.append(INPATH_SEPARATOR).append(uri.getContentWindow());
                break;
            case (NODE | ROOT | CONTENT):
                uriStr.append(uri.getNode());
                uriStr.append(INPATH_SEPARATOR).append(uri.getRootLayout());
                uriStr.append(INPATH_SEPARATOR).append(uri.getContentWindow());
                break;
            case (FRAME | ROOT | CONTENT):
                uriStr.append(uri.getFrame());
                uriStr.append(INPATH_SEPARATOR).append(uri.getRootLayout());
                uriStr.append(INPATH_SEPARATOR).append(uri.getContentWindow());
                break;
            case (NODE | FRAME | ROOT | CONTENT):
                uriStr.append(uri.getFrame());
                uriStr.append(INPATH_SEPARATOR).append(uri.getNode());
                uriStr.append(INPATH_SEPARATOR).append(uri.getRootLayout());
                uriStr.append(INPATH_SEPARATOR).append(uri.getContentWindow());
                break;
            default:
                break;
        }
        toStringBasePathRenderParams(uri, uriStr);
    }

    /**
     * @param uri uri
     * @param uriStr uriStr
     */
    private static void toStringBasePathRenderParams(AtlantalURIInstance uri, StringBuilder uriStr) {
        Iterator it = uri.getRenderParameters().entrySet().iterator();
        if (it.hasNext()) {
            boolean first = true;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String ptlName = (String) entry.getKey();
                Map params = (Map) entry.getValue();
                Iterator it2 = params.entrySet().iterator();
                if (it2.hasNext()) {
                    if (first) {
                        first = false;
                        uriStr.append(PATH_SEPARATOR);
                    } else {
                        uriStr.append(INPATH_SEPARATOR);
                    }
                    uriStr.append(ptlName);
                    do {
                        Map.Entry entry2 = (Map.Entry) it2.next();
                        String name = (String) entry2.getKey();
                        String value;
                        Object obj = entry2.getValue();
                        if (obj instanceof String[]) {
                            value = ((String[]) obj)[0];
                        } else {
                            value = obj.toString();
                        }
                        uriStr.append(INPATH_SUBSEPARATOR);
                        uriStr.append(name);
                        uriStr.append(INPATH_SUBSEPARATOR);
                        uriStr.append(value);
                    } while (it2.hasNext());
                }
            }
        }
    }

    /**
     * @param uri uri
     * @param uriStr uriStr
     */
    private static void toStringNewRenderAndActionParams(AtlantalURIInstance uri, StringBuilder uriStr, Map renderParams, Map actionParams) {
        boolean hasRenderParams = false;
        if (renderParams != null) {
            boolean first = true;
            Iterator it = renderParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String ptlName = (String) entry.getKey();
                Map params = (Map) entry.getValue();
                Iterator it2 = params.entrySet().iterator();
                boolean hasRenderParams2 = it2.hasNext();
                if (hasRenderParams2) {
                    hasRenderParams = true;
                    if (first) {
                        first = false;
                    } else {
                        uriStr.append(INPATH_SEPARATOR);
                    }
                    uriStr.append(ptlName);
                    do {
                        Map.Entry entry2 = (Map.Entry) it2.next();
                        String name = (String) entry2.getKey();
                        String value;
                        Object obj = entry2.getValue();
                        if (obj instanceof String[]) {
                            value = ((String[]) obj)[0];
                        } else {
                            value = obj.toString();
                        }
                        uriStr.append(INPATH_SUBSEPARATOR);
                        uriStr.append(name);
                        uriStr.append(INPATH_SUBSEPARATOR);
                        uriStr.append(value);
                    } while (it2.hasNext());
                }
            }
        }
        boolean hasActionParams = false;
        if (actionParams != null) {
            Iterator it = actionParams.entrySet().iterator();
            if (it.hasNext()) {
                boolean first = true;
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String ptlName = (String) entry.getKey();
                    Map params = (Map) entry.getValue();
                    Iterator it2 = params.entrySet().iterator();
                    boolean hasActionParams2 = it2.hasNext();
                    if (hasActionParams2) {
                        hasActionParams = true;
                        if (first) {
                            first = false;
                            if (hasRenderParams) {
                                uriStr.append(INPATH_SEPARATOR);
                            }
                        } else {
                            uriStr.append(INPATH_SEPARATOR);
                        }
                        uriStr.append(ptlName).append(ACTION_SUFFIX);
                        do {
                            Map.Entry entry2 = (Map.Entry) it2.next();
                            String name = (String) entry2.getKey();
                            String value;
                            Object obj = entry2.getValue();
                            if (obj instanceof String[]) {
                                value = ((String[]) obj)[0];
                            } else {
                                value = obj.toString();
                            }
                            uriStr.append(INPATH_SUBSEPARATOR);
                            uriStr.append(name);
                            uriStr.append(INPATH_SUBSEPARATOR);
                            uriStr.append(value);
                        } while (it2.hasNext());
                    }
                }
            }
        }
        if (!(hasRenderParams || hasActionParams)) {
            uriStr.append("index");
        }
        uriStr.append(".phtml");
    }

    /**
     * @param uri uri
     * @param uriStr uriStr
     */
    private static void toStringQueryParams(AtlantalURIInstance uri, StringBuilder uriStr) {
        Iterator it = uri.getQueryParameters().entrySet().iterator();
        if (it.hasNext()) {
            boolean first = true;
            uriStr.append("?");
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                try {
                    if (first) {
                        first = false;
                    } else {
                        uriStr.append("&amp;");
                    }
                    uriStr.append(name).append("=");
                    uriStr.append(URLCODEC.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }
}
