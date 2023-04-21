package org.neodatis.odb.test.enumeration;

import java.util.List;

/**
 * @author olivier
 *
 */
public class ClassWithInterfaceWithEnum {

    String name;

    List<IUser> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IUser> getUsers() {
        return users;
    }

    public void setUsers(List<IUser> users) {
        this.users = users;
    }

    public ClassWithInterfaceWithEnum(String name, List<IUser> users) {
        super();
        this.name = name;
        this.users = users;
    }
}
