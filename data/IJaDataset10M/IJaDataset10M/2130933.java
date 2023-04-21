package com.sitescape.team.web.portlet.handler;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.portlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.sitescape.team.InternalException;
import com.sitescape.team.context.request.RequestContext;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.dao.ProfileDao;
import com.sitescape.team.domain.NoUserByTheIdException;
import com.sitescape.team.domain.NoUserByTheNameException;
import com.sitescape.team.domain.User;
import com.sitescape.team.module.profile.ProfileModule;
import com.sitescape.team.portletadapter.support.PortletAdapterUtil;
import com.sitescape.team.util.SPropsUtil;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.util.WebHelper;

public class UserPreloadInterceptor implements HandlerInterceptor {

    private ProfileDao profileDao;

    private ProfileModule profileModule;

    protected ProfileDao getProfileDao() {
        return profileDao;
    }

    public void setProfileDao(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    protected ProfileModule getProfileModule() {
        return profileModule;
    }

    public void setProfileModule(ProfileModule profileModule) {
        this.profileModule = profileModule;
    }

    public boolean preHandle(PortletRequest request, PortletResponse response, Object handler) throws Exception {
        RequestContext requestContext = RequestContextHolder.getRequestContext();
        if (requestContext == null) return true;
        if (requestContext.getUser() == null) {
            loadUser(request, requestContext);
        }
        return true;
    }

    public void postHandle(RenderRequest request, RenderResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(PortletRequest request, PortletResponse response, Object handler, Exception ex) throws Exception {
    }

    private void loadUser(PortletRequest request, RequestContext reqCxt) {
        User user;
        PortletSession ses = WebHelper.getRequiredPortletSession(request);
        boolean isRunByAdapter = PortletAdapterUtil.isRunByAdapter(request);
        Long userId = (Long) ses.getAttribute(WebKeys.USER_ID, PortletSession.APPLICATION_SCOPE);
        if (userId == null) {
            String zoneName = reqCxt.getZoneName();
            String userName = reqCxt.getUserName();
            try {
                user = getProfileDao().findUserByName(userName, zoneName);
            } catch (NoUserByTheNameException e) {
                boolean userCreate = SPropsUtil.getBoolean("portal.user.auto.create", false);
                if (userCreate) {
                    Map updates = new HashMap();
                    Map userAttrs = (Map) request.getAttribute(javax.portlet.PortletRequest.USER_INFO);
                    if (userAttrs == null) {
                        throw new InternalException("User must log off and log in again");
                    }
                    if (userAttrs.containsKey("user.name.given")) updates.put("firstName", userAttrs.get("user.name.given"));
                    if (userAttrs.containsKey("user.name.family")) updates.put("lastName", userAttrs.get("user.name.family"));
                    if (userAttrs.containsKey("user.name.middle")) updates.put("middleName", userAttrs.get("user.name.middle"));
                    if (userAttrs.containsKey("user.business-info.online.email")) updates.put("emailAddress", userAttrs.get("user.business-info.online.email"));
                    if (userAttrs.containsKey("user.business-info.postal.organization")) updates.put("organization", userAttrs.get("user.business-info.postal.organization"));
                    updates.put("locale", request.getLocale());
                    user = getProfileModule().addUserFromPortal(zoneName, userName, null, updates);
                    if (!isRunByAdapter) {
                        ses.setAttribute(WebKeys.PORTLET_USER_SYNC, Boolean.TRUE, PortletSession.APPLICATION_SCOPE);
                    }
                } else throw e;
            }
            ses.setAttribute(WebKeys.USER_ID, user.getId(), PortletSession.APPLICATION_SCOPE);
        } else {
            user = getProfileDao().loadUser(userId, reqCxt.getZoneName());
        }
        boolean userModify = SPropsUtil.getBoolean("portal.user.auto.synchronize", false);
        Boolean sync = (Boolean) ses.getAttribute(WebKeys.PORTLET_USER_SYNC, PortletSession.APPLICATION_SCOPE);
        if (userModify && !isRunByAdapter && (sync == null || sync.equals(Boolean.FALSE))) {
            Map updates = new HashMap();
            Map userAttrs = (Map) request.getAttribute(javax.portlet.PortletRequest.USER_INFO);
            if (userAttrs == null) {
                throw new InternalException("User must log off and log in again");
            }
            String val = null;
            if (userAttrs.containsKey("user.name.given")) {
                val = (String) userAttrs.get("user.name.given");
                if (!identical(val, user.getFirstName())) updates.put("firstName", val);
            }
            if (userAttrs.containsKey("user.name.family")) {
                val = (String) userAttrs.get("user.name.family");
                if (!identical(val, user.getLastName())) updates.put("lastName", val);
            }
            if (userAttrs.containsKey("user.name.middle")) {
                val = (String) userAttrs.get("user.name.middle");
                if (!identical(val, user.getMiddleName())) updates.put("middleName", val);
            }
            if (userAttrs.containsKey("user.business-info.online.email")) {
                val = (String) userAttrs.get("user.business-info.online.email");
                if (!identical(val, user.getEmailAddress())) updates.put("emailAddress", val);
            }
            if (userAttrs.containsKey("user.business-info.postal.organization")) {
                val = (String) userAttrs.get("user.business-info.postal.organization");
                if (!identical(val, user.getOrganization())) updates.put("organization", val);
            }
            if (!request.getLocale().equals(user.getLocale())) updates.put("locale", request.getLocale());
            if (!updates.isEmpty()) {
                getProfileModule().modifyUserFromPortal(user, updates);
            }
            ses.setAttribute(WebKeys.PORTLET_USER_SYNC, Boolean.TRUE, PortletSession.APPLICATION_SCOPE);
        }
        reqCxt.setUser(user);
    }

    private boolean identical(String a, String b) {
        if (a == null) {
            if (b == null) return true; else return false;
        } else {
            if (b == null) return false; else return a.equals(b);
        }
    }
}
