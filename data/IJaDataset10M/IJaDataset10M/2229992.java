package t5demo.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import t5demo.model.User;
import t5demo.services.UserDAO;

/**
 * Page used for editing/creating users
 */
public class Edit {

    @Component
    private BeanEditForm form;

    @Inject
    private UserDAO userDAO;

    private User user;

    private long userId = 0;

    public void onActivate(long id) {
        if (id > 0) {
            user = userDAO.find(id);
            this.userId = id;
        }
    }

    public Object onSuccess() {
        System.out.println("+++++++" + userId + "++++++");
        if (userId == 0) {
            userDAO.save(user);
        } else {
            userDAO.update(user);
        }
        return Start.class;
    }

    public void onValidateForm() {
        User anotherUser = userDAO.findUserByName(user.getUserName());
        if (anotherUser != null && anotherUser.getId() != user.getId()) {
            form.recordError("User with the name '" + user.getUserName() + "' already exists");
        }
    }

    public long onPassivate() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
