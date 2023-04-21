package pl.zgora.uz.issi.login;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import pl.zgora.uz.issi.config.EMF;
import pl.zgora.uz.issi.entity.Member;
import pl.zgora.uz.issi.session.IdentityBean;

public class LoginBean implements Serializable {

    private static final long serialVersionUID = 6653979533557327223L;

    private String login;

    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        System.out.println(" try to log in ");
        if ((login != null) && (password != null)) {
            if ((login.compareToIgnoreCase("ppretki") == 0) && (password.compareToIgnoreCase("ppretki") == 0)) {
                Member member = new Member();
                member.setFirstName("Przemyslaw");
                member.setLastName("Pretki");
                member.setEmail("p.pretki@issi.uz.zgora.pl");
                member.setLogin("ppretki");
                IdentityBean identityBean = new IdentityBean();
                identityBean.setMember(member);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("identityBean", identityBean);
                System.out.println("success");
                return ("success");
            }
        }
        System.out.println("failure");
        return ("failure");
    }
}
