package uk.ac.lkl.common.util.config;

import java.util.*;

/***********************************************************
 * This is automatically generated code.
 *
 * (with: 'java TxtConfigurationParser ../src/data/configs/test-main.ini Dummy ../src/ ')
 *
 * DO NOT EDIT THIS CLASS.
 *
 ***********************************************************/
public final class DummyConfiguration implements Configuration {

    private DummyConfiguration() {
        factoryReset();
    }

    private static DummyConfiguration INSTANCE = null;

    private Map<String, Boolean> booleanMap = new HashMap<String, Boolean>();

    private Map<String, Double> doubleMap = new HashMap<String, Double>();

    private Map<String, Integer> integerMap = new HashMap<String, Integer>();

    private Map<String, String> stringMap = new HashMap<String, String>();

    public static DummyConfiguration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DummyConfiguration();
            INSTANCE.factoryReset();
        }
        return INSTANCE;
    }

    public static java.lang.Boolean isBooleanProperty() {
        return getInstance().isBooleanPropertyFromInstance();
    }

    public static void setBooleanProperty(java.lang.Boolean newBooleanProperty) {
        getInstance().setBooleanPropertyFromInstance(newBooleanProperty);
    }

    public java.lang.Boolean isBooleanPropertyFromInstance() {
        checkSync();
        return booleanMap.get("booleanProperty");
    }

    public void setBooleanPropertyFromInstance(java.lang.Boolean newBooleanProperty) {
        booleanMap.put("booleanProperty", newBooleanProperty);
    }

    public static java.lang.Double getDoubleProperty() {
        return getInstance().getDoublePropertyFromInstance();
    }

    public static void setDoubleProperty(java.lang.Double newDoubleProperty) {
        getInstance().setDoublePropertyFromInstance(newDoubleProperty);
    }

    public java.lang.Double getDoublePropertyFromInstance() {
        checkSync();
        return doubleMap.get("doubleProperty");
    }

    public void setDoublePropertyFromInstance(java.lang.Double newDoubleProperty) {
        doubleMap.put("doubleProperty", newDoubleProperty);
    }

    public static java.lang.Integer getIntProperty() {
        return getInstance().getIntPropertyFromInstance();
    }

    public static void setIntProperty(java.lang.Integer newIntProperty) {
        getInstance().setIntPropertyFromInstance(newIntProperty);
    }

    public java.lang.Integer getIntPropertyFromInstance() {
        checkSync();
        return integerMap.get("intProperty");
    }

    public void setIntPropertyFromInstance(java.lang.Integer newIntProperty) {
        integerMap.put("intProperty", newIntProperty);
    }

    public static java.lang.String getStringProperty() {
        return getInstance().getStringPropertyFromInstance();
    }

    public static void setStringProperty(java.lang.String newStringProperty) {
        getInstance().setStringPropertyFromInstance(newStringProperty);
    }

    public java.lang.String getStringPropertyFromInstance() {
        checkSync();
        return stringMap.get("stringProperty");
    }

    public void setStringPropertyFromInstance(java.lang.String newStringProperty) {
        stringMap.put("stringProperty", newStringProperty);
    }

    /**
   * Returns the configuration to its initial default state.
   * 
   * Note: this is automatically generated code. Do not edit this method. 
   */
    public static final void resetToSystemDefault() {
        getInstance().factoryReset();
    }

    @Override
    public final void factoryReset() {
        booleanMap.put("booleanProperty", true);
        doubleMap.put("doubleProperty", 0.5);
        integerMap.put("intProperty", 1);
        stringMap.put("stringProperty", "string-property");
    }

    /**
   * Returns the current configuration.
   * 
   * Note: this is automatically generated code. Do not edit this method. 
   */
    public static final Set<ConfigurationItem> getConfiguration() {
        return getInstance().getConfigurationFromInstance();
    }

    public final Set<ConfigurationItem> getConfigurationFromInstance() {
        checkSync();
        Set<ConfigurationItem> result = new HashSet<ConfigurationItem>();
        result.add(new ConfigurationItem("Boolean", "booleanProperty", booleanMap.get("booleanProperty").toString()));
        result.add(new ConfigurationItem("Double", "doubleProperty", doubleMap.get("doubleProperty").toString()));
        result.add(new ConfigurationItem("Integer", "intProperty", integerMap.get("intProperty").toString()));
        result.add(new ConfigurationItem("String", "stringProperty", stringMap.get("stringProperty").toString()));
        return result;
    }

    @SuppressWarnings("unused")
    private static final String defaultsFileAutoGen_ = "test-main.ini";

    @Override
    public void setGenericProperty(ConfigurationItem ci) {
        String key = ci.getName();
        String type = ci.getShortType();
        Object value = ci.getValueAsObject();
        if ("String".equals(type)) {
            if (!stringMap.containsKey(key)) throw new IllegalArgumentException("Invalid string property name: " + key);
            stringMap.put(key, (String) value);
        } else if ("Boolean".equals(type)) {
            if (!booleanMap.containsKey(key)) throw new IllegalArgumentException("Invalid boolean property name: " + key);
            booleanMap.put(key, (Boolean) value);
        } else if ("Integer".equals(type)) {
            if (!integerMap.containsKey(key)) throw new IllegalArgumentException("Invalid integer property name: " + key);
            integerMap.put(key, (Integer) value);
        } else if ("Double".equals(type)) {
            if (!doubleMap.containsKey(key)) throw new IllegalArgumentException("Invalid double property name: " + key);
            doubleMap.put(key, (Double) value);
        } else {
            if (ConfigurationParser.isValidType(type)) {
                throw new UnsupportedOperationException("Type '" + type + "' not supported yet.");
            } else {
                throw new IllegalArgumentException("Type '" + type + "' is not valid.");
            }
        }
    }

    @SuppressWarnings("unused")
    private static boolean checkSyncAutoGen_ = false;

    public static void checkSync() {
        getInstance().checkConfigurationSync();
    }

    @Override
    public void checkConfigurationSync() {
    }
}
