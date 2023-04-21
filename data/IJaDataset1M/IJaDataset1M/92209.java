package jp.co.uchida.discretelibraryheap.saas;

import java.util.Map;
import javax.annotation.Resource;
import jp.co.uchida.discretelibraryheap.orm.IUserInfo;
import jp.co.uchida.discretelibraryheap.security.LoginUser;
import jp.co.uchida.discretelibraryheap.lib.SessionConfigResolver;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MultiUserDetailService implements UserDetailsService {

    @Autowired
    private MultiUserDAO mulitUserDao;

    @Resource(name = "sessionConfigResolver")
    private SessionConfigResolver mappingSessions;

    protected static Logger logger;

    static {
        logger = LoggerFactory.getLogger(SaaSDaoSupport.class);
    }

    @Override
    public UserDetails loadUserByUsername(String loginID) throws UsernameNotFoundException, DataAccessException {
        Map<String, SessionConfig> map = mappingSessions.getMappingSessions();
        for (String schema : map.keySet()) {
            try {
                SessionFactory sf = (SessionFactory) map.get(schema).getSessionFactoryBean().getObject();
                QueryUserDao dao = new QueryUserDao();
                dao.setSessionFactory(sf);
                IUserInfo emp = dao.query(loginID);
                if (emp != null) {
                    LoginUser user = LoginUser.createInstance(emp, schema);
                    return user;
                }
            } catch (DataAccessException e) {
                logger.error("DataAccess", e);
                throw e;
            } catch (Exception e) {
                logger.error("Other", e);
            }
        }
        throw new UsernameNotFoundException(loginID);
    }

    class QueryUserDao extends HibernateDaoSupport {

        public IUserInfo query(String loginID) {
            IUserInfo emp = mulitUserDao.findUserByLoginID(this.getHibernateTemplate(), loginID);
            return emp;
        }
    }
}
