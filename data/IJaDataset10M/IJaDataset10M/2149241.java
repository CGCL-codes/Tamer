package sk.naive.talker.server;

import sk.naive.talker.*;
import sk.naive.talker.persistence.PersistenceException;
import sk.naive.talker.util.Utils;
import java.util.*;
import java.util.logging.*;

/**
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.17 $ $Date: 2005/02/16 19:27:21 $
 */
public class UserRegistry implements UserFinder {

    private Map<String, User> loginOnlineUsers;

    private Map idOnlineUsers;

    private Map<Integer, String> idLoginMap;

    private Logger logger;

    private UserFactory userFactory;

    public UserRegistry(Map<Integer, String> idLoginMap, UserFactory factory, Logger logger) {
        loginOnlineUsers = new HashMap<String, User>();
        idOnlineUsers = new HashMap();
        this.idLoginMap = idLoginMap;
        this.userFactory = factory;
        this.logger = logger;
    }

    public User findUserById(Integer id) {
        UserProxy u = (UserProxy) idOnlineUsers.get(id);
        if (u != null) {
            return u;
        }
        if (idLoginMap.containsKey(id)) {
            try {
                u = userFactory.createOfflineUser(id);
                new DbLayer().loadProperties(u);
            } catch (PersistenceException e) {
                logger.log(Level.WARNING, "Exception occured while loading properties...", e);
            }
        }
        return u;
    }

    public User findUserByLogin(String login) {
        login = Utils.normalize(login);
        User user = loginOnlineUsers.get(login);
        if (user != null) {
            return user;
        }
        if (idLoginMap.values().contains(login)) {
            return offlineUser(login);
        }
        user = findLoggedUser(login);
        if (user == null) {
            login = Utils.findFirstInCollection(login, idLoginMap.values(), true);
            if (login != null) {
                user = offlineUser(login);
            }
        }
        return user;
    }

    private UserProxy offlineUser(String login) {
        UserProxy user = null;
        try {
            DbLayer db = new DbLayer();
            user = userFactory.createOfflineUser(db.userId(login));
            db.loadProperties(user);
        } catch (PersistenceException e) {
            logger.log(Level.WARNING, "Exception occured while loading properties...", e);
        }
        return user;
    }

    public User findLoggedUser(String name) {
        name = Utils.normalize(name);
        String foundName = Utils.findFirstInCollection(name, loginOnlineUsers.keySet(), false);
        if (foundName == null) {
            return null;
        }
        return loginOnlineUsers.get(foundName);
    }

    public Collection<User> onlineUsers() {
        return loginOnlineUsers.values();
    }

    public Collection<String> getUserLogins() {
        return idLoginMap.values();
    }

    public void logout(User user) {
        loginOnlineUsers.remove(user.getLogin());
        idOnlineUsers.remove(user.getId());
    }

    public void login(User user) {
        if (!idLoginMap.values().contains(user.getLogin())) {
            idLoginMap.put(user.getId(), user.getLogin());
        }
        loginOnlineUsers.put(user.getLogin(), user);
        idOnlineUsers.put(user.getId(), user);
    }
}
