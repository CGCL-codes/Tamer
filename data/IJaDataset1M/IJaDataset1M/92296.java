package org.fao.fenix.web.modules.core.server.security;

import java.util.List;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.security.FenixSecuredUser;
import org.fao.fenix.persistence.security.FenixSecuredUserDao;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public class FenixUserDetailsService implements UserDetailsService {

    FenixSecuredUserDao fenixSecuredUserDao;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException, DataAccessException {
        FenixSecuredUser u = fenixSecuredUserDao.findByName(name);
        if (u == null) {
            System.out.println("Workaround applied!!! ");
            int i = 0;
            List<FenixSecuredUser> list = fenixSecuredUserDao.findAll();
            for (FenixSecuredUser fenixSecuredUser : list) {
                if (fenixSecuredUser.getName().equals(name)) {
                    u = fenixSecuredUser;
                    i++;
                }
            }
            if (i > 1) throw new FenixException("Bug in the fenix model!");
        }
        if (u == null) throw new UsernameNotFoundException(name + " is not a known user for Fenix");
        UserDetails userDetails = new User(u.getName(), u.getPassword(), u.isEnabled(), u.isAccountNonExpired(), u.isCredentialsNonExpired(), u.isAccountNonLocked(), u.getAuthorities());
        return userDetails;
    }

    public void setFenixSecuredUserDao(FenixSecuredUserDao fenixSecuredUserDao) {
        this.fenixSecuredUserDao = fenixSecuredUserDao;
    }
}
