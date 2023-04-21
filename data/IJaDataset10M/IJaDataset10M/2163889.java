package org.avaje.ebean.server.deploy.generatedproperty;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import org.avaje.ebean.server.deploy.meta.DeployBeanProperty;
import org.avaje.ebean.server.plugin.PluginProperties;
import org.avaje.lib.log.LogFactory;

/**
 * Settings used to identify GeneratedValue properties.
 */
public class GeneratedPropertySettings {

    private static final Logger logger = LogFactory.get(GeneratedPropertySettings.class);

    String updateTimestampTypes;

    String updateTimestampProperty;

    String updateTimestampDbColumn;

    String insertTimestampTypes;

    String insertTimestampProperty;

    String insertTimestampDbColumn;

    String counterDbColumn;

    String counterProperty;

    /**
	 *  Possibly useful when DB Timestamp does not store milliseconds.
	 */
    int updateTimestampDataType = Types.TIMESTAMP;

    PluginProperties properties;

    boolean checkProperty = true;

    GeneratedPropertyFactory generatedPropertyFactory;

    HashSet<String> numberTypes = new HashSet<String>();

    /**
	 * Create for a given dbPlugin.
	 */
    public GeneratedPropertySettings(PluginProperties properties) {
        this.properties = properties;
        this.generatedPropertyFactory = new DefaultGeneratedPropertyFactory();
        numberTypes.add(Integer.class.getName());
        numberTypes.add(int.class.getName());
        numberTypes.add(Long.class.getName());
        numberTypes.add(long.class.getName());
        numberTypes.add(Short.class.getName());
        numberTypes.add(short.class.getName());
        numberTypes.add(Double.class.getName());
        numberTypes.add(double.class.getName());
        numberTypes.add(BigDecimal.class.getName());
        init();
    }

    /**
	 * Read the settings from the properties file. This can be used to
	 * automatically identify insert and update timestamp columns.
	 */
    private void init() {
        determineTimestampType();
        String dfltTimestampTypes = "," + Timestamp.class.getName() + ", " + Date.class.getName();
        updateTimestampProperty = properties.getProperty("updatetimestamp.property", null);
        updateTimestampDbColumn = properties.getProperty("updatetimestamp.dbcolumn", null);
        updateTimestampTypes = properties.getProperty("updatetimestamp.types", dfltTimestampTypes);
        insertTimestampProperty = properties.getProperty("inserttimestamp.property", null);
        insertTimestampDbColumn = properties.getProperty("inserttimestamp.dbcolumn", null);
        insertTimestampTypes = properties.getProperty("inserttimestamp.types", dfltTimestampTypes);
        counterProperty = properties.getProperty("counter.property", null);
        counterDbColumn = properties.getProperty("counter.dbcolumn", null);
    }

    /**
	 * Set to false for TableDescriptorFactory as MapBeans may have other
	 * property names.
	 */
    public void setCheckProperty(boolean checkProperty) {
        this.checkProperty = checkProperty;
    }

    /**
	 * Used by Entity bean code generation to determine if a column is a version
	 * column.
	 */
    public boolean isVersion(String propName, String propType, String colName) {
        if (isCounter(propName, propType, colName)) {
            return true;
        }
        if (isUpdateTimestamp(propName, propType, colName)) {
            return true;
        }
        return false;
    }

    public void setGeneratedProperty(DeployBeanProperty prop) {
        Class<?> propertyType = prop.getPropertyType();
        if (propertyType == null) {
            logger.warning("propertyType is null on " + prop.getName());
        } else {
            if (prop.isVersionColumn()) {
                if (isNumberType(prop.getPropertyType().getName())) {
                    setCounter(prop);
                } else {
                    setUpdateTimestamp(prop);
                }
            } else {
                if (isInsertTimestamp(prop)) {
                    setInsertTimestamp(prop);
                }
                if (isUpdateTimestamp(prop)) {
                    setUpdateTimestamp(prop);
                }
                if (isCounter(prop)) {
                    setCounter(prop);
                }
            }
        }
    }

