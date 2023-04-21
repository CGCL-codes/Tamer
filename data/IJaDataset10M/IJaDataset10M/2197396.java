package net.zero.smarttrace.data.values;

import javax.persistence.Entity;
import javax.persistence.Transient;
import net.zero.smarttrace.data.IType;
import net.zero.smarttrace.data.PrimitiveType;

@Entity
public class EIntValue extends EValue {

    private int intValue;

    public EIntValue() {
    }

    public EIntValue(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    @Transient
    public IType getType() {
        return PrimitiveType.INTEGER;
    }

    @Transient
    public Object getValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + intValue;
    }
}
