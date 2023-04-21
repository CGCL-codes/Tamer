package com.aaron.server;

import com.aaron.client.UserService;
import com.aaron.client.User;
import com.aaron.client.LoginException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import siuying.gm.*;
import java.io.*;

/**
 * @author aaron
 * 
 */
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

    static final long serialVersionUID = 234567;

    public User login(String username, String password) throws LoginException, ParsePacketException, IOException, ExceptionInInitializerError, DatabaseException {
        UserServer user_server = new UserServer();
        return user_server.login(username, password);
    }

    public void logout(String auth) {
        try {
            SessionServer session_server = new SessionServer();
            session_server.logout(auth);
        } catch (DatabaseException e) {
            System.err.println("logout: DatabaseException: " + e.getMessage());
        }
    }

    public User getUser(String auth) throws Exception {
        UserCacheServer user_cache_server = new UserCacheServer();
        return user_cache_server.getUser(auth);
    }
}
