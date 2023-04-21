package com.cry.todo.dao;

import java.util.ArrayList;
import com.cry.core.security.UserDetails;
import com.cry.todo.domain.User;

public interface UserDao<T> {

    ArrayList<T> loadAll();

    T load(int id);

    UserDetails loadByUserDetails(String username);
}
