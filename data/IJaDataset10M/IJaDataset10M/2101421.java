package com.javascout.bundlemanager.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.javascout.bundlemanager.domain.User;
import com.javascout.bundlemanager.domain.UserDetailsImpl;
import com.javascout.bundlemanager.service.UserService;

@Component(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userService.get(username);
        UserDetails userDetails = null;
        if (user != null) {
            userDetails = new UserDetailsImpl(user);
        } else {
            throw new UsernameNotFoundException(username + "not found");
        }
        return (userDetails);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
