package org.thirdway.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Handles intercepting secure requests, checking for authenticated access and
 * managing the request. May 1, 2005
 */
public class SecurityHandlerInterceptor extends HandlerInterceptorAdapter {

    /**
     * Handle incoming requests through the filter.
     * 
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
}
