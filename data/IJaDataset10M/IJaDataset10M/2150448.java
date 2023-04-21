package com.avaje.ebean.server.deploy.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.avaje.ebean.server.core.InternString;
import com.avaje.ebean.server.deploy.generatedproperty.GeneratedProperty;
import com.avaje.ebean.server.el.ElPropertyValue;
import com.avaje.ebean.server.reflect.BeanReflectGetter;
import com.avaje.ebean.server.reflect.BeanReflectSetter;
import com.avaje.ebean.server.type.ScalarType;
import com.avaje.ebean.server.type.ScalarTypeEnum;
import com.avaje.ebean.validation.factory.Validator;

/**
 * Description of a property of a bean. Includes its deployment information such
 * as database column mapping information.
 */
public class DeployBeanProperty {

    /**
	 * Advanced bean deployment. To exclude this property from update where
	 * clause.
	 */
    public static final String EXCLUDE_FROM_UPDATE_WHERE = "EXCLUDE_FROM_UPDATE_WHERE";

    /**
	 * Advanced bean deployment. To exclude this property from delete where
	 * clause.
	 */
    public static final String EXCLUDE_FROM_DELETE_WHERE = "EXCLUDE_FROM_DELETE_WHERE";

    /**
	 * Advanced bean deployment. To exclude this property from insert.
	 */
    public static final String EXCLUDE_FROM_INSERT = "EXCLUDE_FROM_INSERT";

    /**
	 * Advanced bean deployment. To exclude this property from update set
	 * clause.
	 */
    public static final String EXCLUDE_FROM_UPDATE = "EXCLUDE_FROM_UPDATE";

    /**
	 * Flag to mark this at part of the unique id.
	 */
    boolean id;

    /**
	 * Flag to mark the property as embedded. This could be on
	 * BeanPropertyAssocOne rather than here. Put it here for checking Id type
	 * (embedded or not).
	 */
    boolean embedded;

    /**
	 * Flag indicating if this the version property.
	 */
    boolean versionColumn;

    /**
	 * Set if this property is nullable.
	 */
    boolean nullable = true;

    boolean unique;

    /**
	 * The length or precision of the DB column.
	 */
    int dbLength;

    int dbScale;

    String dbColumnDefn;

    boolean isTransient;

    /**
	 * Is this property include in database resultSet.
	 */
    boolean dbRead;

    /**
	 * Include this in DB insert.
	 */
    boolean dbInsertable;

    /**
	 * Include this in a DB update.
	 */
    boolean dbUpdateable;

    /**
	 * Set to true if this property is based on a secondary table.
	 */
    boolean secondaryTable;

    /**
	 * The type that owns this property.
	 */
    Class<?> owningType;

    /**
	 * True if the property is a Clob, Blob LongVarchar or LongVarbinary.
	 */
    boolean lob;

    /**
	 * The logical bean property name.
	 */
    String name;

    /**
	 * The reflected field.
	 */
    Field field;

    /**
	 * The bean type.
	 */
    Class<?> propertyType;

    /**
	 * Set for Non-JDBC types to provide logical to db type conversion.
	 */
    ScalarType scalarType;

    /**
	 * The database column. This can include quoted identifiers.
	 */
    String dbColumn;

    String sqlFormulaSelect;

    String sqlFormulaJoin;

    /**
	 * The jdbc data type this maps to.
	 */
    int dbType;

    /**
	 * The default value to insert if null.
	 */
    Object defaultValue;

    /**
	 * Extra deployment parameters.
	 */
    HashMap<String, String> extraAttributeMap = new HashMap<String, String>();

    /**
	 * The method used to read the property.
	 */
    Method readMethod;

    /**
	 * The method used to write the property.
	 */
    Method writeMethod;

    BeanReflectGetter getter;

    BeanReflectSetter setter;

    /**
	 * Generator for insert or update timestamp etc.
	 */
    GeneratedProperty generatedProperty;

    List<Validator> validators = new ArrayList<Validator>();

    final DeployBeanDescriptor<?> desc;

    public DeployBeanProperty(DeployBeanDescriptor<?> desc, Class<?> propertyType) {
        this.desc = desc;
        this.propertyType = propertyType;
    }

    /**
	 * Return true is this is a simple scalar property.
	 */
    public boolean isScalar() {
        return true;
    }

    public String getFullBeanName() {
        return desc.getFullName() + "." + name;
    }

    /**
	 * Return true if this is a primitive type with a nullable DB column.
	 * <p>
	 * This should log a WARNING as primitive types can't be null.
	 * </p>
	 */
    public boolean isNullablePrimitive() {
        if (nullable && propertyType.isPrimitive()) {
            return true;
        }
        return false;
    }

