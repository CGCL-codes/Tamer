package org.jumpmind.symmetric.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jumpmind.symmetric.transport.handler.AuthenticationResourceHandler;
import org.jumpmind.symmetric.transport.handler.AuthenticationResourceHandler.AuthenticationStatus;

/**
 * This better be the first filter that executes ! TODO: if this thing fails,
 * should it prevent further processing of the request?
 * 
 */
public class AuthenticationFilter extends AbstractTransportFilter<AuthenticationResourceHandler> {

    private static final Log logger = LogFactory.getLog(AuthenticationFilter.class);

    @Override
    public boolean isContainerCompatible() {
        return true;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String securityToken = req.getParameter(WebConstants.SECURITY_TOKEN);
        String nodeId = req.getParameter(WebConstants.NODE_ID);
        if (StringUtils.isEmpty(securityToken) || StringUtils.isEmpty(nodeId)) {
            sendError(resp, HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        final AuthenticationStatus status = getTransportResourceHandler().status(nodeId, securityToken);
        if (AuthenticationStatus.FORBIDDEN.equals(status)) {
            sendError(resp, HttpServletResponse.SC_FORBIDDEN);
        } else if (AuthenticationStatus.REGISTRATION_REQUIRED.equals(status)) {
            sendError(resp, WebConstants.REGISTRATION_REQUIRED);
        } else if (AuthenticationStatus.ACCEPTED.equals(status)) {
            chain.doFilter(req, resp);
        }
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
