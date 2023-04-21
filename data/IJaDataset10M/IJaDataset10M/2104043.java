package onepoint.project.modules.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.naming.TimeLimitExceededException;
import onepoint.express.XValidator;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.persistence.OpBroker;
import onepoint.persistence.OpFilter;
import onepoint.persistence.OpQuery;
import onepoint.persistence.OpSiteObject;
import onepoint.persistence.OpTransaction;
import onepoint.project.OpProjectService.ServiceLocalizer;
import onepoint.project.OpProjectSession;
import onepoint.project.OpServiceBase;
import onepoint.project.gwt.server.OpGWTService;
import onepoint.project.gwt.server.login.OpGWTUserServiceImpl;
import onepoint.project.modules.ldap.OpLdapService;
import onepoint.project.modules.resource.OpResource;
import onepoint.project.modules.settings.OpSettings;
import onepoint.project.modules.settings.OpSettingsService;
import onepoint.project.modules.site_management.OpSiteManager;
import onepoint.project.util.OpCollectionCopyHelper;
import onepoint.project.util.OpCollectionSynchronizationHelper;
import onepoint.project.util.OpHashProvider;
import onepoint.resource.XLocale;
import onepoint.resource.XLocaleManager;
import onepoint.resource.XResourceCache;
import onepoint.service.server.XServiceException;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Service Implementation for Users and Groups.
 * This class is capable of:
 * Inserting, updating and deleting users and groups.
 * Signing on and off a user.
 * Assigning users and groups to groups.
 * Removing users and groups from groups.
 * Requesting users and groups by id.
 * Traversing users and groups via children relations.
 *
 * @author dfreis
 */
public class OpUserServiceImpl extends OpServiceBase {

    private static final XLog logger = XLogFactory.getLogger(OpUserServiceImpl.class);

    /**
    * The name of this service.
    */
    public static final String SERVICE_NAME = "UserService";

    /**
    * the map containing all error types.
    */
    public static final OpUserErrorMap ERROR_MAP = new OpUserErrorMap();

    /**
    * the user subject type used for simple type based filtering.
    */
    public static final int TYPE_USER = 1;

    /**
    * the group subject type used for simple type based filtering.
    */
    public static final int TYPE_GROUP = 2;

    /**
    * the all subjects type used to retrieve users and groups.
    */
    public static final int TYPE_ALL = TYPE_USER + TYPE_GROUP;

    private static final String USER_ASSIGNMENT = "select assignment from OpUserAssignment as assignment" + " where assignment.User.id = ? and assignment.Group.id = ?";

    private static final String USER_BY_NAME = "select user from OpUser user where user.Name = :name";

    /**
    * the ldap service to use for ldap identification, may be <code>null</code>.
    */
    private OpLdapService ldapService = null;

    private static final OpGWTUserServiceImpl gwtService = new OpGWTUserServiceImpl();

    /**
    * flag indicating that no update was done before, set to false after first update.
    */
    private boolean initialUpdate = true;

    /**
    * Default Constructor
    */
    public OpUserServiceImpl() {
        super(SERVICE_NAME);
        ldapService = getLDAPService();
    }

    /**
    * Assigns the given {@link OpUser user} to the given {@link Iterator groups}.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user    the user that is to be assigned to the groups.
    * @param groups  the groups to assign the user to.
    * @throws XServiceException {@link OpUserError#USER_NOT_FOUND}
    *                           if {@link OpUser user} is null.
    * @throws XServiceException {@link OpUserError#SUPER_GROUP_NOT_FOUND}
    *                           if {@link OpGroup group} is null.
    * @throws XServiceException {@link OpUserError#INSUFFICIENT_PRIVILEGES}
    *                           if user is not administrator.
    */
    public final void assign(final OpProjectSession session, final OpBroker broker, final OpUser user, final Iterator<OpGroup> groups) throws XServiceException {
        while (groups.hasNext()) {
            assign(session, broker, user, groups.next());
        }
    }

    /**
    * Assigns the given {@link Iterator users} to the given {@link OpGroup group}.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param users   the users that are to be assigned to a group.
    * @param group   the group to assign the users to.
    * @throws XServiceException {@link OpUserError#USER_NOT_FOUND}
    *                           if {@link OpUser user} is null.
    * @throws XServiceException {@link OpUserError#SUPER_GROUP_NOT_FOUND}
    *                           if {@link OpGroup group} is null.
    * @throws XServiceException {@link OpUserError#INSUFFICIENT_PRIVILEGES}
    *                           if user is not administrator.
    */
    public final void assign(final OpProjectSession session, final OpBroker broker, final Iterator<OpUser> users, final OpGroup group) throws XServiceException {
        while (users.hasNext()) {
            OpUser user = users.next();
            assign(session, broker, user, group);
        }
    }

