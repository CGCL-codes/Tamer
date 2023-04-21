package icreate.servlets;

import javax.servlet.http.HttpSession;
import icreate.comps.User;
import icreate.security.ContainerControlList;

public class SessionListener implements javax.servlet.http.HttpSessionListener {

    /** Creates a new instance of SessionListener */
    public SessionListener() {
    }

    public void sessionCreated(javax.servlet.http.HttpSessionEvent httpSessionEvent) {
    }

    public void sessionDestroyed(javax.servlet.http.HttpSessionEvent httpSessionEvent) {
        HttpSession _session = httpSessionEvent.getSession();
        String _s_id = _session.getId();
        javax.servlet.ServletContext _sc = _session.getServletContext();
    }
}
