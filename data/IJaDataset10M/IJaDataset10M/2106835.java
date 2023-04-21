package gov.usda.gdpc.grin;

import gov.usda.gdpc.*;
import gov.usda.gdpc.database.*;
import gov.usda.gdpc.database.jof.*;
import java.sql.*;

/**
 *
 * @author  terryc
 */
public class GRINLocalityDatabaseMapping extends AbstractDatabaseMapping {

    private final GRINDBConnection myConnection;

    /**
     * GRINLocalityDatabaseMapping Constructor.  This represents a GRIN locus
     * mapping from a property to a field in a database table.
     *
     * @param property associated property.  null is creating a mapping
     * to use for matching against a property map.
     * @param dbtable database table
     * @param field database field from table
     */
    public GRINLocalityDatabaseMapping(Property property, GRINDBConnection connection) {
        super(property);
        if (connection == null) {
            throw new IllegalArgumentException("GRINLocalityDatabaseMapping: init: connection can not be null.");
        }
        myConnection = connection;
    }

    /**
     * Returns the java object factory initialized to use the
     * given result set and extra information.
     */
    public JavaObjectFactory getJavaObjectFactory(ResultSet rs, ExtraInfo extraInfo) throws SQLException {
        return new GRINLocalityFactory(myProperty, rs, myConnection);
    }

    /**
     * Returns the string representation of this database mapping.
     *
     * @return string representation
     */
    public String toString() {
        return ("GRINLocalityDatabaseMapping:  property: " + myProperty);
    }
}
