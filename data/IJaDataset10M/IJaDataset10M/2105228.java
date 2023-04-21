package com.raimcomputing.pickforme.domain.dao.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.raimcomputing.pickforme.common.exception.NoSuchUserException;
import com.raimcomputing.pickforme.common.vo.UserVo;
import com.raimcomputing.pickforme.domain.dao.UserDao;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    private Log log = LogFactory.getLog(UserDaoImpl.class);

    /**
	 * 
	 */
    public UserVo loadUserByEmail(String email) throws NoSuchUserException {
        List results = getHibernateTemplate().find("from UserVoImpl as user where user.email=?", email);
        if (results.size() != 1) {
            log.warn("Searching for a user with email '" + email + "' did not return 1 result.");
            throw new NoSuchUserException();
        } else {
            return ((UserVo) results.get(0));
        }
    }

    /**
	 * 
	 * @param user
	 */
    public void save(UserVo user) {
        getHibernateTemplate().saveOrUpdate(user);
    }
}
