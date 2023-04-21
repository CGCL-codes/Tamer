package org.nakedobjects.object.reflect.valueadapter;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.NakedObjectAssociation;
import org.nakedobjects.object.reflect.NakedObjectField;

public abstract class AbstractNakedValue implements NakedValue {

    private NakedObjectSpecification specification;

    public NakedObjectSpecification getSpecification() {
        if (specification == null) {
            specification = NakedObjects.getSpecificationLoader().loadSpecification(getValueClass());
        }
        return specification;
    }

    public abstract String getValueClass();

    public Oid getOid() {
        return null;
    }

    public void copyObject(Naked object) {
    }

    public void clearAssociation(NakedObjectAssociation specification, NakedObject ref) {
    }

    public Naked execute(Action action, Naked[] parameters) {
        return null;
    }

    public Hint getHint(Action action, Naked[] parameters) {
        return null;
    }

    public Hint getHint(NakedObjectField field, Naked value) {
        return null;
    }

    public void clearViewDirty() {
    }

    /**
     * The default minumum length is zero characters.
     */
    public int getMinumumLength() {
        return 0;
    }

    /**
     * There is no default maximum length for this value (returns 0).
     */
    public int getMaximumLength() {
        return 0;
    }

    public boolean canClear() {
        return false;
    }

    public void clear() {
    }

    public boolean isEmpty() {
        return false;
    }
}
