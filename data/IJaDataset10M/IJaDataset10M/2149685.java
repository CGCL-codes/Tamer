package net.sf.dozer.util.mapping.generics;

import java.util.List;

/**
 * 
 * @author garsombke.franz
 *
 */
enum StatusPrime {

    PROCESSING, SUCCESS, ERROR
}

public class UserGroupPrime {

    private StatusPrime statusPrime;

    private String name;

    private List<UserPrime> users;

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public List<UserPrime> getUsers() {
        return users;
    }

    public void setUsers(List<UserPrime> aUsers) {
        users = aUsers;
    }

    public StatusPrime getStatusPrime() {
        return statusPrime;
    }

    public void setStatusPrime(StatusPrime statusPrime) {
        this.statusPrime = statusPrime;
    }
}
