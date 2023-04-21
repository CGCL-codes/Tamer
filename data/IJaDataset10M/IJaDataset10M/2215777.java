package org.vikamine.swing.subgroup.editors.zoomtable;

import org.vikamine.kernel.Describer;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.DefaultAttribute;
import org.vikamine.kernel.data.Value;

class DummyValue extends Value {

    private static final long serialVersionUID = 310419720468690447L;

    String name;

    private DummyAttribute att;

    DummyValue(DummyAttribute att, String name) {
        super(name);
        this.name = name;
        this.att = att;
    }

    @Override
    public boolean isValueContainedInInstance(DataRecord instance) {
        return att.getRawValue((int) instance.getValue(att)).equals(name);
    }

    @Override
    public DefaultAttribute getAttribute() {
        return null;
    }

    @Override
    public boolean isMissingValue() {
        return false;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public String getDescription(Describer d) {
        throw new UnsupportedOperationException();
    }
}
