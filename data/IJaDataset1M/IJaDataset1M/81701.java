package bioinfo.comaWebServer.pages.edit;

import org.acegisecurity.annotation.Secured;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.ioc.annotations.Inject;
import bioinfo.comaWebServer.dataServices.IDataSource;
import bioinfo.comaWebServer.entities.User;

@IncludeStylesheet("context:assets/styles.css")
@Secured("ROLE_ADMIN")
public class EditPersonalParams {

    @Persist
    private User user;

    @Inject
    private IDataSource dataSource;

    @Persist
    private String newPassword;

    @Persist
    private String retypedPassword;

    private String info;

    void onPrepare() {
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    void onActivate(String msg) {
        info = msg;
    }

    String onPassivate() {
        return info;
    }

    @Component
    private Form personalParamsForm;

    @Component(id = "newPassword")
    private PasswordField newPasswordField;

    @Component(id = "retypedPassword")
    private PasswordField retypedPasswordField;

    @OnEvent(component = "personalParamsForm")
    void onValidate() {
        if (newPassword != null && retypedPassword != null) {
            if (!newPassword.equals(retypedPassword)) {
                info = "Passwords do not match.";
                newPassword = null;
                retypedPassword = null;
                personalParamsForm.recordError(retypedPasswordField, "Passwords do not match.");
            }
        }
    }

    @OnEvent(component = "personalParamsForm")
    @Secured("ROLE_ADMIN")
    void onSuccess() {
        user.setPassword(newPassword);
        info = "Updated successfully!";
        try {
            dataSource.saveOrUpdate(user);
        } catch (Exception e) {
            info = "Updating failed!";
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public Form getPersonalParamsForm() {
        return personalParamsForm;
    }

    public void setPersonalParamsForm(Form personalParamsForm) {
        this.personalParamsForm = personalParamsForm;
    }

    public PasswordField getNewPasswordField() {
        return newPasswordField;
    }

    public void setNewPasswordField(PasswordField newPasswordField) {
        this.newPasswordField = newPasswordField;
    }

    public PasswordField getRetypedPasswordField() {
        return retypedPasswordField;
    }

    public void setRetypedPasswordField(PasswordField retypedPasswordField) {
        this.retypedPasswordField = retypedPasswordField;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
