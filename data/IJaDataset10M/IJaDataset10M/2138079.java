package uk.ac.ebi.intact.editor.controller.dashboard;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.ac.ebi.intact.editor.controller.UserSessionController;
import uk.ac.ebi.intact.editor.controller.misc.AbstractUserController;
import uk.ac.ebi.intact.model.user.User;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
@Component
@Scope("conversation.access")
public class UserProfileController extends AbstractUserController {

    private String hashedPassword;

    private String newPassword1;

    private String newPassword2;

    public void loadData(ComponentSystemEvent cse) {
        UserSessionController userSessionController = getUserSessionController();
        User user = getDaoFactory().getUserDao().getByLogin(userSessionController.getCurrentUser().getLogin());
        setUser(user);
    }

    public String updateProfile() {
        User user = getUser();
        if (newPassword1 != null && !newPassword1.isEmpty()) {
            if (newPassword1.equals(newPassword2)) {
                user.setPassword(hashedPassword);
            } else {
                addErrorMessage("Wrong password", "Passwords do not match");
                FacesContext.getCurrentInstance().renderResponse();
                return null;
            }
        }
        getDaoFactory().getUserDao().update(user);
        getUserSessionController().setCurrentUser(user);
        addInfoMessage("User profile", "Profile was updated successfully");
        return "/dashboard/dashboard";
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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
