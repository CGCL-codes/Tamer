package twitter4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import twitter4j.http.HTMLEntity;
import twitter4j.http.Response;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Super class of Twitter Response objects.
 *
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterResponse implements java.io.Serializable {

    private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();

    private static final long serialVersionUID = 3519962197957449562L;

    private transient int rateLimitLimit = -1;

    private transient int rateLimitRemaining = -1;

    private transient long rateLimitReset = -1;

    private static final boolean IS_DALVIK = Configuration.isDalvik();

    public TwitterResponse() {
    }

    public TwitterResponse(Response res) {
        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if (null != limit) {
            rateLimitLimit = Integer.parseInt(limit);
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if (null != remaining) {
            rateLimitRemaining = Integer.parseInt(remaining);
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if (null != reset) {
            rateLimitReset = Long.parseLong(reset);
        }
    }

    protected static void ensureRootNodeNameIs(String rootName, Element elem) throws TwitterException {
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check the availability of the Twitter API at http://status.twitter.com/.");
        }
    }

    protected static void ensureRootNodeNameIs(String[] rootNames, Element elem) throws TwitterException {
        String actualRootName = elem.getNodeName();
        for (String rootName : rootNames) {
            if (rootName.equals(actualRootName)) {
                return;
            }
        }
        String expected = "";
        for (int i = 0; i < rootNames.length; i++) {
            if (i != 0) {
                expected += " or ";
            }
            expected += rootNames[i];
        }
        throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + expected + ". Check the availability of the Twitter API at http://status.twitter.com/.");
    }

    protected static void ensureRootNodeNameIs(String rootName, Document doc) throws TwitterException {
        Element elem = doc.getDocumentElement();
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check the availability of the Twitter API at http://status.twitter.com/");
        }
    }

    protected static boolean isRootNodeNilClasses(Document doc) {
        String root = doc.getDocumentElement().getNodeName();
        return "nil-classes".equals(root) || "nilclasses".equals(root);
    }

    protected static String getChildText(String str, Element elem) {
        return HTMLEntity.unescape(getTextContent(str, elem));
    }

    protected static String getTextContent(String str, Element elem) {
        NodeList nodelist = elem.getElementsByTagName(str);
        if (nodelist.getLength() > 0) {
            Node node = nodelist.item(0).getFirstChild();
            if (null != node) {
                String nodeValue = node.getNodeValue();
                return null != nodeValue ? nodeValue : "";
            }
        }
        return "";
    }

    protected static int getChildInt(String str, Element elem) {
        String str2 = getTextContent(str, elem);
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Integer.valueOf(str2);
        }
    }

    protected static long getChildLong(String str, Element elem) {
        String str2 = getTextContent(str, elem);
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Long.valueOf(str2);
        }
    }

    protected static String getString(String name, JSONObject json, boolean decode) {
        String returnValue = null;
        try {
            returnValue = json.getString(name);
            if (decode) {
                try {
                    returnValue = URLDecoder.decode(returnValue, "UTF-8");
                } catch (UnsupportedEncodingException ignore) {
                }
            }
        } catch (JSONException ignore) {
        }
        return returnValue;
    }

    protected static boolean getChildBoolean(String str, Element elem) {
        String value = getTextContent(str, elem);
        return Boolean.valueOf(value);
    }

    protected static Date getChildDate(String str, Element elem) throws TwitterException {
        return getChildDate(str, elem, "EEE MMM d HH:mm:ss z yyyy");
    }

    protected static Date getChildDate(String str, Element elem, String format) throws TwitterException {
        return parseDate(getChildText(str, elem), format);
    }

    protected static Date parseDate(String str, String format) throws TwitterException {
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(format, sdf);
        }
        try {
            synchronized (sdf) {
                return sdf.parse(str);
            }
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected format(" + str + ") returned from twitter.com");
        }
    }

    protected static int getInt(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if (null == str || "null".equals(str)) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    protected static long getLong(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if (null == str || "null".equals(str)) {
            return -1;
        }
        return Long.parseLong(str);
    }

    protected static boolean getBoolean(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if (null == str || "null".equals(str)) {
            return false;
        }
        return Boolean.valueOf(str);
    }

    public int getRateLimitLimit() {
        return rateLimitLimit;
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public long getRateLimitReset() {
        return rateLimitReset;
    }
}
