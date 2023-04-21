package org.apache.http.examples.client;

import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;

/**
 * This example demonstrates the use of a local HTTP context populated with 
 * custom attributes.
 */
public class ClientCustomContext {

    public static final void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpGet httpget = new HttpGet("http://www.google.com/");
        System.out.println("executing request " + httpget.getURI());
        HttpResponse response = httpclient.execute(httpget, localContext);
        HttpEntity entity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }
        entity.consumeContent();
        System.out.println("----------------------------------------");
    }
}
