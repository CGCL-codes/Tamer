package net.coolcoders.showcase.web.icefaces;

import net.coolcoders.showcase.model.User;
import net.coolcoders.showcase.service.UserService;
import net.coolcoders.showcase.web.scope.ViewScoped;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * User: <a href="mailto:andreas@bambo.it">Andreas Baumgartner, andreas@bambo.it</a>
 * Date: 05.10.2010
 * Time: 16:17:01
 */
@Named
@ViewScoped
public class UsersBean implements Serializable {

    @EJB
    private UserService userService;

    @Inject
    private SessionBean sessionBean;

    private List<User> users;

    private User selectedUser;

    public List<User> getUsers() {
        if (users == null) {
            users = userService.listUsersYouFollow(sessionBean.getCurrentUser().getId());
        }
        return users;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void unfollow(ActionEvent event) {
        if (selectedUser != null) {
            User user = sessionBean.getCurrentUser();
            user.getFollowing().remove(selectedUser);
            userService.merge(user);
            users = null;
        }
    }
}
