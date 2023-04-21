package enterprise.jsf_jpa_war;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class UserManager {

    public static final String USER_SESSION_KEY = "user";

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    private String username;

    private String password;

    private String passwordv;

    private String fname;

    private String lname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordv() {
        return passwordv;
    }

    public void setPasswordv(String passwordv) {
        this.passwordv = passwordv;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String validateUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Wuser user = getUser();
        if (user != null) {
            if (!user.getPassword().equals(password)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed!", "The password specified is not correct.");
                context.addMessage(null, message);
                return null;
            }
            context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
            return "app-main";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed!", "Username '" + username + "' does not exist.");
            context.addMessage(null, message);
            return null;
        }
    }

    public String createUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Wuser wuser = getUser();
        if (wuser == null) {
            if (!password.equals(passwordv)) {
                FacesMessage message = new FacesMessage("The specified passwords do not match.  Please try again");
                context.addMessage(null, message);
                return null;
            }
            wuser = new Wuser();
            wuser.setFirstname(fname);
            wuser.setLastname(lname);
            wuser.setPassword(password);
            wuser.setUsername(username);
            wuser.setSince(new Date());
            try {
                utx.begin();
                em.persist(wuser);
                utx.commit();
                return "login";
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating user!", "Unexpected error when creating your account.  Please contact the system Administrator");
                context.addMessage(null, message);
                Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to create new user", e);
                return null;
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username '" + username + "' already exists!  ", "Please choose a different username.");
            context.addMessage(null, message);
            return null;
        }
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }

    private Wuser getUser() {
        try {
            Wuser user = (Wuser) em.createNamedQuery("Wuser.findByUsername").setParameter("username", username).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
