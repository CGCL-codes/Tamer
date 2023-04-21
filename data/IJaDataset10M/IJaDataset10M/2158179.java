package ijgen.generator.db.impl;

import java.util.Map;
import java.util.HashMap;
import ijgen.generator.db.JavaToDBMapping;
import ijgen.generator.model.FieldDefinition;

/**
 * Implementation of javaToDBMapping interface for mysql database.
 *
 * @author Detelin Zlatev
 *
 */
public class MySQLJavaToDBMapping implements JavaToDBMapping {

    /**
	* Cache map holding mapping between java types and mysql data types
	*/
    private static final Map<String, String> javaToDBTypes;

    /**
	* Represents the default maximum length of field in database.
	*/
    private static final int DEFAULT_MAX_LENGTH = 255;

    private static final String HQL_DIALECT = "org.hibernate.dialect.MySQLDialect";

    static {
        javaToDBTypes = new HashMap<String, String>();
        javaToDBTypes.put("long", "INT");
        javaToDBTypes.put("java.lang.String", "VARCHAR(_)");
        javaToDBTypes.put("java.util.Date", "DATETIME");
        javaToDBTypes.put("java.lang.Object", "BLOB");
    }

    /**
	 * Default constructor.
	 */
    public MySQLJavaToDBMapping() {
    }

    /**
	 * Implements the interface method.
	 * Returns mysql type from the FieldDefinition object passed as a parameter.
	 *
	 * @param field field definition for which corresponding mysql data type is requested
	 *
	 * @return mysql data type for this field definition
	 */
    public String getDBMapping(FieldDefinition field) {
        String dbType = javaToDBTypes.get(field.getFieldType());
        if (dbType != null) {
            if (dbType.indexOf("_") > 0) {
                int maxLength = DEFAULT_MAX_LENGTH;
                if (field.getFieldConstraints() != null && field.getFieldConstraints().getMaxLength() > 0) {
                    maxLength = field.getFieldConstraints().getMaxLength();
                }
                dbType = dbType.replaceAll("_", String.valueOf(maxLength));
            }
        }
        return dbType;
    }

    @Override
    public String getHQLDialect() {
        return HQL_DIALECT;
    }
}
