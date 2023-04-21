package cn.edu.nju.tsip.service.impl;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.nju.tsip.dao.IDao;
import cn.edu.nju.tsip.entity.Authority;
import cn.edu.nju.tsip.entity.Role;
import cn.edu.nju.tsip.entity.User;
import com.google.common.collect.Sets;

@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IDao<User> dao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        System.out.println(username + " " + dao);
        User user = dao.findUniqueBy("from User as user where user.loginName=?", username);
        if (user == null) {
            throw new UsernameNotFoundException("用户" + username + " 不存在");
        }
        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getPassword(), true, true, true, true, obtainGrantedAuthorities(user));
    }

    /**
	 * 获得用户所有角色的权限集合.
	 */
    private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = Sets.newHashSet();
        for (Role role : user.getRoleList()) {
            for (Authority authority : role.getAuthorityList()) {
                authSet.add(new GrantedAuthorityImpl(authority.getPrefixedName()));
            }
        }
        return authSet;
    }

    @Autowired
    public void setDao(IDao<User> dao) {
        this.dao = dao;
    }

    public IDao<User> getDao() {
        return dao;
    }
}
