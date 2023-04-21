package org.slasoi.slamodel.sla.business;

import java.io.Serializable;
import org.slasoi.slamodel.primitives.CONST;
import org.slasoi.slamodel.primitives.STND;

public class PriceModification implements Serializable {

    private STND _type = null;

    private CONST _value = null;

    public PriceModification(STND type, CONST value) {
        setType(type);
        setValue(value);
    }

    public void setType(STND type) {
        if (type == null) throw new IllegalArgumentException("No type specified");
        _type = type;
    }

    public STND getType() {
        return _type;
    }

    public void setValue(CONST value) {
        if (value == null) throw new IllegalArgumentException("No value specified");
        _value = value;
    }

    public CONST getValue() {
        return _value;
    }
}
