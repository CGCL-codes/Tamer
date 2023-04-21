package de.objectcode.openk.soa.webapps.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

public class TargetInvoker {

    private final String[] RESPONSE_HEADER_KEYS = new String[] { "ETag", "Last-Modified", "Content-Type", "Location" };

    HostConfiguration hostConfiguration;

    HttpClient httpClient;

    String basePath;

    public TargetInvoker(TargetParams params) {
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setMaxTotalConnections(20);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);
        hostConfiguration = new HostConfiguration();
        hostConfiguration.setHost(params.getHost(), params.getPort());
        basePath = params.getBasePath();
        httpClient = new HttpClient(connectionManager);
        httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        httpClient.getParams().setConnectionManagerTimeout(20000);
    }

    public TargetResult performGet(HttpState httpState, String pathInfo, String queryString, Map<String, String> requestHeaders) throws IOException {
        queryString = queryString != null ? "?" + queryString : "";
        System.out.println("Get: " + basePath + pathInfo + queryString);
        System.out.println(hostConfiguration);
        System.out.println(httpState);
        GetMethod getMethod = new GetMethod(basePath + pathInfo + queryString);
        getMethod.setFollowRedirects(false);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            getMethod.setRequestHeader(entry.getKey(), entry.getValue());
        }
        int result = httpClient.executeMethod(hostConfiguration, getMethod, httpState);
        Map<String, String> responseHeaders = new HashMap<String, String>();
        for (String headerKey : RESPONSE_HEADER_KEYS) {
            Header header = getMethod.getResponseHeader(headerKey);
            if (header != null) {
                responseHeaders.put(headerKey, header.getValue());
            }
        }
        System.out.println("R: " + result + " " + responseHeaders);
        return new TargetResult(result, responseHeaders, getMethod.getResponseBodyAsStream());
    }

    public TargetResult performPost(HttpState httpState, String pathInfo, Map<String, String> requestHeaders, InputStream content) throws IOException {
        System.out.println("Post: " + basePath + pathInfo);
        PostMethod postMethod = new PostMethod(basePath + pathInfo);
        postMethod.setFollowRedirects(false);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            postMethod.setRequestHeader(entry.getKey(), entry.getValue());
        }
        if (content != null) {
            postMethod.setRequestEntity(new InputStreamRequestEntity(content));
        }
        int result = httpClient.executeMethod(hostConfiguration, postMethod, httpState);
        Map<String, String> responseHeaders = new HashMap<String, String>();
        for (String headerKey : RESPONSE_HEADER_KEYS) {
            Header header = postMethod.getResponseHeader(headerKey);
            if (header != null) {
                responseHeaders.put(headerKey, header.getValue());
            }
        }
        System.out.println("R: " + result + " " + responseHeaders);
        return new TargetResult(result, responseHeaders, postMethod.getResponseBodyAsStream());
    }

    public Cookie createCookie(String name, String value) {
        return new Cookie(hostConfiguration.getHost(), name, value, "/", -1, false);
    }
}
