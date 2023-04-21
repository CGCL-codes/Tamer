package org.hyn.maper;

import org.hyn.bean.User;

public interface UserMapper {

    public void insertUser(User user);

    public User getUser(String name);
}
