package org.apache.axis2.corba.idl.types;

import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;

public class Typedef extends CompositeDataType {

    private DataType dataType;

    protected TypeCode generateTypeCode() {
        return ORB.init().create_alias_tc(getId(), getName(), dataType.getTypeCode());
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
