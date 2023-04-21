package de.grogra.pf.ui.edit;

import de.grogra.reflect.*;
import de.grogra.xl.util.IntList;
import de.grogra.persistence.IndirectField;
import de.grogra.persistence.ManageableType;
import de.grogra.persistence.PersistenceField;
import de.grogra.pf.ui.*;

public abstract class FieldProperty extends Property {

    final Object object;

    final PersistenceField field;

    final int[] indices;

    final int index;

    FieldProperty(Context context, Object object, PersistenceField field, int[] indices, int index) {
        super(context, field.getType());
        this.object = object;
        this.field = field;
        this.indices = indices;
        this.index = index;
        setQuantity(field.getQuantity());
    }

    int[] addIndex(int i) {
        return (i < 0) ? indices : (indices == null) ? new int[] { i } : new IntList(indices).push(i).toArray();
    }

    @Override
    public String toString() {
        return field.toString();
    }

    @Override
    public Object getValue() {
        return field.get(object, indices);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public Property createSubProperty(Type actualType, Field f, int i) {
        if (!(f instanceof ManageableType.Field)) {
            return null;
        }
        return createSubProperty(IndirectField.concat(field, (ManageableType.Field) f), i);
    }

    protected abstract FieldProperty createSubProperty(PersistenceField f, int i);
}
