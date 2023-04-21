package gov.ic.dia.hc.action;

import gov.ic.dia.hc.bean.*;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.*;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.*;

/**
 *
 * @author shawn
 */
public class UserAction extends ActionSupport implements ServletRequestAware, SessionAware {

    private User userBean;

    private String error = "", returnTo;

    private Vector<User> pageList = new Vector<User>();

    private HttpServletRequest request;

    private Map<String, Object> session;

    public void setServletRequest(HttpServletRequest req) {
        this.request = req;
    }

    public void setSession(Map<String, Object> sess) {
        this.session = sess;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String show() throws Exception {
        int id = Integer.parseInt((String) request.getParameter("id"));
        setUserBean((User) User.find(id));
        return SUCCESS;
    }

    public String login() throws Exception {
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        setReturnTo((String) session.get("lastPage"));
        User found = User.findByUsername(username);
        if (found == null) {
            setError("Username does not exist.");
            return ERROR;
        } else if (found.getPassword().equals(password)) {
            session.put("currentUser", found);
            return SUCCESS;
        } else {
            setError("Wrong password.");
            return ERROR;
        }
    }

    public String logout() throws Exception {
        session.remove("currentUser");
        return SUCCESS;
    }

    public void setUserBean(User user) {
        userBean = user;
    }

    private void setError(String error) {
        this.error = error;
    }

    private void setReturnTo(String returnTo) {
        this.returnTo = returnTo;
    }

    public User getUserBean() {
        return userBean;
    }

    public String getError() {
        return error;
    }

    public String getReturnTo() {
        return returnTo;
    }
}