    /**
    * Assigns the given {@link OpUser user} to the given {@link OpGroup group}.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user    the user that is to be assigned to a group.
    * @param group   the group to assign the user to.
    * @throws XServiceException {@link OpUserError#USER_NOT_FOUND}
    *                           if {@link OpUser user} is null.
    * @throws XServiceException {@link OpUserError#SUPER_GROUP_NOT_FOUND}
    *                           if {@link OpGroup group} is null.
    * @throws XServiceException {@link OpUserError#INSUFFICIENT_PRIVILEGES}
    *                           if user is not administrator.
    */
    public final void assign(final OpProjectSession session, final OpBroker broker, final OpUser user, final OpGroup group) throws XServiceException {
        if (user == null) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_NOT_FOUND));
        }
        if (group == null) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.SUPER_GROUP_NOT_FOUND));
        }
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        for (OpPermission permission : getAllOwnedPermissions(group)) {
            if (!user.isPermissionAllowed(permission.getAccessLevel())) {
                throw new XServiceException(session.newError(ERROR_MAP, OpUserError.PERMISSION_LEVEL_ERROR));
            }
        }
        OpQuery query = broker.newQuery(USER_ASSIGNMENT);
        query.setLong(0, user.getId());
        query.setLong(1, group.getId());
        Iterator result = broker.iterate(query);
        if (result.hasNext()) {
            return;
        }
        OpUserAssignment assignment = new OpUserAssignment();
        assignment.setUser(user);
        assignment.setGroup(group);
        broker.makePersistent(assignment);
    }

    /**
    * Gets all the permissions for the given group.
    * Goes recursively upwards collecting permissions from groups.
    *
    * @param group group object to gather the permissions for.
    * @return Set of permissions.
    */
    private Set<OpPermission> getAllOwnedPermissions(OpGroup group) {
        Set<OpPermission> groupPermissions = group.getOwnedPermissions();
        if (groupPermissions == null) {
            groupPermissions = new HashSet<OpPermission>();
        }
        Set<OpPermission> ownedPermissions = new HashSet<OpPermission>(groupPermissions);
        if (group.getSuperGroupAssignments() != null) {
            for (Object o : group.getSuperGroupAssignments()) {
                OpGroupAssignment assignment = (OpGroupAssignment) o;
                ownedPermissions.addAll(getAllOwnedPermissions(assignment.getSuperGroup()));
            }
        }
        return ownedPermissions;
    }

    /**
    * Returns an iterator over all groups that do not have a parent
    * group (= root groups).
    *
    * @param session the session of the user
    * @param broker  the broker to use.
    * @return an iterator over all root groups.
    */
    public final Iterator<OpGroup> getRootGroups(final OpProjectSession session, final OpBroker broker) {
        return null;
    }

    /**
    * Get all direct children {@link OpGroup}s and/or {@link OpUser}s of the given parent {@link OpGroup group}.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param group   the group to get the direct children.
    * @param type    type filter, one of TYPE_GROUP, TYPE_USER or TYPE_ALL.
    * @return an iterator over all children.
    * @throws XServiceException in case of whatever error.
    */
    public Iterator<OpSubject> getSubSubjects(OpProjectSession session, OpBroker broker, OpGroup group, int type) throws XServiceException {
        return (getSubSubjects(session, broker, group, type, null));
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param group   the group to get the direct children.
    * @param type    type filter, one of TYPE_GROUP, TYPE_USER or TYPE_ALL.
    * @param filter  a filter callback.
    * @return an iterator over all children.
    * @throws XServiceException in case of whatever error.
    */
    public Iterator<OpSubject> getSubSubjects(OpProjectSession session, OpBroker broker, OpGroup group, int type, OpFilter filter) throws XServiceException {
        LinkedList<OpSubject> matches = new LinkedList<OpSubject>();
        OpQuery query;
        if ((TYPE_GROUP & type) == TYPE_GROUP) {
            query = broker.newQuery("select SubGroup from OpGroupAssignment where SuperGroup.id = ?");
            query.setLong(0, group.getId());
            Iterator iter = broker.iterate(query);
            OpGroup child_group;
            while (iter.hasNext()) {
                child_group = (OpGroup) iter.next();
                if ((filter == null) || (filter.accept(child_group))) {
                    matches.add(child_group);
                }
            }
        }
        if ((TYPE_USER & type) == TYPE_USER) {
            query = broker.newQuery("select User.Name from OpUserAssignment where Group.id = ?");
            query.setLong(0, group.getId());
            Iterator iter = broker.iterate(query);
            OpUser child_user;
            while (iter.hasNext()) {
                child_user = (OpUser) iter.next();
                if ((filter == null) || (filter.accept(child_user))) {
                    matches.add(child_user);
                }
            }
        }
        return (matches.iterator());
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void insertUser(OpProjectSession session, OpBroker broker, OpUser user) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        if (user.getName() == null || user.getName().length() == 0) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOGIN_MISSING));
        }
        OpContact contact = user.getContact();
        if (!contact.isEmailValid()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.EMAIL_INCORRECT));
        }
        if ((!Boolean.valueOf(OpSettingsService.getService().getStringValue(session, OpSettings.ALLOW_EMPTY_PASSWORD))) && (user.passwordIsEmpty())) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.PASSWORD_MISSING));
        }
        if (!user.isLevelValid()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INVALID_USER_LEVEL));
        }
        try {
            broker.makePersistent(user);
            contact.setUser(user);
            broker.makePersistent(contact);
            broker.getConnection().flush();
        } catch (ConstraintViolationException exc) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOGIN_ALREADY_USED));
        }
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param group
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void insertGroup(OpProjectSession session, OpBroker broker, OpGroup group) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        if (group.getName() == null || group.getName().length() == 0) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NAME_MISSING));
        }
        try {
            broker.makePersistent(group);
            broker.getConnection().flush();
        } catch (ConstraintViolationException exc) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NAME_ALREADY_USED));
        }
    }

    /**
    * Returns the name of the algorithm used to identify the user.
    *
    * @param session  the session within any operation will be performed.
    * @param broker   the broker to perform any operation.
    * @param username the username to get the algorithm for identification for.
    * @return the encryption algorithm used to identify the given username.
    * @pre
    * @post
    */
    public String getHashAlgorithm(OpProjectSession session, OpBroker broker, String username) {
        if (username == null) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_UNKNOWN));
        }
        if (username.equals(OpUser.ADMINISTRATOR_NAME_ALIAS1) || username.equals(OpUser.ADMINISTRATOR_NAME_ALIAS2)) {
            username = OpUser.ADMINISTRATOR_NAME;
        }
        OpQuery query = broker.newQuery("select user from OpUser as user where user.Name = ?");
        query.setString(0, username);
        Iterator users = broker.iterate(query);
        OpUser user = null;
        if (users.hasNext()) {
            user = (OpUser) (users.next());
            logger.debug("### Found user for signOn: " + user.getName() + " (" + user.getDisplayName() + ")");
            if (ldapService != null && ldapService.isEnabled() && user.getSource() == OpUser.LDAP) {
                try {
                    return ldapService.getHashAlgorithm(session, broker, username);
                } catch (TimeLimitExceededException exc) {
                } catch (NamingException exc) {
                    logger.warn(exc);
                    throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_UNKNOWN));
                }
            }
            String pwd = user.getPassword();
            Pattern p = Pattern.compile("^\\{([^\\}]*)\\}.*$");
            Matcher m = p.matcher(pwd);
            if (m.matches()) {
                return (m.group(1));
            }
        } else {
            if (ldapService != null && ldapService.isEnabled()) {
                try {
                    return ldapService.getHashAlgorithm(session, broker, username);
                } catch (TimeLimitExceededException exc) {
                } catch (NamingException exc) {
                    logger.warn(exc);
                    throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_UNKNOWN));
                }
            }
        }
        return OpHashProvider.INTERNAL;
    }

    /**
    * identifies this session with the given username and password.
    *
    * @param session  the session within any operation will be performed.
    * @param username the username
    * @param password the password in encrypted form.
    * @return true if signOn went OK, false otherwise.
    * @throws XServiceException if username or password is not valid.
    * @pre session and broker must be valid
    * @post session is identified with the given username and password if signOn went OK.
    */
    public synchronized OpUser signOn(OpProjectSession session, String username, String password) throws XServiceException {
        return signOn(session, username, password, Boolean.FALSE);
    }

    /**
    * identifies this session with the given username and password.
    *
    * @param session  the session within any operation will be performed.
    * @param username the username
    * @param password the password in encrypted form.
    * @return true if signOn went OK, false otherwise.
    * @throws XServiceException if username or password is not valid.
    * @pre session and broker must be valid
    * @post session is identified with the given username and password if signOn went OK.
    */
    public synchronized OpUser signOn(OpProjectSession session, String username, String password, Boolean rememberMe) throws XServiceException {
        if (username == null) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_UNKNOWN));
        }
        if ((password != null) && (password.length() == 0)) {
            password = null;
        }
        if (username.equals(OpUser.ADMINISTRATOR_NAME_ALIAS1) || username.equals(OpUser.ADMINISTRATOR_NAME_ALIAS2)) {
            username = OpUser.ADMINISTRATOR_NAME;
        }
        boolean systemSite = OpSiteManager.isSystemSite(session.getSite());
        if (systemSite) {
            session.updateSite(OpSiteManager.getAdminSite());
        }
        OpBroker broker = session.newBroker();
        try {
            OpQuery query = broker.newQuery("select user from OpUser as user where user.Name = ?");
            query.setString(0, username);
            logger.debug("...before find: login = " + username + "; pwd " + password);
            Iterator users = broker.iterate(query);
            logger.debug("...after find");
            OpUser user = null;
            boolean active = true;
            if (users.hasNext()) {
                user = (OpUser) (users.next());
                logger.debug("### Found user for signOn: " + user.getName() + " (" + user.getDisplayName() + ")");
                if (!user.isActive()) {
                    user = null;
                    active = false;
                }
            }
            String hashedPassword = password;
            if ((user == null || !user.isSystemUsers()) && ldapService != null && ldapService.isEnabled()) {
                if (initialUpdate) {
                    initialUpdate = false;
                    try {
                        ldapService.initialUpdate(session);
                    } catch (RuntimeException exc) {
                        logger.warn(exc.getMessage(), exc);
                    }
                }
                String decryptedPassword = password;
                String[] values = OpUser.splitAlgorithmAndPassword(decryptedPassword);
                if (values[0] == null) {
                    decryptedPassword = ldapService.decryptEncryptedPassword(password);
                    values = OpUser.splitAlgorithmAndPassword(decryptedPassword);
                    hashedPassword = new OpHashProvider().calculateHash(decryptedPassword);
                }
                if ("PLAIN".equals(values[0])) {
                    decryptedPassword = values[1];
                    hashedPassword = new OpHashProvider().calculateHash(decryptedPassword);
                }
                if (active && (user == null || user.getSource() == OpUser.LDAP)) {
                    try {
                        OpProjectSession adminSession = new OpProjectSession(OpSiteManager.getAdminSite());
                        OpBroker adminBroker = adminSession.newBroker();
                        try {
                            if (!ldapService.signOn(adminSession, adminBroker, username, decryptedPassword)) {
                                logger.debug("==> ldap Passwords do not match, or user unknown: Access denied");
                                throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_OR_PASSWORD_UNKNOWN));
                            }
                            adminSession.authenticateUser(adminBroker, adminSession.administrator(adminBroker));
                            OpTransaction t = null;
                            try {
                                t = adminBroker.newTransaction();
                                if (user != null) {
                                    ldapService.updateUser(adminSession, adminBroker, user, hashedPassword);
                                } else {
                                    user = ldapService.addUser(adminSession, adminBroker, username, hashedPassword);
                                }
                                t.commit();
                            } finally {
                                if (t != null) {
                                    t.rollbackIfNecessary();
                                }
                            }
                            if (user == null) {
                                throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_UNKNOWN));
                            }
                        } finally {
                            adminBroker.closeAndEvict();
                            adminSession.close();
                        }
                        OpUser newUser = broker.getObject(OpUser.class, user.getId());
                        authenticateUser(session, broker, newUser);
                        OpSettingsService.getService().configureServerCalendar(session);
                        session.setVariable(OpUserService.REMEMBER_ME, rememberMe);
                        return newUser;
                    } catch (TimeLimitExceededException exc) {
                        if (user == null) {
                            logger.debug("ldap timeout for user logIn");
                            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_OR_PASSWORD_UNKNOWN));
                        }
                    } catch (NamingException exc) {
                        logger.warn(exc);
                        throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_OR_PASSWORD_UNKNOWN));
                    }
                }
            }
            if (user != null) {
                if (!user.validatePassword(password)) {
                    logger.debug("==> Passwords do not match: Access denied");
                    throw new XServiceException(session.newError(ERROR_MAP, OpUserError.PASSWORD_MISMATCH));
                }
                if (!user.getSite().isStatusOk()) {
                    throw new XServiceException(session.newError(ERROR_MAP, OpUserError.SITE_IS_INVALID));
                }
                authenticateUser(session, broker, user);
                OpSettingsService.getService().configureServerCalendar(session);
                session.setVariable(OpUserService.REMEMBER_ME, rememberMe);
                return user;
            }
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_OR_PASSWORD_UNKNOWN));
        } catch (XServiceException exc) {
            if (systemSite) {
                session.updateSite(OpSiteManager.getSystemSite());
            }
            throw exc;
        } finally {
            broker.close();
        }
    }

    /**
    * @param session
    * @param broker
    * @param user
    * @pre
    * @post
    */
    private void authenticateUser(OpProjectSession session, OpBroker broker, OpUser user) {
        session.authenticateUser(broker, user);
        updateUserLocale(session, user);
    }

    private void updateUserLocale(OpProjectSession session, OpUser user) {
        XLocale user_locale = null;
        if ((user != null) && (user.getPreferences() != null)) {
            Iterator preferences = user.getPreferences().iterator();
            OpPreference preference = null;
            while (preferences.hasNext()) {
                preference = (OpPreference) preferences.next();
                if (preference.getName().equals(OpPreference.LOCALE_ID)) {
                    user_locale = XLocaleManager.findLocale(preference.getValue());
                }
            }
        }
        if (user_locale == null) {
            logger.info("Cannot determine user locale. Using global locale");
        }
        session.setLocale(user_locale);
        session.setLocalizerParameters(OpSettingsService.getI18NParametersMap(session));
    }

    /**
    * signs of the currently signed on user.
    *
    * @param session the session within any operation will be performed.
    * @pre session and broker must be valid
    * @post none
    */
    public synchronized void signOff(OpProjectSession session) {
        session.clearSession();
        XResourceCache.clearCache();
        session.setLocale(null);
    }

    /**
    * Returns the ldap service.
    *
    * @return the ldap service.
    */
    public OpLdapService getLDAPService() {
        try {
            ldapService = (OpLdapService) Class.forName("onepoint.project.team.ldap.OpLdapServiceImpl").newInstance();
            ldapService.init();
            return ldapService;
        } catch (ClassNotFoundException exc) {
            logger.debug(exc);
        } catch (InstantiationException exc) {
            logger.debug(exc);
        } catch (IllegalAccessException exc) {
            logger.debug(exc);
        } catch (NamingException exc) {
            logger.debug(exc);
        } catch (NoClassDefFoundError exc) {
            logger.warn(exc.getMessage() + ", please make sure you run tomcat with >= jre 1.5");
            logger.debug(exc);
        } catch (Exception exc) {
            exc.printStackTrace();
            logger.debug(exc);
        }
        return null;
    }

    @Override
    public OpGWTService getGWTService() {
        return gwtService;
    }

    /**
    * Returns the username of the user that is currently signed on, or <code>null</code> if no user is currently signed on.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @return the currently signed on username or <code>null</code> if no user is currently signed on.
    * @pre session and broker must be valid
    * @post none
    */
    public OpUser signedOnAs(OpProjectSession session, OpBroker broker) {
        return broker.getObject(OpUser.class, session.getUserID());
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user
    * @param group
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void removeUserFromGroup(OpProjectSession session, OpBroker broker, OpUser user, OpGroup group) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        OpQuery userAssignmentQuery = broker.newQuery("delete from OpUserAssignment as assignment where assignment.User.id = ? and assignment.Group.id = ?");
        userAssignmentQuery.setLong(0, user.getId());
        userAssignmentQuery.setLong(1, group.getId());
        broker.execute(userAssignmentQuery);
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param group
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void updateGroup(OpProjectSession session, OpBroker broker, OpGroup group) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        String groupName = group.getName();
        if (groupName == null || groupName.length() == 0) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NAME_MISSING));
        }
        try {
            broker.getConnection().flush();
        } catch (ConstraintViolationException exc) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NAME_ALREADY_USED));
        }
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void updateUser(OpProjectSession session, OpBroker broker, OpUser user) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        String userName = user.getName();
        if (userName == null || userName.length() == 0) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOGIN_MISSING));
        }
        OpContact contact = user.getContact();
        if (!contact.isEmailValid()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.EMAIL_INCORRECT));
        }
        if (!Boolean.valueOf(OpSettingsService.getService().getStringValue(session, OpSettings.ALLOW_EMPTY_PASSWORD)).booleanValue()) {
            if (user.validatePassword(null)) {
                throw new XServiceException(session.newError(ERROR_MAP, OpUserError.PASSWORD_MISSING));
            }
        }
        if (!user.isLevelValid()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INVALID_USER_LEVEL));
        }
        try {
            broker.getConnection().flush();
        } catch (ConstraintViolationException exc) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOGIN_ALREADY_USED));
        }
    }

    /**
    * Returns the OnePoint user for the given id.
    *
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param id      the id of the user to get.
    * @return the OnePoint user for the given id.
    */
    public OpUser getUserById(OpProjectSession session, OpBroker broker, long id) {
        OpUser user = (broker.getObject(OpUser.class, id));
        if (user != null) {
            return user;
        }
        throw new XServiceException(session.newError(ERROR_MAP, OpUserError.USER_NOT_FOUND));
    }

    /**
    * Assigns the given group to the given superGroup.
    *
    * @param session    the session within any operation will be performed.
    * @param broker     the broker to perform any operation.
    * @param group      the group to assign to its super group.
    * @param superGroup the super group to assign the group to.
    * @throws XServiceException in case of whatever error.
    */
    public void assign(OpProjectSession session, OpBroker broker, OpGroup group, OpGroup superGroup) throws XServiceException {
        if (superGroup == null) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.SUPER_GROUP_NOT_FOUND));
        }
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        if (!isAssignable(session, broker, group, superGroup)) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOOP_ASSIGNMENT));
        }
        for (OpPermission permission : getAllOwnedPermissions(superGroup)) {
            if (group.getUserAssignments() == null) {
                continue;
            }
            for (OpUserAssignment userAssignement : group.getUserAssignments()) {
                OpUser user = userAssignement.getUser();
                if (!user.isPermissionAllowed(permission.getAccessLevel())) {
                    throw new XServiceException(session.newError(ERROR_MAP, OpUserError.PERMISSION_LEVEL_ERROR));
                }
            }
        }
        OpGroupAssignment assignment = new OpGroupAssignment();
        assignment.setSubGroup(group);
        assignment.setSuperGroup(superGroup);
        broker.makePersistent(assignment);
    }

    /**
    * @param session     the session within any operation will be performed.
    * @param broker      the broker to perform any operation.
    * @param group
    * @param super_group
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void removeGroupFromGroup(OpProjectSession session, OpBroker broker, OpGroup group, OpGroup super_group) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        OpQuery groupAssignmentQuery = broker.newQuery("delete from OpGroupAssignment as assignment where assignment.SubGroup.id = ? and assignment.SuperGroup.id = ?");
        groupAssignmentQuery.setLong(0, group.getId());
        groupAssignmentQuery.setLong(1, super_group.getId());
        broker.execute(groupAssignmentQuery);
    }

    /**
    * @param session      the session within any operation will be performed.
    * @param broker       the broker to perform any operation.
    * @param group
    * @param super_groups
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void assign(OpProjectSession session, OpBroker broker, OpGroup group, Iterator<OpGroup> super_groups) throws XServiceException {
        while (super_groups.hasNext()) {
            assign(session, broker, group, super_groups.next());
        }
    }

    /**
    * @param session     the session within any operation will be performed.
    * @param broker      the broker to perform any operation.
    * @param group
    * @param super_group
    * @return true if the given group may be assigned to the given superGroup, false otherwise.
    */
    public boolean isAssignable(OpProjectSession session, OpBroker broker, OpGroup group, OpGroup super_group) {
        Vector<OpGroup> super_groups = new Vector<OpGroup>();
        super_groups.add(super_group);
        return (isAssignable(session, broker, group, super_groups.iterator()));
    }

    /**
    * @param session      the session within any operation will be performed.
    * @param broker       the broker to perform any operation.
    * @param group
    * @param super_groups
    * @return true if the given group may be assigned to all the given super groups, false otherwise.
    * @pre session and broker must be valid
    * @post
    */
    public boolean isAssignable(OpProjectSession session, OpBroker broker, OpGroup group, Iterator<OpGroup> super_groups) {
        if ((group == null) || (super_groups == null)) {
            throw new IllegalArgumentException("params must not be null");
        }
        OpGroup current_super_group;
        while (super_groups.hasNext()) {
            current_super_group = super_groups.next();
            if (current_super_group == null) {
                return (false);
            }
            if (group.equals(current_super_group)) {
                return (false);
            }
            Set ass = current_super_group.getSuperGroupAssignments();
            if (ass != null) {
                Iterator iter = ass.iterator();
                OpGroupAssignment super_group_assignment;
                OpGroup super_super_group;
                Vector<OpGroup> super_super_groups = new Vector<OpGroup>();
                while (iter.hasNext()) {
                    super_group_assignment = (OpGroupAssignment) iter.next();
                    if (super_group_assignment == null) {
                        throw new IllegalArgumentException("super_group must not be null");
                    }
                    super_super_group = super_group_assignment.getSuperGroup();
                    super_super_groups.add(super_super_group);
                }
                if (!isAssignable(session, broker, group, super_super_groups.iterator())) {
                    return (false);
                }
            }
        }
        return (true);
    }

    public OpUser getUserByName(OpProjectSession session, OpBroker broker, String name) {
        OpQuery q = broker.newQuery(USER_BY_NAME);
        q.setString("name", name);
        return broker.uniqueResult(q, OpUser.class);
    }

    /**
    * @param session   the session within any operation will be performed.
    * @param broker    the broker to perform any operation.
    * @param id_string
    * @return the user for the given id_string.
    * @pre session and broker must be valid
    * @post
    */
    public OpUser getUserByIdString(OpProjectSession session, OpBroker broker, String id_string) {
        return ((OpUser) broker.getObject(id_string));
    }

    /**
    * @param session     the session within any operation will be performed.
    * @param broker      the broker to perform any operation.
    * @param assignments
    * @pre session and broker must be valid
    * @post
    */
    public void deleteUserAssignments(OpProjectSession session, OpBroker broker, Iterator<OpUserAssignment> assignments) {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        while (assignments.hasNext()) {
            broker.deleteObject(assignments.next());
        }
    }

    /**
    * @param session    the session within any operation will be performed.
    * @param broker     the broker to perform any operation.
    * @param assignment
    * @pre session and broker must be valid
    * @post
    */
    public void deleteUserAssignment(OpProjectSession session, OpBroker broker, OpUserAssignment assignment) {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        broker.deleteObject(assignment);
    }

    /**
    * @param session   the session within any operation will be performed.
    * @param broker    the broker to perform any operation.
    * @param id_string
    * @return the group for the given id_string.
    * @pre session and broker must be valid
    * @post
    */
    public OpGroup getGroupByIdString(OpProjectSession session, OpBroker broker, String id_string) {
        return ((OpGroup) broker.getObject(id_string));
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param id
    * @return the group for the given id.
    * @pre session and broker must be valid
    * @post
    */
    public OpGroup getGroupById(OpProjectSession session, OpBroker broker, long id) {
        return (broker.getObject(OpGroup.class, id));
    }

    /**
    * @param session    the session within any operation will be performed.
    * @param broker     the broker to perform any operation.
    * @param assignment
    * @pre session and broker must be valid
    * @post
    */
    public void deleteGroupAssignment(OpProjectSession session, OpBroker broker, OpGroupAssignment assignment) {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        broker.deleteObject(assignment);
    }

    /**
    * @param session     the session within any operation will be performed.
    * @param broker      the broker to perform any operation.
    * @param assignments
    * @pre session and broker must be valid
    * @post
    */
    public void deleteGroupAssignments(OpProjectSession session, OpBroker broker, Iterator<OpGroupAssignment> assignments) {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        while (assignments.hasNext()) {
            broker.deleteObject(assignments.next());
        }
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param user
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void deleteUser(OpProjectSession session, OpBroker broker, OpUser user) throws XServiceException {
        checkUser(session, broker, user);
        logger.info("deleting user " + user.getName());
        Set res = user.getResources();
        if (res != null) {
            for (Iterator iterator = res.iterator(); iterator.hasNext(); ) {
                OpResource resource = (OpResource) iterator.next();
                resource.setUser(null);
            }
            user.setResources(new HashSet<OpResource>());
        }
        broker.deleteObject(user);
    }

    public void checkUser(OpProjectSession session, OpBroker broker, OpUser user) throws XServiceException {
        if (session.getUserID() == user.getId()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.SESSION_USER));
        }
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        if (user.getControllingSheets() != null && !user.getControllingSheets().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.CONTROLLING_SHEETS_EXIST));
        }
        if (user.getOwnedLocks() != null && !user.getOwnedLocks().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.LOCKS_EXIST));
        }
        if (user.getWorkSlips() != null && !user.getWorkSlips().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.WORKSLIPS_EXIST));
        }
        if (user.getWorkRecords() != null && !user.getWorkRecords().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.WORKRECORDS_EXIST));
        }
        if (user.getCostRecords() != null && !user.getCostRecords().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.COSTRECORDS_EXIST));
        }
        if (user.getActivities() != null && !user.getActivities().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.ACTIVITIES_EXIST));
        }
        if (user.getDiscussionArticles() != null && !user.getDiscussionArticles().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.DISCUSSION_ARTICLES_EXIST));
        }
        if (user.getDocuments() != null && !user.getDocuments().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.DOCUMENTS_EXIST));
        }
        if (user.getDocumentNodes() != null && !user.getDocumentNodes().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.DOCUMENTS_EXIST));
        }
    }

    public void checkGroup(OpProjectSession session, OpBroker broker, OpGroup group) throws XServiceException {
        if (!session.userIsAdministrator()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INSUFFICIENT_PRIVILEGES));
        }
        if (group.getUserAssignments() != null && !group.getUserAssignments().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NOT_EMPTY));
        }
        if (group.getSubGroupAssignments() != null && !group.getSubGroupAssignments().isEmpty()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.GROUP_NOT_EMPTY));
        }
        OpGroup everyone = session.everyone(broker);
        if (everyone != null && group.getId() == everyone.getId()) {
            throw new XServiceException(session.newError(ERROR_MAP, OpUserError.EVERYONE_GROUP));
        }
    }

    /**
    * @param session the session within any operation will be performed.
    * @param broker  the broker to perform any operation.
    * @param group
    * @throws XServiceException
    * @pre session and broker must be valid
    * @post
    */
    public void deleteGroup(OpProjectSession session, OpBroker broker, OpGroup group) throws XServiceException {
        checkGroup(session, broker, group);
        broker.deleteObject(group);
    }

    public static Set<OpSubject> flattenSubject(OpSubject in) {
        Set<OpSubject> subjects = new HashSet<OpSubject>();
        flattenSubject(in, subjects);
        return subjects;
    }

    public static void flattenSubject(OpSubject in, Set<OpSubject> subjects) {
        if (subjects.add(in)) {
            if (in instanceof OpGroup) {
                OpGroup g = (OpGroup) in;
                if (g.getSubGroupAssignments() != null) {
                    for (OpGroupAssignment ga : g.getSubGroupAssignments()) {
                        flattenSubject(ga.getSubGroup(), subjects);
                    }
                }
                if (g.getUserAssignments() != null) {
                    for (OpUserAssignment ua : g.getUserAssignments()) {
                        flattenSubject(ua.getUser(), subjects);
                    }
                }
            }
        }
    }

    public static String userChoice(OpProjectSession session, OpUser user) {
        ServiceLocalizer localizer = new ServiceLocalizer(OpUserService.USER_OBJECTS_RESOURCE_MAP, session);
        return XValidator.choice(user.locator(), localizer.localizeString(user.getDisplayName(), null));
    }

    private static class PermissionCopyHelper extends OpCollectionCopyHelper<OpPermission, OpPermission> {

        private OpBroker broker = null;

        private OpPermissionable tgt = null;

        public PermissionCopyHelper(OpBroker broker, OpPermissionable tgt) {
            this.broker = broker;
            this.tgt = tgt;
        }

        @Override
        protected void deleteInstance(OpPermission del) {
            del.setObject(null);
            del.getSubject().removeOwnedPermission(del);
            broker.deleteObject(del);
        }

        @Override
        protected OpPermission newInstance(OpPermission src) {
            OpPermission np = new OpPermission();
            broker.makePersistent(np);
            src.getSubject().addOwnedPermission(np);
            tgt.addPermission(np);
            np.setAccessLevel(src.getAccessLevel());
            np.setSystemManaged(src.getSystemManaged());
            return np;
        }
    }

    private static class PermissionSynchronizatonHelper extends OpCollectionSynchronizationHelper<OpPermission, PermissionDescription> {

        private OpProjectSession session = null;

        private OpBroker broker = null;

        private Map<OpSubject, Set<OpSubject>> flattenedSubjectCache = null;

        private OpSubject administratorUser = null;

        public PermissionSynchronizatonHelper(OpProjectSession session, OpBroker broker) {
            this.session = session;
            this.broker = broker;
            this.flattenedSubjectCache = new HashMap<OpSubject, Set<OpSubject>>();
            this.administratorUser = session.administrator(broker);
        }

        @Override
        protected void cloneInstance(OpPermission tgt, PermissionDescription src, boolean isNew) throws onepoint.project.util.OpCollectionSynchronizationHelper.SynchronizationException {
            boolean promoteAdmin = !src.isSystemManaged() && tgt.getSubject() != null && administratorUser != null && tgt.getSubject().getId() == administratorUser.getId();
            tgt.setAccessLevel(promoteAdmin ? OpPermission.ADMINISTRATOR : src.getAccessLevel());
            tgt.setSystemManaged(src.isSystemManaged());
            OpSubject subject = tgt.getSubject();
            byte accessLevel = tgt.getAccessLevel();
            checkForMaxAccessLevel(subject, accessLevel);
        }

        private void checkForMaxAccessLevel(OpSubject subject, byte accessLevel) {
            Set<OpSubject> flattened = flattenedSubjectCache.get(subject);
            if (flattened == null) {
                flattened = flattenSubject(subject);
                flattenedSubjectCache.put(subject, flattened);
            }
            for (OpSubject s : flattened) {
                if (s == null) {
                    continue;
                }
                if (OpUser.class.isAssignableFrom(s.getClass())) {
                    OpUser u = (OpUser) s;
                    byte maxAccessLevel = OpPermission.ADMINISTRATOR;
                    switch(u.getLevel()) {
                        case OpUser.MANAGER_USER_LEVEL:
                            break;
                        case OpUser.CONTRIBUTOR_USER_LEVEL:
                            maxAccessLevel = OpPermission.CONTRIBUTOR;
                            break;
                        case OpUser.OBSERVER_USER_LEVEL:
                        case OpUser.OBSERVER_CUSTOMER_USER_LEVEL:
                            maxAccessLevel = OpPermission.OBSERVER;
                            break;
                        default:
                            maxAccessLevel = OpPermission.NO_ACCESS;
                    }
                    if (accessLevel > maxAccessLevel) {
                        throw new XServiceException(session.newError(ERROR_MAP, OpUserError.INVALID_USER_LEVEL));
                    }
                }
            }
        }

        @Override
        protected int corresponds(OpPermission tgt, PermissionDescription src) throws onepoint.project.util.OpCollectionSynchronizationHelper.SynchronizationException {
            long cmp = 0;
            cmp = cmp != 0 ? cmp : (getNullSafeObjectIdFromPermission(tgt) - src.getObject().getId());
            cmp = cmp != 0 ? cmp : (tgt.getSubject().getId() - src.getSubject().getId());
            cmp = cmp != 0 ? cmp : (boolToNum(tgt.getSystemManaged()) - boolToNum(src.isSystemManaged()));
            return Long.signum(cmp);
        }

        private long boolToNum(boolean b) {
            return b ? 1 : 0;
        }

        @Override
        protected void deleteInstance(OpPermission del) {
            if (del.getSubject() != null) {
                del.getSubject().removeOwnedPermission(del);
            }
            if (del.getObject() != null) {
                del.getObject().removePermission(del);
            }
            broker.deleteObject(del);
        }

        @Override
        protected OpPermission newInstance(PermissionDescription src) {
            OpPermission permission = new OpPermission();
            src.getObject().addPermission(permission);
            src.getSubject().addOwnedPermission(permission);
            broker.makePersistent(permission);
            return permission;
        }

        @Override
        protected int sourceOrder(PermissionDescription srcA, PermissionDescription srcB) {
            long cmp = 0;
            cmp = cmp != 0 ? cmp : (srcA.getObject().getId() - srcB.getObject().getId());
            cmp = cmp != 0 ? cmp : (srcA.getSubject().getId() - srcB.getSubject().getId());
            cmp = cmp != 0 ? cmp : (boolToNum(srcA.isSystemManaged()) - boolToNum(srcB.isSystemManaged()));
            return Long.signum(cmp);
        }

        @Override
        protected int targetOrder(OpPermission tgtA, OpPermission tgtB) {
            long cmp = 0;
            cmp = cmp != 0 ? cmp : (getNullSafeObjectIdFromPermission(tgtA) - getNullSafeObjectIdFromPermission(tgtB));
            cmp = cmp != 0 ? cmp : (tgtA.getSubject().getId() - tgtB.getSubject().getId());
            cmp = cmp != 0 ? cmp : (boolToNum(tgtA.getSystemManaged()) - boolToNum(tgtB.getSystemManaged()));
            cmp = cmp != 0 ? cmp : tgtA.getId() - tgtB.getId();
            return Long.signum(cmp);
        }

        private long getNullSafeObjectIdFromPermission(OpPermission tgt) {
            return tgt.getObject() != null ? tgt.getObject().getId() : tgt.getId();
        }
    }

    public static class PermissionDescription {

        private OpPermissionable object = null;

        private OpSubject subject = null;

        private byte accessLevel = OpPermission.OBSERVER;

        private boolean systemManaged = false;

        public PermissionDescription(OpPermissionable object, OpSubject subject, byte accessLevel, boolean systemManaged) {
            this.object = object;
            this.subject = subject;
            this.accessLevel = accessLevel;
            this.systemManaged = systemManaged;
        }

        public OpSubject getSubject() {
            return subject;
        }

        public OpPermissionable getObject() {
            return object;
        }

        public byte getAccessLevel() {
            return accessLevel;
        }

        public boolean isSystemManaged() {
            return systemManaged;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PermissionDescription) {
                PermissionDescription pd = (PermissionDescription) obj;
                return OpSiteObject.nullSafeEquals(object, pd.object) && OpSiteObject.nullSafeEquals(subject, pd.subject) && accessLevel == pd.accessLevel && systemManaged == pd.systemManaged;
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (object == null || subject == null) {
                return super.hashCode();
            }
            return object.hashCode() + subject.hashCode();
        }

        public void detach() {
        }

        public void attach(OpProjectSession session, OpBroker broker) {
            this.object = (OpPermissionable) broker.getObject(object.locator());
            this.subject = (OpSubject) broker.getObject(subject.locator());
        }

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[PD: S:");
            buffer.append(subject.getName());
            buffer.append(" O:");
            buffer.append(object.getId());
            buffer.append(" L:");
            buffer.append(accessLevel);
            buffer.append("]");
            return buffer.toString();
        }
    }

    public void synchronizePermissions(OpProjectSession session, OpBroker broker, Collection<OpPermission> dbPermissions, Collection<PermissionDescription> newPermissions) {
        PermissionSynchronizatonHelper sh = new PermissionSynchronizatonHelper(session, broker);
        sh.copy(dbPermissions, newPermissions);
    }

    public static void copyPermissions(OpProjectSession session, OpBroker broker, OpPermissionable tgt, OpPermissionable src) {
        PermissionCopyHelper pch = new PermissionCopyHelper(broker, tgt);
        pch.copy(tgt.getPermissions(), src.getPermissions());
    }

    public Set<OpPermission> getPermissionProductForSubjectsAndObjects(OpBroker broker, Collection<? extends OpSubject> subjects, Collection<? extends OpPermissionable> objects, Boolean systemManaged) {
        Set<OpPermission> existingPermissions = new HashSet<OpPermission>();
        if (subjects != null && !subjects.isEmpty() && objects != null && !objects.isEmpty()) {
            OpQuery permQ = broker.newQuery("" + "select p from OpPermission as p " + "where p.Object.id in (:objectIds) " + "and p.Subject.id in (:subjectIds) " + (systemManaged != null ? "and p.SystemManaged = :system" : ""));
            permQ.setCollection("objectIds", OpSiteObject.getIdsFromObjects(objects));
            permQ.setCollection("subjectIds", OpSiteObject.getIdsFromObjects(subjects));
            if (systemManaged != null) {
                permQ.setBoolean("system", systemManaged.booleanValue());
            }
            Iterator<OpPermission> pit = broker.iterate(permQ);
            while (pit.hasNext()) {
                existingPermissions.add(pit.next());
            }
        }
        return existingPermissions;
    }

    public static class PermissionableSubjectPair {

        private OpPermissionable permissionable = null;

        private OpSubject subject = null;

        public PermissionableSubjectPair(OpPermissionable p, OpSubject s) {
            this.permissionable = p;
            this.subject = s;
        }

        public OpPermissionable getPermissionable() {
            return permissionable;
        }

        public OpSubject getSubject() {
            return subject;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PermissionableSubjectPair) {
                PermissionableSubjectPair pair = (PermissionableSubjectPair) obj;
                return OpSiteObject.nullSafeEquals(permissionable, pair.permissionable) && OpSiteObject.nullSafeEquals(subject, pair.subject);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (permissionable != null && subject != null) ? (permissionable.hashCode() + subject.hashCode()) : super.hashCode();
        }
    }

    public Set<OpPermission> getPermissionsForObjectSubjectPairs(OpBroker broker, Collection<PermissionableSubjectPair> pairs, Boolean systemManaged) {
        Map<OpPermissionable, Set<OpSubject>> pnaMap = new HashMap<OpPermissionable, Set<OpSubject>>();
        Set<OpSubject> allSubjects = new HashSet<OpSubject>();
        for (PermissionableSubjectPair pair : pairs) {
            OpPermissionable obj = pair.getPermissionable();
            OpSubject sub = pair.getSubject();
            if (sub != null && obj != null) {
                Set<OpSubject> subjects = pnaMap.get(obj);
                if (subjects == null) {
                    subjects = new HashSet<OpSubject>();
                    pnaMap.put(obj, subjects);
                }
                subjects.add(sub);
                allSubjects.add(sub);
            }
        }
        Set<OpPermission> permissionsForPna = getPermissionProductForSubjectsAndObjects(broker, allSubjects, pnaMap.keySet(), systemManaged);
        Iterator<OpPermission> pit = permissionsForPna.iterator();
        while (pit.hasNext()) {
            OpPermission p = pit.next();
            Set<OpSubject> subjects = pnaMap.get(p.getObject());
            if (subjects == null || !subjects.contains(p.getSubject())) {
                pit.remove();
            }
        }
        return permissionsForPna;
    }
}
