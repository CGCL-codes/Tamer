package jme;

import java.io.Serializable;
import java.util.HashMap;

public final class FullWorld implements Serializable {

    private static final long serialVersionUID = 1L;

    static {
        Utils.forceHandlingOfTransientModifiersForXMLSerialization(FullWorld.class);
    }

    private HashMap<String, EntityParameters> entityParameterTable;

    public FullWorld() {
    }

    public final HashMap<String, EntityParameters> getEntityParameterTable() {
        return (entityParameterTable);
    }

    public final void setEntityParameterTable(HashMap<String, EntityParameters> entityParameterTable) {
        this.entityParameterTable = entityParameterTable;
    }
}
