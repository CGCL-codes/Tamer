package com.fortuityframework.hibernate.events;

import com.fortuityframework.core.event.jpa.JPAEntityCreateEvent;
import com.fortuityframework.hibernate.testentities.User;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class UserCreateEvent extends JPAEntityCreateEvent<User> {

    /**
	 * @param entity
	 */
    public UserCreateEvent(User user) {
        super(user);
    }
}
