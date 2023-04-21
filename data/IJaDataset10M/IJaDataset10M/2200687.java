package com.lalfa.framework.service.user;

import com.lalfa.framework.entity.user.Authority;
import com.lalfa.framework.entity.user.Role;
import com.lalfa.framework.entity.user.User;
import com.lalfa.framework.service.ServiceException;
import com.lalfa.modules.orm.hibernate.Page;
import com.lalfa.modules.orm.hibernate.SimpleHibernateTemplate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * 整个User模块内的业务逻辑Facade类.
 * 组合user,role两者的DAO.DAO均直接使用泛型的SimpleHibernateTemplate.
 * 使用Spring的@Service/@Autowired 指定IOC设置.
 * 使用Spring的@Transactional指定事务管理.
 * 
 * @author lalfa.shi
 */
@Service
@Transactional
public class UserManager {

    private static String AUTH_HQL = "select count(u) from User u where u.loginName=? and u.password=?";

    private SimpleHibernateTemplate<User, Long> userDao;

    private SimpleHibernateTemplate<Role, Long> roleDao;

    private SimpleHibernateTemplate<Authority, Long> authDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        userDao = new SimpleHibernateTemplate<User, Long>(sessionFactory, User.class);
        roleDao = new SimpleHibernateTemplate<Role, Long>(sessionFactory, Role.class);
        authDao = new SimpleHibernateTemplate<Authority, Long>(sessionFactory, Authority.class);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userDao.get(id);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Page<User> page) {
        return userDao.findAll(page);
    }

    @Transactional(readOnly = true)
    public User getUserByLoginName(String loginName) {
        return userDao.findUniqueByProperty("loginName", loginName);
    }

    public void saveUser(User user) {
        userDao.save(user);
    }

    public void deleteUser(Long id) {
        if (id == 1) throw new ServiceException("不能删除超级用户");
        User user = userDao.get(id);
        userDao.delete(user);
    }

    @Transactional(readOnly = true)
    public boolean isLoginNameUnique(String loginName, String orgLoginName) {
        return userDao.isPropertyUnique("loginName", loginName, orgLoginName);
    }

    /**
	 * 验证用户名密码. 
	 * 因为使用acegi做安全管理,此函数已作废,仅用作demo.
	 * 
	 * @return 验证通过时返回true.用户名或密码错误返回false.
	 */
    @Transactional(readOnly = true)
    public boolean auth(String loginName, String password) {
        if (!StringUtils.hasText(loginName) || !StringUtils.hasText(password)) return false;
        return (userDao.findLong(AUTH_HQL, loginName, password) == 1);
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Transactional(readOnly = true)
    public Role getRole(Long id) {
        return roleDao.get(id);
    }

    public void saveRole(Role role) {
        roleDao.save(role);
    }

    public void deleteRole(Long id) {
        if (id == 1) throw new ServiceException("不能删除超级管理员角色");
        Role role = roleDao.get(id);
        roleDao.delete(role);
    }

    @Transactional(readOnly = true)
    public List<Authority> getAllAuths() {
        return authDao.findAll();
    }

    @Transactional(readOnly = true)
    public Authority getAuthority(Long id) {
        return authDao.get(id);
    }
}
