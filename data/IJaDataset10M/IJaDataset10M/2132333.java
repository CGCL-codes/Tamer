package com.inet.qlcbcc.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.webos.core.option.Option;
import org.webos.core.repository.RepositoryException;
import org.webos.repository.hibernate.AbstractHibernateReadableRepository;
import com.inet.qlcbcc.repository.UserReadableRepository;
import com.inet.qlcbcc.domain.Account;
import com.inet.qlcbcc.domain.User;

/**
 * UserReadableRepositorySupport.
 *
 * @author Dzung Nguyen
 * @version $Id: UserReadableRepositorySupport.java 2011-05-01 02:59:49z nguyen_dv $
 *
 * @since 1.0
 */
@Repository(value = "userReadableRepository")
public class UserReadableRepositorySupport extends AbstractHibernateReadableRepository<User, String> implements UserReadableRepository {

    /**
   * Creates {@link UserReadableRepositorySupport} from.
   */
    public UserReadableRepositorySupport() {
        super(User.class);
    }

    public Option<User> findByCode(String code) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("code", code);
        List<User> users = findByNamedQuery("User.findByCode", params);
        return (users == null || users.isEmpty() ? Option.<User>none() : Option.<User>some(users.get(0)));
    }

    public Option<User> findByUsername(String username) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("uname", username);
        List<User> users = findByNamedQuery("User.findByUsername", params);
        return (users == null || users.isEmpty() ? Option.<User>none() : Option.<User>some(users.get(0)));
    }

    public boolean exists(String username) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("uname", username);
        return (uniqueResultNamedQuery("User.exists", params, Integer.class).getOrElse(0) > 0);
    }

    public List<Account> findAccountBy(String username) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("uname", username);
        List<Account> accounts = findByNamedQuery("User.findAccountBy", params);
        return (accounts == null ? Collections.<Account>emptyList() : Collections.unmodifiableList(accounts));
    }

    public Option<User> findByUnameAndFunctionType(String username, String functionType) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("uname", username);
        params.put("functionType", functionType + ":%");
        List<User> users = findByNamedQuery("User.findUserByFunctionType", params);
        return (users == null || users.isEmpty() ? Option.<User>none() : Option.<User>some(users.get(0)));
    }

    public int getRevision(String username) throws RepositoryException {
        final HashMap<String, Object> params = new HashMap<String, Object>(1);
        params.put("uname", username);
        return uniqueResultNamedQuery("User.getRevision", params, Integer.class).getOrElse(0);
    }
}
