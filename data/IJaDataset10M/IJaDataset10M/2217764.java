package in.espirit.tracer.ext;

import in.espirit.tracer.database.dao.UserDao;
import in.espirit.tracer.model.User;
import net.sourceforge.stripes.action.ActionBeanContext;

public class MyActionBeanContext extends ActionBeanContext {

    public void setLoggedUser(String userName) {
        getRequest().getSession().setAttribute("loggedUser", userName);
    }

    public String getLoggedUser() {
        return (String) getRequest().getSession().getAttribute("loggedUser");
    }

    public void setUser(User user) {
        getRequest().getSession().setAttribute("user", user);
    }

    public User getUser() {
        return (User) getRequest().getSession().getAttribute("user");
    }

    public String getUserRole() throws Exception {
        return UserDao.getUserRole(getLoggedUser());
    }

    public void deleteLoggedUser() {
        getRequest().getSession().removeAttribute("loggedUser");
    }

    public void setCurrentSection(String section) {
        getRequest().getSession().setAttribute("section", section);
    }

    public String getCurrentSection() {
        return (String) getRequest().getSession().getAttribute("section");
    }
}
