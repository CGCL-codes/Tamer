package net.sourceforge.brightside.estiemo.util;

import net.sourceforge.brightside.estiemo.domain.User;
import net.sourceforge.brightside.estiemo.domain.service.UserServices;

public class CheckUser {

    private UserServices users;

    public CheckUser(UserServices users) {
        super();
        this.users = users;
    }

    public User checkUser(String username, String password) {
        User user = users.getUser(username);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(password)) {
            return (User) user;
        }
        return null;
    }
}
