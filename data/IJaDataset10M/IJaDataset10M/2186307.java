package org.nakedobjects.reflector.java.reflect;

import org.nakedobjects.object.Action;
import org.nakedobjects.object.ActionParameterSet;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.MemberIdentifier;

class DummyAction implements Action {

    private final ActionPeer peer;

    public DummyAction(ActionPeer peer) {
        this.peer = peer;
    }

    public int getParameterCount() {
        return 0;
    }

    public Type getType() {
        return peer.getType();
    }

    public Target getTarget() {
        return peer.getTarget();
    }

    public Object getExtension(Class cls) {
        return null;
    }

    public Class[] getExtensions() {
        return new Class[0];
    }

    public boolean hasReturn() {
        return false;
    }

    public NakedObjectSpecification[] getParameterTypes() {
        return null;
    }

    public Naked[] parameterStubs() {
        return null;
    }

    public NakedObjectSpecification getReturnType() {
        return null;
    }

    public ActionParameterSet getParameterSet(NakedObject object) {
        return null;
    }

    public Naked execute(NakedObject object, Naked[] parameters) {
        return null;
    }

    public MemberIdentifier getIdentifier() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getId() {
        return peer.getIdentifier().getName();
    }

    public Consent isUsable(NakedObject target) {
        return null;
    }

    public Consent hasValidParameters(NakedObject object, Naked[] parameters) {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public Consent isVisible(NakedObject target) {
        return null;
    }

    public boolean isAuthorised() {
        return false;
    }
}
