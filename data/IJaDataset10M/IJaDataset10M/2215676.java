package org.hippoproject.spider.extracter;

import java.net.URL;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author shixin
 *
 */
public class FilterContentCapturer implements IContentCapturer {

    @Override
    public IContentCaptureResponse capture(IContentCaptureRequest request) throws Exception {
        IContentCapturer dftCapturer = new DefaultContentCapturer();
        IContentCaptureResponse response = dftCapturer.capture(request);
        if (response.getCode() == HttpStatus.SC_OK) {
            if (response.getDocument() != null) {
                response = this.filter(request, response);
            }
        }
        return response;
    }

    private IContentCaptureResponse filter(IContentCaptureRequest request, IContentCaptureResponse response) throws Exception {
        IContentCaptureRequest req = null;
        IContentCaptureResponse res = response;
        Document document = response.getDocument();
        req = this.getFormRequest(request, document, response);
        if (req != null) {
            res = new DefaultContentCapturer().capture(req);
        }
        return res;
    }

    private IContentCaptureRequest getFormRequest(IContentCaptureRequest request, Document document, IContentCaptureResponse response) {
        IContentCaptureRequest r = null;
        try {
            Element frmElement = (Element) XPathFactory.newInstance().newXPath().evaluate("//form", document, XPathConstants.NODE);
            if (frmElement != null) {
                String action = frmElement.getAttribute("action");
                String method = frmElement.getAttribute("method");
                if (action == null || action.length() == 0) {
                    action = request.getUrl();
                }
                r = new ContentCaptureRequest(this.resolveUrl(request.getUrl(), action));
                r.setCharset(request.getCharset());
                if ("post".equalsIgnoreCase(method)) {
                    r.setMethod("post");
                } else {
                    r.setMethod("get");
                }
                NodeList inputs = frmElement.getElementsByTagName("input");
                if (inputs != null) for (int i = 0, n = inputs.getLength(); i < n; i++) {
                    Element iptElement = (Element) inputs.item(i);
                    String type = iptElement.getAttribute("type");
                    if ("hidden".equalsIgnoreCase(type) || "text".equalsIgnoreCase(type)) {
                        String name = iptElement.getAttribute("name");
                        String value = iptElement.getAttribute("value");
                        if (name != null && value != null) {
                            r.getDatas().put(name, value);
                        }
                    }
                }
                Header cookies = response.getMethod().getResponseHeader("Set-Cookie");
                if (cookies != null) {
                    r.getHeaders().put("Cookie", cookies.getValue());
                }
            }
        } catch (Exception e) {
        }
        return r;
    }

    private String resolveUrl(String from, String to) throws Exception {
        if (to.startsWith("http://") || to.startsWith("https://")) {
            return to;
        }
        URL url = new URL(from);
        url = new URL(url, to);
        return url.toString();
    }
}
