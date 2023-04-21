package org.or5e.web.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.or5e.core.BaseObject;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = 1L;

    protected String userAuthKey = null;

    protected String userID = null;

    protected String currentPage = "HOME";

    protected Map<String, Object> session;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public final String getCurrentPage() {
        return currentPage;
    }

    public final void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public final String getUserID() {
        return userID;
    }

    public final void setUserID(String userID) {
        this.userID = userID;
    }

    public final String getUserAuthKey() {
        return userAuthKey;
    }

    public final void setUserAuthKey(String userAuthKey) {
        this.userAuthKey = userAuthKey;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    private BaseObject baseObj = new BaseObject() {

        @Override
        public String getName() {
            return getClass().getName();
        }
    };

    protected final void info(Object message) {
        baseObj.info(message);
    }

    protected final void debug(Object message) {
        baseObj.debug(message);
    }

    protected final void warn(Object message) {
        baseObj.warn(message);
    }

    protected final void error(Object message) {
        baseObj.error(message);
    }

    protected final String getproperty(String key) {
        return baseObj.getproperty(key);
    }

    protected final void setProperty(String key, String value) {
        baseObj.setProperty(key, value);
    }

    public static void putMessage(Object message) {
        System.out.println(message);
    }
}
