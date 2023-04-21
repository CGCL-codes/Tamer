package ch.olsen.servicecontainer.internalservice.auth;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.olsen.products.util.Random;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.products.util.mail.EMail;
import ch.olsen.servicecontainer.commongwt.client.RegistrationExceptionException;
import ch.olsen.servicecontainer.commongwt.client.SessionException;
import ch.olsen.servicecontainer.commongwt.client.UserRoleAndService;
import ch.olsen.servicecontainer.domain.SCDomain;
import ch.olsen.servicecontainer.gwt.client.Login;
import ch.olsen.servicecontainer.internalservice.auth.AccessControlElement.Role;
import ch.olsen.servicecontainer.internalservice.persistence.PersistenceSession;
import ch.olsen.servicecontainer.naming.OsnURI;
import ch.olsen.servicecontainer.node.SCNode;
import ch.olsen.servicecontainer.service.Logging;
import ch.olsen.servicecontainer.service.PersistenceService;
import ch.olsen.servicecontainer.service.Service;
import ch.olsen.servicecontainer.service.ServicePostActivate;
import ch.olsen.servicecontainer.service.ServicePrePassivate;
import ch.olsen.servicecontainer.service.Timer;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

@Service
public class AuthService implements AuthInterface {

    AuthConfiguration cfg;

    SCNode parentNode;

    @Logging
    Logger log;

    @PersistenceService
    public PersistenceSession dbStorage;

    Map<String, UserRoleAndService> authChecksCache = new LinkedHashMap<String, UserRoleAndService>();

    SessionCache sessionCache = new SessionCache();

    private static final int MaxCacheSize = 1000;

    private static final int MaxSessionCacheSize = 1000;

    public AuthService(SCNode parentNode, AuthConfiguration cfg) {
        this.parentNode = parentNode;
        this.cfg = cfg;
    }

    @ServicePostActivate
    public void init() {
    }

    @ServicePrePassivate
    public void passivate() {
    }

