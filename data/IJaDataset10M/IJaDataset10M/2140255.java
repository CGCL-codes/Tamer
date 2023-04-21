package com.evasion.plugin.security;

import com.evasion.entity.Group;
import com.evasion.entity.User;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.dao.salt.ReflectionSaltSource;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

public class UserDetailsAdapter extends org.springframework.security.userdetails.User {

    public UserDetailsAdapter(User account) {
        super(account.getUsername(), account.getPassword(), true, true, true, true, makeGrantedAuthorities(account));
    }

    private static GrantedAuthority[] makeGrantedAuthorities(User account) {
        GrantedAuthority[] result = null;
        Collection<String> auths = new HashSet<String>();
        if (account.getAuthorities() != null && !(account.getAuthorities().isEmpty())) {
            auths.addAll(account.getAuthorities());
        }
        if (account.getGroups() != null && !(account.getGroups().isEmpty())) {
            for (Group grp : account.getGroups()) {
                auths.addAll(grp.getAuthorities());
            }
        }
        int nbrAuth = auths.size();
        if (nbrAuth > 0) {
            result = new GrantedAuthority[nbrAuth];
            int i = 0;
            for (String role : auths) {
                result[i] = new GrantedAuthorityImpl(role);
                i++;
            }
        }
        return result;
    }

    public static void encodPassword(User user) {
        UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
        String password = userDetails.getPassword();
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        Object salt = saltSource.getSalt(userDetails);
        user.setPassword((new ShaPasswordEncoder()).encodePassword(password, salt));
    }

    public static void validGrantedAuthority(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        boolean result;
        result = (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) || (user.getGroups() != null && !user.getGroups().isEmpty());
        if (!result) {
            HashSet<String> auth = (new HashSet<String>());
            auth.add("ROLE_USER");
            user.setAuthorities(auth);
        }
    }
}
