package csiebug.web.taglib.rss;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import csiebug.util.AssertUtility;
import csiebug.util.WebUtility;

/**
 * RSS Feed tag
 * @author George_Tsai
 * @version 2010/10/19
 */
public class RSSFeedTag extends BodyTagSupport implements TryCatchFinally {

    static final long serialVersionUID = 20101019;

    private String url;

    private String feedId;

    private WebUtility webutil = new WebUtility();

    public int doStartTag() throws JspException {
        try {
            AssertUtility.notNullAndNotSpace(url);
            AssertUtility.notNullAndNotSpace(feedId);
            HttpParams params = new BasicHttpParams();
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            Document document = DocumentHelper.parseText(EntityUtils.toString(response.getEntity(), "UTF-8"));
            webutil.setRequestAttribute(feedId, document);
        } catch (Exception e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public void doFinally() {
    }

    public void doCatch(Throwable e) throws Throwable {
        throw new JspException("RSSFeedTag Problem: " + e.getMessage());
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedId() {
        return feedId;
    }
}