    /**
	 * Possibly useful when DB Timestamp does not store millis.
	 */
    private void determineTimestampType() {
        String type = properties.getProperty("updatetimestamp.datatype", null);
        if (type != null) {
            if (type.equalsIgnoreCase("timestamp")) {
                updateTimestampDataType = Types.TIMESTAMP;
            } else if (type.equalsIgnoreCase("long")) {
                updateTimestampDataType = Types.BIGINT;
            } else if (type.equalsIgnoreCase("double")) {
                updateTimestampDataType = Types.DOUBLE;
            } else if (type.equalsIgnoreCase("bigint")) {
                updateTimestampDataType = Types.BIGINT;
            } else {
                String msg = "Undetermined [updatetimestamp.datatype] of [" + type + "]";
                throw new PersistenceException(msg);
            }
        }
    }

    /**
	 * Modify the BeanProperty to be a 'Update Timestamp' generated value if
	 * isSet is true.
	 */
    private void setUpdateTimestamp(DeployBeanProperty prop) {
        GeneratedProperty gp = generatedPropertyFactory.createUpdateTimestamp(prop);
        prop.setGeneratedProperty(gp);
        prop.setVersionColumn(true);
        prop.setDbType(updateTimestampDataType);
    }

    /**
	 * Modify the BeanProperty to be a 'Insert Timestamp' generated value if
	 * isSet is true.
	 */
    private void setInsertTimestamp(DeployBeanProperty prop) {
        GeneratedProperty gp = generatedPropertyFactory.createInsertTimestamp(prop);
        prop.setGeneratedProperty(gp);
    }

    /**
	 * Modify the BeanProperty to be a 'Counter' generated value if isSet is
	 * true.
	 */
    private void setCounter(DeployBeanProperty prop) {
        GeneratedProperty counter = generatedPropertyFactory.createCounter(prop);
        prop.setGeneratedProperty(counter);
        prop.setVersionColumn(true);
    }

    /**
	 * Return true if this looks like a 'Insert Timestamp' property.
	 */
    private boolean isInsertTimestamp(DeployBeanProperty prop) {
        if (insertTimestampDbColumn == null && insertTimestampProperty == null) {
            return false;
        }
        if (insertTimestampDbColumn != null) {
            if (!insertTimestampDbColumn.equalsIgnoreCase(prop.getDbColumn())) {
                return false;
            }
        }
        if (checkProperty && insertTimestampProperty != null) {
            if (!insertTimestampProperty.equalsIgnoreCase(prop.getName())) {
                return false;
            }
        }
        if (insertTimestampTypes != null) {
            String propType = prop.getPropertyType().getName();
            if (insertTimestampTypes.indexOf(propType) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Return true if this looks like a 'Counter' property.
	 */
    private boolean isCounter(DeployBeanProperty prop) {
        String propName = prop.getName();
        String propType = prop.getPropertyType().getName();
        String column = prop.getDbColumn();
        return isCounter(propName, propType, column);
    }

    private boolean isCounter(String propName, String propType, String column) {
        if (counterDbColumn == null && counterProperty == null) {
            return false;
        }
        if (counterDbColumn != null) {
            if (!counterDbColumn.equalsIgnoreCase(column)) {
                return false;
            }
        }
        if (counterProperty != null) {
            if (!counterProperty.equalsIgnoreCase(propName)) {
                return false;
            }
        }
        if (!isNumberType(propType)) {
            return false;
        }
        return true;
    }

    /**
	 * Return true if this looks like a 'Update Timestamp' property.
	 */
    private boolean isUpdateTimestamp(DeployBeanProperty prop) {
        String propName = prop.getName();
        Class<?> propertyType = prop.getPropertyType();
        if (propertyType == null) {
            logger.warning("Null PropertyType for " + prop.getName() + "  dbCol:" + prop.getDbFullName());
            return false;
        }
        String propType = propertyType.getName();
        String column = prop.getDbColumn();
        return isUpdateTimestamp(propName, propType, column);
    }

    private boolean isUpdateTimestamp(String propName, String propType, String column) {
        if (updateTimestampDbColumn == null && updateTimestampProperty == null) {
            return false;
        }
        if (updateTimestampDbColumn != null) {
            if (!updateTimestampDbColumn.equalsIgnoreCase(column)) {
                return false;
            }
        }
        if (checkProperty && updateTimestampProperty != null) {
            if (!updateTimestampProperty.equalsIgnoreCase(propName)) {
                return false;
            }
        }
        if (updateTimestampTypes != null) {
            if (updateTimestampTypes.indexOf(propType) == -1) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumberType(String typeClassName) {
        return numberTypes.contains(typeClassName);
    }
}
