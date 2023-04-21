package org.datanucleus.store.mapped.mapping.pg;

import org.datanucleus.store.mapped.mapping.SingleFieldMapping;
import org.postgis.PGbox2d;

/**
 * Mapping for org.postgis.PGbox2d to its datastore representation.
 */
public class PGbox2dMapping extends SingleFieldMapping {

    public Class getJavaType() {
        return PGbox2d.class;
    }
}
