package gov.usda.gdpc.database.jof;

import gov.usda.gdpc.*;
import java.sql.*;

/**
 *
 * @author  terryc
 */
public class BooleanFactory extends AbstractJavaObjectFactory {

    private final int myIndex;

    /** Creates a new instance of BooleanFactory */
    public BooleanFactory(Property property, int index) {
        super(property);
        myIndex = index;
    }

    public Object value(Object[] row) {
        return Convert.convertObjectToBoolean(row[myIndex]);
    }
}
