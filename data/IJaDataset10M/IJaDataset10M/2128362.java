package org.nakedobjects.testing;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedReference;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.reflect.AbstractNakedObjectField;
import org.nakedobjects.utility.DebugString;

public class DummyField extends AbstractNakedObjectField {

    private boolean isObject;

    private boolean isCollection;

    private boolean isValue;

    public DummyField(final String name, final NakedObjectSpecification spec) {
        super(name, spec);
        isObject = spec.isObject();
        isCollection = spec.isCollection();
        isValue = spec.isValue();
    }

    public Naked get(final NakedObject fromObject) {
        return ((DummyNakedObject) fromObject).getField(this);
    }

    public String getName() {
        return null;
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isEmpty(final NakedObject adapter) {
        return false;
    }

    public Object getExtension(final Class cls) {
        return null;
    }

    public Class[] getExtensions() {
        return new Class[0];
    }

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isObject() {
        return isObject;
    }

    public boolean isValue() {
        return isValue;
    }

    public String getDescription() {
        return null;
    }

    public boolean isAuthorised() {
        return true;
    }

    public Consent isAvailable(final NakedReference target) {
        return null;
    }

    public boolean isVisible(final NakedReference target) {
        return true;
    }

    public boolean isHidden() {
        return false;
    }

    public String getHelp() {
        return null;
    }

    public void debugData(final DebugString debugString) {
    }
}
