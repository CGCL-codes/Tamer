package sqs.ejb.sessionbean;

import java.util.List;
import javax.ejb.Remote;
import sqs.exception.UserNotFoundException;
import sqs.model.User;

/**
 * @author kjleng
 *
 */
@Remote
public interface UserMgr {

    public User getUserByUserName(String username) throws UserNotFoundException;

    public List<User> getAllUsers();

    public void updateUser(User user) throws UserNotFoundException;

    public void removeUser(User user);

    public void createUser(User user);
}
