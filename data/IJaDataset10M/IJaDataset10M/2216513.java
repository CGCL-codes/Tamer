package net.teqlo.components.standard.restV0_01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.teqlo.TeqloException;
import net.teqlo.components.standard.javascriptV0_01.AbstractScriptActivity;
import net.teqlo.components.standard.javascriptV0_01.AbstractScriptExecutor;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.User;
import net.teqlo.db.sleepycat.SleepycatXmlDatabase;
import net.teqlo.environment.servlet.TeqloRestServlet;
import net.teqlo.management.NanoTimer;
import net.teqlo.management.NanoTimers;
import net.teqlo.util.TeqloProperties;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * This class uses the Apache HttpClient library which is easier to use and more fully featured than the stuff in java.net
 * 
 * @author jthwaites
 * 
 */
public class RestActivity extends AbstractScriptActivity {

    public static final String RESPONSE_SIZE_PROP = "net.teqlo.activity.rest.maxResponseSize";

    public static final String RESPONSE_TIMEOUT_PROP = "net.teqlo.activity.rest.timeout";

    public static int DEFAULT_MAX_RESPONSE_SIZE = 128000;

    public static String MAX_RESPONSE_SIZE_ATTRIBUTE = "rest.maxResponseSize";

    public static int DEFAULT_TIMEOUT_MILLIS = 60000;

    public static final String REST_METHOD = "rest.method";

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final String REST_URL = "rest.url";

    public static final String REST_ENCODE_URL = "rest.encodeurl";

    public static final String REST_PARAMS = "rest.params";

    public static final String REST_HEADERS = "rest.headers";

    public static final String REST_BODY = "rest.body";

    public static final String REST_USERNAME = "rest.username";

    public static final String REST_PASSWORD = "rest.password";

    public static final String REQUEST_BODY_FIELD = "requestBody";

    public static final String RESPONSE_JAVASCRIPT_OBJECT = "response";

    public static final String STATUSCODE_JAVASCRIPT_OBJECT = "statusCode";

    private int maxResponseSize = TeqloProperties.getInteger(RESPONSE_SIZE_PROP, DEFAULT_MAX_RESPONSE_SIZE);

    /**
     * Constructor as per abstract script activity
     * 
     * @param executor
     * @param context
     * @param fqn
     * @throws TeqloException
     */
    public RestActivity(User user, AbstractScriptExecutor executor, ActivityLookup al) throws TeqloException {
        super(user, executor, al);
    }

    protected void actionsOnOpen() throws TeqloException {
        super.actionsOnOpen();
    }

