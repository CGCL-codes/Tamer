package com.generalynx.ecos.data.types;

public abstract class IntPersistentEnum implements PersistentEnum {

    protected abstract Integer toIntegerCode();

    public Object toSwitchCode() {
        return toIntegerCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IntPersistentEnum)) return false;
        IntPersistentEnum ipe = (IntPersistentEnum) obj;
        return toIntegerCode().equals(ipe.toIntegerCode());
    }
}
