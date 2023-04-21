package com.ihc.app.service;

import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import com.ihc.app.model.User;
import org.springframework.dao.DataAccessException;

public class MockUserDetailsService implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return new User("testuser");
    }
}
