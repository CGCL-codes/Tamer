package com.simpletasks.stripes.context;

import net.sourceforge.stripes.action.ActionBeanContext;
import javax.servlet.http.HttpSession;
import com.simpletasks.domain.User;

/**
 * Created by IntelliJ IDEA.
 * User: Mario Arias
 * Date: 30/05/2008
 * Time: 08:35:14 PM
 */
public class SimpleTasksActionBeanContext extends ActionBeanContext {

    private static final String ST_USER = "ST.user";

    public HttpSession getSession() {
        return getRequest().getSession();
    }

    public User getUser() {
        return (User) getSession().getAttribute(ST_USER);
    }

    public void setUser(User user) {
        getSession().setAttribute(ST_USER, user);
    }
}
