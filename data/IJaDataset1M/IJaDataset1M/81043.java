package demo.service;

import demo.entity.User;

public interface UserService extends GenericService<User, Integer> {

    User login(String queryString, String[] values);
}
