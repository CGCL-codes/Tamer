package it.pallamia.pallamiacontroller;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import it.pallamia.pallamiabo.Users;
import it.pallamia.pallamiabo.base.*;
import it.pallamia.pallamiabo.dao.*;
import java.util.*;

public class ViewUsersController extends SimpleFormController {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView onSubmit(Object command) throws ServletException {
        Users u = (Users) command;
        if (u.getId() == null) {
            _RootDAO.initialize();
            UsersDAO dao = new UsersDAO();
            u.setCreated(new Date());
            u.setUpdated(new Date());
            dao.save(u);
        } else {
            UsersDAO dao = new UsersDAO();
            u.setUpdated(new Date());
            dao.update(u);
        }
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    public Object formBackingObject(HttpServletRequest request) throws ServletException {
        String now = (new java.util.Date()).toString();
        logger.info("returning hello view with " + now);
        if (request.getParameter("uid") == null) {
            return new Users();
        } else {
            _RootDAO.initialize();
            UsersDAO dao = new UsersDAO();
            Session s = dao.createNewSession();
            Query q = s.createQuery("select u from u in class Users where u.id=:id");
            int uid = Integer.valueOf(request.getParameter("uid")).intValue();
            q.setInteger("id", uid);
            Users thisUser = (Users) q.list().get(0);
            dao.closeSession(s);
            return thisUser;
        }
    }
}
