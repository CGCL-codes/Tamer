package in.espirit.tracer.action;

import in.espirit.tracer.database.dao.UserDao;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

@UrlBinding("/user/changepassword")
public class ChangePasswordActionBean extends BaseActionBean {

    private static final String URL = "/WEB-INF/jsp/changePassword.jsp";

    private String currentPassword;

    private String newPassword;

    @ValidationMethod(when = ValidationState.NO_ERRORS)
    public void valMethod(ValidationErrors errors) throws Exception {
        if (!UserDao.checkPassword(getContext().getLoggedUser().toLowerCase(), getCurrentPassword())) {
            errors.addGlobalError(new SimpleError("Current Password is wrong!"));
        }
    }

    @DontValidate
    @DefaultHandler
    public Resolution open() {
        return new ForwardResolution(URL);
    }

    public Resolution submit() throws Exception {
        UserDao.changePassword(getContext().getLoggedUser(), newPassword);
        logger.warn("password changed for user > " + getContext().getLoggedUser());
        getContext().getMessages().add(new SimpleMessage("Password Changed Successfully."));
        return new ForwardResolution(DashboardActionBean.class);
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
