package com.jwp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpProtocolParams;
import com.jwp.constants.ConstantParam;

public class Action {

    protected String userName = "sl514@163.com";

    protected String password = "8802514";

    public static String bdStokenStr = "";

    public static DefaultHttpClient client = null;

    /**
	 * 使用get方法提交
	 * 
	 * @param client
	 *            HttpClient对象
	 * @param url
	 *            提交地址
	 * @param referer
	 *            Referer信息
	 * @return 请求后返回的网页流
	 * @throws ContactListImporterException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 * @throws HttpException
	 * @throws IOException
	 */
    protected InputStream doGet(HttpClient client, String url, String referer) throws URISyntaxException, InterruptedException, HttpException, IOException {
        getHttpClient().getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        setHeaders(get, referer);
        HttpResponse resp = getHttpClient().execute(get, getHttpClient().getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return content;
    }

    protected String doGetString(String url, String referer) throws Exception {
        getHttpClient().getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        setHeaders(get, referer);
        HttpResponse resp = getHttpClient().execute(get, getHttpClient().getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return readInputStream(content, ConstantParam.ENCODING);
    }

    /**
	 * 使用post方法提交
	 * 
	 * @param client
	 *            HttpClient对象
	 * @param url
	 *            提交地址
	 * @param data
	 *            要提交到服务器的参数
	 * @param referer
	 *            头部Referer信息
	 * @return 请求后返回的网页流
	 * @throws ContactListImporterException
	 * @throws HttpException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 */
    protected InputStream doPost(HttpClient client, String url, NameValuePair data[], String referer) throws HttpException, IOException, InterruptedException, URISyntaxException {
        client.getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        if (url.indexOf("?") != -1) url = url + "&dd=" + System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        setHeaders(post, referer);
        post.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        post.setEntity(new UrlEncodedFormEntity(data, ConstantParam.ENCODING));
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = client.execute(post, client.getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return content;
    }

    protected String doPostString(String url, NameValuePair data[], String referer) throws Exception {
        getHttpClient().getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        if (url.indexOf("?") != -1) url = url + "&dd=" + System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        setHeaders(post, referer);
        post.addHeader("refer", referer);
        post.addHeader("Host", "login.dbank.com");
        post.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        post.setEntity(new UrlEncodedFormEntity(data, ConstantParam.ENCODING));
        HttpProtocolParams.setUseExpectContinue(getHttpClient().getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = getHttpClient().execute(post, getHttpClient().getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return readInputStream(content, ConstantParam.ENCODING);
    }

    /**
	 * 读取流并拼接转换成字符串
	 * 
	 * @param is
	 *            流
	 * @param encode 字符集编码 null的时候为gb2312
	 * @return 转换后的字符串
	 * @throws IOException
	 */
    protected String readInputStream(InputStream is, String encode) throws IOException {
        if (encode == null) {
            encode = "gb2312";
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is, encode));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
            if (line.indexOf("<title>") != -1) {
                System.out.println(line);
            }
        }
        is.close();
        return buffer.toString();
    }

    protected void setHeaders(HttpRequest req, String referer) {
    }

    protected static DefaultHttpClient getHttpClient() {
        if (client == null) {
            client = new DefaultHttpClient();
            client.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
            List<Header> headers = new ArrayList<Header>();
            client.getParams().setParameter("http.default-headers", headers);
            return client;
        } else {
            return client;
        }
    }

    public void printCookie(DefaultHttpClient client) {
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = client.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
    }

    public CookieStore getCookie() {
        CookieStore ct = new BasicCookieStore();
        ct.addCookie(new BasicClientCookie("ALF", "1296376104	/	sina.com.cn	Sun, 30-Jan-2011 08:28:24 GMT"));
        ct.addCookie(new BasicClientCookie("SINAGLOBAL", "112.81.58.174.1389612828255909	/	sina.com.cn	Tue, 12-Jan-2021 08:49:44 GMT"));
        ct.addCookie(new BasicClientCookie("SUR", "uid=1870176684&user=ij2ee%40139.com&nick=michaeljackson&email=&dob=1984-04-11&ag=4&sex=1&ssl=0	/	sina.com.cn	Sun, 30-Jan-2011 08:28:24 GMT"));
        ct.addCookie(new BasicClientCookie("ULV", "1295771289171:6:6:1:122.193.51.90.549361295771286322:1295590941200	/	sina.com.cn	Wed, 18-Jan-2012 08:28:09 GMT"));
        ct.addCookie(new BasicClientCookie("UOR", ",t,	/	sina.com.cn	Sun, 15-Jan-2012 08:49:37 GMT"));
        ct.addCookie(new BasicClientCookie("vjlast", "1295452137	/	sina.com.cn	Fri, 01-Jan-2038 00:00:00 GMT"));
        ct.addCookie(new BasicClientCookie("vjuids", "-aff40e8b.12d97cd3462.0.218b55ea6e633c	/	sina.com.cn	Fri, 01-Jan-2038 00:00:00 GMT"));
        ct.addCookie(new BasicClientCookie("WEIBOALC", "cv=1.0&es=7e8b99646e01bdf2f54ac173cf2f578e&bt=1295771306&et=1296376106&uid=1870176684	/	t.sina.com.cn	Sun, 30-Jan-2011 08:28:27 GMT"));
        return ct;
    }

    public CookieStore getCookieT() {
        CookieStore ct = new BasicCookieStore();
        ct.addCookie(new BasicClientCookie("_s_upa", "3"));
        ct.addCookie(new BasicClientCookie("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
        ct.addCookie(new BasicClientCookie("Accept-Encoding", "gzip, deflate"));
        ct.addCookie(new BasicClientCookie("Accept-Language", "en-US"));
        ct.addCookie(new BasicClientCookie("Cache-Control", "no-cache"));
        ct.addCookie(new BasicClientCookie("Connection", "Keep-Alive"));
        ct.addCookie(new BasicClientCookie("Content-Length", "136"));
        ct.addCookie(new BasicClientCookie("Content-Type", "application/x-www-form-urlencoded"));
        ct.addCookie(new BasicClientCookie("Cookie", "__utma=149211947.83072624.1297512586.1297512586.1297512586.1; __utmz=149211947.1297512586.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=61755443.1100150337.1297512420.1297512420.1297666095.2; __utmz=61755443.1297512420.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=61755443.1.10.1297666095; __utmc=61755443"));
        ct.addCookie(new BasicClientCookie("Host", "login.dbank.com"));
        ct.addCookie(new BasicClientCookie("Referer", "http://www.dbank.com/"));
        ct.addCookie(new BasicClientCookie("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB6.6; InfoPath.2; .NET CLR 2.0.50727)"));
        return ct;
    }

    public NameValuePair[] fillParam(Map<String, String> dataMap) {
        NameValuePair data[] = new NameValuePair[dataMap.size()];
        Object key[] = dataMap.keySet().toArray();
        Iterator iterator = dataMap.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String ks = iterator.next().toString();
            data[i++] = new BasicNameValuePair(ks, dataMap.get(ks));
        }
        return data;
    }
}
