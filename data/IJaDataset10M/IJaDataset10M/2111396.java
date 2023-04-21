package com.javaframe.role.service;

import java.util.List;
import java.util.Map;
import com.javaframe.role.entity.User;
import com.javaframe.role.tools.ConditionInfo;
import com.javaframe.role.tools.PageInfo;

public interface UserService {

    public void save(User user);

    public void delete(Integer id);

    public void delete(Map<?, ?> map);

    public List<User> findAll();

    public List<User> findAll(List<ConditionInfo> conditions, PageInfo page);

    public User findById(Integer id);
}
