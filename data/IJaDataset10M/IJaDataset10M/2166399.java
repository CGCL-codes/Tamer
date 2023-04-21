package de.uni_leipzig.lots.server.services.impl;

import de.uni_leipzig.lots.common.exceptions.LoginException;
import de.uni_leipzig.lots.common.exceptions.NoSuchUserException;
import de.uni_leipzig.lots.common.objects.Role;
import de.uni_leipzig.lots.common.objects.User;
import de.uni_leipzig.lots.common.util.SetUtil;
import de.uni_leipzig.lots.server.persist.DatabaseRepository;
import de.uni_leipzig.lots.server.services.AclManager;
import de.uni_leipzig.lots.server.services.AuthenticationModule;
import de.uni_leipzig.lots.server.services.UserAdministration;
import de.uni_leipzig.lots.webfrontend.container.NormalUserContainer;
import de.uni_leipzig.lots.webfrontend.container.UserContainer;
import de.uni_leipzig.lots.webfrontend.utils.PasswordFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Kiel
 * @version $Id: DefaultAuthenticationModule.java,v 1.17 2007/10/23 06:29:28 mai99bxd Exp $
 */
public class DefaultAuthenticationModule implements AuthenticationModule {

    private static final Logger logger = Logger.getLogger(DefaultAuthenticationModule.class.getName());

    private UserAdministration userAdministration;

    private AclManager aclManager;

    private DatabaseRepository databaseRepository;

    private PasswordFactory passwordFactory;

    /**
     * Roles allowed to authenticate.
     */
    @NotNull
    private final Set<Role> allowedRoles;

    public DefaultAuthenticationModule() {
        allowedRoles = new HashSet<Role>(Arrays.asList(Role.values()));
    }

    @Required
    public void setUserAdministration(@NotNull UserAdministration userAdministration) {
        this.userAdministration = userAdministration;
    }

    @Required
    public void setAclManager(@NotNull AclManager aclManager) {
        this.aclManager = aclManager;
    }

    @Required
    public void setDatabaseRepository(@NotNull DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @Required
    public void setPasswordFactory(@NotNull PasswordFactory passwordFactory) {
        this.passwordFactory = passwordFactory;
    }

    @NotNull
    public UserContainer authenticate(@NotNull String username, @NotNull String password) throws LoginException {
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "authenticate", "username = " + username);
        }
        User user;
        try {
            user = userAdministration.loadByUsername(username);
        } catch (NoSuchUserException e) {
            throw new LoginException("LI003");
        }
        if (user.getRoles().contains(Role.anonymousGuest)) {
            throw new LoginException("LI004");
        }
        if (!passwordFactory.isEqual(user.getPassword(), password)) {
            throw new LoginException("LI003");
        }
        if (user.isSuspended()) {
            throw new LoginException("LI009", new Object[] { user.getUsername(), user.getSuspension() }, new Object[] { user.getSuspension() });
        }
        if (SetUtil.intersection(user.getRoles(), allowedRoles).isEmpty()) {
            throw new LoginException("LI010", user.getUsername());
        }
        return new NormalUserContainer(user);
    }

    @NotNull
    public UserContainer authenticateAnonymousGuest(@NotNull String username) throws LoginException {
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "authenticateAnonymousGuest", "username = " + username);
        }
        User user;
        try {
            user = userAdministration.loadByUsername(username);
        } catch (NoSuchUserException e) {
            throw new LoginException("LI003");
        }
        if (!user.getRoles().contains(Role.anonymousGuest)) {
            throw new LoginException("LI005");
        }
        if (user.getRoles().size() != 1) {
            throw new LoginException("LI005");
        }
        if (user.isSuspended()) {
            throw new LoginException("LI009", new Object[] { user.getUsername(), user.getSuspension() }, new Object[] { user.getSuspension() });
        }
        return new NormalUserContainer(user);
    }

    @NotNull
    public UserContainer authenticate(@NotNull String autoLoginId) throws LoginException {
        throw new LoginException("LI001");
    }

    public void enableAuthentication() {
        allowedRoles.addAll(Arrays.asList(Role.values()));
    }

    public void enableAuthentication(@NotNull Role role) {
        allowedRoles.add(role);
    }

    public void disableAuthentication() {
        allowedRoles.clear();
    }

    public void disableAuthentication(@NotNull Role role) {
        allowedRoles.remove(role);
    }
}
