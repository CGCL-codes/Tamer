package com.nexirius.framework.datamodel;

public interface DataModelValue {

    public boolean sameValue(DataModelValue other);

    public void assignValue(DataModelValue other);
}
