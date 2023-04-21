package org.vosao.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vosao.business.impl.BusinessImpl;
import org.vosao.business.impl.mq.MessageQueueImpl;
import org.vosao.common.VosaoContext;
import org.vosao.service.impl.BackServiceImpl;
import org.vosao.service.impl.FrontServiceImpl;

/**
 * Vosao context creation and request injection.
 * 
 * @author Alexander Oleynik
 *
 */
public class ContextFilter extends AbstractFilter implements Filter {

    public ContextFilter() {
        super();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        VosaoContext ctx = VosaoContext.getInstance();
        ctx.setRequest((HttpServletRequest) request);
        ctx.setResponse((HttpServletResponse) response);
        ctx.setConfig(null);
        if (ctx.getMessageQueue() == null) {
            ctx.setMessageQueue(new MessageQueueImpl());
        }
        if (ctx.getBusiness() == null) {
            ctx.setBusiness(new BusinessImpl());
        }
        if (ctx.getFrontService() == null) {
            ctx.setFrontService(new FrontServiceImpl());
        }
        if (ctx.getBackService() == null) {
            ctx.setBackService(new BackServiceImpl());
        }
        ctx.getPageRenderingContext().clear();
        chain.doFilter(request, response);
        ctx.getPageRenderingContext().clear();
    }
}
