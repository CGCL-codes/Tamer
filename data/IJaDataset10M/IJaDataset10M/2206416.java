package gleam.executive.dao.ldap;

import gleam.executive.dao.RoleDao;
import gleam.executive.dao.UserDao;
import gleam.executive.model.Role;
import gleam.executive.model.User;
import org.acegisecurity.ldap.LdapUtils;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.ldap.support.DirContextAdapter;
import org.springframework.ldap.support.DistinguishedName;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.Filter;
import org.springframework.ldap.support.filter.NotFilter;
import javax.naming.Name;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

/**
 * @author mraible
 */
public class UserDaoLdap extends LdapDaoSupport implements UserDao, UserDetailsService {

    private static final Log log = LogFactory.getLog(UserDaoLdap.class);

    private JdbcTemplate jdbcTemplate;

    private String userTableName;

    private DataFieldMaxValueIncrementer incrementer;

    private UserContextMapper userContextMapper;

    private RoleDao roleDao;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setUserTableName(String userTableName) {
        this.userTableName = userTableName;
    }

    public void setUserContextMapper(UserContextMapper userContextMapper) {
        this.userContextMapper = userContextMapper;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void create(User user) {
        user.setId(incrementer.nextLongValue());
        jdbcTemplate.update("insert into " + userTableName + " (id, username, version) values (?, ?, ?)", new Object[] { user.getId(), user.getUsername(), 1 });
        user.setVersion(1);
        DirContextAdapter context = new DirContextAdapter();
        mapToContext(user, context);
        ldapTemplate.bind(buildDn(user), context, null);
        for (Role role : user.getRoles()) {
            Role r = roleDao.getRoleByName(role.getName());
            String[] members = new String[r.getMembers().length + 1];
            for (int i = 0; i < r.getMembers().length; i++) {
                members[i] = r.getMembers()[i];
            }
            members[r.getMembers().length] = MessageFormat.format("uid={0},ou=users,ou=system", user.getUsername());
            r.setMembers(members);
            roleDao.saveRole(r);
        }
    }

    public void update(User user) {
        jdbcTemplate.update("update " + userTableName + " set username=?, version=? where id=?", new Object[] { user.getUsername(), user.getVersion(), user.getId() });
        Name dn = buildDn(user);
        DirContextAdapter context = (DirContextAdapter) ldapTemplate.lookup(dn);
        mapToContext(user, context);
        ldapTemplate.modifyAttributes(dn, context.getModificationItems());
        for (Role role : user.getRoles()) {
            Role r = roleDao.getRoleByName(role.getName());
            List<String> members = Arrays.asList(r.getMembers());
            List<String> modifiedMembers = new ArrayList<String>(members);
            String userDn = MessageFormat.format("uid={0},ou=users,ou=system", user.getUsername());
            if (!members.contains(userDn)) {
                modifiedMembers.add(userDn);
            }
            r.setMembers(modifiedMembers.toArray(new String[0]));
            roleDao.saveRole(r);
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading from LDAP...");
        Name dn = buildDn(new User(username));
        User user = (User) ldapTemplate.lookup(dn, userContextMapper);
        String sql = "select id from " + userTableName + " where username=?";
        Long id = (Long) jdbcTemplate.queryForObject(sql, new Object[] { username }, Long.class);
        user.setId(id);
        return user;
    }

    public List<User> getUsers() {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new NotFilter(new EqualsFilter("uid", "superadmin")));
        return ldapTemplate.search("ou=users", filter.encode(), userContextMapper);
    }

    public List<User> getAllUsers() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search("ou=users", filter.encode(), userContextMapper);
    }

    public List<String> getUserIds() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search("ou=users", filter.encode(), userContextMapper);
    }

    protected Name buildDn(User user) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "users");
        dn.add("uid", user.getUsername());
        return dn;
    }

    protected void mapToContext(User user, DirContextAdapter context) {
        context.setAttributeValues("objectclass", new String[] { "top", "person", "inetOrgPerson" });
        context.setAttributeValue("uid", user.getUsername());
        context.setAttributeValue("userPassword", LdapUtils.getUtf8Bytes(user.getPassword()));
        context.setAttributeValue("cn", user.getFirstName());
        context.setAttributeValue("sn", user.getLastName());
        context.setAttributeValue("displayName", user.getFullName());
        context.setAttributeValue("mail", user.getEmail());
        context.setAttributeValue("telephoneNumber", user.getPhoneNumber());
        context.setAttributeValue("passwordHint", user.getPasswordHint());
        context.setAttributeValue("version", String.valueOf(user.getVersion()));
        context.setAttributeValue("accountEnabled", String.valueOf(user.isEnabled()));
        context.setAttributeValue("accountExpired", String.valueOf(user.isAccountExpired()));
        context.setAttributeValue("accountLocked", String.valueOf(user.isAccountLocked()));
        context.setAttributeValue("credentialsExpired", String.valueOf(user.isCredentialsExpired()));
    }

    public User getUser(Long userId) {
        String sql = "select username from " + userTableName + " where id=?";
        String username = (String) jdbcTemplate.queryForObject(sql, new Object[] { userId }, String.class);
        User user = (User) loadUserByUsername(username);
        user.setId(userId);
        return user;
    }

    public void saveUser(User user) {
        if (user.getVersion() == null) {
            create(user);
        } else {
            update(user);
        }
    }

    public void removeUser(Long userId) {
        User user = getUser(userId);
        ldapTemplate.unbind(buildDn(user));
        for (Role role : user.getRoles()) {
            Role r = roleDao.getRoleByName(role.getName());
            List<String> members = new ArrayList<String>(r.getMembers().length);
            for (String member : r.getMembers()) {
                if (member.indexOf(user.getUsername()) == -1) {
                    members.add(member);
                }
            }
            r.setMembers(members.toArray(new String[0]));
        }
        jdbcTemplate.update("delete from " + userTableName + " where id=?", new Object[] { userId });
    }

    /**
     * @see gleam.executive.dao.UserDao#getUsersByRole(String)
     */
    public List getUsersWithRole(String roleName) {
        throw new NotImplementedException();
    }

    /**
     * @see gleam.executive.dao.UserDao#getUserProjects(String)
     */
    public List getUserProjects(Long userId) {
        throw new NotImplementedException();
    }

    public void enableUser(Long userId, boolean enabled) {
        throw new NotImplementedException();
    }
}
