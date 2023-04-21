package keymind.keyaccess.generator;

/**
 * Describes a field, based on column meta data 
 */
public class Field {

    /**
     * Field name; derived from column (or mapping override)
     */
    private String name;

    /**
     * Underlying column name, without any decoration (such as the "__" prefix for collections)
     */
    private String rawName;

    /**
     * Java data type name
     */
    private String javaTypeName;

    /**
     * Java type
     */
    private Class javaType;

    /**
     * If > 0, denotes the sequence in a primary key
     */
    private short primaryKeySequence;

    /**
     * Class descriptor that owns this field
     */
    private Clazz owner;

    /**
     * Upper cardinality constraint. Must be >=1 . -1 is infinite.
     */
    private int cardinalityHigh;

    /**
     * If true, the field represents a releation to another table/class
     */
    private boolean isRelation;

    /**
     * Underlying java.sql.Types type constant
     */
    private int sqlDataType;

    /**
     * C'tor
     *
     * @param owner Owning class
     */
    Field(Clazz owner) {
        cardinalityHigh = 1;
        isRelation = false;
        this.owner = owner;
    }

    /**
     * Generates a field name for a field representing a relation to another table. If
     * there are other relations to the table, new relations are suffixed with a sequence number.
     *
     * @param otherTable Other table
     */
    public void generateNameFromRelation(String otherTable) {
        String relationName = Clazz.AUTO_RELATION_PREFIX + otherTable;
        int relationIndex = 2;
        while (owner.getRelationSet().contains(relationName)) {
            relationName += relationIndex++;
        }
        owner.getRelationSet().add(relationName);
        setName(relationName);
    }

    /**
     * Determines if the field represents a relation
     *
     * @return True if the field is a relation
     */
    public boolean isRelation() {
        return isRelation;
    }

    /**
     * Set relation flag
     *
     * @param relation True if the field is a relation
     */
    public void setRelation(boolean relation) {
        isRelation = relation;
    }

    /**
     * Get field name
     *
     * @return Field name
     */
    public String getName() {
        return name;
    }

    /**
     * Set field name
     *
     * @param name Field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get column name underlying the field
     *
     * @return Column name
     */
    public String getRawName() {
        return rawName != null ? rawName : name;
    }

    /**
     * Set column name underlying the field
     *
     * @param rawName Column name
     */
    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    /**
     * Get a name suitable for a getter for collections
     *
     * @return Getter name
     */
    public String getCollectionGetterName() {
        String rawUpper = getRawName().substring(0, 1).toUpperCase();
        rawUpper = rawUpper + getRawName().substring(1);
        return "get" + rawUpper.replace("__", "") + "Objects";
    }

    /**
     * Get a getter name
     *
     * @return Getter name
     */
    public String getGetterName() {
        String rawUpper = getRawName().substring(0, 1).toUpperCase();
        rawUpper = rawUpper + getRawName().substring(1);
        String name = "get" + rawUpper.replace("__", "");
        if (isRelation()) {
            name += "Object";
        }
        return name;
    }

    /**
     * Get setter name
     *
     * @return Setter name
     */
    public String getSetterName() {
        String rawUpper = getRawName().substring(0, 1).toUpperCase();
        rawUpper = rawUpper + getRawName().substring(1);
        String name = "set" + rawUpper.replace("__", "");
        if (isRelation()) {
            name += "Object";
        }
        return name;
    }

    /**
     * Returns the field's Java type name
     *
     * @return Java type name
     */
    public String getJavaTypeName() {
        return javaTypeName;
    }

    /**
     * Get Java type. This is either a primitive type wrapper or another generated
     * domain class.
     *
     * @return Java type
     */
    public Class getJavaType() {
        if (javaType == null) {
            try {
                javaType = Class.forName(javaTypeName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return javaType;
    }

    /**
     * Set Java type
     *
     * @param javaType Java type
     */
    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    /**
     * Set Java type name
     *
     * @param javaTypeName Java type name
     */
    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    /**
     * Cardinality, upper limit
     *
     * @return Upper limit
     */
    public int getCardinalityHigh() {
        return cardinalityHigh;
    }

    /**
     * Set cardinality, upper limit
     *
     * @param cardinalityHigh Upper limit
     */
    public void setCardinalityHigh(int cardinalityHigh) {
        this.cardinalityHigh = cardinalityHigh;
    }

    /**
     * Returns true if the field represents a collection (one-to-many or many-to-many relation)
     *
     * @return True if the field is a collection
     */
    public boolean isCollection() {
        return cardinalityHigh == -1;
    }

    /**
     * Get primary key sequence
     *
     * @return Primary key sequence number, or 0 if not part of primary key
     */
    public short getPrimaryKeySequence() {
        return primaryKeySequence;
    }

    /**
     * Set primary key sequence number
     *
     * @param primaryKeySequence Primary key sequence number
     */
    public void setPrimaryKeySequence(short primaryKeySequence) {
        this.primaryKeySequence = primaryKeySequence;
    }

    /**
     * Set SQL data type
     *
     * @param sqlDataType java.sql.Types constant
     */
    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    /**
     * Get SQL data type
     *
     * @return java.sql.Types constant
     */
    public int getSqlDataType() {
        return sqlDataType;
    }
}
