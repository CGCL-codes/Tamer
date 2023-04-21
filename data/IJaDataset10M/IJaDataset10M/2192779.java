package jard.webshop.jsfbeans;

import jard.webshop.management.UserManagement;
import jard.webshop.nbp.User;
import jard.webshop.util.Utils;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Disaia
 */
@ManagedBean(name = "accountBacking")
@RequestScoped
public class AccountBacking {

    @ManagedProperty(value = "#{userModelBean.user}")
    private User user = null;

    private String oldPassWord = "";

    /** Creates a new instance of AccountBacking */
    public AccountBacking() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOldPassWord() {
        return oldPassWord;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }

    public String checked() {
        if (user != null) {
            return user.getNewsLetter() ? "checked='checked'" : "";
        } else {
            return "";
        }
    }

    public void verifyPassword(FacesContext facesContext, UIComponent uiComponent, Object newValue) {
        System.out.println("(AccountBacking.verifyPassword)");
        String oldPass = (String) uiComponent.getAttributes().get("oldpass");
        String pass = (String) uiComponent.getAttributes().get("pass");
        String repeatPass = (String) newValue;
        System.out.println("(AccountBacking.verifyPasswordValidator) pass=" + pass + ", repeatPass=" + repeatPass);
        if (oldPass == null) {
            throw new ValidatorException(new FacesMessage("Please enter your old password."));
        } else if (!Utils.hash(oldPass).equals(user.getPassword())) {
            throw new ValidatorException(new FacesMessage("Old password is incorrect!"));
        } else if (repeatPass == null) {
            throw new ValidatorException(new FacesMessage("Please repeat the new password."));
        } else if (!repeatPass.equals(pass)) {
            throw new ValidatorException(new FacesMessage("Repeated password does not match!"));
        }
    }

    public void checkEmail(FacesContext facesContext, UIComponent uiComponent, Object newValue) {
        System.out.println("(AccountBacking.checkEmail)");
        User u = UserManagement.getInstance().getUser((String) newValue);
        if (u != null && !user.equals(u)) {
            if (!u.equals(user)) {
                throw new ValidatorException(new FacesMessage("Email address already in use by someone else!"));
            }
        }
    }
}
