package org.openeye.events;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.management.JpaIdentityStore;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.openeye.model.Role;
import org.openeye.model.User;

@Name("authenticationEvents")
public class AuthenticationEvents {

    @SuppressWarnings("unused")
    @Logger
    private Log log;

    @In
    Identity identity;

    @In(required = false)
    private Actor actor;

    @Observer(JpaIdentityStore.EVENT_USER_AUTHENTICATED)
    public void loginSuccessful(User user) {
        actor.setId(user.getUserName());
        for (Role role : user.getUserRoles()) {
            actor.getGroupActorIds().add(role.getName());
        }
    }
}
