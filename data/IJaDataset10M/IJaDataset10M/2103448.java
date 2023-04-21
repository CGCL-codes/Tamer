package test.org.nakedobjects.object;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.reflect.MemberIdentifier;
import org.nakedobjects.object.reflect.OneToOnePeer;

public class DummyOneToOneAssociation implements NakedObjectField {

    private final OneToOnePeer fieldPeer;

    public DummyOneToOneAssociation(OneToOnePeer fieldPeer) {
        this.fieldPeer = fieldPeer;
    }

    public OneToOnePeer getPeer() {
        return fieldPeer;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isEmpty(NakedObject adapter) {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isValue() {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }

    public Class[] getExtensions() {
        return null;
    }

    public Naked get(NakedObject fromObject) {
        return null;
    }

    public String getLabel(NakedObject object) {
        return null;
    }

    public Object getExtension(Class cls) {
        return null;
    }

    public MemberIdentifier getIdentifier() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getId() {
        return fieldPeer.getIdentifier().getName();
    }

    public boolean hasHint() {
        return false;
    }

    public String getDescription() {
        return null;
    }

    public boolean isAuthorised() {
        return false;
    }

    public Consent isUsable(NakedObject target) {
        return null;
    }

    public Consent isVisible(NakedObject target) {
        return null;
    }

    public boolean isHidden() {
        return false;
    }
}
