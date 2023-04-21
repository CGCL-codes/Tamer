package gfriends.server;

import gfriends.client.LoginService;
import gfriends.client.model.LoginInfo;
import gfriends.server.model.Contact;
import java.util.List;
import javax.jdo.PersistenceManager;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private static final long serialVersionUID = -3029793714379333127L;

    public LoginInfo login(String requestUri) {
        PersistenceManager pm = null;
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        LoginInfo loginInfo = new LoginInfo();
        try {
            if (user != null) {
                loginInfo.setLoggedIn(true);
                loginInfo.setEmailAddress(user.getEmail());
                loginInfo.setNickname(user.getNickname());
                loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
                Contact contact = null;
                pm = PMF.get().getPersistenceManager();
                String query = "select from " + Contact.class.getName() + " where email == :email";
                @SuppressWarnings("unchecked") List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute(user.getEmail());
                if (contactList != null && contactList.size() > 0) {
                    contact = contactList.get(0);
                }
                if (contact != null && contact.isEnable()) {
                    loginInfo.setRegisted(true);
                } else {
                    loginInfo.setRegisted(false);
                }
            } else {
                loginInfo.setLoggedIn(false);
                loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
            }
        } finally {
            if (pm != null) {
                pm.close();
                pm = null;
            }
        }
        return loginInfo;
    }
}
