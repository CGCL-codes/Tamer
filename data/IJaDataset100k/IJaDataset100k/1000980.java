package net.sf.smailstandalone.config;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 
 * @since 19.02.2011
 * @author S�bastien CHATEL
 */
public class HttpClientURLStreamHandlerFactory implements URLStreamHandlerFactory, InitializingBean {

    private HttpClient httpClient;

    private HttpClientURLStreamHandler streamHandler;

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.httpClient, "property 'httpClient' is required");
        this.streamHandler = new HttpClientURLStreamHandler(this.httpClient);
        URL.setURLStreamHandlerFactory(this);
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("http".equals(protocol) || "https".equals(protocol)) {
            return this.streamHandler;
        }
        return null;
    }
}
