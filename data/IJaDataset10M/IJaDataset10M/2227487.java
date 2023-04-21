package com.corratech.opensuite.web.administration.actions;

import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.corratech.opensuite.web.administration.utils.CoreServiceUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Aleksandr Kryzhak
 * 
 */
@SuppressWarnings("unchecked")
public class LogoutAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1L;

    private Map session;

    public String execute() {
        session.remove(CoreServiceUtil.class.getName());
        return SUCCESS;
    }

    public void setSession(Map session) {
        this.session = session;
    }
}