    protected void actionsOnRun() throws Exception {
        String maxResponseSizeAttr = getAttributeValueOrInput(MAX_RESPONSE_SIZE_ATTRIBUTE);
        if (maxResponseSizeAttr != null && maxResponseSizeAttr.length() > 0) this.maxResponseSize = Integer.parseInt(maxResponseSizeAttr);
        HttpClient client = new HttpClient();
        HttpClientParams params = client.getParams();
        params.setSoTimeout(TeqloProperties.getInteger(RESPONSE_TIMEOUT_PROP, RestActivity.DEFAULT_TIMEOUT_MILLIS));
        HttpMethod method = this.setupRestMethod();
        String response = null;
        Integer statusCode = null;
        NanoTimer timer = NanoTimers.start("RestActivity");
        try {
            Credentials credentials = this.getCredentials();
            if (credentials != null) {
                client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), credentials);
                method.setDoAuthentication(true);
            }
            statusCode = client.executeMethod(method);
            char[] buf = new char[4096];
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), SleepycatXmlDatabase.STRING_ENCODING));
            for (; ; ) {
                int count = br.read(buf);
                if (count == -1) break;
                if (sb.length() + count > maxResponseSize) throw new TeqloException(this, maxResponseSize, null, "Response exceeded the maximum size of " + maxResponseSize); else sb.append(buf, 0, count);
            }
            response = sb.toString();
        } finally {
            method.releaseConnection();
            client = null;
        }
        this.setScriptProperty(RESPONSE_JAVASCRIPT_OBJECT, response);
        this.setScriptProperty(STATUSCODE_JAVASCRIPT_OBJECT, statusCode);
        this.setScriptInputProperties();
        this.runScript();
        this.getScriptOutputProperties();
        NanoTimers.step(timer, "actionsOnRun", response == null ? 0 : response.length());
    }

    /**
     * 
     * @return
     */
    private HttpMethod setupRestMethod() throws TeqloException {
        String method = getAttributeValueOrInput(REST_METHOD);
        if (method == null || method.length() == 0 || method.equalsIgnoreCase(GET_METHOD)) return setupGetMethod(); else if (method.equalsIgnoreCase(POST_METHOD)) return setupPostMethod(); else throw new TeqloException(this, al, null, "Activity must specify rest.method attribute of 'GET' or 'POST'");
    }

    /**
     * Returns credentials if method needs authentication.
     * 
     * @return null or the appropriate credentials
     */
    protected Credentials getCredentials() throws TeqloException {
        String username = getAttributeValueOrInput(REST_USERNAME);
        if (username != null) return new UsernamePasswordCredentials(username, getAttributeValueOrInput(REST_PASSWORD));
        return null;
    }

    /**
     * Returns a new name value pair array containing the map entries
     * 
     * @param map to convert
     * @return array of name value pairs
     */
    protected NameValuePair[] mapToNameValuePairArray(Map<String, String> map) {
        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (String key : map.keySet()) nvps.add(new NameValuePair(key, map.get(key)));
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }

    public String getURLAttribute() throws TeqloException {
        String url = getAttributeValueOrInput(REST_URL);
        if (url == null || url.length() == 0) throw new TeqloException(this, REST_URL, null, "This REST activity attribute must be specified");
        if (TeqloRestServlet.isTrue(this.getAttributeValueOrInput(REST_ENCODE_URL))) try {
            url = encodeURI(url);
        } catch (IllegalArgumentException e) {
            throw new TeqloException(this, url, e, "Invalid URL");
        }
        return url;
    }

    private HttpMethod setupGetMethod() throws TeqloException {
        GetMethod method = new GetMethod(getURLAttribute());
        Map<String, String> headers = this.getAttributeValuesOrInput(REST_HEADERS);
        if (headers != null) {
            for (String header : headers.keySet()) method.setRequestHeader(header, headers.get(header));
        }
        Map<String, String> params = this.getAttributeValuesOrInput(REST_PARAMS);
        if (params != null && params.size() > 0) method.setQueryString(this.mapToNameValuePairArray(params));
        return method;
    }

    private HttpMethod setupPostMethod() throws TeqloException {
        PostMethod method = new PostMethod(getURLAttribute());
        Map<String, String> headers = this.getAttributeValuesOrInput(REST_HEADERS);
        if (headers != null) {
            for (String header : headers.keySet()) method.setRequestHeader(header, headers.get(header));
        }
        Map<String, String> params = this.getAttributeValuesOrInput(REST_PARAMS);
        if (params != null && params.size() > 0) {
            method.setRequestBody(this.mapToNameValuePairArray(params));
        }
        Set<String> inputKeys = this.al.getInput().getKeys();
        if (inputKeys != null && inputKeys.contains(REQUEST_BODY_FIELD)) {
            String body = (String) this.input.get(REQUEST_BODY_FIELD);
            StringRequestEntity request = new StringRequestEntity(body);
            method.setRequestEntity(request);
        }
        return method;
    }

    private String getAttributeValueOrInput(String name) throws TeqloException {
        if (input.containsKey(name)) return (String) input.get(name);
        return getAttributeValue(name);
    }

    @SuppressWarnings({ "unchecked" })
    private Map<String, String> getAttributeValuesOrInput(String name) throws TeqloException {
        if (input.containsKey(name)) return (Map<String, String>) input.get(name);
        return getAttributeValues(name);
    }

    @SuppressWarnings("null")
    private static String encodeURI(String str) {
        byte[] utf8buf = null;
        StringBuffer sb = null;
        for (int k = 0, length = str.length(); k != length; ++k) {
            char C = str.charAt(k);
            if (encodeUnescaped(C, true)) {
                if (sb != null) {
                    sb.append(C);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuffer(length + 3);
                    sb.append(str);
                    sb.setLength(k);
                    utf8buf = new byte[6];
                }
                if (0xDC00 <= C && C <= 0xDFFF) {
                    throw new IllegalArgumentException("Malformed URL");
                }
                int V;
                if (C < 0xD800 || 0xDBFF < C) {
                    V = C;
                } else {
                    k++;
                    if (k == length) {
                        throw new IllegalArgumentException("Malformed URL");
                    }
                    char C2 = str.charAt(k);
                    if (!(0xDC00 <= C2 && C2 <= 0xDFFF)) {
                        throw new IllegalArgumentException("Malformed URL");
                    }
                    V = ((C - 0xD800) << 10) + (C2 - 0xDC00) + 0x10000;
                }
                int L = oneUcs4ToUtf8Char(utf8buf, V);
                for (int j = 0; j < L; j++) {
                    int d = 0xff & utf8buf[j];
                    sb.append('%');
                    sb.append(toHexChar(d >>> 4));
                    sb.append(toHexChar(d & 0xf));
                }
            }
        }
        return (sb == null) ? str : sb.toString();
    }

    private static char toHexChar(int i) {
        if (i >> 4 != 0) throw new IllegalStateException("FAILED ASSERTION");
        return (char) ((i < 10) ? i + '0' : i - 10 + 'a');
    }

    private static boolean encodeUnescaped(char c, boolean fullUri) {
        if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9')) {
            return true;
        }
        if ("-_.!~*'()".indexOf(c) >= 0) return true;
        if (fullUri) {
            return URI_DECODE_RESERVED.indexOf(c) >= 0;
        }
        return false;
    }

    private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";

    private static int oneUcs4ToUtf8Char(byte[] utf8Buffer, int ucs4Char) {
        int utf8Length = 1;
        if ((ucs4Char & ~0x7F) == 0) utf8Buffer[0] = (byte) ucs4Char; else {
            int i;
            int a = ucs4Char >>> 11;
            utf8Length = 2;
            while (a != 0) {
                a >>>= 5;
                utf8Length++;
            }
            i = utf8Length;
            while (--i > 0) {
                utf8Buffer[i] = (byte) ((ucs4Char & 0x3F) | 0x80);
                ucs4Char >>>= 6;
            }
            utf8Buffer[0] = (byte) (0x100 - (1 << (8 - utf8Length)) + ucs4Char);
        }
        return utf8Length;
    }
}
