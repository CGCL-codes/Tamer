package com.m4f.gaeweb.business.service.impl;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.m4f.gaeweb.model.GaeM4FUser;

public abstract class GaeM4FUserDetailService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(GaeM4FUserDetailService.class.getName());

    public static final String ROLE_ROOT = "ROLE_ROOT";

    protected String roleRoot = ROLE_ROOT;

    protected String rootUserName;

    protected String rootEncodedPassword;

    public GaeM4FUserDetailService(String rootUser, String rootPassword) {
        this.rootUserName = rootUser;
        this.rootEncodedPassword = this.getEncodedPassword(rootPassword);
    }

    public abstract GaeM4FUser loadGaeM4FUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;

    public abstract GaeM4FUser createRootUser();

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        if (isRootUser(userName)) return makeRootUser();
        return this.loadGaeM4FUserByUsername(userName);
    }

    protected GaeM4FUser makeRootUser() {
        GaeM4FUser rootUser = this.createRootUser();
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl(roleRoot));
        rootUser.setAuthorities(authorities);
        return rootUser;
    }

    protected String getEncodedPassword(String plainPassword) {
        Object salt = null;
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(plainPassword, salt);
    }

    protected boolean isRootUser(String userName) {
        if (rootUserName.equals(userName)) return true; else return false;
    }
}
