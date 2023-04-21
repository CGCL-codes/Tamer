package org.nakedobjects.object.security;

import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.OneToManyPeer;
import org.nakedobjects.object.reflect.OneToOnePeer;
import org.nakedobjects.object.reflect.ReflectionPeerFactory;

public class SecurityPeerFactory implements ReflectionPeerFactory {

    private AuthorisationManager manager;

    public void setAuthorisationManager(AuthorisationManager manager) {
        this.manager = manager;
    }

    /**
     * Expose as a .NET property
     * 
     * @property
     */
    public void set_AuthorisationManager(AuthorisationManager manager) {
        setAuthorisationManager(manager);
    }

    public ActionPeer createAction(ActionPeer peer) {
        return new ActionAuthorisation(peer, manager);
    }

    public OneToManyPeer createField(OneToManyPeer peer) {
        return new OneToManyAuthorisation(peer, manager);
    }

    public OneToOnePeer createField(OneToOnePeer peer) {
        return new OneToOneAuthorisation(peer, manager);
    }

    public void init() {
        manager.init();
    }

    public void shutdown() {
        manager.shutdown();
    }
}
