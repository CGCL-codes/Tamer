package org.esk.dablog.web.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.acegisecurity.annotation.Secured;
import org.acegisecurity.util.UrlUtils;
import org.acegisecurity.AccessDeniedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * This class checks form security annotations Secured and forces the secured roles
 * User: esk
 * Date: 26.12.2006
 * Time: 11:21:41
 * $Id:$
 */
public class AnnotationSecurityInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) {
            return true;
        }
        Secured s = handler.getClass().getAnnotation(Secured.class);
        if (s == null) {
            return true;
        }
        String[] roles = s.value();
        if (roles == null) {
            return true;
        }
        for (String role : roles) {
            if (!request.isUserInRole(role)) {
                throw new AccessDeniedException(role);
            }
        }
        return super.preHandle(request, response, handler);
    }
}
