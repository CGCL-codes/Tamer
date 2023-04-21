package com.casheen.xfire.annotation.service;

import java.util.List;
import javax.jws.WebService;
import com.casheen.xfire.annotation.model.User;

@WebService
public interface UserManager {

    public void saveOrUpdate(User user);

    public User get(Integer id);

    public List<User> list();
}
