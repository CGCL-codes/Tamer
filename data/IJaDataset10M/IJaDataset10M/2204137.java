package org.apache.commons.httpclient.params;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.util.DateUtil;

/**
 * @since 3.0
 */
public class DefaultHttpParamsFactory implements HttpParamsFactory {

    private HttpParams httpParams;

    /**
     * 
     */
    public DefaultHttpParamsFactory() {
        super();
    }

    public synchronized HttpParams getDefaultParams() {
        if (httpParams == null) {
            httpParams = createParams();
        }
        return httpParams;
    }

    protected HttpParams createParams() {
        HttpClientParams params = new HttpClientParams(null);
        params.setParameter(HttpMethodParams.USER_AGENT, "Jakarta Commons-HttpClient/3.0.1");
        params.setVersion(HttpVersion.HTTP_1_1);
        params.setConnectionManagerClass(SimpleHttpConnectionManager.class);
        params.setCookiePolicy(CookiePolicy.RFC_2109);
        params.setHttpElementCharset("US-ASCII");
        params.setContentCharset("ISO-8859-1");
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        ArrayList datePatterns = new ArrayList();
        datePatterns.addAll(Arrays.asList(new String[] { DateUtil.PATTERN_RFC1123, DateUtil.PATTERN_RFC1036, DateUtil.PATTERN_ASCTIME, "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" }));
        params.setParameter(HttpMethodParams.DATE_PATTERNS, datePatterns);
        String agent = null;
        try {
            agent = System.getProperty("httpclient.useragent");
        } catch (SecurityException ignore) {
        }
        if (agent != null) {
            params.setParameter(HttpMethodParams.USER_AGENT, agent);
        }
        String preemptiveDefault = null;
        try {
            preemptiveDefault = System.getProperty("httpclient.authentication.preemptive");
        } catch (SecurityException ignore) {
        }
        if (preemptiveDefault != null) {
            preemptiveDefault = preemptiveDefault.trim().toLowerCase();
            if (preemptiveDefault.equals("true")) {
                params.setParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, Boolean.TRUE);
            } else if (preemptiveDefault.equals("false")) {
                params.setParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, Boolean.FALSE);
            }
        }
        String defaultCookiePolicy = null;
        try {
            defaultCookiePolicy = System.getProperty("apache.commons.httpclient.cookiespec");
        } catch (SecurityException ignore) {
        }
        if (defaultCookiePolicy != null) {
            if ("COMPATIBILITY".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            } else if ("NETSCAPE_DRAFT".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.NETSCAPE);
            } else if ("RFC2109".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.RFC_2109);
            }
        }
        return params;
    }
}
