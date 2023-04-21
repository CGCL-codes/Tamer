package org.opennms.wicket.svclayer.model.userAdmin.xsd.users;

import java.util.ArrayList;

public class Users {

    public void addUser(User user) {
        userList.add(user);
    }

    public User getUser(int index) {
        return (User) userList.get(index);
    }

    public int sizeUserList() {
        return userList.size();
    }

    protected ArrayList userList = new ArrayList();
}
