package org.petsoar.security;

public interface UserAccessor {

    boolean authenticate(String username, String password);

    void signup(User user);

    User getUser(String username);
}