    /**
	 * Return the DB column length for character columns.
	 * <p>
	 * Note if there is no length explicitly defined then
	 * the scalarType is checked to see if that has one
	 * (primarily to support putting a length on Enum types).
	 * </p>
	 */
    public int getDbLength() {
        if (dbLength == 0 && scalarType != null) {
            return scalarType.getLength();
        }
        return dbLength;
    }

    /**
	 * Set the DB column length for character columns.
	 */
    public void setDbLength(int dbLength) {
        this.dbLength = dbLength;
    }

    /**
	 * Return the Db scale for numeric columns.
	 */
    public int getDbScale() {
        return dbScale;
    }

    /**
	 * Set the Db scale for numeric columns.
	 */
    public void setDbScale(int dbScale) {
        this.dbScale = dbScale;
    }

    /**
	 * Return the DB column definition if defined.
	 */
    public String getDbColumnDefn() {
        return dbColumnDefn;
    }

    /**
	 * Set a specific DB column definition.
	 */
    public void setDbColumnDefn(String dbColumnDefn) {
        if (dbColumnDefn == null || dbColumnDefn.trim().length() == 0) {
            this.dbColumnDefn = null;
        } else {
            this.dbColumnDefn = InternString.intern(dbColumnDefn);
        }
    }

    public String getDbConstraintExpression() {
        if (scalarType instanceof ScalarTypeEnum) {
            ScalarTypeEnum etype = (ScalarTypeEnum) scalarType;
            return "check (" + dbColumn + " in " + etype.getContraintInValues() + ")";
        }
        return null;
    }

