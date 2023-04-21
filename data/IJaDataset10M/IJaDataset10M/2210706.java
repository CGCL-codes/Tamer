package com.starlight.access.user;

import com.starlight.access.LoadException;
import com.starlight.access.SaveException;

/**
 *
 */
public interface UserSupportDriver {

    public User login(String username, char[] password, String server, boolean dev_mode) throws LoadException;

    public boolean verifyPassword(User user, char[] password) throws LoadException;

    public void changePassword(User user, char[] new_password) throws SaveException;

    public User loadUser(int id) throws LoadException;

    public User[] loadUsers() throws LoadException;
}
