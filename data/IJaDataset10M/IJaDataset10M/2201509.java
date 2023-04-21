package org.tamacat.httpd.core;

import java.net.URL;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.tamacat.httpd.config.ReverseUrl;
import org.tamacat.httpd.util.RequestUtils;
import org.tamacat.httpd.util.ReverseUtils;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;

/**
 * <p>The client side request for reverse proxy.<br>
 * The case including the entity uses {@link ReverseHttpEntityEnclosingRequest}
 * for a request.
 */
public class ReverseHttpRequest extends BasicHttpRequest {

    static final Log LOG = LogFactory.getLog(ReverseHttpRequest.class);

    protected ReverseUrl reverseUrl;

    /**
	 * <p>Constructs with the {@link RequestLine}.
	 * @param line
	 * @param reverseUrl
	 */
    public ReverseHttpRequest(RequestLine line, ReverseUrl reverseUrl) {
        super(line);
        this.reverseUrl = reverseUrl;
    }

    /**
	 * <p>Constructs with the original request of {@link HttpRequest}.
	 * @param request
	 * @param reverseUrl
	 */
    public ReverseHttpRequest(HttpRequest request, HttpContext context, ReverseUrl reverseUrl) {
        super(new BasicRequestLine(request.getRequestLine().getMethod(), reverseUrl.getReverseUrl(request.getRequestLine().getUri()).toString(), request.getRequestLine().getProtocolVersion()));
        this.reverseUrl = reverseUrl;
        setRequest(request, context);
    }

    /**
	 * <p>Set the original request.
	 * @param request
	 */
    public void setRequest(HttpRequest request, HttpContext context) {
        rewriteHostHeader(request, context);
        setHeaders(request.getAllHeaders());
        setParams(request.getParams());
        ReverseUtils.removeRequestHeaders(this);
    }

    protected void rewriteHostHeader(HttpRequest request, HttpContext context) {
        Header[] hostHeaders = request.getHeaders(HTTP.TARGET_HOST);
        for (Header hostHeader : hostHeaders) {
            String value = hostHeader.getValue();
            URL host = RequestUtils.getRequestURL(request, context, reverseUrl.getServiceUrl());
            reverseUrl.setHost(host);
            String before = host.getAuthority();
            int beforePort = host.getPort();
            if (beforePort != 80 && beforePort > 0) {
                before = before + ":" + beforePort;
            }
            String after = reverseUrl.getReverse().getHost();
            int afterPort = reverseUrl.getReverse().getPort();
            if (afterPort != 80 && afterPort > 0) {
                after = after + ":" + afterPort;
            }
            String newValue = value.replace(before, after);
            LOG.trace("Host: " + value + " >> " + newValue);
            Header newHeader = new BasicHeader(hostHeader.getName(), newValue);
            request.removeHeader(hostHeader);
            request.addHeader(newHeader);
        }
    }
}
