package org.bionote.om.dao.hibernate;

import java.sql.SQLException;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bionote.om.ISpace;
import org.bionote.om.IUser;
import org.bionote.om.IUserSpace;
import org.bionote.om.dao.UserSpaceDAO;
import org.bionote.om.key.UserSpaceKey;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author mbreese
 *
 */
public class UserSpaceDAOImpl extends HibernateDaoSupport implements UserSpaceDAO {

    protected final Log logger = LogFactory.getLog(getClass());

    public UserSpaceDAOImpl() {
        super();
    }

    public IUserSpace findUserSpace(final ISpace space, final IUser user) {
        return (IUserSpace) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Criteria crit = arg0.createCriteria(IUserSpace.class);
                UserSpaceKey key = new UserSpaceKey(user, space);
                crit.add(Expression.eq("key", key));
                IUserSpace userSpace = (IUserSpace) crit.uniqueResult();
                return userSpace;
            }
        });
    }

    public void save(IUserSpace userSpace) {
        if (findUserSpace(userSpace.getSpace(), userSpace.getUser()) == null) {
            getHibernateTemplate().save(userSpace);
        } else {
            delete(userSpace);
        }
        getHibernateTemplate().save(userSpace);
    }

    public void delete(IUserSpace userSpace) {
        getHibernateTemplate().delete(userSpace);
    }
}
