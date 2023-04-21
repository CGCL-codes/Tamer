package com.tiger.aowim.i18n;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.framework.util.StringUtils;

public class SiteLanguageHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        org.springframework.web.servlet.i18n.SessionLocaleResolver sessionLocale = new org.springframework.web.servlet.i18n.SessionLocaleResolver();
        if (!("").equals((StringUtils.replaceNull(request.getParameter("locale"), "")))) sessionLocale.setLocale(request, response, new java.util.Locale(request.getParameter("locale")));
        return true;
    }
}
