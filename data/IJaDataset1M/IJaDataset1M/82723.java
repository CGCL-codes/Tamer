package org.jnf.security.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

public class RIAAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success: " + authResult.toString());
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain the following Authentication: '" + authResult + "'");
        }
        String targetUrl = this.isAlwaysUseDefaultTargetUrl() ? null : obtainFullRequestUrl(request);
        if (targetUrl == null) {
            targetUrl = getDefaultTargetUrl();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to target URL from HTTP Session (or default): " + targetUrl);
        }
        onSuccessfulAuthentication(request, response, authResult);
        this.getRememberMeServices().loginSuccess(request, response, authResult);
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        if (isRIARequest(request)) {
            response.getWriter().write("{success:true, sessionId:'" + request.getSession().getId() + "'}");
        } else {
            sendRedirect(request, response, targetUrl);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
        }
        String failureUrl = this.getExceptionMappings().getProperty(failed.getClass().getName(), this.getAuthenticationFailureUrl());
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString());
        }
        try {
            request.getSession().setAttribute(ACEGI_SECURITY_LAST_EXCEPTION_KEY, failed);
        } catch (Exception ignored) {
        }
        onUnsuccessfulAuthentication(request, response, failed);
        this.getRememberMeServices().loginFail(request, response);
        if (isRIARequest(request)) {
            String errors = "";
            JSONObject errorJSON = new JSONObject();
            if (failed != null) {
                errorJSON.put("cause", failed.getMessage());
                response.getWriter().write("{success: false, errors: " + errorJSON.toString() + "}");
            } else {
                errorJSON.put("cause", "Unexpected error occurs.");
                errorJSON.put("success", "false");
                errors = errorJSON.toString();
                response.getWriter().write(errors);
            }
            response.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");
        } else {
            sendRedirect(request, response, failureUrl);
        }
    }

    private boolean isRIARequest(HttpServletRequest request) {
        String source = request.getParameter("source");
        if (source != null && source.equals("app-ria")) {
            return true;
        }
        return false;
    }
}
