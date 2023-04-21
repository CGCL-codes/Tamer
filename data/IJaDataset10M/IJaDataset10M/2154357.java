package org.makumba.abstr;

import org.makumba.*;

public class ptrRelHandler extends ptrIndexHandler {

    public RecordInfo getForeignTable() {
        return (RecordInfo) fi.extra1;
    }

    public boolean isAssignableFrom(FieldInfo fi) {
        return "nil".equals(fi.getType()) || getType().equals(fi.getType()) && fi.extra1 instanceof RecordInfo && ((RecordInfo) fi.extra1).getName().equals(getForeignTable().getName());
    }

    public RecordInfo getPointedType() {
        return getForeignTable();
    }
}
