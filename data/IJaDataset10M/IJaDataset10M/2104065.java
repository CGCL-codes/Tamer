package org.blueoxygen.rest.android.geneology.action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.blueoxygen.cimande.CimandeAction;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactory;
import org.blueoxygen.rest.security.User;
import org.blueoxygen.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author Dian Aditya
 * 
 */
@Action("android")
public class LoginController extends CimandeAction implements ModelDriven {

    @Autowired
    protected HibernateSessionFactory hsf;

    protected Session session;

    private Object model;

    private Map<String, Object> data = new HashMap<String, Object>();

    @PostConstruct
    public void init() {
        session = hsf.createSession();
    }

    @PreDestroy
    public void destroy() {
        session.close();
    }

    public String index() {
        data.put("message", "error");
        model = data;
        return "android/android-login";
    }

    public String show() {
        Map<String, Object> sess = ActionContext.getContext().getSession();
        System.out.println("Parameter:" + request.getParameter("secret"));
        System.out.println("Session  :" + sess.get("secret"));
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("secret", request.getParameter("secret")));
        criteria.add(Restrictions.eq("username", request.getParameter("user")));
        User us = (User) criteria.uniqueResult();
        if (us != null) {
            if (us.getSecret().equals(request.getParameter("secret"))) {
                User u = (User) us;
                data.put("message", "success");
                data.put("data", u.getDetail());
                model = u.getDetail();
            } else {
                data.put("message", "error");
                model = "error";
            }
        } else if (id.equalsIgnoreCase("login")) {
            login();
        } else if (id.equalsIgnoreCase("logout")) {
            ServletActionContext.getContext().getSession().clear();
            data.put("message", "success");
            model = "success";
        }
        return "android/android-login";
    }

    private void login() {
        StringUtils utils = new StringUtils();
        String username = request.getParameter("user");
        String password = request.getParameter("password");
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        criteria.add(Restrictions.eq("password", password));
        User user = (User) criteria.uniqueResult();
        if (user == null) {
            data.put("message", "error");
            model = "error";
        } else {
            String secret = UUID.randomUUID().toString().replaceAll("-", "");
            ActionContext.getContext().getSession().put("uid", user.getId());
            ActionContext.getContext().getSession().put("secret", secret);
            user.setSecret(secret);
            model = secret;
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        }
    }

    public Object getModel() {
        return model;
    }
}
