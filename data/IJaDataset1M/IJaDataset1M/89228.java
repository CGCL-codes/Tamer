package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.ColumnSort;

public interface UsersDAO {

    User getUser(int uid);

    ArrayList<User> getUsers(int offset, int limit, ColumnSort sort);

    int getUsersCount();

    boolean registerUser(String name, String email, int level, String password);

    User login(String username, String password);

    boolean update(User user);

    ArrayList<User> getUsersById(ArrayList<Integer> uids, int offset, int limit, ColumnSort sort);
}
