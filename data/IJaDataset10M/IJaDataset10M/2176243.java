package com.sunflower.common.http;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpService {

    private Log log = LogFactory.getLog(HttpService.class);

    public static final String PROXY_IN_USE = "Y";

    public static final String PROXY_UN_USE = "N";

    private String proxy;

    private String proxyHost;

    private String proxyPort;

    private String proxyProtocol;

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(String proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    /**
	 * 通过代理访问指定url
	 * @param url
	 * @param proxyHost
	 * @param proxyPort
	 * @param proxyProtocol
	 * @return
	 */
    public String getResponseViaProxy(String url, String proxyHost, String proxyPort, String proxyProtocol) {
        HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort), proxyProtocol);
        HttpClient httpClient = new DefaultHttpClient();
        String responseBody = null;
        try {
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpGet httpget = new HttpGet(url);
            log.info("executing request to " + httpget.getURI() + " via " + proxy);
            HttpResponse rsp = httpClient.execute(httpget);
            HttpEntity entity = rsp.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    /**
	 * 不通过代理访问
	 * @param url
	 * @return
	 */
    public String getResponse(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        String responseBody = null;
        try {
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            log.info("executing request " + httpget.getURI());
            responseBody = httpClient.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    public String fetchData(String url) {
        String data = null;
        if (proxy != null && proxy.toUpperCase().equals(PROXY_IN_USE)) {
            data = getResponseViaProxy(url, proxyHost, proxyPort, proxyProtocol);
            log.info("httpclient 通过代理请求返回 的数据为： " + data);
        } else {
            data = getResponse(url);
            log.info("httpclient 请求返回 的数据为： " + data);
        }
        return data;
    }
}
