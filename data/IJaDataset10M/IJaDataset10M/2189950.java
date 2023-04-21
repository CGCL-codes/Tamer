package gov.usda.gdpc.germinate;

import gov.usda.gdpc.*;
import gov.usda.gdpc.database.*;
import gov.usda.gdpc.database.jof.JavaObjectFactory;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  terryc
 */
public class GerminatePolyTypeDatabaseMapping extends StringDatabaseMapping {

    private static final Logger myLogger = Logger.getLogger(GerminatePolyTypeDatabaseMapping.class);

    /**
     * GerminatePolyTypeDatabaseMapping Constructor.  This represents a Germinate
     * locus type mapping from a property to a field in a database table.
     *
     * @param property associated property.  null is creating a mapping
     * to use for matching against a property map.
     * @param dbtable database table
     * @param field database field from table
     */
    public GerminatePolyTypeDatabaseMapping(Property property, String dbtable, String field) {
        super(property, dbtable, field);
    }

    private void PolyTypeToString(String PolyType, List list) {
        if (PolyType.equals(LocusProperty.LOCUS_TYPE_UNKNOWN)) {
            list.add("SSR");
            list.add("genomic clone");
        } else if (PolyType.equals(LocusProperty.LOCUS_TYPE_CYTOLOGICAL)) {
            list.add("cytological");
        } else if (PolyType.equals(LocusProperty.LOCUS_TYPE_GENE)) {
            list.add("gene");
            list.add("cdna clone");
        } else {
        }
        return;
    }

    /**
     * Returns the java object factory initialized to use the
     * given result set and extra information.
     */
    public JavaObjectFactory getJavaObjectFactory(ResultSet rs, ExtraInfo extraInfo) throws SQLException {
        int index = -1;
        index = rs.findColumn(myField);
        if (myProperty == GenotypeExperimentProperty.POLY_TYPE) {
            return new GerminatePolyTypeFactory(myProperty, rs, index);
        } else {
            throw new IllegalStateException("GerminatePolyTypeDatabaseMapping: getJavaObjectFactory: Don't know how to convert: " + myProperty);
        }
    }

    /**
     * Returns the string representation of this database mapping.
     *
     * @return string representation
     */
    public String toString() {
        return ("GerminatePolyTypeDatabaseMapping:  property: " + myProperty + "   table: " + myDBTable + "   field: " + myField);
    }
}
