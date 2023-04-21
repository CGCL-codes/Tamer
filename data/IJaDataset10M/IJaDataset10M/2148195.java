package org.neodatis.odb.core.layers.layer2.meta;

import java.util.Observable;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.core.NeoDatisError;

public class DefaultObjectRepresentation extends Observable implements ObjectRepresentation {

    private final NonNativeObjectInfo nnoi;

    public DefaultObjectRepresentation(NonNativeObjectInfo nnoi) {
        this.nnoi = nnoi;
    }

    public Object getValueOf(String attributeName) {
        if (nnoi.isNull()) {
            throw new ODBRuntimeException(NeoDatisError.TRIGGER_CALLED_ON_NULL_OBJECT.addParameter(nnoi.getClassInfo().getFullClassName()).addParameter(attributeName));
        }
        return nnoi.getValueOf(attributeName);
    }

    public void setValueOf(String attributeName, Object value) {
        throw new ODBRuntimeException(NeoDatisError.NOT_YET_SUPPORTED);
    }

    public OID getOid() {
        return this.nnoi.getOid();
    }

    public String getObjectClassName() {
        return nnoi.getClassInfo().getFullClassName();
    }

    public final NonNativeObjectInfo getNnoi() {
        return nnoi;
    }
}
