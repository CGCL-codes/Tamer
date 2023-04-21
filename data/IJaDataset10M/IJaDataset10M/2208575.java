package com.babelstudio.cpasss.lib;

import java.util.List;
import org.apache.struts2.ServletActionContext;
import com.babelstudio.cpasss.action.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.babelstudio.cpasss.hibernate.*;

public class UserRoleAuthorizationInterceptor implements Interceptor {

    private static final long serialVersionUID = 5067790608840427509L;

    private PageDAO pageDAO;

    private RolepagerelationDAO rolepagerelationDAO;

    public PageDAO getPageDAO() {
        return pageDAO;
    }

    public void setPageDAO(PageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    public RolepagerelationDAO getRolepagerelationDAO() {
        return rolepagerelationDAO;
    }

    public void setRolepagerelationDAO(RolepagerelationDAO rolepagerelationDAO) {
        this.rolepagerelationDAO = rolepagerelationDAO;
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        if (getSessionUser() == null) {
            throw new ApplicationException("登录超时，请重新登录。");
        }
        Object action = invocation.getAction();
        BaseAction baseAction = null;
        if (action instanceof BaseAction) {
            baseAction = (BaseAction) action;
        }
        if (baseAction != null && "js".equals(baseAction.getResultType())) {
            if (!checkPermission(invocation.getProxy().getActionName(), baseAction.getUserSession().getUser().getRoleid())) throw new ApplicationException("没有权限。");
        }
        return invocation.invoke();
    }

    public UserSession getSessionUser() {
        return (UserSession) ActionContext.getContext().getSession().get("UserSession");
    }

    public boolean checkPermission(String actionName, Integer roleID) {
        try {
            List<com.babelstudio.cpasss.hibernate.Page> pages = pageDAO.findByName(actionName);
            if (pages.size() == 0) {
                return true;
            }
            Rolepagerelation relation = new Rolepagerelation();
            relation.setPageid(pages.get(0).getId());
            relation.setRoleid(roleID);
            List<Rolepagerelation> rprs = rolepagerelationDAO.findByExample(relation);
            if (rprs.size() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void destroy() {
    }

    public void init() {
    }
}
