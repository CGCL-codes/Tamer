package com.osgix.common.security.springsecurity;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Sets;
import com.osgix.authorize.service.RoleManager;
import com.osgix.authorize.service.UserManager;
import com.osgix.common.pojo.Role;
import com.osgix.authorize.entity.User;

;

/**
 * 实现SpringSecurity的UserDetailsService接口,实现获取用户Detail信息的回调函数.
 * 
 * 扩展SpringSecurity的User类加入loginTime信息.
 */
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserManager userManager;

    private RoleManager roleManager;

    /**
	 * 获取用户Detail信息的回调函数.
	 */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userManager.getUserByAccount(username);
        List<Role> roles = (Role) roleManager.getRolesByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户" + username + " 不存在");
        }
        Set<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(user, roles);
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        OperatorDetails userDetails = new OperatorDetails(user.getLoginName(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);
        userDetails.setLoginTime(new Date());
        userDetails.setRoleList(roles);
        return userDetails;
    }

    /**
	 * 获得用户所有角色的权限.
	 */
    private Set<GrantedAuthority> obtainGrantedAuthorities(User user, List<Role> roles) {
        Set<GrantedAuthority> authSet = Sets.newHashSet();
        for (Role role : roles) {
            authSet.add(new GrantedAuthorityImpl("ROLE_" + role.getRoleName()));
        }
        return authSet;
    }

    @Resource
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Resource
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
