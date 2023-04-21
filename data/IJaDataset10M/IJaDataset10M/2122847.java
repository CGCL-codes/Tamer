package org.wdcode.web.http;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.wdcode.common.constants.EncodingConstants;
import org.wdcode.common.constants.StringConstants;
import org.wdcode.common.exception.CustomRuntimeException;
import org.wdcode.common.tools.Lists;
import org.wdcode.common.util.CommonUtil;

/**
 * 实现HTTP模拟浏览器提交的实现类
 * @author WD
 * @since JDK6
 * @version 1.0 2009-06-03
 */
final class HttpClientImpl extends DefaultHttpClient implements HttpClient {

    private static final String USER_AGENT_KEY;

    private static final String USER_AGENT_VAL;

    private static final String CONTENT_CHARSET;

    private static final String COOKIE_HEADER;

    private static final String ACCEPT_KEY;

    private static final String ACCEPT_VAL;

    private static final String ACCEPT_LANGUAGE_KEY;

    private static final String ACCEPT_LANGUAGE_VAL;

    private static final String ACCEPT_CHARSET_KEY;

    private static final String ACCEPT_CHARSET_VAL;

    private static final String CONTENT_TYPE_KEY;

    private static final String CONTENT_TYPE_VAL;

    private static final String REFERER_KEY;

    private String currentURL;

    private HttpContext context;

    static {
        ACCEPT_KEY = "Accept";
        ACCEPT_VAL = "text/xml,text/javascript,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5";
        ACCEPT_LANGUAGE_KEY = "Accept-Language";
        ACCEPT_LANGUAGE_VAL = "en-us;q=0.7,en;q=0.3";
        ACCEPT_CHARSET_KEY = "Accept-Charset";
        ACCEPT_CHARSET_VAL = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";
        CONTENT_TYPE_KEY = "Content-Type";
        CONTENT_TYPE_VAL = "application/x-www-form-urlencoded";
        REFERER_KEY = "Referer";
        USER_AGENT_KEY = "User-Agent";
        USER_AGENT_VAL = "Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13";
        CONTENT_CHARSET = "http.protocol.content-charset";
        COOKIE_HEADER = "http.protocol.single-cookie-header";
    }

    /**
	 * 构造方法
	 * @param encoding 编码
	 */
    public HttpClientImpl(String encoding) {
        super();
        setCookieStore(new BasicCookieStore());
        getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        List<Header> headers = Lists.getList();
        headers.add(new BasicHeader(USER_AGENT_KEY, USER_AGENT_VAL));
        getParams().setParameter(ClientPNames.DEFAULT_HEADERS, headers);
        getParams().setParameter(CONTENT_CHARSET, encoding);
        getParams().setParameter(COOKIE_HEADER, true);
        setHttpContext();
    }

    /**
	 * 获得HttpContext的属性
	 * @param id 属性ID
	 * @return 获得的对象
	 */
    public Object getAttribute(String id) {
        return context.getAttribute(id);
    }

    /**
	 * 设置HttpContext
	 */
    public void setHttpContext() {
        context = createHttpContext();
    }

    /**
	 * 获得当前的URL
	 * @return URL地址
	 */
    public String getCurrentURL() {
        return currentURL;
    }

    /**
	 * 添加Cookie
	 * @param cookie Cookie对象
	 */
    public void addCookie(Cookie cookie) {
        getCookieStore().addCookie(cookie);
    }

    /**
	 * 添加Cookie
	 * @param name Cookie名
	 * @param value Cookie值
	 */
    public void addCookie(String name, String value) {
        addCookie(new BasicClientCookie(name, value));
    }

    /**
	 * 根据name获得Cookie
	 * @param name cookie名
	 * @return Cookie 如果没有找到返回null
	 */
    public Cookie getCookie(String name) {
        if (CommonUtil.isEmpty(name)) {
            return null;
        }
        List<Cookie> lsCookie = getCookies();
        Cookie cookie = null;
        for (int i = 0; i < lsCookie.size(); i++) {
            if (lsCookie.get(i).getName().equals(name)) {
                cookie = lsCookie.get(i);
                break;
            }
        }
        return cookie;
    }

