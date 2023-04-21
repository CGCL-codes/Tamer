package actions;

import java.util.Map;
import model.Project;
import model.Role;
import model.User;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.classic.Session;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import database.HibernateUtil;
import database.RugidoQuery;

public class UserAction extends ActionSupport implements SessionAware, Preparable {

    private Map session;

    private Long idUser;

    private User user;

    private boolean admin;

    private String oldPassword;

    private String newPassword1;

    private String newPassword2;

    public void prepare() throws Exception {
        if (this.idUser != null) {
            this.user = RugidoQuery.getInstance().loadUser(idUser);
        }
        Long id = (Long) getSession().get(Constants.PROJECT_KEY);
        if ((id != null) && (user != null)) {
            this.admin = (user.getProjectRole(RugidoQuery.getInstance().loadProject(id))).getAdmin();
        }
    }

    public String saveUser() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Long id = (Long) getSession().get(Constants.PROJECT_KEY);
        Project project = RugidoQuery.getInstance().loadProject(id);
        Role role = this.user.getProjectRole(project);
        role.setAdmin(this.admin);
        session.update(this.user);
        return SUCCESS;
    }

    public String saveInfo() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(this.user);
        return SUCCESS;
    }

    public String savePassword() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        if (!user.getPassword().equals(oldPassword)) {
            this.addActionError(getText("user.password.invalid"));
            return ERROR;
        }
        user.setPassword(newPassword1);
        session.update(this.user);
        return SUCCESS;
    }

    public void setSession(Map value) {
        session = value;
    }

    public Map getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