    public String login(String user, String password) {
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(User.class);
            q.descend("userName").constrain(user);
            q.descend("password").constrain(password);
            ObjectSet<User> res = q.execute();
            if (res.hasNext()) {
                User u = res.next();
                String s = Random.getRandomString(32);
                long lastAccess = System.currentTimeMillis();
                db.set(new AuthSession(u, s, lastAccess));
                return s;
            }
        } finally {
            dbStorage.closeSession(db);
        }
        return null;
    }

    private AuthSession getSession(ObjectContainer db, String session) throws SessionException {
        if (session == null) throw new SessionException("Invalid session string");
        {
            AuthSession authSession = sessionCache.get(session);
            if (authSession != null) {
                authSession.lastAccess = System.currentTimeMillis();
                return authSession;
            }
        }
        Query q = db.query();
        q.constrain(AuthSession.class);
        q.descend("session").constrain(session);
        ObjectSet<AuthSession> os = q.execute();
        if (os.hasNext()) {
            AuthSession authSession = os.next();
            authSession.lastAccess = System.currentTimeMillis();
            db.set(authSession);
            sessionCache.put(session, authSession, db);
            return authSession;
        }
        return null;
    }

    private class SessionCache implements Serializable {

        private static final long serialVersionUID = 1L;

        Map<String, AuthSession> map = new LinkedHashMap<String, AuthSession>();

        public AuthSession get(String session) {
            return map.get(session);
        }

        public void put(String session, AuthSession as, ObjectContainer db) {
            if (map.size() > MaxSessionCacheSize) {
                Iterator<AuthSession> it = map.values().iterator();
                AuthSession toRemove = it.next();
                if (toRemove.dbId != -1) db.ext().bind(toRemove, toRemove.dbId);
                db.set(toRemove);
                it.remove();
            }
            map.put(session, as);
        }

        public void remove(String session) {
            map.remove(session);
        }
    }

    public User getUser(String session) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            AuthSession s = getSession(db, session);
            if (s == null) throw new SessionException("Session expired");
            Query q = db.query();
            q.constrain(User.class);
            q.descend("userId").constrain(s.userId);
            ObjectSet<User> res = q.execute();
            if (res.hasNext()) {
                return res.next();
            }
        } finally {
            dbStorage.closeSession(db);
        }
        throw new SessionException("Invalid session");
    }

    public void closeSession(String session) {
        sessionCache.remove(session);
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(AuthSession.class);
            q.descend("session").constrain(session);
            ObjectSet<AuthSession> os = q.execute();
            while (os.hasNext()) {
                AuthSession authSession = os.next();
                db.delete(authSession);
            }
        } finally {
            dbStorage.closeSession(db);
        }
    }

    private static class AuthSession {

        long dbId = -1;

        int userId;

        String session;

        long lastAccess;

        String onOwner;

        public AuthSession(User user, String session, long lastAccess) {
            this.userId = user.userId;
            this.session = session;
            this.lastAccess = lastAccess;
        }
    }

    @Timer(millis = 3600000)
    public void checkExpiredSessions() {
        long timeNow = new Date().getTime();
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(AuthSession.class);
            ObjectSet<AuthSession> os = q.execute();
            while (os.hasNext()) {
                AuthSession s = os.next();
                AuthSession inCache = sessionCache.get(s.session);
                if (inCache != null) {
                    if (timeNow - inCache.lastAccess > cfg.sessionTimeout.value() * 1000L * 60L) {
                        db.delete(s);
                    } else {
                        db.ext().bind(inCache, db.ext().getID(s));
                        db.set(inCache);
                    }
                } else {
                    if (timeNow - s.lastAccess > cfg.sessionTimeout.value() * 1000L * 60L) {
                        db.delete(s);
                    }
                }
            }
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public String forgotPassword(String userName) {
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(User.class);
            q.descend("userName").constrain(userName);
            ObjectSet<User> res = q.execute();
            if (res.hasNext()) {
                User u = res.next();
                String s = Random.getRandomString(8);
                u.password = AuthInterface.Encrtypt.enctrypt(s);
                db.set(u);
                try {
                    EMail.send(u.email, "noreply@olsen.ch", "Your new Password", "Hi " + u.fullName + "\nYour new password " + "for the Olsen service container is: " + s);
                } catch (Exception e) {
                    log.warn("Could not send email to user " + u.email, e);
                }
                return "The password has been reset. An email has been sent to you account";
            }
        } finally {
            dbStorage.closeSession(db);
        }
        return "Non existing user";
    }

    public void register(String userName, String password, String fullName, String email) throws RegistrationExceptionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(User.class);
            Constraint c = q.descend("userName").constrain(userName);
            q.descend("email").constrain(email).or(c);
            ObjectSet<User> found = q.execute();
            if (found.hasNext()) {
                throw new RegistrationExceptionException("The user is already " + "registered, please provide a different userName/email");
            }
            String activateId = Random.getRandomString(20);
            String link = parentNode.getRootDomain().getHttpService().getBaseURL() + "#" + Login.NAME + "/activateId=" + activateId;
            EMail.send(email, "noreply@olsen.ch", "Your new Account at the Olsen Service Container", "Hi " + fullName + "\nYou have requested an account on the " + "Olsen Service Container.\n " + "To activate your account please click on the " + "following link: \n" + link + "\n\n");
            PendingUser p = new PendingUser();
            p.fullName = fullName;
            p.activationString = activateId;
            p.email = email;
            p.password = AuthInterface.Encrtypt.enctrypt(password);
            p.userName = userName;
            p.requestDate = new Date(System.currentTimeMillis());
            db.set(p);
        } catch (RegistrationExceptionException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not register user: " + e.getMessage(), e);
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public String completeRegistration(String activateId) throws RegistrationExceptionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            PendingUser pu = new PendingUser();
            pu.activationString = activateId;
            ObjectSet<PendingUser> res = db.get(pu);
            if (res.hasNext()) {
                pu = res.next();
                db.delete(pu);
                Query q = db.query();
                q.constrain(User.class);
                Constraint c = q.descend("userName").constrain(pu.userName);
                q.descend("email").constrain(pu.email).or(c);
                ObjectSet<User> found = q.execute();
                if (found.hasNext()) {
                    throw new RegistrationExceptionException("The user is already " + "registered, please provide a different userName/email");
                }
                User user = new User();
                user.userName = pu.userName;
                user.fullName = pu.fullName;
                user.email = pu.email;
                user.password = pu.password;
                ObjectSet<AuthStatus> statQ = db.get(new AuthStatus());
                AuthStatus status;
                if (statQ.size() == 0) {
                    status = new AuthStatus();
                    user.userId = 1;
                    status.nextId = 2;
                } else if (statQ.size() == 1) {
                    status = statQ.next();
                    user.userId = status.nextId++;
                } else {
                    while (statQ.hasNext()) {
                        status = statQ.next();
                        if (user.userId < status.nextId) user.userId = status.nextId;
                        db.delete(status);
                    }
                    status = new AuthStatus();
                    status.nextId = user.userId + 1;
                }
                db.set(status);
                db.set(user);
                String s = Random.getRandomString(32);
                long lastAccess = new Date().getTime();
                AuthSession as = new AuthSession(user, s, lastAccess);
                db.set(as);
                sessionCache.put(s, as, db);
                return s;
            } else {
                throw new RegistrationExceptionException("Invalid activation code, maybe your request has expired.");
            }
        } catch (RegistrationExceptionException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not register user: " + e.getMessage(), e);
            return null;
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public String changePassword(String session, String newpwd) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            AuthSession s = getSession(db, session);
            if (s == null) throw new SessionException("Session expired");
            Query q = db.query();
            q.constrain(User.class);
            q.descend("userId").constrain(s.userId);
            ObjectSet<User> res = q.execute();
            if (res.hasNext()) {
                User u = res.next();
                u.password = newpwd;
                db.set(u);
                return "Password updated";
            }
        } finally {
            dbStorage.closeSession(db);
        }
        throw new SessionException("Invalid session");
    }

    public User getUser(int id) {
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(User.class);
            q.descend("userId").constrain(id);
            ObjectSet<User> res = q.execute();
            if (res.hasNext()) {
                return res.next();
            }
        } finally {
            dbStorage.closeSession(db);
        }
        return null;
    }

    public UserRoleAndService checkAccess(String session, String roleName, OsnURI uri, String objId) {
        UserRoleAndService prev;
        ObjectContainer db = dbStorage.openSession();
        UserRoleAndService ret;
        String cacheId = null;
        try {
            AuthSession s = getSession(db, session);
            if (s == null) return null;
            cacheId = buildCacheId(s.userId, roleName, uri, objId);
            synchronized (authChecksCache) {
                prev = authChecksCache.get(cacheId);
            }
            if (prev != null) return prev;
            ret = checkAccess_internal(s.userId, roleName, uri, objId, db);
        } catch (SessionException e) {
            ret = null;
        } finally {
            dbStorage.closeSession(db);
        }
        if (cacheId != null) {
            synchronized (authChecksCache) {
                while (authChecksCache.size() > MaxCacheSize) {
                    Iterator<UserRoleAndService> it = authChecksCache.values().iterator();
                    it.next();
                    it.remove();
                }
                authChecksCache.put(cacheId, ret);
            }
        }
        return ret;
    }

    public UserRoleAndService checkAccess(User user, String roleName, OsnURI uri, String objId) {
        UserRoleAndService prev;
        String cacheId = buildCacheId(user.userId, roleName, uri, objId);
        synchronized (authChecksCache) {
            prev = authChecksCache.get(cacheId);
        }
        if (prev != null) return prev;
        UserRoleAndService ret;
        ObjectContainer db = dbStorage.openSession();
        try {
            ret = checkAccess_internal(user.userId, roleName, uri, objId, db);
        } finally {
            dbStorage.closeSession(db);
        }
        synchronized (authChecksCache) {
            while (authChecksCache.size() > MaxCacheSize) {
                Iterator<UserRoleAndService> it = authChecksCache.values().iterator();
                it.next();
                it.remove();
            }
            authChecksCache.put(cacheId, ret);
        }
        return ret;
    }

    private String buildCacheId(int userId, String roleName, OsnURI uri, String objId) {
        return userId + "#" + roleName + "#" + uri.toString() + "#" + objId;
    }

    private UserRoleAndService buildFromName(String user, String role, String uri, String objId, String inheritedRight) {
        UserRoleAndService rs = new UserRoleAndService();
        rs.role = role;
        rs.service = uri.substring(6);
        rs.objId = objId;
        rs.userName = user;
        rs.inheritedRight = inheritedRight;
        return rs;
    }

    private UserRoleAndService checkAccess_internal(int userId, String roleName, OsnURI osnUri, String objId, ObjectContainer db) {
        SCDomain target = parentNode.lookup(osnUri);
        User user = getUser(userId);
        if (target != null && roleName.equals(AuthInterface.GUEST)) {
            if (target.isAnonymousAccess()) return buildFromName(user.userName, AuthInterface.GUEST, osnUri.toString(), objId, "osn://");
        }
        String uris[];
        int k;
        if (objId.length() > 0) {
            uris = new String[osnUri.getPathElements().length + 2];
            uris[0] = buildAccessId(osnUri, objId);
            k = 1;
        } else {
            uris = new String[osnUri.getPathElements().length + 1];
            k = 0;
        }
        for (int n = uris.length - (k + 1); n >= 0; n--) {
            String newuri = "osn://";
            for (int j = 0; j < n; j++) {
                newuri += osnUri.getPathElements()[j] + "/";
            }
            uris[k] = newuri + "#";
            k++;
        }
        Role roleRoot = null;
        if (target != null) roleRoot = target.getRoleHierarchy();
        int n = 0;
        for (String uri : uris) {
            Query q = db.query();
            q.constrain(AccessToken.class);
            Constraint c = q.descend("uid").constrain(userId);
            c = q.descend("uri").constrain(uri).and(c);
            ObjectSet<AccessToken> res = q.execute();
            while (res.hasNext()) {
                AccessToken at = res.next();
                if (at.roleName.equals(AuthInterface.SUPERADMIN)) return buildFromName(user.userName, AuthInterface.SUPERADMIN, osnUri.toString(), objId, n == 0 ? null : uri);
                if (at.roleName.equals(AuthInterface.OWNER) && !at.roleName.equals(AuthInterface.SUPERADMIN)) return buildFromName(user.userName, AuthInterface.OWNER, osnUri.toString(), objId, n == 0 ? null : uri);
                if (at.roleName.equals(AuthInterface.ADMIN) && !at.roleName.equals(AuthInterface.SUPERADMIN) && !at.roleName.equals(AuthInterface.OWNER)) return buildFromName(user.userName, AuthInterface.ADMIN, osnUri.toString(), objId, n == 0 ? null : uri);
                if (findRole(roleRoot, roleName, at.roleName, false)) return buildFromName(user.userName, roleName, osnUri.toString(), objId, n == 0 ? null : uri);
            }
            n++;
        }
        return null;
    }

    /**
	 * recursive function to traverse the role hierarchy and see if we
	 * find targetRole and checkRole related
	 * @param roleRoot
	 * @param targetRole
	 * @param checkRole
	 */
    private boolean findRole(Role r, String targetRole, String checkRole, boolean targetInPath) {
        if (!targetInPath) {
            if (targetRole.equals(AuthInterface.GUEST)) targetInPath = true; else if (r.name.equals(targetRole)) targetInPath = true;
        }
        if (r.name.equals(checkRole)) {
            return targetInPath;
        }
        for (Role r2 : r.parents) {
            if (findRole(r2, targetRole, checkRole, targetInPath)) return true;
        }
        return false;
    }

    private final String buildAccessId(OsnURI uri, String objId) {
        return uri.toString() + "#" + objId;
    }

    public void grantAccess(String session, String role, SCDomain domain, String objId) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            AuthSession s = getSession(db, session);
            AccessToken at = new AccessToken();
            at.uid = s.userId;
            at.roleName = role;
            at.uri = buildAccessId(domain.getOsnUri(), objId);
            ObjectSet<AccessToken> res = db.get(at);
            if (res.size() == 0) {
                db.set(at);
            } else if (res.size() == 1) {
                AccessToken ext = res.next();
                long id = db.ext().getID(ext);
                db.ext().bind(at, id);
                db.set(at);
            } else {
                while (res.hasNext()) db.delete(res.next());
                db.set(at);
            }
            String cacheId = buildCacheId(s.userId, role, domain.getOsnUri(), objId);
            while (authChecksCache.size() > MaxCacheSize) {
                Iterator<UserRoleAndService> it = authChecksCache.values().iterator();
                it.next();
                it.remove();
            }
            User user = getUser(s.userId);
            authChecksCache.put(cacheId, buildFromName(user.userName, role, domain.getOsnUri().toString(), objId, null));
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public static class AuthStatus {

        public int nextId;
    }

    /**
	 * get all the services where a user has rights, including children
	 */
    public UserRoleAndService[] getRolesAndServices(String session) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            AuthSession s = getSession(db, session);
            User user = getUser(session);
            Query q = db.query();
            q.constrain(AccessToken.class);
            q.descend("uid").constrain(s.userId);
            ObjectSet<AccessToken> res = q.execute();
            List<UserRoleAndService> ret = new LinkedList<UserRoleAndService>();
            while (res.hasNext()) {
                AccessToken at = res.next();
                UserRoleAndService rs = new UserRoleAndService();
                rs.userName = user.userName;
                rs.service = at.uri.substring(6, at.uri.indexOf("#"));
                rs.objId = at.uri.substring(at.uri.indexOf("#") + 1);
                rs.inheritedRight = null;
                translateRole(at.roleName, rs);
                ret.add(rs);
                Query q2 = db.query();
                q2.constrain(AccessToken.class);
                q2.descend("uri").constrain(rs.service).like();
                ObjectSet<AccessToken> res2 = q2.execute();
                while (res2.hasNext()) {
                    AccessToken at2 = res2.next();
                    if (at2.uri.equals(at.uri) && at2.roleName.equals(at.roleName)) continue;
                    UserRoleAndService rs2 = new UserRoleAndService();
                    rs2.service = at2.uri.substring(6, at2.uri.indexOf("#"));
                    rs2.objId = at2.uri.substring(at2.uri.indexOf("#") + 1);
                    rs2.role = rs.role;
                    rs2.isAdmin = rs.isAdmin;
                    rs2.isOwner = rs.isOwner;
                    rs2.isSuperAdmin = rs.isSuperAdmin;
                    rs2.inheritedRight = at2.uri;
                    ret.add(rs2);
                }
            }
            UserRoleAndService ret2[] = ret.toArray(new UserRoleAndService[0]);
            Arrays.sort(ret2);
            return ret2;
        } finally {
            dbStorage.closeSession(db);
        }
    }

    private void translateRole(String roleName, UserRoleAndService rs) {
        if (roleName.equals(AuthInterface.SUPERADMIN)) {
            rs.role = "Super Administrator";
            rs.isSuperAdmin = true;
            rs.isOwner = true;
            rs.isAdmin = true;
        } else if (roleName.equals(AuthInterface.OWNER)) {
            rs.role = "Owner";
            rs.isOwner = true;
            rs.isAdmin = true;
        } else if (roleName.equals(AuthInterface.ADMIN)) {
            rs.role = "Administrator";
            rs.isAdmin = true;
        } else {
            rs.role = roleName;
        }
    }

    /**
	 * gets all the user who have a role on this service
	 */
    public UserRoleAndService[] getRolesForService(String session, String service, String objId) throws SessionException {
        Map<Integer, String> lookedUpNames = new HashMap<Integer, String>();
        ObjectContainer db = dbStorage.openSession();
        try {
            OsnURI osnUri = new OsnURI(service);
            User user = getUser(session);
            lookedUpNames.put(user.userId, user.userName);
            String uri = service + "#" + objId;
            List<UserRoleAndService> ret = new LinkedList<UserRoleAndService>();
            boolean firstQuery = true;
            while (uri != null) {
                Query q = db.query();
                q.constrain(AccessToken.class);
                q.descend("uri").constrain(uri);
                ObjectSet<AccessToken> res = q.execute();
                while (res.hasNext()) {
                    AccessToken at = res.next();
                    UserRoleAndService rs = new UserRoleAndService();
                    rs.service = uri.substring(6, uri.indexOf("#"));
                    rs.objId = uri.substring(uri.indexOf("#") + 1);
                    translateRole(at.roleName, rs);
                    rs.userName = lookedUpNames.get(at.uid);
                    if (rs.userName == null) {
                        Query q2 = db.query();
                        q2.constrain(User.class);
                        q2.descend("userId").constrain(at.uid);
                        ObjectSet<User> res2 = q2.execute();
                        if (res2.hasNext()) {
                            User user2 = res2.next();
                            rs.userName = user2.userName;
                            lookedUpNames.put(user2.userId, user2.userName);
                        }
                    }
                    rs.hasRightOverSelected = firstQuery && checkAccess(session, AuthInterface.ADMIN, osnUri, objId) != null && checkAccess(session, at.roleName, osnUri, objId) != null;
                    if (!firstQuery) rs.inheritedRight = uri;
                    ret.add(rs);
                }
                if (uri.indexOf("#") > 0 && uri.indexOf("#") + 1 != uri.length()) {
                    uri = uri.substring(0, uri.indexOf("#"));
                } else {
                    if (uri.indexOf("#") > 0) uri = uri.substring(0, uri.indexOf("#"));
                    OsnURI oUri = new OsnURI(uri);
                    if (oUri.getPathElements().length == 0) uri = null; else {
                        uri = "osn://";
                        for (int n = 0; n < oUri.getPathElements().length - 1; n++) uri += oUri.getPathElements()[n] + "/";
                        uri += "#";
                    }
                }
                firstQuery = false;
            }
            UserRoleAndService ret2[] = ret.toArray(new UserRoleAndService[0]);
            Arrays.sort(ret2);
            return ret2;
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public void deleteToken(String session, UserRoleAndService rs) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        if (rs.role.equals("Super Administrator")) rs.role = AuthInterface.SUPERADMIN; else if (rs.role.equals("Owner")) rs.role = AuthInterface.OWNER; else if (rs.role.equals("Administrator")) rs.role = AuthInterface.ADMIN;
        try {
            OsnURI osnUri = new OsnURI("osn://" + rs.service);
            Query q = db.query();
            q.constrain(AccessToken.class);
            Constraint c = q.descend("uri").constrain("osn://" + rs.service + "#" + rs.objId);
            c = q.descend("roleName").constrain(rs.role).and(c);
            ObjectSet<AccessToken> res = q.execute();
            boolean found = false;
            while (res.hasNext()) {
                AccessToken at = res.next();
                User u = getUser(at.uid);
                if (u.userName.equals(rs.userName)) {
                    if (checkAccess(session, AuthInterface.ADMIN, osnUri, rs.objId) != null && checkAccess(session, at.roleName, osnUri, rs.objId) != null) {
                        db.delete(at);
                        found = true;
                    }
                }
            }
            if (!found) throw new SessionException("Access Token not found");
        } finally {
            dbStorage.closeSession(db);
        }
    }

    /**
	 * gets all the roles one user has on this service
	 */
    public String[] getAccessibleRoles(String session, String service, String objId, boolean inherit) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            AuthSession s = getSession(db, session);
            return getAccessibleRoles(s.userId, service, objId, inherit, db);
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public UserRoleAndService[] getAccessibleRoles(User user, String service, String objId, boolean inherit) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            String uri = service + "#" + objId;
            boolean first = true;
            List<UserRoleAndService> ret = new LinkedList<UserRoleAndService>();
            while (uri != null) {
                Query q = db.query();
                q.constrain(AccessToken.class);
                q.descend("uid").constrain(user.userId);
                q.descend("uri").constrain(uri);
                ObjectSet<AccessToken> res = q.execute();
                while (res.hasNext()) {
                    AccessToken at = res.next();
                    UserRoleAndService rs = new UserRoleAndService();
                    ret.add(rs);
                    if (at.roleName.equals(AuthInterface.SUPERADMIN)) {
                        rs.role = "Super Administrator";
                        rs.isSuperAdmin = true;
                        rs.isOwner = true;
                        rs.isAdmin = true;
                    } else if (at.roleName.equals(AuthInterface.OWNER)) {
                        rs.role = "Owner";
                        rs.isOwner = true;
                        rs.isAdmin = true;
                    } else if (at.roleName.equals(AuthInterface.ADMIN)) {
                        rs.role = "Administrator";
                        rs.isAdmin = true;
                    } else {
                        rs.role = at.roleName;
                    }
                    rs.service = uri.split("#")[0].substring(6);
                    rs.objId = objId;
                    rs.userName = user.userName;
                    if (!first) rs.inheritedRight = uri;
                }
                if (uri.indexOf("#") > 0 && uri.indexOf("#") + 1 != uri.length()) {
                    uri = uri.substring(0, uri.indexOf("#"));
                } else {
                    if (uri.indexOf("#") > 0) uri = uri.substring(0, uri.indexOf("#"));
                    OsnURI oUri = new OsnURI(uri);
                    if (oUri.getPathElements().length == 0) uri = null; else {
                        uri = "osn://";
                        for (int n = 0; n < oUri.getPathElements().length - 1; n++) uri += oUri.getPathElements()[n];
                        uri += "#";
                    }
                }
                first = false;
            }
            return ret.toArray(new UserRoleAndService[0]);
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public String[] getAccessibleRoles(int userId, String service, String objId, boolean inherit, ObjectContainer db) throws SessionException {
        SCDomain domain = parentNode.lookup(new OsnURI(service));
        Role root = null;
        if (domain != null) root = domain.getRoleHierarchy();
        Set<String> ret = new HashSet<String>();
        String uri = service + "#" + objId;
        while (uri != null) {
            Query q = db.query();
            q.constrain(AccessToken.class);
            q.descend("uid").constrain(userId);
            q.descend("uri").constrain(uri);
            ObjectSet<AccessToken> res = q.execute();
            while (res.hasNext()) {
                AccessToken at = res.next();
                boolean topLevel = false;
                if (at.roleName.equals(AuthInterface.SUPERADMIN)) {
                    ret.add("Super Administrator");
                    if (inherit) {
                        ret.add("Owner");
                        ret.add("Administrator");
                    }
                    topLevel = true;
                } else if (at.roleName.equals(AuthInterface.OWNER)) {
                    ret.add("Owner");
                    if (inherit) ret.add("Administrator");
                    topLevel = true;
                } else if (at.roleName.equals(AuthInterface.ADMIN)) {
                    ret.add("Administrator");
                    topLevel = true;
                }
                findBelowRoles(root, at.roleName, ret, topLevel, inherit);
            }
            if (uri.indexOf("#") > 0 && uri.indexOf("#") + 1 != uri.length()) {
                uri = uri.substring(0, uri.indexOf("#"));
            } else {
                if (uri.indexOf("#") > 0) uri = uri.substring(0, uri.indexOf("#"));
                OsnURI oUri = new OsnURI(uri);
                if (oUri.getPathElements().length == 0) uri = null; else {
                    uri = "osn://";
                    for (int n = 0; n < oUri.getPathElements().length - 1; n++) uri += oUri.getPathElements()[n];
                    uri += "#";
                }
            }
        }
        return ret.toArray(new String[0]);
    }

    /**
	 * 
	 * @param role
	 * @param roleName
	 * @param ret
	 * @param topLevel if true, we have to include everythiong
	 * @param inherit 
	 * @return
	 */
    private boolean findBelowRoles(Role role, String roleName, Set<String> ret, boolean topLevel, boolean inherit) {
        if (role == null) return false;
        if (topLevel && inherit) ret.add(role.name); else if (role.name.equals(roleName)) {
            ret.add(role.name);
            return true;
        }
        boolean found = false;
        for (Role child : role.parents) {
            if ((found = findBelowRoles(child, roleName, ret, topLevel, inherit)) && inherit) ret.add(role.name);
        }
        return found;
    }

    public void inviteUser(String session, String name, String email, String role, String service, String objId) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            User user = getUser(session);
            if (checkAccess(session, role, new OsnURI(service), objId) == null) throw new SessionException("You don't have enough rights");
            String invitationId = Random.getRandomString(40);
            String link = parentNode.getRootDomain().getHttpService().getBaseURL() + "#" + Login.NAME + "/invitationId=" + invitationId;
            EMail.send(email, "noreply@olsen.ch", user.fullName + " wants to share a resource with you", "Hi " + name + "\nThis is an automatically generated message from the " + "Olsen Service Container.\n" + "User " + user.userName + " has sent you an invitation to " + "share the resource " + service + " # " + objId + ".\n " + "To activate your access to the resource please click on the " + "following link: \n" + link + "\n\n");
            PendingInvitation pi = new PendingInvitation();
            pi.uri = service;
            pi.objId = objId;
            pi.role = role;
            pi.invitationId = invitationId;
            db.set(pi);
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public void activateAccess(String session, String id) throws SessionException {
        ObjectContainer db = dbStorage.openSession();
        try {
            Query q = db.query();
            q.constrain(PendingInvitation.class);
            q.descend("invitationId").constrain(id);
            ObjectSet<PendingInvitation> res = q.execute();
            if (res.size() == 0) throw new SessionException("Activation Id not found");
            while (res.hasNext()) {
                PendingInvitation pi = res.next();
                SCDomain domain = parentNode.lookup(new OsnURI(pi.uri));
                if (domain == null) {
                    throw new SessionException("The resource does not exist any long");
                }
                if (pi.role.equals("Super Administrator")) pi.role = AuthInterface.SUPERADMIN; else if (pi.role.equals("Owner")) pi.role = AuthInterface.OWNER; else if (pi.role.equals("Administrator")) pi.role = AuthInterface.ADMIN;
                grantAccess(session, pi.role, domain, pi.objId);
                db.delete(pi);
            }
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public void unregisterService(OsnURI osnUri) {
        ObjectContainer db = dbStorage.openSession();
        try {
            for (AccessToken at : db.query(new UriFilter(osnUri.toString()))) {
                db.delete(at);
            }
        } finally {
            dbStorage.closeSession(db);
        }
    }

    public static class UriFilter extends Predicate<AccessToken> {

        private static final long serialVersionUID = 1L;

        final String uri;

        public UriFilter(String uri) {
            this.uri = uri;
        }

        public boolean match(AccessToken arg0) {
            String s[] = arg0.uri.split("#");
            if (s.length >= 1 && s[0].equals(uri)) return true;
            return false;
        }
    }

    public String getOwnerSession(OsnURI osnUri) {
        long lastAccess = System.currentTimeMillis();
        ObjectContainer db = dbStorage.openSession();
        try {
            {
                Query q = db.query();
                q.constrain(AuthSession.class);
                q.descend("onOwner").constrain(osnUri.toString());
                ObjectSet<AuthSession> os = q.execute();
                if (os.hasNext()) {
                    AuthSession s = os.next();
                    if (checkAccess(s.session, AuthInterface.OWNER, osnUri, "") != null) {
                        s.lastAccess = lastAccess;
                        db.set(s);
                        return s.session;
                    }
                    db.delete(s);
                }
            }
            Query q = db.query();
            q.constrain(AccessToken.class);
            q.descend("uri").constrain(osnUri.toString() + "#");
            q.descend("roleName").constrain(AuthInterface.OWNER);
            ObjectSet<AccessToken> res = q.execute();
            if (res.hasNext()) {
                AccessToken at = res.next();
                User u = getUser(at.uid);
                String s = Random.getRandomString(32);
                AuthSession as = new AuthSession(u, s, lastAccess);
                as.onOwner = osnUri.toString();
                db.set(as);
                return s;
            }
        } finally {
            dbStorage.closeSession(db);
        }
        return null;
    }
}
