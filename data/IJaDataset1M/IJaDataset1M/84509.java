package org.datanucleus.store.rdbms.mapping.pg2mysql;

import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.postgis.LineString;
import org.postgis.LinearRing;

/**
 * Mapping for a PostGIS-JDBC LinearRing object to MySQL.
 */
public class LinearRingRDBMSMapping extends LineStringRDBMSMapping {

    public LinearRingRDBMSMapping(JavaTypeMapping mapping, MappedStoreManager storeMgr, DatastoreField field) {
        super(mapping, storeMgr, field);
    }

    public LinearRingRDBMSMapping(MappedStoreManager storeMgr, JavaTypeMapping mapping) {
        super(storeMgr, mapping);
    }

    public Object getObject(Object rs, int exprIndex) {
        LineString lineString = (LineString) super.getObject(rs, exprIndex);
        if (lineString == null) return null;
        LinearRing linearRing = new LinearRing(lineString.getPoints());
        linearRing.setSrid(lineString.getSrid());
        return linearRing;
    }

    public void setObject(Object ps, int exprIndex, Object value) {
        LineString lineString = null;
        if (value != null) {
            LinearRing linearRing = (LinearRing) value;
            lineString = new LineString(linearRing.getPoints());
            lineString.setSrid(linearRing.getSrid());
        }
        super.setObject(ps, exprIndex, lineString);
    }
}
