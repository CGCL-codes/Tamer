package com.cromoteca.meshcms.server.toolbox;

import com.cromoteca.meshcms.client.toolbox.Path;
import com.cromoteca.meshcms.server.core.Context;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Web {

    public static final String ATTRIBUTE_INCLUDE_PATH_INFO = "javax.servlet.include.path_info";

    public static final String ATTRIBUTE_INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";

    public static final String ATTRIBUTE_FORWARD_PATH_INFO = "javax.servlet.forward.path_info";

    public static final String ATTRIBUTE_FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";

    public static final Map<Character, String> NUMBER_TO_ENTITY;

    public static final Pattern QUERY_STRING_PARAM = Pattern.compile("[?&]([^?=&#]+)=([^=&#]*)");

    /**
	 * The default array of welcome file names. This array is used if the welcome
	 * file names can't be found in the web.xml configuration file.
	 */
    public static final String[] DEFAULT_WELCOME_FILES = { "index.html", "index.htm", "index.jsp" };

    public static final Pattern HYPERLINK_REGEX = Pattern.compile("(?:https?://|ftp://|www\\.)[a-zA-Z_0-9-/=:#%&\\?\\.]+[a-zA-Z_0-9-/=:#%&\\?]");

    public static final Pattern EMAIL_REGEX = Pattern.compile("[a-zA-Z_0-9-\\.]+@[a-zA-Z_0-9-\\.]+\\.[a-zA-Z]{2,6}");

    public static final Pattern PRE_REGEX = Pattern.compile("(?s)<pre[^>]*>.*?</pre>", Pattern.CASE_INSENSITIVE);

    public static final Pattern PRE_BR_REGEX = Pattern.compile("<br[^>]*>(?:\\s*\\n)?", Pattern.CASE_INSENSITIVE);

    public static final Pattern SRC_HREF_REGEX = Pattern.compile("(<(?:a|img)(?:[^>]*?)(?:src|href)=\")([^\":?]*)(\"[^>]*?>)", Pattern.CASE_INSENSITIVE);

    static {
        String[] entities = { "39", "#39", "160", "nbsp", "161", "iexcl", "162", "cent", "163", "pound", "164", "curren", "165", "yen", "166", "brvbar", "167", "sect", "168", "uml", "169", "copy", "170", "ordf", "171", "laquo", "172", "not", "173", "shy", "174", "reg", "175", "macr", "176", "deg", "177", "plusmn", "178", "sup2", "179", "sup3", "180", "acute", "181", "micro", "182", "para", "183", "middot", "184", "cedil", "185", "sup1", "186", "ordm", "187", "raquo", "188", "frac14", "189", "frac12", "190", "frac34", "191", "iquest", "192", "Agrave", "193", "Aacute", "194", "Acirc", "195", "Atilde", "196", "Auml", "197", "Aring", "198", "AElig", "199", "Ccedil", "200", "Egrave", "201", "Eacute", "202", "Ecirc", "203", "Euml", "204", "Igrave", "205", "Iacute", "206", "Icirc", "207", "Iuml", "208", "ETH", "209", "Ntilde", "210", "Ograve", "211", "Oacute", "212", "Ocirc", "213", "Otilde", "214", "Ouml", "215", "times", "216", "Oslash", "217", "Ugrave", "218", "Uacute", "219", "Ucirc", "220", "Uuml", "221", "Yacute", "222", "THORN", "223", "szlig", "224", "agrave", "225", "aacute", "226", "acirc", "227", "atilde", "228", "auml", "229", "aring", "230", "aelig", "231", "ccedil", "232", "egrave", "233", "eacute", "234", "ecirc", "235", "euml", "236", "igrave", "237", "iacute", "238", "icirc", "239", "iuml", "240", "eth", "241", "ntilde", "242", "ograve", "243", "oacute", "244", "ocirc", "245", "otilde", "246", "ouml", "247", "divide", "248", "oslash", "249", "ugrave", "250", "uacute", "251", "ucirc", "252", "uuml", "253", "yacute", "254", "thorn", "255", "yuml", "402", "fnof", "913", "Alpha", "914", "Beta", "915", "Gamma", "916", "Delta", "917", "Epsilon", "918", "Zeta", "919", "Eta", "920", "Theta", "921", "Iota", "922", "Kappa", "923", "Lambda", "924", "Mu", "925", "Nu", "926", "Xi", "927", "Omicron", "928", "Pi", "929", "Rho", "931", "Sigma", "932", "Tau", "933", "Upsilon", "934", "Phi", "935", "Chi", "936", "Psi", "937", "Omega", "945", "alpha", "946", "beta", "947", "gamma", "948", "delta", "949", "epsilon", "950", "zeta", "951", "eta", "952", "theta", "953", "iota", "954", "kappa", "955", "lambda", "956", "mu", "957", "nu", "958", "xi", "959", "omicron", "960", "pi", "961", "rho", "962", "sigmaf", "963", "sigma", "964", "tau", "965", "upsilon", "966", "phi", "967", "chi", "968", "psi", "969", "omega", "977", "thetasym", "978", "upsih", "982", "piv", "8226", "bull", "8230", "hellip", "8242", "prime", "8243", "Prime", "8254", "oline", "8260", "frasl", "8472", "weierp", "8465", "image", "8476", "real", "8482", "trade", "8501", "alefsym", "8592", "larr", "8593", "uarr", "8594", "rarr", "8595", "darr", "8596", "harr", "8629", "crarr", "8656", "lArr", "8657", "uArr", "8658", "rArr", "8659", "dArr", "8660", "hArr", "8704", "forall", "8706", "part", "8707", "exist", "8709", "empty", "8711", "nabla", "8712", "isin", "8713", "notin", "8715", "ni", "8719", "prod", "8721", "sum", "8722", "minus", "8727", "lowast", "8730", "radic", "8733", "prop", "8734", "infin", "8736", "ang", "8743", "and", "8744", "or", "8745", "cap", "8746", "cup", "8747", "int", "8756", "there4", "8764", "sim", "8773", "cong", "8776", "asymp", "8800", "ne", "8801", "equiv", "8804", "le", "8805", "ge", "8834", "sub", "8835", "sup", "8836", "nsub", "8838", "sube", "8839", "supe", "8853", "oplus", "8855", "otimes", "8869", "perp", "8901", "sdot", "8968", "lceil", "8969", "rceil", "8970", "lfloor", "8971", "rfloor", "9001", "lang", "9002", "rang", "9674", "loz", "9824", "spades", "9827", "clubs", "9829", "hearts", "9830", "diams", "34", "quot", "38", "amp", "60", "lt", "62", "gt", "338", "OElig", "339", "oelig", "352", "Scaron", "353", "scaron", "376", "Yuml", "710", "circ", "732", "tilde", "8194", "ensp", "8195", "emsp", "8201", "thinsp", "8204", "zwnj", "8205", "zwj", "8206", "lrm", "8207", "rlm", "8211", "ndash", "8212", "mdash", "8216", "lsquo", "8217", "rsquo", "8218", "sbquo", "8220", "ldquo", "8221", "rdquo", "8222", "bdquo", "8224", "dagger", "8225", "Dagger", "8240", "permil", "8249", "lsaquo", "8250", "rsaquo", "8364", "euro" };
        NUMBER_TO_ENTITY = new HashMap<Character, String>();
        for (int i = 0; i < entities.length; i += 2) {
            NUMBER_TO_ENTITY.put((char) Integer.parseInt(entities[i]), entities[i + 1]);
        }
    }

    public static String convertToHTMLEntities(String s) {
        return convertToHTMLEntities(s, IO.SYSTEM_CHARSET, false);
    }

    public static String convertToHTMLEntities(CharSequence s, String charset, boolean encodeTags) {
        if (s == null) {
            return null;
        }
        CharsetEncoder ce = charset == null ? null : Charset.forName(charset).newEncoder();
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int n = ((int) c) & 0xFFFF;
            if (n > 127) {
                if (ce != null && ce.canEncode(c)) {
                    sb.append(c);
                } else {
                    String ent = NUMBER_TO_ENTITY.get(c);
                    if (ent == null) {
                        sb.append("&#").append(n).append(';');
                    } else {
                        sb.append('&').append(ent).append(';');
                    }
                }
            } else if (encodeTags && (n == 34 || n == 39 || n == 60 || n == 62)) {
                sb.append('&').append(NUMBER_TO_ENTITY.get(c)).append(';');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getURLParameter(CharSequence url, String name, boolean decodeValue) {
        String value = null;
        Matcher matcher = QUERY_STRING_PARAM.matcher(url);
        while (value == null && matcher.find()) {
            if (matcher.group(1).equals(name)) {
                value = matcher.group(2);
            }
        }
        if (decodeValue && value != null) {
            try {
                value = URLDecoder.decode(value, IO.SYSTEM_CHARSET);
            } catch (UnsupportedEncodingException ex) {
                Context.log(ex);
            }
        }
        return value;
    }

    /**
	 * Use null value to remove parameter
	 */
    public static String setURLParameter(String url, String name, String value, boolean encodeValue) {
        if (encodeValue) {
            try {
                value = URLEncoder.encode(value, IO.SYSTEM_CHARSET);
            } catch (UnsupportedEncodingException ex) {
                Context.log(ex);
            }
        }
        int hashIndex = url.indexOf('#');
        String anchor;
        if (hashIndex < 0) {
            anchor = "";
        } else {
            anchor = url.substring(hashIndex);
            url = url.substring(0, hashIndex);
        }
        int qi = url.indexOf('?');
        if (qi < 0) {
            if (value != null) {
                url += '?' + name + '=' + value + anchor;
            }
            return url;
        }
        boolean found = false;
        boolean qm = true;
        Matcher matcher = QUERY_STRING_PARAM.matcher(url);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String n = matcher.group(1);
            String v = matcher.group(2);
            if (n.equals(name)) {
                v = value;
                found = true;
                if (v == null) {
                    matcher.appendReplacement(sb, "");
                    continue;
                }
            }
            matcher.appendReplacement(sb, (qm ? '?' : '&') + Strings.escapeRegexReplacement(n + '=' + v));
            qm = false;
        }
        matcher.appendTail(sb);
        if (!(found || value == null)) {
            sb.append('&').append(name).append('=').append(value);
        }
        sb.append(anchor);
        return sb.toString();
    }

    /**
	 * Parses the web.xml configuration file and returns an array of welcome file
	 * names. If the names can't be found, {@link #DEFAULT_WELCOME_FILES} is
	 * returned.
	 *
	 * @param sc ServletContext required to access the <code>web.xml</code> file
	 *
	 * @return an array of welcome file names
	 */
    public static String[] getWelcomeFiles(ServletContext sc) {
        InputStream webXml = sc.getResourceAsStream("/WEB-INF/web.xml");
        String[] welcomeFiles = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.parse(webXml);
            Element root = document.getDocumentElement();
            Element wfl = (Element) root.getElementsByTagName("welcome-file-list").item(0);
            NodeList wfnl = wfl.getElementsByTagName("welcome-file");
            welcomeFiles = new String[wfnl.getLength()];
            for (int i = 0; i < welcomeFiles.length; i++) {
                welcomeFiles[i] = wfnl.item(i).getFirstChild().getNodeValue();
            }
        } catch (Exception ex) {
            Context.log(ex);
        }
        if (welcomeFiles == null || welcomeFiles.length == 0) {
            welcomeFiles = DEFAULT_WELCOME_FILES;
        }
        return welcomeFiles;
    }

    public static String findEmails(CharSequence text) {
        Matcher m = EMAIL_REGEX.matcher(text);
        StringBuffer sb = new StringBuffer(text.length());
        while (m.find()) {
            String email = m.group();
            String tag = "<a href=\"mailto:" + email + "\" rel=\"nofollow\">" + email + "</a>";
            m.appendReplacement(sb, Strings.escapeRegexReplacement(tag));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String findLinks(CharSequence text, boolean noFollow) {
        String rel = noFollow ? " rel=\"nofollow\"" : "";
        Matcher m = HYPERLINK_REGEX.matcher(text);
        StringBuffer sb = new StringBuffer(text.length());
        while (m.find()) {
            String link = m.group();
            String url = link;
            if (url.indexOf("://") < 0) {
                url = "http://" + url;
            }
            String tag = "<a href=\"" + url + "\"" + rel + '>' + link + "</a>";
            m.appendReplacement(sb, Strings.escapeRegexReplacement(tag));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String fixRelativeURLs(CharSequence html, Path root, boolean relative) {
        Matcher m = SRC_HREF_REGEX.matcher(html);
        StringBuffer sb = new StringBuffer(html.length());
        while (m.find()) {
            Path path = relative ? new Path(m.group(2)).getRelativeTo(root) : root.add(m.group(2));
            m.appendReplacement(sb, "$1" + Strings.escapeRegexReplacement(path.asLink()) + "$3");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String[] getAcceptedLanguages(HttpServletRequest request) {
        Enumeration alEnum = request.getHeaders("Accept-Language");
        List<String> list = new ArrayList<String>();
        while (alEnum.hasMoreElements()) {
            String s = (String) alEnum.nextElement();
            StringTokenizer st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int sc = token.indexOf(';');
                if (sc >= 0) {
                    token = token.substring(0, sc);
                }
                token = token.trim().replace("-", "_");
                if (!list.contains(token)) {
                    list.add(token);
                }
            }
        }
        int n = list.size();
        for (int i = 0; i < n; i++) {
            String token = list.get(i);
            int us = token.indexOf('_');
            if (us >= 0) {
                token = token.substring(0, us);
                if (!list.contains(token)) {
                    list.add(token);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
	 * Reconstructs the full URL of the context home. The URL is returned as a
	 * StringBuilder so other elements can be added easily.
	 */
    public static StringBuilder getContextHomeURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(request.getServerName());
        int port = request.getServerPort();
        if (!((port == 443 && scheme.startsWith("https")) || (port == 80))) {
            sb.append(':').append(port);
        }
        sb.append(request.getContextPath());
        return sb;
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder sb = getContextHomeURL(request);
        sb.append(Strings.noNull(request.getServletPath())).append(Strings.noNull(request.getPathInfo()));
        String qs = request.getQueryString();
        if (qs != null) {
            sb.append('?').append(qs);
        }
        return sb.toString();
    }

    public static String getRequestPath(HttpServletRequest request) {
        String s = (String) request.getAttribute(ATTRIBUTE_INCLUDE_SERVLET_PATH);
        if (s == null) {
            s = Strings.noNull(request.getServletPath()) + Strings.noNull(request.getPathInfo());
        } else {
            s += Strings.noNull((String) request.getAttribute(ATTRIBUTE_INCLUDE_PATH_INFO));
        }
        return s;
    }

    public static String getServletPath(HttpServletRequest request) {
        return isInclude(request) ? (String) request.getAttribute(ATTRIBUTE_INCLUDE_SERVLET_PATH) : request.getServletPath();
    }

    public static String getPathInfo(HttpServletRequest request) {
        return isInclude(request) ? (String) request.getAttribute(ATTRIBUTE_INCLUDE_PATH_INFO) : request.getPathInfo();
    }

    public static boolean isInclude(ServletRequest request) {
        return !(request.getAttribute(ATTRIBUTE_INCLUDE_SERVLET_PATH) == null && request.getAttribute(ATTRIBUTE_INCLUDE_PATH_INFO) == null);
    }
}
