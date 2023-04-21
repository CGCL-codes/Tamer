package org.photovault.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 Change to a value field, i.e. field whose value does not contain any substate.
 
 @author Harri Kaimio
 @since 0.6.0
 */
final class ValueChange extends FieldChange implements Externalizable {

    /**
     New value for the field
     */
    private Object value;

    /**
     Default constructor should be used only by serialization.
     */
    public ValueChange() {
        super();
    }

    /**
     Constructor
     @param name Name of the field
     @param newValue New value for the field
     */
    public ValueChange(String name, Object newValue) {
        super(name);
        value = newValue;
    }

    /**
     Returns the new value for changed field
     */
    public Object getValue() {
        return value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean conflictsWith(FieldChange ch) {
        if (ch instanceof ValueChange) {
            return value.equals(((ValueChange) ch).value);
        }
        return true;
    }

    @Override
    public void addChange(FieldChange ch) {
        if (ch instanceof ValueChange) {
            ValueChange vc = (ValueChange) ch;
            value = vc.value;
        }
    }

    @Override
    public void addEarlier(FieldChange ch) {
    }

    @Override
    public FieldChange merge(FieldChange ch) {
        if (!(ch instanceof ValueChange)) {
            throw new IllegalArgumentException("Cannot merge " + ch.getClass().getName() + " to ValueChange");
        }
        ValueChange vc = (ValueChange) ch;
        Object vcVal = vc.getValue();
        ValueChange ret = new ValueChange(name, value);
        if (value != vcVal && (value == null || !value.equals(vcVal))) {
            List values = new ArrayList(2);
            values.add(value);
            values.add(vcVal);
            ret.addConflict(new ValueFieldConflict(ret, values));
        }
        return ret;
    }

    @Override
    public FieldChange getReverse(Change baseline) {
        Object prevValue = null;
        for (Change c = baseline; c != null; c = c.getPrevChange()) {
            ValueChange fc = (ValueChange) c.getFieldChange(name);
            if (fc != null) {
                prevValue = fc.getValue();
                break;
            }
        }
        return new ValueChange(name, prevValue);
    }

    @Override
    public String toString() {
        return "" + name + " -> " + value;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(value);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        value = in.readObject();
    }
}
