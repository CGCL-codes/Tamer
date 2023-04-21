package com.javaeye.common.service;

import java.util.List;
import com.javaeye.common.dto.User;

public interface IUserService {

    public List<User> getUsers();

    public User saveUser(User user);

    public boolean removeUser(String id);

    public User getUser(String id);

    public User login(User user);

    public long getUserByName(String userName);
}
