package es.devel.opentrats.booking.service.auth;

import es.devel.opentrats.booking.beans.User;
import es.devel.opentrats.booking.service.dao.IUserDao;
import es.devel.opentrats.booking.service.dao.impl.UserDaoImpl;
import es.devel.opentrats.booking.service.business.EnvironmentService;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 *
 * @author Fran Serrano
 */
public class AuthManager {

    /** Creates a new instance of AuthManager */
    public AuthManager() {
    }

    public static boolean validateUser(String login, String password) {
        try {
            IUserDao userDao = new UserDaoImpl();
            User user = userDao.load(login);
            if (user != null && user.getClave().equals(password)) {
                EnvironmentService.getInstance().setUser(user);
                return true;
            }
            return false;
        } catch (HibernateException he) {
            he.printStackTrace();
            Logger.getRootLogger().error(he.toString());
            return false;
        }
    }
}