    /**
	 * Add a validator to this property.
	 */
    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    /**
	 * Return true if the property contains a validator of a given type.
	 * <p>
	 * Used to detect if a validator has already been assigned when trying to
	 * automatically add validators such as Length and NotNull.
	 * </p>
	 */
    public boolean containsValidatorType(Class<?> type) {
        Iterator<Validator> it = validators.iterator();
        while (it.hasNext()) {
            Validator validator = (Validator) it.next();
            if (validator.getClass().equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Return the validators for this property.
	 */
    public Validator[] getValidators() {
        return validators.toArray(new Validator[validators.size()]);
    }

    /**
	 * Return the scalarType. This returns null for native JDBC types, otherwise
	 * it is used to convert between logical types and jdbc types.
	 */
    public ScalarType getScalarType() {
        return scalarType;
    }

    public void setScalarType(ScalarType scalarType) {
        this.scalarType = scalarType;
    }

    public BeanReflectGetter getGetter() {
        return getter;
    }

    public BeanReflectSetter getSetter() {
        return setter;
    }

    /**
	 * Return the getter method.
	 */
    public Method getReadMethod() {
        return readMethod;
    }

    /**
	 * Return the setter method.
	 */
    public Method getWriteMethod() {
        return writeMethod;
    }

    /**
	 * Set to the owning type form a Inheritance heirarchy.
	 */
    public void setOwningType(Class<?> owningType) {
        this.owningType = owningType;
    }

    public Class<?> getOwningType() {
        return owningType;
    }

    /**
	 * Return true if this is local to this type - aka not from a super type.
	 */
    public boolean isLocal() {
        return owningType == null || owningType.equals(desc.getBeanType());
    }

    /**
	 * Set the getter used to read the property value from a bean.
	 */
    public void setGetter(BeanReflectGetter getter) {
        this.getter = getter;
    }

    /**
	 * Set the setter used to set the property value to a bean.
	 */
    public void setSetter(BeanReflectSetter setter) {
        this.setter = setter;
    }

    /**
	 * Return the name of the property.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the name of the property.
	 */
    public void setName(String name) {
        this.name = InternString.intern(name);
    }

    /**
	 * Return the bean Field associated with this property.
	 */
    public Field getField() {
        return field;
    }

    /**
	 * Set the bean Field associated with this property.
	 */
    public void setField(Field field) {
        this.field = field;
    }

    /**
	 * Return true if this is a generated property like update timestamp and
	 * create timestamp.
	 */
    public boolean isGenerated() {
        return generatedProperty != null;
    }

    /**
	 * Return the GeneratedValue. Used to generate update timestamp etc.
	 */
    public GeneratedProperty getGeneratedProperty() {
        return generatedProperty;
    }

    /**
	 * Set the GeneratedValue. Used to generate update timestamp etc.
	 */
    public void setGeneratedProperty(GeneratedProperty generatedValue) {
        this.generatedProperty = generatedValue;
    }

    /**
	 * Return true if this property is mandatory.
	 */
    public boolean isNullable() {
        return nullable;
    }

    /**
	 * Set the not nullable of this property.
	 */
    public void setNullable(boolean isNullable) {
        this.nullable = isNullable;
    }

    /**
	 * Return true if the DB column is unique.
	 */
    public boolean isUnique() {
        return unique;
    }

    /**
	 * Set to true if the DB column is unique.
	 */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
	 * Return true if this is a version column used for concurrency checking.
	 */
    public boolean isVersionColumn() {
        return versionColumn;
    }

    /**
	 * Set if this is a version column used for concurrency checking.
	 */
    public void setVersionColumn(boolean isVersionColumn) {
        this.versionColumn = isVersionColumn;
    }

    /**
	 * Return the formula this property is based on.
	 */
    public String getSqlFormulaSelect() {
        return sqlFormulaSelect;
    }

    public String getSqlFormulaJoin() {
        return sqlFormulaJoin;
    }

    /**
	 * The property is based on a formula.
	 */
    public void setSqlFormula(String formulaSelect, String formulaJoin) {
        this.sqlFormulaSelect = formulaSelect;
        this.sqlFormulaJoin = formulaJoin.equals("") ? null : formulaJoin;
        this.dbRead = true;
        this.dbInsertable = false;
        this.dbUpdateable = false;
    }

    public String getElPlaceHolder() {
        if (sqlFormulaSelect != null) {
            return sqlFormulaSelect;
        } else {
            return ElPropertyValue.ROOT_ELPREFIX + dbColumn;
        }
    }

    /**
	 * The database column name this is mapped to.
	 */
    public String getDbColumn() {
        if (sqlFormulaSelect != null) {
            return sqlFormulaSelect;
        }
        return dbColumn;
    }

    /**
	 * Set the database column name this is mapped to.
	 */
    public void setDbColumn(String dbColumn) {
        this.dbColumn = InternString.intern(dbColumn);
    }

    /**
	 * Return the database jdbc data type this is mapped to.
	 */
    public int getDbType() {
        return dbType;
    }

    /**
	 * Set the database jdbc data type this is mapped to.
	 */
    public void setDbType(int dbType) {
        this.dbType = dbType;
        this.lob = isLobType(dbType);
    }

    /**
	 * Return true if this is mapped to a Clob Blob LongVarchar or
	 * LongVarbinary.
	 */
    public boolean isLob() {
        return lob;
    }

    private boolean isLobType(int type) {
        switch(type) {
            case Types.CLOB:
                return true;
            case Types.BLOB:
                return true;
            case Types.LONGVARBINARY:
                return true;
            case Types.LONGVARCHAR:
                return true;
            default:
                return false;
        }
    }

    /**
	 * Return true if this property is based on a secondary table.
	 */
    public boolean isSecondaryTable() {
        return secondaryTable;
    }

    /**
	 * Set to true if this property is included in persisting.
	 */
    public void setSecondaryTable() {
        this.secondaryTable = true;
    }

    /**
	 * Return true if this property is included in database queries.
	 */
    public boolean isDbRead() {
        return dbRead;
    }

    /**
	 * Set to true if this property is included in database queries.
	 */
    public void setDbRead(boolean isDBRead) {
        this.dbRead = isDBRead;
    }

    public boolean isDbInsertable() {
        return dbInsertable;
    }

    public void setDbInsertable(boolean insertable) {
        this.dbInsertable = insertable;
    }

    public boolean isDbUpdateable() {
        return dbUpdateable;
    }

    public void setDbUpdateable(boolean updateable) {
        this.dbUpdateable = updateable;
    }

    /**
	 * Return true if the property is transient.
	 */
    public boolean isTransient() {
        return isTransient;
    }

    /**
	 * Mark the property explicitly as a transient property.
	 */
    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    /**
	 * Set the bean read method.
	 * <p>
	 * NB: That a BeanReflectGetter is used to actually perform the getting of
	 * property values from a bean. This is due to performance considerations.
	 * </p>
	 */
    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    /**
	 * Set the bean write method.
	 * <p>
	 * NB: That a BeanReflectSetter is used to actually perform the setting of
	 * property values to a bean. This is due to performance considerations.
	 * </p>
	 */
    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    /**
	 * Return the property type.
	 */
    public Class<?> getPropertyType() {
        return propertyType;
    }

    /**
	 * Return true if this is included in the unique id.
	 */
    public boolean isId() {
        return id;
    }

    /**
	 * Set to true if this is included in the unique id.
	 */
    public void setId(boolean id) {
        this.id = id;
    }

    /**
	 * Return true if this is an Embedded property. In this case it shares the
	 * table and pk of its owner object.
	 */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
	 * Set to true if this is an embedded property.
	 */
    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public Map<String, String> getExtraAttributeMap() {
        return extraAttributeMap;
    }

    /**
	 * Return an extra attribute set on this property.
	 */
    public String getExtraAttribute(String key) {
        return (String) extraAttributeMap.get(key);
    }

    /**
	 * Set an extra attribute set on this property.
	 */
    public void setExtraAttribute(String key, String value) {
        extraAttributeMap.put(key, value);
    }

    /**
	 * Return the default value.
	 */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
	 * Set the default value. Inserted if the value is null.
	 */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String toString() {
        return desc.getFullName() + "." + name;
    }
}
