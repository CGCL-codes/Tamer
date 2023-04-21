package com.gwtaf.ext.core.client.record.fielddef;

import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.Record;

public class FloatFieldDefEx extends FieldDefEx<Float> {

    public FloatFieldDefEx(String name) {
        super(new FloatFieldDef(name));
    }

    public Float getValue(Record r) {
        return r.getAsFloat(getName());
    }

    @Override
    public void setValue(Record r, Float value) {
        if (value != null) r.set(getName(), value.floatValue());
        r.set(getName(), value);
    }
}
