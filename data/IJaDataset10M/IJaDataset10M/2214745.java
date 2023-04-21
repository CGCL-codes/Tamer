package org.nakedobjects.reflector.original.control;

import org.nakedobjects.applib.control.Role;
import org.nakedobjects.applib.control.User;
import org.nakedobjects.object.security.Session;

public abstract class AbstractInfo implements InfoAccessor {

    private StringBuffer debug = new StringBuffer();

    private String description;

    private String help;

    private boolean isAuthorised;

    private String name;

    private Session session;

    public AbstractInfo(final Session session) {
        this.session = session;
        isAuthorised = true;
        description = "";
    }

    protected void concatDebug(final String string) {
        debug.append(debug.length() > 0 ? "; " : "");
        debug.append(string);
    }

    protected boolean currentUserHasRole(final Role role) {
        return session instanceof RoleBasedSession ? ((RoleBasedSession) session).hasRole(role) : false;
    }

    protected User getCurrentUser() {
        if (session instanceof RoleBasedSession) {
            return ((RoleBasedSession) session).getName();
        } else {
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return help;
    }

    public String getName() {
        return name;
    }

    protected void invisibleToUser(final User user) {
        if (getCurrentUser() == user) {
            unauthorised("Invisible to user " + user);
        }
    }

    protected void invisibleToUsers(final User[] users) {
        for (int i = 0; i < users.length; ++i) {
            debug.append(i > 0 ? ". " : "" + users[i].getName());
        }
        for (int i = 0; i < users.length; ++i) {
            if (getCurrentUser() == users[i]) {
                unauthorised("Invisible to users ");
                break;
            }
        }
    }

    public boolean isAuthorised() {
        return isAuthorised;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setHelp(final String help) {
        this.help = help;
    }

    public void setName(final String name) {
        this.name = name;
    }

    private void unauthorised(final String string) {
        concatDebug(string);
        isAuthorised = false;
    }

    protected void visibleOnlyToRole(final Role role) {
        visibleOnlyToRoles(new Role[] { role });
    }

    protected void visibleOnlyToRoles(final Role[] roles) {
        concatDebug("Visible only to roles ");
        boolean validRole = false;
        for (int i = 0; i < roles.length; ++i) {
            if (roles[i] == null) {
                continue;
            }
            debug.append(i > 0 ? ", " : "" + roles[i].getName());
            validRole = validRole || (currentUserHasRole(roles[i]));
        }
        if (!validRole) {
            unauthorised("User not in specified role list");
        }
    }

    protected void visibleOnlyToUser(final User user) {
        visibleOnlyToUsers(new User[] { user });
    }

    protected void visibleOnlyToUsers(final User[] users) {
        for (int i = 0; i < users.length; ++i) {
            if (users[i] == null) {
                continue;
            }
            if (getCurrentUser() == users[i]) {
                return;
            }
        }
        unauthorised("User not in specified user list");
    }
}
