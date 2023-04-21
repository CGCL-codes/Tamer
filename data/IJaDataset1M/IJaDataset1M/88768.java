package org.apache.axis2.corba.idl.types;

import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;

public class SequenceType extends AbstractCollectionType {

    protected TypeCode generateTypeCode() {
        return ORB.init().create_sequence_tc(elementCount, dataType.getTypeCode());
    }
}
