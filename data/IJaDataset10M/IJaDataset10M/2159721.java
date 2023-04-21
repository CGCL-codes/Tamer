package com.gamalocus.sgs.profile.listener.report;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class RawAccessedObjectsDetail implements Serializable {

    private static final long serialVersionUID = -7658184224724034852L;

    public RawTransactionId conflictingId;

    public RawConflictType conflictType;

    public RawAccessedObject[] accessedObjects;

    RawAccessedObjectsDetail() {
    }

    public int getNumberOfAccessedObjects(EnumSet<RawAccessType> access_types) {
        Set<Long> result = new HashSet<Long>();
        for (RawAccessedObject o : accessedObjects) {
            if (access_types.contains(o.access_type)) {
                result.add(o.oid);
            }
        }
        return result.size();
    }
}
