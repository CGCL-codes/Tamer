package scouter.server.groups;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import scouter.server.ServletUtils;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;

/**
 * Manages all of the {@linkplain Group}s.
 * @author User
 */
public class GroupManager {

    private static final int MAX_AGE = 300000;

    public static final float MAX_AGE_MINUTES = MAX_AGE / 60000;

    public static final String visiblePattern = "[^<>\"']+";

    private static final MemcacheService cache = MemcacheServiceFactory.getMemcacheService("groups");

    private static final Map<String, Group> map = Collections.synchronizedMap(new HashMap<String, Group>());

    private static final Map<Group, Long> ages = Collections.synchronizedMap(new HashMap<Group, Long>());

    private GroupManager() {
    }

    public static Group getGroup(String name) throws GroupNotFoundException {
        Group r;
        Long time = System.currentTimeMillis();
        if ((r = map.get(name)) == null || ages.get(r) - time > MAX_AGE) {
            synchronized (cache) {
                r = (Group) cache.get(name);
            }
            if (r != null) {
                map.put(name, r);
                ages.put(r, time - MAX_AGE * 7 / 10);
            } else {
                String old = NamespaceManager.get();
                try {
                    NamespaceManager.set("groups");
                    r = new Group(ServletUtils.d.get(KeyFactory.createKey("Group", name)));
                    synchronized (cache) {
                        cache.put(name, r, Expiration.byDeltaMillis(MAX_AGE));
                    }
                    map.put(name, r);
                    ages.put(r, time);
                } catch (EntityNotFoundException e) {
                    throw new GroupNotFoundException(e, name);
                } finally {
                    NamespaceManager.set(old);
                }
            }
        }
        return r;
    }

    public static Set<Group> getAllGroups() {
        String old = NamespaceManager.get();
        try {
            NamespaceManager.set("groups");
            return getGroups(new Query("Group"));
        } finally {
            NamespaceManager.set(old);
        }
    }

    public static Set<Group> getGroups(User user) {
        String old = NamespaceManager.get();
        try {
            NamespaceManager.set("groups");
            Query q1 = new Query("Group"), q2 = new Query("Group");
            q1.addFilter("users", FilterOperator.EQUAL, user.getEmail());
            q2.addFilter("admins", FilterOperator.EQUAL, user.getEmail());
            return getGroups(q1, q2);
        } finally {
            NamespaceManager.set(old);
        }
    }

    private static Set<Group> getGroups(Query... qs) {
        Set<Group> r = new HashSet<Group>();
        Long time = System.currentTimeMillis();
        for (Query q : qs) {
            for (Entity c : ServletUtils.d.prepare(q).asIterable()) {
                Group g = new Group(c);
                map.put(g.getName(), g);
                ages.put(g, time);
                synchronized (cache) {
                    cache.put(g.getName(), g, Expiration.byDeltaMillis(MAX_AGE));
                }
                r.add(g);
            }
        }
        return r;
    }

    public static class BadInternalNameException extends Exception {

        private static final long serialVersionUID = 2317126269811633805L;

        private final String badName;

        public BadInternalNameException(String badName) {
            super("'" + badName + "' is not a legal group internal name.");
            this.badName = badName;
        }

        public String getBadName() {
            return badName;
        }
    }

    public static class BadNameException extends Exception {

        private static final long serialVersionUID = -8787764468991299730L;

        private final String badName;

        public BadNameException(String badName) {
            super("'" + badName + "' is not a legal group name.");
            this.badName = badName;
        }

        public String getBadName() {
            return badName;
        }
    }

    public static class DuplicateGroupException extends Exception {

        private static final long serialVersionUID = 7503106302497790459L;

        private final String name;

        public DuplicateGroupException(String badName) {
            super("There is already a group with the internal name '" + badName + "'.");
            this.name = badName;
        }

        public String getName() {
            return name;
        }
    }

    public static void createGroup(String name, String visible) throws BadInternalNameException, BadNameException, DuplicateGroupException {
        if (!name.matches("[A-Za-z0-9_]*")) {
            throw new BadInternalNameException(name);
        }
        if (!visible.matches(visiblePattern)) {
            throw new BadNameException(visible);
        }
        try {
            getGroup(name);
            throw new DuplicateGroupException(name);
        } catch (GroupNotFoundException ignore) {
            if (ServletUtils.d.prepare(new Query()).countEntities(FetchOptions.Builder.withDefaults()) == 0) {
                Group g = new Group(name, visible);
                g.addAdmin(ServletUtils.u.getCurrentUser());
                map.put(name, g);
                ages.put(g, System.currentTimeMillis());
                synchronized (cache) {
                    cache.put(name, g, Expiration.byDeltaMillis(MAX_AGE));
                }
                ServletUtils.d.put(g.getEntity());
            } else {
            }
        }
    }

    public static Group getGroupForEditing(Group group) throws GroupNotFoundException {
        String old = NamespaceManager.get();
        try {
            NamespaceManager.set("groups");
            Group r = new Group(ServletUtils.d.get(KeyFactory.createKey("Group", group.getName())));
            map.put(r.getName(), r);
            ages.put(r, System.currentTimeMillis());
            synchronized (cache) {
                cache.put(r.getName(), r, Expiration.byDeltaMillis(MAX_AGE));
            }
            return r;
        } catch (EntityNotFoundException e) {
            throw new GroupNotFoundException(e, group.getName());
        } finally {
            NamespaceManager.set(old);
        }
    }

    static void deleteGroup(Group group) {
        String old = NamespaceManager.get();
        try {
            NamespaceManager.set("groups");
            ServletUtils.d.delete(KeyFactory.createKey("Group", group.getName()));
        } finally {
            NamespaceManager.set(old);
        }
        cache.delete(group.getName());
        map.remove(group.getName());
        ages.remove(group);
    }
}
