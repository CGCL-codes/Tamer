package com.webdeninteractive.bie.persistent.privileges;

import java.util.*;

/**
 * TODO: run error
 * @author  jdepons
 */
public class TestUser {

    /** Creates a new instance of Test */
    public TestUser() {
    }

    /**
     * JVM Args: -Dxbotts.home=D:\eclipse-SDK\WorkspaceBIE\BIE6.0.5\dist
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String xbottsHome = System.getProperty("xbotts.home");
        System.setProperty("com.webdeninteractive.datasource.PropertiesDir", xbottsHome + "/conf");
        Role r = Role.findByName("routeUsers");
        User u = User.findByName("jdepons");
        r.removeUser(u);
        ArrayList users = r.getUsers();
        System.out.println("len = " + users.size());
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.get(i);
            System.out.println("username = " + user.getUserName());
        }
    }
}
