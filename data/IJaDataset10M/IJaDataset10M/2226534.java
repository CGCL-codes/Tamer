package net.taylor.seam.editor;

import net.taylor.seam.ComboBox;
import net.taylor.seam.RefEntity;

public class RefEntityComboBox<COMP, SRC> extends ComboBox<COMP, RefEntity, SRC> {

    public RefEntityComboBox(Object component, String name) {
        super(component, name);
    }

    protected Class getEntityClass() {
        return RefEntity.class;
    }

    protected String getAlias() {
        return "t1";
    }

    protected String getItemLabel() {
        return "#{t1.name} (#{t1.id})";
    }

    protected String getFrom() {
        return "RefEntity t1";
    }

    protected String getOrderBy() {
        return "t1.name";
    }
}
