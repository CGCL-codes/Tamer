package ru.scriptum.view.handler;

import ru.scriptum.model.domaindata.User;

/**
 * The managed bean with session scope. 
 * <p>
 * It is used as a session scope cache.
 * In JSF, the properties are set by bean management facility.
 * 
 * @author <a href="mailto:dev@scriptum.ru">Developer</a>
 */
public class SessionBean {

    private User user;

    public SessionBean() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
