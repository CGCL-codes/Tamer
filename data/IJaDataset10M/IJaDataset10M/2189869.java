package com.googlecode.beauti4j.security.service;

import org.springframework.context.ApplicationEvent;
import com.googlecode.beauti4j.security.model.User;

/**
 * An example for creating spring's Application Event.
 * 
 * @author Hang Yuan (anthony.yuan@gmail.com)
 */
public class UserCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2491504639999978533L;

    private User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
