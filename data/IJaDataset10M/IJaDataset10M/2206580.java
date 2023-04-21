package net.taylor.seam;

import net.taylor.richfaces.Picker;

public class RefEntityPicker<COMP, SRC> extends Picker<COMP, RefEntity, SRC> {

    public RefEntityPicker(Object component, String name) {
        super(component, name);
    }

    protected Class getEntityClass() {
        return RefEntity.class;
    }

    protected String getItemLabel() {
        return "#{t1.name} (#{t1.id})";
    }
}
