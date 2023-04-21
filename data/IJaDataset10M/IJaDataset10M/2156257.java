package de.cue4net.eventservice.model.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import de.cue4net.eventservice.model.Login;
import de.cue4net.eventservice.model.User;

/**
 *
 * @author Keino Uelze - cue4net
 * @version $Id: LoginManager.java,v 1.5 2007-07-08 13:59:05 keino Exp $
 */
public class LoginManager extends HibernateDaoSupport {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public Login findLoginByUsername(String username) {
        List<?> helper = getHibernateTemplate().find("from Login where username=?", username);
        if (helper.size() == 0) return null;
        return (Login) helper.get(0);
    }

    public User getUserByUsername(String username) {
        return findLoginByUsername(username).getUser();
    }

    public void saveOrUpdate(Login login) {
        getHibernateTemplate().saveOrUpdate(login);
    }
}
