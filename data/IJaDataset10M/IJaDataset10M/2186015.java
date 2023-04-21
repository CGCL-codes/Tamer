package com.io_software.catools.search.login;

import javax.security.auth.login.LoginContext;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.AccessController;
import javax.security.auth.Subject;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Iterator;
import com.io_software.catools.search.test.TestPermission;

/** A test implementation of the abstract {@link UserDBLoginModule} class. Uses
    a trivial {@link UserDB} implementation that knows only one user:
    "testUser" with password "testPassword". The {@link User} implementation is
    also a trivial one, only carrying the name.

    @author Axel Uhl
    @version $Id: TestUserDBLoginModule.java,v 1.5 2001/04/01 19:59:48 aul Exp $
*/
public class TestUserDBLoginModule extends UserDBLoginModule {

    /** creates an instance of this class by initializing the superclass with a
	{@link UserDB} implementation that is trivial and done in an anonymous
	inner class defined herein.<p>

	By providing this constructor the requirement that a login module must
	have a no-arg constructor is fulfilled.
    */
    public TestUserDBLoginModule() throws RemoteException {
        super(new UserDB() {

            public boolean authenticate(String username, String password) {
                return username.equals("testUser") && password.equals("testPassword");
            }

            public User getUser(final String username) {
                return new UserImpl(username);
            }
        });
    }

    /** creates a subject by assuming that this login module is configured to
	be used by a {@link LoginContext} that is named "TestUserDBLogin". The
	username is taken from <tt>args[0]</tt> and the password from
	<tt>args[1]</tt>. From this information a {@link
	UsernamePasswordCallback} is constructed.
    */
    public static void main(String[] args) {
        try {
            LoginContext lc = new LoginContext("TestUserDBLogin", new UsernamePasswordCallbackHandler(args[0], args[1]));
            lc.login();
            Subject s = lc.getSubject();
            Set principals = s.getPrincipals();
            for (Iterator i = principals.iterator(); i.hasNext(); ) {
                Principal p = (Principal) i.next();
                System.out.println("Principal: " + p.getName());
                System.out.println("Principal type: " + p.getClass().getName());
            }
            Subject.doAs(s, new TestAction(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/** private class implementing the user interface */
class UserImpl implements User {

    public UserImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        return (o instanceof UserImpl) && ((UserImpl) o).getName().equals(getName());
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String toString() {
        return "UserImpl: " + name;
    }

    private String name;

    /** prints a debug message */
    static {
        System.out.println(" *** class TestUserDBLoginModule loaded ***");
    }
}
