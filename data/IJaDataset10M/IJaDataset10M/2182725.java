package de.erdesignerng.dialect.mysql;

import de.erdesignerng.dialect.GenericDataTypeImpl;

/**
 * A MySQL DataType.
 * 
 * @author mirkosertic
 */
public class MySQLDataType extends GenericDataTypeImpl {

    public MySQLDataType(String aName, String aDefinition, int... aSQLType) {
        super(aName, aDefinition, aSQLType);
    }

    public MySQLDataType(String aName, String aDefinition, boolean aIdentity, int... aJdbcType) {
        super(aName, aDefinition, aJdbcType);
        identity = aIdentity;
        if (aIdentity) {
            maxOccursPerTable = 1;
        }
    }
}
