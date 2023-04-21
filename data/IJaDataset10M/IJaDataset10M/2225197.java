package org.nakedobjects.nof.reflect.security;

import org.nakedobjects.noa.security.Session;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;

public abstract class AbstractAuthorisationManager {

    private Authorisor authorisor;

    public boolean isEditable(final Session session, final MemberIdentifier identifier) {
        for (int i = 0; i < session.getRoles().length; i++) {
            if (authorisor.isUsable(session.getRoles()[i], identifier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVisible(final Session session, final MemberIdentifier identifier) {
        for (int i = 0; i < session.getRoles().length; i++) {
            if (authorisor.isVisible(session.getRoles()[i], identifier)) {
                return true;
            }
        }
        return false;
    }

    public void init() {
        initAuthorisor();
        authorisor.init();
    }

    public void shutdown() {
        authorisor.shutdown();
    }

    protected abstract void initAuthorisor();

    protected void setAuthorisor(final Authorisor authorisor) {
        this.authorisor = authorisor;
    }
}
