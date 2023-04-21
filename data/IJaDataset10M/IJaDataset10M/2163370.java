package com.yeep.study.hibernate.model;

/**
 * A TestPersonAddress POJO for Hibernate.
 *
 * @author Roger.Yee
 */
public class Address {

    private String id;

    private String name;

    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
