package net.sf.mustang.xml;

import java.util.Vector;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import net.sf.mustang.Mustang;
import net.sf.mustang.bean.Bean;
import net.sf.mustang.log.KLog;
import net.sf.mustang.service.Request;

public class HttpClientXMLTool {

    private static KLog log = Mustang.getLog(HttpClientXMLTool.class);

    private static final String XML_HTTPCLIENT = "mustang.xml.httpclient";

    private static Object monitor = new Object();

    public static Document fetchRemoteDom(Request request, Vector<Bean> beanList, String url) throws Exception {
        return fetchRemoteDom(getHttpClient(request), beanList, url);
    }

    public static Document fetchRemoteDom(HttpClient httpclient, Vector<Bean> beanList, String url) throws Exception {
        Document doc = null;
        PostMethod post = getPost(beanList, url);
        httpclient.executeMethod(post);
        if (post.getResponseHeader("Content-Type").getValue().startsWith("text/xml")) doc = XMLTool.readXML(post.getResponseBodyAsStream());
        post.releaseConnection();
        return doc;
    }

    public static void callRemote(Request request, Vector<Bean> beanList, String url) throws Exception {
        callRemote(getHttpClient(request), beanList, url);
    }

    public static void callRemote(HttpClient httpclient, Vector<Bean> beanList, String url) throws Exception {
        PostMethod post = getPost(beanList, url);
        try {
            httpclient.executeMethod(post);
            post.releaseConnection();
        } catch (Exception e) {
            if (log.isError()) log.error(e);
        }
    }

    public static PostMethod getPost(Vector<Bean> beanList, String url) throws Exception {
        Document sendDoc = null;
        if (beanList != null && beanList.size() > 0) if (beanList.size() == 1) sendDoc = BeanXMLTool.beanToDoc(beanList.get(0)); else sendDoc = BeanXMLTool.listToDoc(beanList);
        PostMethod post = new PostMethod(url);
        if (sendDoc != null) {
            sendDoc.setXMLEncoding(Mustang.DEFAULT_ENCODING);
            RequestEntity re = new StringRequestEntity(sendDoc.asXML(), "text/xml", sendDoc.getXMLEncoding());
            post.setRequestEntity(re);
        }
        return post;
    }

    public static HttpClient getHttpClient(Request request) {
        HttpClient retVal = (HttpClient) request.getSession().getAttribute(XML_HTTPCLIENT);
        if (retVal == null) {
            synchronized (monitor) {
                retVal = (HttpClient) request.getSession().getAttribute(XML_HTTPCLIENT);
                if (retVal == null) retVal = new HttpClient();
                request.getSession().setAttribute(XML_HTTPCLIENT, retVal);
            }
        }
        return retVal;
    }
}
