package su2;

import su2.user.*;
import javax.jws.WebService;
import su2_proxy.*;

@WebService(targetNamespace = "http://su2")
public class UserSvc implements IUserSvc {

    private IUserSvc m_dispatcher = new UserImpl();

    public User getUser(int userId) {
        return m_dispatcher.getUser(userId);
    }

    public int[] getUserList() {
        return m_dispatcher.getUserList();
    }

    public User getUserViaName(String user) {
        return m_dispatcher.getUserViaName(user);
    }
}
