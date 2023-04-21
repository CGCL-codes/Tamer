package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import ru.rsdn.Janus.JanusUserInfo;
import ru.rsdn.Janus.UserResponse;

/**
 * @author Alexey
 */
public class UsersList {

    private final User[] users;

    private final Version version;

    public UsersList(User[] users, Version version) {
        this.users = users;
        this.version = version;
    }

    public UsersList(UserResponse r) {
        this.version = new Version(r.getLastRowVersion());
        JanusUserInfo[] users = r.getUsers();
        this.users = new User[users.length];
        for (int i = 0; i < users.length; i++) {
            this.users[i] = new User(users[i]);
        }
    }

    public User[] getUsers() {
        return users;
    }

    public Version getVersion() {
        return version;
    }
}
