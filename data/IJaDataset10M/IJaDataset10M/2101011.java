package com.casheen.springsecurity.cas.service;

import java.util.List;
import org.springframework.security.annotation.Secured;
import com.casheen.springsecurity.cas.model.User;

public interface UserManager {

    @Secured("ROLE_admin")
    public void saveOrUpdate(User user);

    public User get(Integer id);

    public List<User> list();

    /** 根据帐号查找用户 */
    public User getByAccount(String account);
}
