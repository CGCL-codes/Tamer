package net.sf.webwarp.modules.user.ui.trinidad;

import net.sf.webwarp.modules.user.User;
import net.sf.webwarp.modules.user.UserDAO;
import net.sf.webwarp.modules.user.UserGroup;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.shale.tiger.managed.Bean;
import org.apache.shale.tiger.managed.Property;
import org.apache.shale.tiger.managed.Scope;

@Bean(name = "changePass", scope = Scope.REQUEST)
public class ChangePasswordController {

    private String returnTarget = "default";

    @Property("#{UserDAO}")
    private UserDAO<User, UserGroup> userDAO;

    @Property("#{PasswordValidator}")
    private PasswordValidator passwordValidator;

    @Property("#{PasswordEncoder}")
    private PasswordEncoder passwordEncoder;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    public String changePassword() {
        if (passwordValidator.validate(oldPassword, newPassword, confirmPassword)) {
            User currentUser = UserUtils.getCurrentUser();
            currentUser.setPassword(passwordEncoder.encodePassword(newPassword, null));
            userDAO.changePassword(currentUser.getId(), currentUser.getPassword(), UserUtils.getCurrentUser().getUsername());
            return returnTarget;
        }
        return null;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setUserDAO(UserDAO<User, UserGroup> userDAO) {
        this.userDAO = userDAO;
    }

    public String getReturnTarget() {
        return returnTarget;
    }

    public void setReturnTarget(String returnTarget) {
        this.returnTarget = returnTarget;
    }
}
