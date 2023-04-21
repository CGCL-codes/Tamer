package artem.finance.server.persist.beans;

import java.io.Serializable;
import artem.finance.server.persist.User;

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User user;

    public UserBean() {
        this.user = new User();
    }

    public UserBean(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
