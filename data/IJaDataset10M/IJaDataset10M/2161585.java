package com.jcorporate.expresso.core.security.filters;

import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.misc.StringUtil;
import com.jcorporate.expresso.services.dbobj.Setup;
import javax.servlet.http.HttpServletRequest;

/**
 * This class subclasses HtmlFilter,
 * plus it creates anchor (<a>) tags for anything that starts with 'http://', 'www.', etc.
 *
 * This filter is best used for DISPLAY ONLY, NOT EDITING, like:
 * Filter old = myDbObject.setFilterClass(new RawFilter());<br>
 * String myDisplayText = myDbObject.getField("MyField"); <br>
 * myDbObject.setFilterClass(old); // restore<br>
 *
 * This class is not recommended for use in the 'permanent' setting of DBObject.setStringFilterClass()
 * because if you call getField() to EDIT the original text, an anchor tag would show up for editing,
 * and if saved, the '<' character would then be stripped, since tags are filtered out of the database data by this filter.
 *
 * In my application, I use the RawFilter for getting editable, since my fields are presented as HTML text areas.
 *
 * @author Larry Hamel
 */
public class HtmlPlusURLFilter extends HtmlFilter {

    public static final String[] URL_TYPES = { "http://", "https://", "ftp://", "mailto:", "news:" };

    public static final String[] URL_INFORMAL_PREFIXES = { "www.", "www2." };

    /**
     * name for Setup value which decides if we are limiting anchor labels
     */
    public static final String MAX_CHARS_IN_URL_LABEL = "MaxCharsURL_Label";

    /**
     * no-args constructor required
     *
     * @throws IllegalArgumentException
     */
    public HtmlPlusURLFilter() throws IllegalArgumentException {
    }

    /**
     * This filter HTML encodes all special characters defined by the replacement
     * list.  If a particular character doesn't exist in the map, then the chracter
     * is passed appended into the result set.
     * <p/>
     * If it does exist, then the value the special character maps to is appended
     * into the list instead.
     *
     * @param data The string to encode.
     * @return The filtered string
     */
    public String standardFilter(String data) {
        String result = super.standardFilter(data);
        return insertHrefTags(result);
    }

    /**
     * Given a url string, if it's null or equals "" then just return
     * it as is. Otherwise check if it is valid form, that is, starts
     * with http:// or ftp:// or some other valid url prefix. If not,
     * prepend http://.
     *
     * @param url the url string
     * @return the url with http:// prepended, if needed
     */
    public static String addHttpPrefixIfNeeded(String url) {
        if (StringUtil.isBlankOrNull(url)) {
            return url;
        }
        String validUrl = url;
        if (!hasValidUrlPrefix(url)) validUrl = "http://" + url;
        return validUrl;
    }

    /**
     * Return true if the url has a valid prefix, like http://
     *
     * @param url
     * @return
     */
    public static boolean hasValidUrlPrefix(String url) {
        boolean valid = false;
        for (int i = 0; i < URL_TYPES.length; i++) {
            if (url.startsWith(URL_TYPES[i])) {
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Return true if the url is valid. Checks that it is not
     * null, that it has a valid prefix, and that it contains
     * a dot (must, to have a domain name) and at least 2 characters
     * after the dot (the domain). Add more tests here as appropriate.
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        if (StringUtil.isBlankOrNull(url)) {
            return false;
        }
        if (!hasValidUrlPrefix(url)) {
            return false;
        }
        int dotIndex = url.indexOf(".");
        if (dotIndex < 0) {
            return false;
        }
        String domain = url.substring(dotIndex);
        if (domain.length() < 2) {
            return false;
        }
        return true;
    }

    /**
     * Get web server address
     *
     * @return the address of this web server
     */
    public static String getWebHostPort(ControllerRequest request) {
        ServletControllerRequest sreq = (ServletControllerRequest) request;
        HttpServletRequest hreq = (HttpServletRequest) sreq.getServletRequest();
        String serverDomainName = hreq.getServerName();
        int serverPort = hreq.getServerPort();
        if (serverPort != 80) {
            serverDomainName = serverDomainName + ":" + serverPort;
        }
        return serverDomainName;
    }

    /**
     * Insert a href tag around any http, https, www, or www2 strings
     *
     * @param s The string to search in and insert
     * @return A String with <a href></a> tags and http:// if needed
     */
    public static String insertHrefTags(String s) {
        boolean appendHttp = false;
        String result = s;
        int hIndex = -1;
        for (int i = 0; i < URL_TYPES.length; i++) {
            String urlType = URL_TYPES[i];
            hIndex = s.indexOf(urlType);
            if (hIndex != -1) break;
        }
        if (hIndex == -1) {
            for (int i = 0; i < URL_INFORMAL_PREFIXES.length; i++) {
                String urlType = URL_INFORMAL_PREFIXES[i];
                hIndex = s.indexOf(urlType);
                if (hIndex != -1) {
                    appendHttp = true;
                    break;
                }
                hIndex = s.indexOf(urlType.toUpperCase());
                if (hIndex != -1) {
                    appendHttp = true;
                    break;
                }
            }
        }
        if (hIndex >= 0) {
            int endIndex = findEndOfHref(s, hIndex);
            String href = s.substring(hIndex, endIndex);
            href = StringUtil.replaceAll(href, "&lt;", "");
            href = StringUtil.replaceAll(href, "&LT;", "");
            href = StringUtil.replaceAll(href, "&lT;", "");
            href = StringUtil.replaceAll(href, "&Lt;", "");
            href = StringUtil.replaceAll(href, "%3c", "");
            href = StringUtil.replaceAll(href, "%3C", "");
            StringBuffer link = new StringBuffer();
            link.append(" <a href=\"");
            if (appendHttp) {
                link.append("http://");
            }
            link.append(href);
            link.append("\" target=\"_blank\">");
            String max = Setup.getValueUnrequired(DBConnection.DEFAULT_DB_CONTEXT_NAME, MAX_CHARS_IN_URL_LABEL);
            if (max != null) {
                try {
                    int maxchars = Integer.parseInt(max);
                    if (href.length() > maxchars) {
                        link.append(href.substring(0, maxchars));
                        link.append("&#133");
                    } else {
                        link.append(href);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                link.append(href);
            }
            link.append("</a>");
            String linksBefore = "";
            String linksAfter = "";
            if (hIndex > 5) {
                linksBefore = insertHrefTags(s.substring(0, hIndex));
            }
            if (endIndex != s.length()) {
                linksAfter = insertHrefTags(s.substring(endIndex));
            }
            return linksBefore + link.toString() + linksAfter;
        } else {
            return result;
        }
    }

    /**
     * Finds the end of a hyperlink
     *
     * @param s     the string
     * @param start the url's starting index
     */
    public static int findEndOfHref(String s, int start) {
        char[] chars = s.toCharArray();
        int end = s.length();
        for (int i = start; i < end; i++) {
            char c = chars[i];
            if (Character.isLetterOrDigit(c)) {
                continue;
            }
            switch(c) {
                case '.':
                case ',':
                case ')':
                case '(':
                case '@':
                case '?':
                case '&':
                case '=':
                case '-':
                case '_':
                case '/':
                case '#':
                case ':':
                case '~':
                case '%':
                case '+':
                case ';':
                case '!':
                case '*':
                case '\'':
                case '$':
                    continue;
                default:
                    return i;
            }
        }
        return end;
    }
}
