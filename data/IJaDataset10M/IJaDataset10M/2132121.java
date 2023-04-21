package org.openprojectservices.opsadmin.wicket.util;

import org.apache.wicket.Request;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebSession;

public class SessionWithRoles extends WebSession {

    private static final long serialVersionUID = 1L;

    private final Roles roles;

    public SessionWithRoles(final Request request, final Roles roles) {
        super(request);
        this.roles = roles;
    }

    public Roles getRoles() {
        return roles;
    }
}
