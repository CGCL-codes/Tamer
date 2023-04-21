package org.exist.security.realm.openid;

import java.util.Collection;
import java.util.List;
import org.exist.EXistException;
import org.exist.config.Configuration;
import org.exist.config.ConfigurationException;
import org.exist.config.annotation.ConfigurationClass;
import org.exist.config.annotation.ConfigurationFieldAsAttribute;
import org.exist.security.AbstractRealm;
import org.exist.security.Account;
import org.exist.security.AuthenticationException;
import org.exist.security.Group;
import org.exist.security.PermissionDeniedException;
import org.exist.security.Subject;
import org.exist.security.internal.SecurityManagerImpl;

/**
 * OpenID realm.
 * 
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
@ConfigurationClass("realm")
public class OpenIDRealm extends AbstractRealm {

    public static OpenIDRealm instance = null;

    @ConfigurationFieldAsAttribute("id")
    public static String ID = "OpenID";

    @ConfigurationFieldAsAttribute("version")
    public static final String version = "1.0";

    public OpenIDRealm(SecurityManagerImpl sm, Configuration config) {
        super(sm, config);
        instance = this;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public List<String> findUsernamesWhereNameStarts(Subject invokingUser, String startsWith) {
        return null;
    }

    @Override
    public List<String> findUsernamesWhereUsernameStarts(Subject invokingUser, String startsWith) {
        return null;
    }

    @Override
    public List<String> findGroupnamesWhereGroupnameStarts(Subject invokingUser, String startsWith) {
        return null;
    }

    @Override
    public Collection<? extends String> findGroupnamesWhereGroupnameContains(Subject invokingUser, String fragment) {
        return null;
    }

    @Override
    public List<String> findUsernamesWhereNamePartStarts(Subject invokingUser, String startsWith) {
        return null;
    }

    @Override
    public List<String> findAllGroupNames(Subject invokingUser) {
        return null;
    }

    @Override
    public List<String> findAllGroupMembers(Subject invokingUser, String groupName) {
        return null;
    }

    @Override
    public Subject authenticate(String accountName, Object credentials) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean deleteAccount(Account account) throws PermissionDeniedException, EXistException, ConfigurationException {
        return false;
    }

    @Override
    public boolean updateGroup(Subject invokingUser, Group group) throws PermissionDeniedException, EXistException, ConfigurationException {
        return false;
    }

    @Override
    public boolean deleteGroup(Group group) throws PermissionDeniedException, EXistException, ConfigurationException {
        return false;
    }
}
