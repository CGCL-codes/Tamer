package cn.feigme.components.acegi;

import java.util.ArrayList;
import java.util.List;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import cn.feigme.identity.dao.UserDao;

/**
 * @author Feigme 
 *
 */
public class PfUserDetailsService implements UserDetailsService {

    private static Logger logger = Logger.getLogger(PfUserDetailsService.class);

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException, DataAccessException {
        logger.info("acegi check user --- start!");
        cn.feigme.identity.model.User user = getUserDao().getUserByName(arg0);
        UserDetails userDetails = null;
        if (user != null) {
            GrantedAuthority[] arrayAuths = getAuthoritiesByUsernameQuery(arg0);
            userDetails = new org.acegisecurity.userdetails.User(user.getEmail(), user.getPassword(), user.getEnabled().booleanValue(), true, true, true, arrayAuths);
        }
        logger.info("acegi check user --- end!");
        return userDetails;
    }

    public GrantedAuthority[] getAuthoritiesByUsernameQuery(String username) throws DataAccessException {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        GrantedAuthority gai = new GrantedAuthorityImpl("ROLE_a");
        list.add(gai);
        GrantedAuthority[] grantedAuthoritys = new GrantedAuthority[list.size()];
        return list.toArray(grantedAuthoritys);
    }
}