    /**
	 * 根据name获得Cookie值
	 * @param name cookie名
	 * @return Cookie值 如果没有找到返回""
	 */
    public String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        return CommonUtil.isEmpty(cookie) ? StringConstants.EMPTY : cookie.getValue();
    }

    /**
	 * 获得所有Cookie列表
	 * @return Cookie列表
	 */
    public List<Cookie> getCookies() {
        return getCookieStore().getCookies();
    }

    /**
	 * 设置连接超时
	 * @param value 毫秒数
	 */
    public void setTimeOut(long value) {
        getParams().setLongParameter(HttpConnectionParams.SO_TIMEOUT, value);
    }

    /**
	 * 关闭资源
	 */
    public void close() {
        getConnectionManager().shutdown();
        context = null;
    }

    /**
	 * 模拟get提交
	 * @param url get提交地址
	 * @return InputStream 提交后的流
	 */
    public InputStream doGet(String url) {
        return doGet(url, null);
    }

    /**
	 * 模拟get提交
	 * @param url get提交地址
	 * @param referer referer地址
	 * @return InputStream 提交后的流
	 */
    public InputStream doGet(String url, String referer) {
        HttpGet get = null;
        try {
            get = new HttpGet(url);
            setHeaders(get, referer);
            HttpResponse response = execute(get, context);
            updateCurrentUrl(get);
            if (response.getStatusLine().getStatusCode() == 302) {
                return doGet(getRedirectHandler().getLocationURI(response, context).toString(), referer);
            } else {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            throw new CustomRuntimeException();
        } finally {
            getConnectionManager().closeExpiredConnections();
        }
    }

    /**
	 * 模拟post提交
	 * @param url post提交地址
	 * @param data 提交参数
	 * @param referer referer地址
	 * @param encoding 提交参数的编码格式
	 * @return InputStream 提交后的流
	 */
    public InputStream doPost(String url, Map<String, String> data, String referer, String encoding) {
        HttpPost post = null;
        try {
            post = new HttpPost(url);
            setHeaders(post, referer);
            post.addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VAL);
            if (!CommonUtil.isEmpty(data)) {
                List<NameValuePair> list = Lists.getList(data.size());
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                post.setEntity(new UrlEncodedFormEntity(list, encoding));
            }
            HttpResponse response = execute(post, context);
            updateCurrentUrl(post);
            if (response.getStatusLine().getStatusCode() == 302) {
                return doPost(getRedirectHandler().getLocationURI(response, context).toString(), data, referer);
            } else {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            throw new CustomRuntimeException();
        } finally {
            getConnectionManager().closeExpiredConnections();
        }
    }

    /**
	 * 模拟post提交
	 * @param url post提交地址
	 * @param data 提交参数
	 * @param referer referer地址
	 * @return InputStream 提交后的流
	 */
    public InputStream doPost(String url, Map<String, String> data, String referer) {
        return doPost(url, data, referer, EncodingConstants.UTF_8);
    }

    /**
	 * 模拟post提交 默认使用UTF-8格式
	 * @param url post提交地址
	 * @param data 提交参数
	 * @return InputStream 提交后的流
	 */
    public InputStream doPost(String url, Map<String, String> data) {
        return doPost(url, data, null, EncodingConstants.UTF_8);
    }

    /**
	 * 设置头
	 * @param req
	 * @param referer
	 */
    private void setHeaders(HttpRequest req, String referer) {
        req.addHeader(ACCEPT_KEY, ACCEPT_VAL);
        req.addHeader(ACCEPT_LANGUAGE_KEY, ACCEPT_LANGUAGE_VAL);
        req.addHeader(ACCEPT_CHARSET_KEY, ACCEPT_CHARSET_VAL);
        if (!CommonUtil.isEmpty(referer)) {
            req.addHeader(REFERER_KEY, referer);
        }
    }

    private void updateCurrentUrl(HttpUriRequest request) {
        currentURL = request.getURI().toString();
    }
}
