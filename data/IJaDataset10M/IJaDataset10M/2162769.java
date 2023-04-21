package net.sourceforge.mandalore.page;

import net.sourceforge.mandalore.dto.UserDto;
import net.sourceforge.mandalore.model.User;
import net.sourceforge.mandalore.model.enums.UserRoleEnum;
import net.sourceforge.mandalore.model.enums.StatusTypeEnum;
import net.sourceforge.mandalore.service.UserService;
import net.sourceforge.mandalore.jsf.JsfUtils;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import org.hibernate.StaleObjectStateException;

/**
 * Created by IntelliJ IDEA.
 * User: ctoth
 * Date: Sep 14, 2006
 * Time: 11:31:58 AM
 */
public class UserPage extends AbstractPageBase {

    private transient UserService userService;

    private UserDto user;

    private boolean editing = false;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = userService.getUserById(currentServiceContext(), user.getId());
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isEditing() {
        return editing;
    }

    public void init() {
    }

    public String save() {
        try {
            userService.save(currentServiceContext(), user);
            JsfUtils.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, "messages.USER_SAVED");
            return "gotoUsers";
        } catch (RuntimeException e) {
            if (JsfUtils.isExceptionCausedBy(e, StaleObjectStateException.class)) {
                JsfUtils.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, "messages.ENTITY_MODIFIED");
                user = userService.getUserById(currentServiceContext(), user.getId());
                return "";
            } else throw e;
        }
    }

    public void reset() {
        user = userService.getUserById(currentServiceContext(), user.getId());
        clearInputs(FacesContext.getCurrentInstance().getViewRoot().getFacetsAndChildren());
    }
}
