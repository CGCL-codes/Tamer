package ch.sahits.codegen.php;

/**
 * Interface contains constants that are used over all the PHP class generator
 * @author Andi Hotz, Sahits GmbH
 * @version 2.1.0
 */
final class PHPConstants {

    /** Substitution constant for all DB fields */
    public static final String DB_FIELDS = "DB_FIELDS";

    /** Substitution constant for getter methods for all DB fields */
    public static final String DB_FIELD_GETTER = "DB_FIELD_GETTER";

    /** Substitution constant for setter methods for all DB fields */
    public static final String DB_FIELD_SETTER = "DB_FIELD_SETTER";

    /** Substitution constant for methods loading by foreign keys */
    public static final String LOAD_BY_FK = "LOAD_BY_FK";

    /** Substitution constant for method for unique load by primary key */
    public static final String LOAD_UNIQUE = "LOAD_UNIQUE";

    /** Substitution constant for primary key columns */
    public static final String PK = "PK";

    /** Substitution constant for foreign key columns */
    public static final String FK = "FK";

    /** Substitution constant for the table name */
    public static final String TABLE_NAME = "TABLE_NAME";

    /** Substitution constant for the save method */
    public static final String SAVE = "SAVE";

    /** Substitution constant for the toString method */
    public static final String TO_STRING = "TO_STRING";

    /** Substitution constant for the initPOST method */
    public static final String INIT_POST = "INIT_POST";

    /** Substitution constant for the initGET method */
    public static final String INIT_GET = "INIT_GET";

    /** Substitution constant for the initLoad method */
    public static final String INIT_LOAD = "INIT_LOAD";

    /** Substitution constant for the insert method */
    public static final String INSERT = "INSERT";

    /** Substitution constant for the insert method */
    public static final String UPDATE = "UPDATE";

    /** Substitution constant for an array of all DB fields */
    public static final String FIELD_ARRAY = "FIELD_ARRAY";

    /** Substitution constant for an where clause for the prefix */
    public static final String WHERE = "_WHERE";

    /** Substitution constant for an object name */
    public static final String OBJECT_NAME = "OBJECT_NAME";

    /** Substitution constant for an class name */
    public static final String CLASS_NAME = "CLASS_NAME";

    /** Substitution constant for the DB name */
    public static final String DB_NAME = "DB_NAME";

    /** Substitution constant for the DB user */
    public static final String DB_USER = "DB_USER";

    /** Substitution constant for the DB password */
    public static final String DB_PASSWD = "DB_PASSWD";

    /** Substitution constant for the DB host */
    public static final String DB_HOST = "DB_HOST";
}
