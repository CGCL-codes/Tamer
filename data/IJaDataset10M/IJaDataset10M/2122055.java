package org.mbari.aosn.realm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.catalina.Container;
import org.apache.catalina.Group;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.apache.catalina.core.StandardHost;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JsonUserDatabase
 * Implemenaton of org.apache.catalina.UserDatabase
 * 
 * @author godin
 *
 */
public class JsonUserDatabase implements UserDatabase {

    Log log = LogFactory.getLog("org.mbari.aosn.realm.JsonUserDatabase");

    /**
	 * 
	 */
    public JsonUserDatabase(String name) {
        super();
        this.id = name + "_" + version;
    }

    public void setFilename(String jsonFilename) {
        this.jsonFilename = jsonFilename;
        jsonFile = new File(jsonFilename);
    }

    public String getFilename() {
        return jsonFilename;
    }

    public synchronized void jsonCheckModified() {
        if (jsonFile.lastModified() != lastModified) {
            lastModified = jsonFile.lastModified();
            StringBuffer buffer = new StringBuffer("{\n");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                String line;
                while (null != (line = reader.readLine())) {
                    buffer.append(line);
                    buffer.append("\n");
                }
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            buffer.append("}");
            try {
                json = new JSONObject(buffer.toString());
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            users.clear();
            JSONObject usersObject = JsonUtil.getJSONObject(json, "users");
            if (null != usersObject) {
                JSONArray usersNames = usersObject.names();
                for (int i = 0; i < usersNames.length(); ++i) {
                    String name = usersNames.getString(i);
                    User user = new JsonUser(this, name, usersObject.getJSONObject(name));
                    users.add(user);
                }
            }
            groups.clear();
            JSONObject groupsObject = JsonUtil.getJSONObject(json, "groups");
            if (groupsObject != null) {
                JSONArray groupsNames = groupsObject.names();
                for (int i = 0; i < groupsNames.length(); ++i) {
                    String name = groupsNames.getString(i);
                    Group group = new JsonGroup(this, name, groupsObject.getJSONObject(name));
                    groups.add(group);
                }
            }
            roles.clear();
            JSONObject rolesObject = JsonUtil.getJSONObject(json, "roles");
            if (null != rolesObject) {
                JSONArray rolesNames = rolesObject.names();
                for (int i = 0; i < rolesNames.length(); ++i) {
                    String name = rolesNames.getString(i);
                    Role role = new JsonRole(this, name, rolesObject.getJSONObject(name));
                    roles.add(role);
                }
            }
        }
    }

    /**
	 * Return the set of Groups defined in this user database.
	 * Do not check if the user database has changed along the way.
	 * @see org.apache.catalina.UserDatabase#getGroups()
	 */
    public Iterator<Group> getGroups() {
        jsonCheckModified();
        return groups.iterator();
    }

    /**
	 * Return the unique global identifier of this user database.
	 * @see org.apache.catalina.UserDatabase#getId()
	 */
    public String getId() {
        return id;
    }

    /**
	 * Return the set of Roles defined in this user database.
	 * @see org.apache.catalina.UserDatabase#getRoles()
	 */
    public Iterator<Role> getRoles() {
        jsonCheckModified();
        return roles.iterator();
    }

    /**
	 * Return the set of Users defined in this user database.
	 * @see org.apache.catalina.UserDatabase#getUsers()
	 */
    public Iterator<User> getUsers() {
        jsonCheckModified();
        return users.iterator();
    }

    /**
	 * Finalize access to this user database.
	 * @see org.apache.catalina.UserDatabase#close()
	 */
    public void close() throws Exception {
    }

    /**
	 * Create and return a new Group defined in this user database.
	 * @see org.apache.catalina.UserDatabase#createGroup(java.lang.String, java.lang.String)
	 */
    public Group createGroup(String groupname, String description) {
        return null;
    }

    /**
	 * Create and return a new Role defined in this user database.
	 * @see org.apache.catalina.UserDatabase#createRole(java.lang.String, java.lang.String)
	 */
    public Role createRole(String rolename, String description) {
        return null;
    }

    /**
	 * Create and return a new User defined in this user database.
	 * @see org.apache.catalina.UserDatabase#createUser(java.lang.String, java.lang.String, java.lang.String)
	 */
    public User createUser(String username, String password, String fullName) {
        return null;
    }

    /**
	 * Return the Group with the specified group name, if any; otherwise return null.
	 * @see org.apache.catalina.UserDatabase#findGroup(java.lang.String)
	 */
    public Group findGroup(String groupname) {
        Iterator<Group> groupIterator = getGroups();
        while (groupIterator.hasNext()) {
            Group group = groupIterator.next();
            if (group.getGroupname().equals(groupname)) {
                return group;
            }
        }
        return null;
    }

    /**
	 * Return the Role with the specified role name, if any; otherwise return null.
	 * @see org.apache.catalina.UserDatabase#findRole(java.lang.String)
	 */
    public Role findRole(String rolename) {
        Iterator<Role> roleIterator = getRoles();
        while (roleIterator.hasNext()) {
            Role role = roleIterator.next();
            if (role.getRolename().equals(rolename)) {
                return role;
            }
        }
        return null;
    }

    /**
	 * Return the User with the specified user name, if any; otherwise return null.
	 * @see org.apache.catalina.UserDatabase#findUser(java.lang.String)
	 */
    public User findUser(String username) {
        Iterator<User> userIterator = getUsers();
        while (userIterator.hasNext()) {
            User user = userIterator.next();
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
	 * Initialize access to this user database.
	 * @see org.apache.catalina.UserDatabase#open()
	 */
    public void open() throws Exception {
    }

    /**
	 * Remove the specified Group from this user database.
	 * @see org.apache.catalina.UserDatabase#removeGroup(org.apache.catalina.Group)
	 */
    public void removeGroup(Group group) {
    }

    /**
	 * Remove the specified Role from this user database.
	 * @see org.apache.catalina.UserDatabase#removeRole(org.apache.catalina.Role)
	 */
    public void removeRole(Role role) {
    }

    /**
	 * Remove the specified User from this user database.
	 * @see org.apache.catalina.UserDatabase#removeUser(org.apache.catalina.User)
	 */
    public void removeUser(User user) {
    }

    /**
	 * Save any updated information to the persistent storage location for this user database.
	 * @see org.apache.catalina.UserDatabase#save()
	 */
    public void save() throws Exception {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String pathname = "test-users.json";
        JsonUserDatabase userDatabase = new JsonUserDatabase("JsonUserDatabase");
        userDatabase.setFilename(pathname);
        String[] usernames = { "testUser1", "testUser2", "testUser3", "testUser4", "testUser5" };
        User[] users = new User[usernames.length];
        String[] passwords = { "password1", "password2", "password3", "password4", "password5" };
        for (int i = 0; i < usernames.length; ++i) {
            users[i] = userDatabase.findUser(usernames[i]);
        }
        Group testGroup2 = userDatabase.findGroup("testGroup2");
        System.out.println("User:" + usernames[1] + " is in group " + testGroup2.getName() + "? " + users[1].isInGroup(testGroup2));
        System.out.println("User:" + usernames[2] + " is in group " + testGroup2.getName() + "? " + users[2].isInGroup(testGroup2));
        Group testGroup23 = userDatabase.findGroup("testGroup23");
        System.out.println("User:" + usernames[1] + " is in group " + testGroup23.getName() + "? " + users[1].isInGroup(testGroup23));
        System.out.println("User:" + usernames[2] + " is in group " + testGroup23.getName() + "? " + users[2].isInGroup(testGroup23));
        System.out.println("User:" + usernames[3] + " is in group " + testGroup23.getName() + "? " + users[3].isInGroup(testGroup23));
        Role testRole12 = userDatabase.findRole("testRole12");
        System.out.println("User:" + usernames[0] + " is in role " + testRole12.getName() + "? " + users[0].isInRole(testRole12));
        System.out.println("User:" + usernames[1] + " is in role " + testRole12.getName() + "? " + users[1].isInRole(testRole12));
        System.out.println("User:" + usernames[2] + " is in role " + testRole12.getName() + "? " + users[2].isInRole(testRole12));
        Role testRole13 = userDatabase.findRole("testRole13");
        System.out.println("User:" + usernames[0] + " is in role " + testRole13.getName() + "? " + users[0].isInRole(testRole13));
        System.out.println("User:" + usernames[2] + " is in role " + testRole13.getName() + "? " + users[2].isInRole(testRole13));
        System.out.println("User:" + usernames[3] + " is in role " + testRole13.getName() + "? " + users[3].isInRole(testRole13));
        Role testRole23 = userDatabase.findRole("testRole23");
        System.out.println("User:" + usernames[1] + " is in role " + testRole23.getName() + "? " + users[1].isInRole(testRole23));
        System.out.println("User:" + usernames[2] + " is in role " + testRole23.getName() + "? " + users[2].isInRole(testRole23));
        System.out.println("User:" + usernames[3] + " is in role " + testRole23.getName() + "? " + users[3].isInRole(testRole23));
        JsonUserDatabaseRealm realm = new JsonUserDatabaseRealm(userDatabase);
        Container container = new StandardHost();
        realm.setContainer(container);
        try {
            realm.start();
        } catch (LifecycleException e) {
            System.out.println(e);
        }
        for (int i = 0; i < usernames.length; ++i) {
            Principal principal = realm.authenticate(usernames[i], passwords[i]);
            System.out.println("Authenticated Principal: " + (null == principal ? "null" : principal.getName()));
            if (principal != null) {
                Iterator<Group> groups = ((JsonUser) principal).getGroups();
                while (groups.hasNext()) {
                    System.out.print("  group: " + groups.next().getGroupname());
                }
                Iterator<Role> roles = ((JsonUser) principal).getRoles();
                while (roles.hasNext()) {
                    System.out.print("  role: " + roles.next().getRolename());
                }
                System.out.println();
            }
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuffer out = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i]);
            out.append("0".substring(0, hex.length() > 1 ? 0 : 1) + hex.substring(hex.length() > 2 ? 6 : 0));
        }
        return out.toString();
    }

    private String jsonFilename;

    private File jsonFile;

    private JSONObject json;

    private String version = "0.01";

    private String id;

    private List<User> users = new ArrayList<User>();

    private List<Group> groups = new ArrayList<Group>();

    private List<Role> roles = new ArrayList<Role>();

    private long lastModified = 0;
}
