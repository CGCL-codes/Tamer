package ru.dragon.bugzilla.api.v3;

import ru.dragon.bugzilla.BugzillaException;
import ru.dragon.bugzilla.api.UserService;
import java.util.Map;

/**
 * Class user as proxy for bugzilla <code>Webservice::User</code> api
 *
 * @author <a href="mailto: NIzhikov@gmail.com">Izhikov Nikolay</a>
 * @see <a href="http://www.bugzilla.org/docs/tip/html/api/Bugzilla/WebService/User.html">Bugzilla documentation</a>
 */
public class UserServiceImpl extends AbstractService implements UserService {

    private boolean logined = false;

    public Integer login(final String bugzillaUrl, final String login, final String password) throws BugzillaException {
        Map result = executeService(getWebServiceUrl(bugzillaUrl), "User.login", new ServiceExecutor.Parameter("login", login), new ServiceExecutor.Parameter("password", password), new ServiceExecutor.Parameter("remember", true));
        logined = true;
        return (Integer) result.get("id");
    }

    public void logout(String bugzillaUrl) throws BugzillaException {
        logined = false;
    }

    public boolean logined() {
        return logined;
    }
}
