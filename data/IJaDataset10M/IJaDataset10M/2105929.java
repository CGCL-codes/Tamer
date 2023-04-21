package com.kinglian.learn.reservation;

import java.util.List;

/**
 * @author kinglian
 *
 */
public interface UserService {

    public User getUser(int userID);

    public int createUser(User user);

    public void deleteUser(int userID);

    public List<String> getReservedServers(User user);
}
