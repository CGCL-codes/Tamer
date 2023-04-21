package org.apache.shindig.gadgets.servlet;

import static org.apache.shindig.gadgets.rewrite.image.BasicImageRewriter.PARAM_NO_EXPAND;
import static org.apache.shindig.gadgets.rewrite.image.BasicImageRewriter.PARAM_RESIZE_HEIGHT;
import static org.apache.shindig.gadgets.rewrite.image.BasicImageRewriter.PARAM_RESIZE_QUALITY;
import static org.apache.shindig.gadgets.rewrite.image.BasicImageRewriter.PARAM_RESIZE_WIDTH;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.LockedDomainService;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.http.RequestPipeline;
import org.apache.shindig.gadgets.rewrite.RequestRewriterRegistry;
import org.apache.shindig.gadgets.rewrite.RewritingException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles open proxy requests.
 */
@Singleton
public class ProxyHandler extends ProxyBase {

    private static final Logger logger = Logger.getLogger(ProxyHandler.class.getName());

    private static final String[] INTEGER_RESIZE_PARAMS = new String[] { PARAM_RESIZE_HEIGHT, PARAM_RESIZE_WIDTH, PARAM_RESIZE_QUALITY, PARAM_NO_EXPAND };

    static final String FALLBACK_URL_PARAM = "fallback_url";

    private final RequestPipeline requestPipeline;

    private final LockedDomainService lockedDomainService;

    private final RequestRewriterRegistry contentRewriterRegistry;

    @Inject
    public ProxyHandler(RequestPipeline requestPipeline, LockedDomainService lockedDomainService, RequestRewriterRegistry contentRewriterRegistry) {
        this.requestPipeline = requestPipeline;
        this.lockedDomainService = lockedDomainService;
        this.contentRewriterRegistry = contentRewriterRegistry;
    }

    /**
   * Generate a remote content request based on the parameters sent from the client.
   */
    private HttpRequest buildHttpRequest(HttpServletRequest request, String urlParam) throws GadgetException {
        String theUrl = request.getParameter(urlParam);
        if (theUrl == null) {
            return null;
        }
        Uri url = validateUrl(theUrl);
        HttpRequest req = new HttpRequest(url).setContainer(getContainer(request));
        copySanitizedIntegerParams(request, req);
        if (request.getParameter(GADGET_PARAM) != null) {
            req.setGadget(Uri.parse(request.getParameter(GADGET_PARAM)));
        }
        req.setRewriteMimeType(request.getParameter(REWRITE_MIME_TYPE_PARAM));
        req.setIgnoreCache(getIgnoreCache(request));
        req.setSanitizationRequested("1".equals(request.getParameter(SANITIZE_CONTENT_PARAM)));
        if (request.getParameter(REFRESH_PARAM) != null) {
            try {
                req.setCacheTtl(Integer.parseInt(request.getParameter(REFRESH_PARAM)));
            } catch (NumberFormatException nfe) {
            }
        }
        this.setRequestHeaders(request, req);
        return req;
    }

    private void copySanitizedIntegerParams(HttpServletRequest request, HttpRequest req) {
        for (String resizeParamName : INTEGER_RESIZE_PARAMS) {
            if (request.getParameter(resizeParamName) != null) {
                req.setParam(resizeParamName, NumberUtils.createInteger(request.getParameter(resizeParamName)));
            }
        }
    }

    @Override
    protected void doFetch(HttpServletRequest request, HttpServletResponse response) throws IOException, GadgetException {
        if (request.getHeader("If-Modified-Since") != null) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        String host = request.getHeader("Host");
        if (!lockedDomainService.isSafeForOpenProxy(host)) {
            String msg = "Embed request for url " + getParameter(request, URL_PARAM, "") + " made to wrong domain " + host;
            logger.info(msg);
            throw new GadgetException(GadgetException.Code.INVALID_PARAMETER, msg);
        }
        HttpRequest rcr = buildHttpRequest(request, URL_PARAM);
        HttpResponse results = requestPipeline.execute(rcr);
        if (results.isError()) {
            HttpRequest fallbackRcr = buildHttpRequest(request, FALLBACK_URL_PARAM);
            if (fallbackRcr != null) {
                results = requestPipeline.execute(fallbackRcr);
            }
        }
        if (contentRewriterRegistry != null) {
            try {
                results = contentRewriterRegistry.rewriteHttpResponse(rcr, results);
            } catch (RewritingException e) {
                throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR, e);
            }
        }
        for (Map.Entry<String, String> entry : results.getHeaders().entries()) {
            String name = entry.getKey();
            if (!DISALLOWED_RESPONSE_HEADERS.contains(name.toLowerCase())) {
                response.addHeader(name, entry.getValue());
            }
        }
        String responseType = results.getHeader("Content-Type");
        if (!StringUtils.isEmpty(rcr.getRewriteMimeType())) {
            String requiredType = rcr.getRewriteMimeType();
            if (requiredType.endsWith("/*") && !StringUtils.isEmpty(responseType)) {
                requiredType = requiredType.substring(0, requiredType.length() - 2);
                if (!responseType.toLowerCase().startsWith(requiredType.toLowerCase())) {
                    response.setContentType(requiredType);
                    responseType = requiredType;
                }
            } else {
                response.setContentType(requiredType);
                responseType = requiredType;
            }
        }
        setResponseHeaders(request, response, results);
        if (results.getHttpStatusCode() != HttpResponse.SC_OK) {
            response.sendError(results.getHttpStatusCode());
        }
        IOUtils.copy(results.getResponse(), response.getOutputStream());
    }
}
