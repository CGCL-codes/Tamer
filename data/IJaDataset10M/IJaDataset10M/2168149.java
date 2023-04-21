package ru.point.control;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import ru.point.dao.SmartDao;
import ru.point.model.Role;
import ru.point.model.Session;
import ru.point.model.User;
import javax.servlet.http.Cookie;

/**
 * @author Mikhail Sedov [25.03.2009]
 */
public class AbstractController {

    @Autowired
    protected SmartDao dao;

    protected void putCookie(Cookie sessionCookie, ModelMap model) {
        if (sessionCookie != null) {
            Session session = dao.get(Session.class, sessionCookie.getValue());
            if (session != null) {
                Hibernate.initialize(session.getUser());
                model.put("session", session);
            }
        }
    }

    protected boolean isAllowedForCurrentUser(Cookie sessionCookie, User user) {
        if (sessionCookie != null) {
            Session session = dao.get(Session.class, sessionCookie.getValue());
            if (session != null) {
                if (session.getUser().getId() == user.getId()) {
                    return true;
                } else if (session.getUser().getMainActivity().getRole().getGroupPolicy() == Role.SEO_POLICY) {
                    return true;
                }
            }
        }
        return false;
    }

    public ModelAndView error(Exception ex, ModelMap model) {
        model.put("exception", ex);
        return new ModelAndView("error", model);
    }
}
