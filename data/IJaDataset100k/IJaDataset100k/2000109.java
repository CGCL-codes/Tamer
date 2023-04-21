package jmemento.impl.dao.db;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import jmemento.api.dao.db.IUserDao;
import jmemento.api.domain.user.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author rusty
 */
@Repository
public final class UserDao extends SqlMapClientDaoSupport implements IUserDao {

    private final transient Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SqlMapClient sqlMapClient;

    @PostConstruct
    public void init() {
        this.setSqlMapClient(sqlMapClient);
    }

    @Override
    public void addNewUser(final IUser user) {
        log.debug("user: {}", user);
        getSqlMapClientTemplate().insert("User.addUser", user);
    }

    @Override
    public IUser getUserById(final String userDisplayId) {
        log.debug("userDisplayId: {}", userDisplayId);
        final IUser user = (IUser) getSqlMapClientTemplate().queryForObject("User.userByDisplayId", userDisplayId);
        log.debug("user: {}", user);
        return (user);
    }

    @Override
    public IUser getUserByName(final String userName) {
        log.debug("userName: {}", userName);
        final IUser user = (IUser) getSqlMapClientTemplate().queryForObject("User.userByName", userName);
        log.debug("user: {}", user);
        return (user);
    }

    @Override
    public String getDisplayId(final String userName) {
        log.debug("userName: {}", userName);
        final String userDisplayId = (String) getSqlMapClientTemplate().queryForObject("User.userDisplayIdByName", userName);
        log.debug("userDisplayId: {}", userDisplayId);
        return (userDisplayId);
    }

    @Override
    public String getPassword(final String userDisplayId) {
        log.debug("userName: {}", userDisplayId);
        final String password = (String) getSqlMapClientTemplate().queryForObject("User.getPasswordByDisplayId", userDisplayId);
        return (password);
    }

    @Override
    public void setPassword(final String userDisplayId, final String password) {
        log.debug("userDisplayId: {}", userDisplayId);
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userDisplayId", userDisplayId);
        map.put("password", password);
        getSqlMapClientTemplate().update("User.setPasswordByDisplayId", map);
    }
}
