package com.sitescape.team.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;

/**
 * See com.sitescape.team.web.portlet.handler.OpenSessionInViewInterceptor for 
 * explanation
 * @author Janet McCann
 *
 */
public class OpenSessionInViewInterceptor extends org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor {

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws DataAccessException {
        super.afterCompletion(request, response, handler, ex);
    }
}
