package net.zero.smarttrace.data.values;

import javax.persistence.Entity;
import javax.persistence.Transient;
import net.zero.smarttrace.data.IType;
import net.zero.smarttrace.data.PrimitiveType;

@Entity
public class EFloatValue extends EValue {

    private float floatValue;

    public EFloatValue() {
    }

    public EFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    @Transient
    public IType getType() {
        return PrimitiveType.FLOAT;
    }

    @Transient
    public Object getValue() {
        return floatValue;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + floatValue;
    }
}
