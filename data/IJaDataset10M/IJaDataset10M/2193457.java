package org.acegisecurity.portlet.interceptors;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.web.portlet.HandlerInterceptor;
import org.springframework.web.portlet.ModelAndView;

public class PortletSecurityInterceptor implements HandlerInterceptor, InitializingBean {

    private PortletModeRoleMappingSource portletModeRoleMappingSource;

    private String accessDeniedPage;

    private Log logger = LogFactory.getLog(getClass());

    public void afterActionCompletion(ActionRequest arg0, ActionResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    public void afterRenderCompletion(RenderRequest arg0, RenderResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    public void postHandleRender(RenderRequest arg0, RenderResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
    }

    public boolean preHandleAction(ActionRequest request, ActionResponse response, Object handler) throws Exception {
        if (checkPortletModeIsAllowed(request.getPortletMode())) {
            if (logger.isDebugEnabled()) {
                logger.debug("user is allowed to access portlet");
            }
            return true;
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("user is not allowed to access portlet");
            }
            return false;
        }
    }

    public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) throws Exception {
        if (checkPortletModeIsAllowed(request.getPortletMode())) {
            if (logger.isDebugEnabled()) {
                logger.debug("user is allowed to access portlet");
            }
            return true;
        } else {
            if (logger.isInfoEnabled()) {
                logger.debug("user is not allowed to access portlet");
            }
            response.setContentType("text/html");
            PortletRequestDispatcher dispatcher = request.getPortletSession().getPortletContext().getRequestDispatcher(accessDeniedPage);
            dispatcher.include(request, response);
            return false;
        }
    }

    @Required
    public void setPortletModeRoleMappingSource(PortletModeRoleMappingSource portletModeRoleMappingSource) {
        this.portletModeRoleMappingSource = portletModeRoleMappingSource;
    }

    private boolean checkPortletModeIsAllowed(PortletMode portletMode) {
        if (logger.isDebugEnabled()) {
            logger.debug("Checking security config for portlet mode :" + portletMode);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            GrantedAuthority[] authorities = auth.getAuthorities();
            List<PortletModeRoleMapping> mappings = portletModeRoleMappingSource.lookupMappings(authorities);
            if (mappings.size() == 0) {
                logger.warn("No security config attribute found for portlet mode :" + portletMode + ", returning true for authorization check...");
            }
            for (Iterator<PortletModeRoleMapping> iterator = mappings.iterator(); iterator.hasNext(); ) {
                PortletModeRoleMapping mapping = iterator.next();
                String portletModeName = mapping.getPortletMode();
                if (portletModeName.equals("*") || portletModeName.equalsIgnoreCase(portletMode.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setAccessDeniedPage(String accessDeniedPage) {
        this.accessDeniedPage = accessDeniedPage;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(portletModeRoleMappingSource, "portletModeRoleMappingSource shouldnt be null");
        Assert.hasLength(accessDeniedPage, "accessDeniedPage must have value");
    }
}
