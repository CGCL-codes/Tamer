package org.dcm4chee.xero.servlet;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class sits in front of another page and applies the requested XSLT to
 * the page. The XSLT to be applied may only depend on the base URL (eg
 * /xero/image/image.seam), the browser and the language.
 * 
 * @author bwallace
 */
public class XsltFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(XsltFilter.class);

    public String XSLT_PARAMETER = "xslt";

    FilterConfig filterConfig;

    Map<String, Transformer> transformers = new HashMap<String, Transformer>();

    public void destroy() {
    }

    /**
     * Gets the transformer appropriate to transform the given request, as
     * embodied in the responseWrapper. Currently this replaces the last bit of
     * the URL (.seam) with Xsl.xsl and does a request, checking first to see if
     * it is in the cache. Assume this filter is single-threaded - if not, we
     * need to use a rental model whereby there is a replace transformer call.
     */
    protected Transformer getTransformer(HttpServletRequest request, String xml) throws IOException {
        String xsl = request.getRequestURI();
        int lastDot = xsl.lastIndexOf('.');
        if (lastDot < 0) throw new IllegalArgumentException("Unknown request - need to be .X for some X");
        UrlUriResolver resolve = new UrlUriResolver(request, filterConfig.getServletContext());
        xsl = xsl.substring(0, lastDot) + "Xsl.xsl";
        log.debug("Looking for XSL " + xsl);
        Transformer transformer = transformers.get(xsl);
        if (transformer == null || "true".equalsIgnoreCase(request.getParameter("refresh"))) {
            try {
                Source styleSource = resolve.resolve(xsl, "/");
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setURIResolver(resolve);
                transformer = transformerFactory.newTransformer(styleSource);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            transformers.put(xsl, transformer);
        }
        return transformer;
    }

    /**
     * Detect if XSLT should be applied
     */
    protected boolean checkApplyXslt(HttpServletRequest request) {
        if (request.getParameter(XSLT_PARAMETER) != null) return "true".equalsIgnoreCase(request.getParameter(XSLT_PARAMETER));
        String agent = request.getHeader("USER-AGENT");
        log.debug("Agent={}", agent);
        if (agent.indexOf("Opera") >= 0) {
            return true;
        }
        if (agent.indexOf("Konqueror") >= 0) {
            return true;
        }
        if (agent.indexOf("Mobile") >= 0) {
            return true;
        }
        if (agent.indexOf("BlackBerry") >= 0) {
            return true;
        }
        return false;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filters) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        if (!checkApplyXslt(hRequest)) {
            log.debug("Not applying XSLT transformation to {}", hRequest.getRequestURI());
            filters.doFilter(request, response);
            return;
        }
        log.debug("Apply XSLT.");
        response.setContentType("application/xhtml+xml");
        PrintWriter out = response.getWriter();
        CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) response);
        filters.doFilter(request, responseWrapper);
        String xml = responseWrapper.toString();
        if (xml.indexOf("<?xml-stylesheet") == -1) {
            log.debug("Not applying XSLT - no xml-stylesheet applied.");
            response.setContentLength(xml.length());
            out.write(xml);
            return;
        }
        StringReader sr = new StringReader(xml);
        Source xmlSource = new StreamSource(sr);
        try {
            Transformer useTransform = getTransformer(hRequest, xml);
            useTransform.setURIResolver(new UrlUriResolver(hRequest, filterConfig.getServletContext()));
            CharArrayWriter caw = new CharArrayWriter();
            StreamResult result = new StreamResult(caw);
            useTransform.transform(xmlSource, result);
            String html = caw.toString();
            response.setContentLength(html.length());
            out.write(html);
        } catch (Exception ex) {
            out.println(ex.toString());
            out.write(responseWrapper.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }
}
