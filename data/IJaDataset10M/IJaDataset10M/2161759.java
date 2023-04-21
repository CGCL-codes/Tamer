package diuf.diva.hephaistk.fusion.model;

import java.io.Serializable;
import java.util.ArrayList;
import diuf.diva.hephaistk.config.LoggingManager;
import diuf.diva.hephaistk.dialog.TypeConverter;

public class TriggerHL implements Serializable {

    private static final long serialVersionUID = 8924207879130443844L;

    private boolean anyValue = false;

    private String name = null;

    private String modality = null;

    private Class<?> valueType = null;

    private Class<?> wrapperClass = null;

    private ArrayList<Object> values = null;

    private LoggingManager logger = null;

    public TriggerHL(String name, String modality, Class<?> valueType, Class<?> wrapperClass) {
        logger = LoggingManager.getLogger();
        this.name = name;
        this.modality = modality;
        if (valueType.isPrimitive()) {
            valueType = TypeConverter.convertPrimitive(valueType);
        }
        this.valueType = valueType;
        this.wrapperClass = wrapperClass;
        values = new ArrayList<Object>();
        anyValue = true;
    }

    /**
	 * @return the anyValue
	 */
    public boolean isAnyValue() {
        return anyValue;
    }

    /**
	 * @param anyValue the anyValue to set
	 */
    public void setAnyValue(boolean anyValue) {
        this.anyValue = anyValue;
    }

    /**
	 * @return the modality
	 */
    public String getModality() {
        return modality;
    }

    /**
	 * @param modality the modality to set
	 */
    public void setModality(String modality) {
        this.modality = modality;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the valueType
	 */
    public Class<?> getValueType() {
        return valueType;
    }

    /**
	 * @param valueType the valueType to set
	 */
    public void setValueType(Class<?> valueType) {
        if (valueType.isPrimitive()) {
            valueType = TypeConverter.convertPrimitive(valueType);
        }
        this.valueType = valueType;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class<?> wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public void addValue(Object value) {
        anyValue = false;
        if (!value.getClass().equals(valueType)) {
            try {
                value = valueType.cast(value);
            } catch (ClassCastException e) {
                logger.error("TriggerHL " + name + " -- value " + value + " is of type " + value.getClass() + "; should be " + valueType);
            }
        } else {
            values.add(value);
        }
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Trigger '" + name + "' (" + modality + " -" + valueType + "-); ");
        if (anyValue) {
            ret.append(" accepts any value");
        } else {
            ret.append("expects values '");
            for (Object val : values) {
                ret.append(val.toString() + "; ");
            }
            ret.delete(ret.length() - 2, ret.length());
            ret.append("'");
        }
        return ret.toString();
    }

    public void resetValues() {
        values = new ArrayList<Object>();
    }
}
