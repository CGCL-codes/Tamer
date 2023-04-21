package com.apusic.ebiz.framework.web.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.GenericFilterBean;
import com.apusic.ebiz.framework.web.handler.ExceptionHandler;

public class ExceptionFilter extends GenericFilterBean {

    private static final Log log = LogFactory.getLog(ExceptionFilter.class);

    private List<ExceptionHandler> handlers;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                if (log.isDebugEnabled()) {
                    log.debug("Runtime exception occurs");
                }
                if (CollectionUtils.isNotEmpty(handlers)) {
                    for (ExceptionHandler handler : handlers) {
                        handler.handle((HttpServletRequest) request, (HttpServletResponse) response, e);
                    }
                } else {
                    throw (RuntimeException) e;
                }
            } else if (e instanceof ServletException) {
                throw (ServletException) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new java.lang.IllegalStateException("must not have any other exception");
            }
        }
    }

    public void setHandlers(List<ExceptionHandler> handlers) {
        this.handlers = handlers;
    }
}
