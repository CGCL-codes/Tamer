package com.gr.staffpm.security.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.gr.staffpm.dao.HibernateDao;
import com.gr.staffpm.datatypes.Group;
import com.gr.staffpm.datatypes.Permission;
import com.gr.staffpm.datatypes.Role;
import com.gr.staffpm.datatypes.User;
import com.gr.staffpm.security.constants.StaffPMRoles;

@Repository("userDAO")
@SuppressWarnings("unchecked")
public class HibernateUserDAO extends HibernateDao implements UserDAO {

    @Override
    public User getUser(int userId) {
        User result = (User) getSession().get(User.class, userId);
        return result;
    }

    @Override
    public User findUser(String username) {
        User result = null;
        try {
            Criteria crit = getSession().createCriteria(User.class);
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            crit.add(Restrictions.eq(User.USERNAME, username));
            result = (User) crit.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void createUser(User user) {
        getSession().save(user);
    }

    @Override
    public List<User> getAllUsers() {
        Criteria crit = getSession().createCriteria(User.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.gt(User.ID, 1));
        List<User> result = crit.list();
        return result;
    }

    @Override
    public void deleteUser(int userId) {
        User user = getUser(userId);
        if (user != null) {
            getSession().delete(user);
        }
    }

    @Override
    public void addOrUpdateUser(User user) {
        getSession().saveOrUpdate(user);
    }

    @Override
    public List<User> getAllUsers(String property, boolean ascending) {
        Criteria crit = getSession().createCriteria(User.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.gt(User.ID, 1));
        Order order;
        if (property == null) {
            if (ascending) order = Order.asc(User.USERNAME); else order = Order.desc(User.USERNAME);
        } else if (property == User.FULL_NAME) {
            if (ascending) {
                crit.addOrder(Order.asc(User.LAST_NAME));
                order = Order.asc(User.FIRST_NAME);
            } else {
                crit.addOrder(Order.desc(User.LAST_NAME));
                order = Order.desc(User.FIRST_NAME);
            }
        } else {
            if (ascending) order = Order.asc(property); else order = Order.desc(property);
        }
        crit.addOrder(order);
        List<User> result = crit.list();
        return result;
    }

    @Override
    public List<User> getUsersInGroupByGroupName(String groupName, User currUser, int userLevel) {
        Criteria crit = getSession().createCriteria(User.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.createCriteria(User.GROUP).add(Restrictions.eq(Group.NAME, groupName));
        List<User> users = crit.list();
        List<User> result = new ArrayList<User>();
        for (User user : users) {
            if ((userLevel < getUsersHighestLevel(user) || user.equals(currUser)) && !result.contains(user)) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public Role getUserRole() {
        Criteria crit = getSession().createCriteria(Role.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.eq(Role.ROLE, StaffPMRoles.USER));
        Role result = (Role) crit.uniqueResult();
        return result;
    }

    @Override
    public void addRole(User user, String roleName) {
        Criteria crit = getSession().createCriteria(Role.class);
        crit.add(Restrictions.eq(Role.ROLE, roleName));
        Role role = (Role) crit.uniqueResult();
        user.getRoles().add(role);
        getSession().saveOrUpdate(user);
    }

    @Override
    public List<Role> getAllRoles() {
        Criteria crit = getSession().createCriteria(Role.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.ne(Role.ROLE, StaffPMRoles.ADMIN));
        List<Role> result = crit.list();
        return result;
    }

    @Override
    public void addRoles(User user, List<Role> roles) {
        user.getRoles().addAll(roles);
        getSession().saveOrUpdate(user);
    }

    @Override
    public List<Group> getAllGroups() {
        Criteria crit = getSession().createCriteria(Group.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Group> result = crit.list();
        return result;
    }

    @Override
    public boolean checkUserExists(User user) {
        Criteria crit = getSession().createCriteria(User.class);
        crit.add(Restrictions.eq(User.USERNAME, user.getUsername()));
        boolean result = (crit.uniqueResult() != null);
        return result;
    }

    @Override
    public List<Role> getAllRoles(String property, boolean ascending) {
        Criteria crit = getSession().createCriteria(Role.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.ne(Role.ROLE, StaffPMRoles.ADMIN));
        Order order;
        if (property == null) {
            if (ascending) order = Order.asc(Role.ROLE); else order = Order.desc(Role.ROLE);
        } else {
            if (ascending) order = Order.asc(property); else order = Order.desc(property);
        }
        crit.addOrder(order);
        List<Role> result = crit.list();
        return result;
    }

    @Override
    public Role getRole(int id) {
        Criteria crit = getSession().createCriteria(Role.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.eq("roleId", id));
        Role result = (Role) crit.uniqueResult();
        return result;
    }

    @Override
    public List<Permission> getAllPermissions() {
        Criteria crit = getSession().createCriteria(Permission.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Permission> result = crit.list();
        return result;
    }

    @Override
    public void createOrUpdateRole(Role newRole) {
        getSession().saveOrUpdate(newRole);
    }

    @Override
    public void createPermission(Permission permission, Role role) {
        role.getPermissions().add(permission);
        getSession().saveOrUpdate(permission);
        getSession().merge(role);
    }

    @Override
    public void deletePermission(String permissionString) {
        Criteria crit = getSession().createCriteria(Permission.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Restrictions.eq("permission", permissionString));
        getSession().delete(crit.uniqueResult());
    }

    public int getUsersHighestLevel(User user) {
        int highestLevel = -1;
        for (Role role : user.getRoles()) {
            if (highestLevel == -1) {
                highestLevel = role.getLevel();
            } else {
                if (highestLevel > role.getLevel()) highestLevel = role.getLevel();
            }
        }
        return highestLevel;
    }
}
