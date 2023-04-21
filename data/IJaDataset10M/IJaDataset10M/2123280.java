package com.googlecode.phugushop.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private Map<String, User> map;

    UserDao() {
        initMap();
    }

    private void initMap() {
        map = new HashMap<String, User>(6);
        map.put("debbie", new User("debbie", "Debbie Goode", "dg"));
        map.put("gelgar", new User("gelgar", "Greg Elgar", "ge"));
        map.put("emma", new User("emma", "Emma Kenyon", "ek"));
        map.put("heather", new User("heather", "Heather Callaway", "hc"));
        map.put("stefan", new User("stefan", "Stefan Pauls", "sp"));
        map.put("hugo", new User("hugo", "HugoParker", "sp"));
    }

    public Collection<User> retrieveAllUsers() {
        return map.values();
    }

    public User retrieveUserByName(String userName) {
        return map.get(userName);
    }
}
