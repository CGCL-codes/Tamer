package com.addermason.monitoring.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class SecurityManager {

    public static boolean reject(String cause) {
        UserService us = UserServiceFactory.getUserService();
        User user = us.getCurrentUser();
        if (user == null) {
            return true;
        }
        return false;
    }
}
