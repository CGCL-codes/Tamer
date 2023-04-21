package web.xnova.forms;

import org.mvc.Dispatcher;
import org.mvc.form.Element;
import org.mvc.form.Form;
import org.mvc.form.FormException;
import org.mvc.form.elements.PasswordField;
import org.mvc.form.elements.Submit;
import org.mvc.form.elements.TextField;
import org.mvc.validators.NotEmpty;
import web.xnova.persistence.entities.User;
import web.xnova.persistence.managers.ManagerException;
import web.xnova.persistence.managers.UserManager;
import java.io.IOException;

public class LoginForm extends Form {

    public LoginForm() {
        super();
        this.setAction(this._dispatcher.routePath("/auth/login"));
        this.setMethod("POST");
        TextField loginEl = (TextField) this.addElement(new TextField("login"));
        loginEl.addValidator(new NotEmpty()).setAttribute(Element.ATTR_LABEL, "Enter your login");
        PasswordField passwordEl = (PasswordField) this.addElement(new PasswordField("password"));
        passwordEl.setAttribute(Element.ATTR_LABEL, "And password").addValidator(new NotEmpty());
        Submit submitBtn = (Submit) this.addElement(new Submit("submit"));
        submitBtn.setValue("Sign In");
    }

    @Override
    protected void mainProcess() throws FormException {
        try {
            User u = UserManager.getInstance().findByCredentials(this.getValue("login"), this.getValue("password"));
            if (u != null) {
                this.getSession().setAttribute("user", u);
                try {
                    this._dispatcher.redirect("/user/profile");
                } catch (IOException e) {
                    throw new FormException(e.getMessage());
                }
            } else {
                this.getElement("login").addError("User with such login or password is not founds!");
            }
        } catch (ManagerException e) {
            throw new FormException("Database error!");
        }
    }
}
