package org.sucri.servlet.handler;

import org.apache.commons.collections15.map.MultiKeyMap;
import org.sucri.dao.UsersEntity;
import org.sucri.floxs.servlet.RequestHandler;
import org.sucri.floxs.servlet.User;
import org.sucri.floxs.Tools;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Aug 26, 2007
 * Time: 9:59:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterHandler extends RequestHandler$ {

    public RegisterHandler(String page) {
        super(page, "register");
    }

    public RegisterHandler(MultiKeyMap<String, RequestHandler> s) {
        super(null, "register", s);
    }

    public boolean processRequest(EntityManagerFactory emf, User user, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            UsersEntity person = new UsersEntity();
            person.setFirstname(req.getParameter("firstname"));
            person.setLastname(req.getParameter("lastname"));
            person.setUname(req.getParameter("username"));
            person.setPassword(req.getParameter("password"));
            person.setEmail(req.getParameter("email"));
            em.persist(person);
            em.getTransaction().commit();
            res.getOutputStream().println("{success:true}");
        } catch (Exception e) {
            e.printStackTrace();
            res.getOutputStream().println("{errors: true}");
        } finally {
            em.close();
        }
        return true;
    }
}
