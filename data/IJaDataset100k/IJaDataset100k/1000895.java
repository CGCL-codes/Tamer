package lebah.portal.db;

import java.util.Hashtable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserRole {

    public static final String ROOT = "root";

    public static final String ADMIN = "admin";

    public static final String USER = "user";

    public static final String TEACHER = "teacher";

    public static final String STUDENT = "student";

    public static final String ANON = "anon";

    public static String[] roles = { ANON, STUDENT, TEACHER, USER, ADMIN, ROOT };

    public static String[] roleDescription = { "Anonymous users (not registered or as guest to portal)", "Student", "Teacher", "Registered users", "Administrator users", "Root users" };

    public static Hashtable tbRoles = new Hashtable();

    static {
        for (int i = 0; i < roles.length; i++) {
            tbRoles.put(roles[i], roleDescription[i]);
        }
    }

    public static Hashtable getTbRoles() {
        return tbRoles;
    }
}
