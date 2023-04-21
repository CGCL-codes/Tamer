package org.middleheaven.ui.web.tags;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.middleheaven.aas.Subject;
import org.middleheaven.global.Culture;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpChannel;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.HttpServerResponse;
import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.process.web.server.global.HttpCultureResolver;

public class TagContext extends ServletWebContext {

    private PageContext pageContex;

    public TagContext(PageContext pageContext) {
        super(resolveHttpCultureResolveService(pageContext));
        this.pageContex = pageContext;
    }

    public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
        if (ContextScope.RENDERING.equals(scope)) {
            return type.cast(pageContex.getAttribute(name));
        } else {
            return super.getAttributes().getAttribute(scope, name, type);
        }
    }

    public void setAttribute(ContextScope scope, String name, Object value) {
        if (ContextScope.RENDERING.equals(scope)) {
            pageContex.setAttribute(name, value);
        } else {
            super.getAttributes().setAttribute(scope, name, value);
        }
    }

    private static HttpCultureResolver resolveHttpCultureResolveService(PageContext pageContext) {
        return (HttpCultureResolver) pageContext.getRequest().getAttribute("__" + HttpCultureResolver.class.getName());
    }

    @Override
    public Map<String, String[]> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    protected ServletRequest getServletRequest() {
        return pageContex.getRequest();
    }

    @Override
    protected ServletResponse getServletResponse() {
        return pageContex.getResponse();
    }

    @Override
    protected ServletContext getServletContext() {
        return pageContex.getServletContext();
    }

    @Override
    protected HttpSession getSession() {
        return pageContex.getSession();
    }

    @Override
    protected void setHeaderAttribute(ContextScope scope, String name, Object value) {
        throw new UnsupportedOperationException("Cannot set " + scope.toString() + " in the " + this.getClass().getSimpleName());
    }

    @Override
    public InetAddress getRemoteAddress() {
        try {
            return InetAddress.getByName(pageContex.getRequest().getRemoteAddr());
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
