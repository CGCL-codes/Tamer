package net.sourceforge.hlm.impl.generic;

import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.exceptions.*;
import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.util.storage.*;

public class StringReferenceImpl implements Reference<String> {

    public StringReferenceImpl(StoredObject parent, int index) {
        this.parent = parent;
        this.index = index;
    }

    public Class<String> getType() {
        return String.class;
    }

    public void set(String object) throws DependencyException, InvalidValueException {
        this.parent.setString(this.index, object);
    }

    public String get() {
        return this.parent.getString(this.index);
    }

    public boolean isEmpty() {
        return (this.parent.getString(this.index) == null);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StringReferenceImpl)) {
            return false;
        }
        StringReferenceImpl stringReference = (StringReferenceImpl) object;
        return (this.parent.equals(stringReference.parent) && this.index == stringReference.index);
    }

    @Override
    public int hashCode() {
        return (this.parent.hashCode() + this.index);
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    public StoredObject parent;

    public int index;
}
