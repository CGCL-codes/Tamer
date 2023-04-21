package com.cusp.pt.model.security.eao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import com.cusp.pt.model.security.User;
import com.cusp.pt.model.security.eao.GroupEAO;
import com.cusp.pt.model.security.eao.UserEAO;

public class UserEAOImpl extends JpaDaoSupport implements UserEAO {

    private GroupEAO groupEAO;

    @SuppressWarnings("unchecked")
    public User findUserByEmailAndPassword(String emailAddress, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", emailAddress);
        params.put("password", password);
        List<User> userList = getJpaTemplate().findByNamedParams("SELECT u FROM User u WHERE emailAddress LIKE :email AND password LIKE :password", params);
        if (userList.size() > 1) {
            throw new IllegalStateException("More then one user with same credentials exists in datasource: " + emailAddress);
        }
        User user = userList.size() > 0 ? userList.get(0) : null;
        return user;
    }

    @SuppressWarnings("unchecked")
    public User findUserByEmail(String emailAddress) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", emailAddress);
        List<User> userList = getJpaTemplate().findByNamedParams("SELECT u FROM User u WHERE emailAddress LIKE :email", params);
        if (userList.size() > 1) {
            throw new IllegalStateException("More then one user with same credentials exists in datasource: " + emailAddress);
        }
        User user = userList.size() > 0 ? userList.get(0) : null;
        return user;
    }

    public void createUser(User user) {
        if (user.getGroup() != null) {
            user.setGroup(groupEAO.getGroupById(user.getGroup().getId()));
        }
        getJpaTemplate().persist(user);
    }

    public GroupEAO getGroupEAO() {
        return groupEAO;
    }

    public void setGroupEAO(GroupEAO groupEAO) {
        this.groupEAO = groupEAO;
    }

    public void updateUser(User user) {
        getJpaTemplate().merge(user);
    }

    @SuppressWarnings("unchecked")
    public List<User> findUsersForClientId(Integer clientId) {
        if (clientId == null) {
            return getJpaTemplate().find("SELECT u FROM User u");
        }
        System.out.println();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId", clientId);
        List<User> userList = getJpaTemplate().findByNamedParams("SELECT u FROM User u WHERE u.client.id = :clientId", params);
        return userList;
    }
}
