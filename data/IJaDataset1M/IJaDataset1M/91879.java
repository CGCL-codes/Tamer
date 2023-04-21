package code.service;

import java.util.List;
import code.model.User;

public interface ManageUserService {

    public abstract boolean insertUser(User user);

    public abstract User getUserById(long uid);

    public abstract User getUserByName(String name);

    public abstract List<User> getUserlistByName(String name);

    public abstract long deleteUser(long userid);

    public abstract boolean updateUser(User user);
}
