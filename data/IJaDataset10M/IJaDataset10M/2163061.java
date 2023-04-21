package com.objecteffects.sample.api.service;

import com.objecteffects.sample.domain.User;

/**
 * @author Rusty Wright
 */
public interface IUserService {

    /**
     * @return anonymous User
     */
    public User getUser();

    /**
     * @param id
     * @param email
     * @param admin
     * @return User
     */
    User getUser(String id, String email, boolean admin);
}
