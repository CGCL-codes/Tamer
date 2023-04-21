package org.apache.myfaces.trinidadinternal.config.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@SuppressWarnings("deprecation")
public class DispatchServletResponse extends HttpServletResponseWrapper {

    public DispatchServletResponse(ExternalContext ec) {
        super((HttpServletResponse) ec.getResponse());
        _request = (HttpServletRequest) ec.getRequest();
    }

    @Override
    public void setContentType(String contentTypeAndCharset) {
        if ((_request.getAttribute("javax.servlet.include.request_uri") == null) && (contentTypeAndCharset != null)) {
            Matcher matcher = _CONTENT_TYPE_PATTERN.matcher(contentTypeAndCharset);
            if (matcher.matches()) {
                String contentType = matcher.group(1);
                String charset = (matcher.groupCount() > 1) ? matcher.group(2) : null;
                _request.setAttribute(DispatchResponseConfiguratorImpl.__CONTENT_TYPE_KEY, contentType);
                if ("application/xhtml+xml".equals(contentType)) {
                    String userAgent = _request.getHeader("User-agent");
                    if (userAgent.indexOf("compatible; MSIE") != -1) {
                        contentTypeAndCharset = "text/html";
                        if (charset != null) contentTypeAndCharset += ";charset=" + charset;
                    }
                }
            }
        }
        super.setContentType(contentTypeAndCharset);
    }

    private final HttpServletRequest _request;

    private static final Pattern _CONTENT_TYPE_PATTERN = Pattern.compile("([^;]+)(?:;charset=(.*))?");
}
