package com.ictconnect.database.entity;

import java.util.Set;
import org.hibernate.annotations.Entity;

@Entity
public class Group {

    private long id;

    private Set<User> users;

    private Set<Role> roles;
}
