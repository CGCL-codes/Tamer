package com.sebulli.fakturama.data;

import java.util.HashMap;
import com.sebulli.fakturama.logger.Logger;

/**
 * Abstract class for all UniData set Contains a UniData HashMap to store all
 * values of one set and provides methods to access to this has map by an key.
 * 
 * @author Gerd Bartelt
 */
public abstract class UniDataSet {

    public static final String ID = "com.sebulli.fakturama.data.UniDataSet";

    protected String sqlTabeName = "";

    protected HashMap<String, UniData> hashMap = new HashMap<String, UniData>();

    private String key;

    private UniDataSet uds;

    private UniData ud;

    final UniData defaultUniData = new UniData();

    /**
	 * Get the hash map that contains all the values of this set
	 * 
	 * @return The hash map
	 */
    public HashMap<String, UniData> getHashMap() {
        return this.hashMap;
    }

    /**
	 * Get a value of the UniDataSet by a key
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as UniDataType
	 */
    public UniDataType getUniDataTypeByKey(String key) {
        try {
            return this.hashMap.get(key).getDataType();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return UniDataType.NONE;
        }
    }

    /**
	 * Get a value of the UniDataSet by a key
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as integer
	 */
    public int getIntValueByKey(String key) {
        try {
            return this.hashMap.get(key).getValueAsInteger();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return 0;
        }
    }

    /**
	 * Get a value of the UniDataSet by a key
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as Boolean
	 */
    public boolean getBooleanValueByKey(String key) {
        try {
            return this.hashMap.get(key).getValueAsBoolean();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return false;
        }
    }

    /**
	 * Get a value of the UniDataSet by a key
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as Double
	 */
    public Double getDoubleValueByKey(String key) {
        try {
            return this.hashMap.get(key).getValueAsDouble();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return 0.0;
        }
    }

    /**
	 * Get a string value of the UniDataSet by a key
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as String
	 */
    public String getStringValueByKey(String key) {
        try {
            return this.hashMap.get(key).getValueAsString();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return "";
        }
    }

    /**
	 * Get a string value of the UniDataSet by a key and format it
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as String
	 */
    public String getFormatedStringValueByKey(String key) {
        try {
            return this.hashMap.get(key).getValueAsFormatedString();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key. Key " + key + " not in dataset");
            return "";
        }
    }

    /**
	 * Get a string value of the UniDataSet by a key The key can also be in an
	 * other table. Access to values in other tables with the syntax:
	 * "id.TABLENAME:key"
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as String
	 */
    public String getStringValueByKeyFromOtherTable(String key) {
        extractUniDataSetByUniDataSetAndExtendedKey(this, key);
        return ud.getValueAsString();
    }

    /**
	 * Get a Double value of the UniDataSet by a key The key can also be in an
	 * other table. Access to values in other tables with the syntax:
	 * "id.TABLENAME:key"
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as Double
	 */
    public Double getDoubleValueByKeyFromOtherTable(String key) {
        extractUniDataSetByUniDataSetAndExtendedKey(this, key);
        return ud.getValueAsDouble();
    }

    /**
	 * Get a string value of the UniDataSet by a key and format it The key can
	 * also be in an other table. Access to values in other tables with the
	 * syntax: "id.TABLENAME:key"
	 * 
	 * @param key
	 *            Key to access to the value
	 * @return The Value as String
	 */
    public String getFormatedStringValueByKeyFromOtherTable(String key) {
        extractUniDataSetByUniDataSetAndExtendedKey(this, key);
        return ud.getValueAsFormatedString();
    }

    /**
	 * Test, if this is equal to an other UniDataSet
	 * 
	 * @param uds
	 *            Other UniDataSet
	 * @return True, if it's equal
	 */
    protected boolean isTheSameAs(UniDataSet uds) {
        return false;
    }

    /**
	 * Test, if the has map contains a key
	 * 
	 * @param key
	 *            The key to test
	 * @return True, if the key exists.
	 */
    public boolean containsKey(String key) {
        return (hashMap.containsKey(key));
    }

    /**
	 * Set an integer value in the hash map by a key.
	 * 
	 * @param key
	 *            The key
	 * @param i
	 *            The value to set
	 */
    public void setIntValueByKey(String key, int i) {
        try {
            hashMap.get(key).setValue(i);
        } catch (Exception e) {
            Logger.logError(e, "Error setting key. Key " + key + " not in dataset");
        }
    }

    /**
	 * Set a boolean value in the hash map by a key.
	 * 
	 * @param key
	 *            The key
	 * @param b
	 *            The value to set
	 */
    public void setBooleanValueByKey(String key, boolean b) {
        try {
            hashMap.get(key).setValue(b);
        } catch (Exception e) {
            Logger.logError(e, "Error setting key. Key " + key + " not in dataset");
        }
    }

    /**
	 * Set a double value in the hash map by a key.
	 * 
	 * @param key
	 *            The key
	 * @param d
	 *            The value to set
	 */
    public void setDoubleValueByKey(String key, double d) {
        try {
            hashMap.get(key).setValue(d);
        } catch (Exception e) {
            Logger.logError(e, "Error setting key. Key " + key + " not in dataset");
        }
    }

    /**
	 * Set a string value in the hash map by a key.
	 * 
	 * @param key
	 *            The key
	 * @param s
	 *            The value to set
	 */
    public void setStringValueByKey(String key, String s) {
        try {
            hashMap.get(key).setValue(s);
        } catch (Exception e) {
            Logger.logError(e, "Error setting key. Key " + key + " not in dataset");
        }
    }

    /**
	 * Get the category value
	 * 
	 * @return The category value
	 */
    public String getCategory() {
        try {
            return hashMap.get("category").getValueAsString();
        } catch (Exception e) {
            Logger.logError(e, "Error getting key category.");
        }
        return "";
    }

    /**
	 * String representation of this has map
	 * 
	 * @return the value of the entry "name"
	 */
    @Override
    public String toString() {
        return this.hashMap.get("name").getValueAsString();
    }

    /**
	 * Returns the hashCode of this object. The entry "id" and the table name
	 * are used to calculate the hash.
	 * 
	 * @return hash code.
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.hashMap.get("id").getValueAsString().hashCode() + this.sqlTabeName.hashCode() + this.hashMap.hashCode();
        return result;
    }

    /**
	 * Compare this object with an other
	 * 
	 * @param obj
	 *            The other object
	 * @return True, if they are equal
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UniDataSet other = (UniDataSet) obj;
        for (String key : this.hashMap.keySet()) {
            if (this.hashMap.get(key) == null) {
                if (other.hashMap.get(key) != null) return false;
            } else if (!this.hashMap.get(key).getValueAsString().equals(other.hashMap.get(key).getValueAsString())) return false;
        }
        return true;
    }

    /**
	 * To access to values in other tables with the syntax: "id.TABLENAME:key",
	 * this extended key has to be interpreted. This method splits the extended
	 * key and gets the value from an other table
	 * 
	 * @param uniDataSet
	 * @param path
	 */
    private void extractUniDataSetByUniDataSetAndExtendedKey(UniDataSet uniDataSet, String path) {
        String[] pathParts = path.split("\\.");
        String[] tableAndKey;
        String table;
        int id = 0;
        if (pathParts.length <= 1) {
            this.key = path;
            this.uds = uniDataSet;
            this.ud = this.uds.hashMap.get(this.key);
            if (this.ud == null) {
                this.ud = defaultUniData;
            }
            return;
        }
        uds = uniDataSet;
        for (String pathPart : pathParts) {
            tableAndKey = pathPart.split(":");
            if (tableAndKey.length == 2) {
                table = tableAndKey[0];
                this.key = tableAndKey[1];
                if (id >= 0) this.uds = Data.INSTANCE.getUniDataSetByTableNameAndId(table, id); else this.uds = null;
                if (this.uds != null) id = uds.getIntValueByKey(this.key); else {
                    id = 0;
                }
            } else {
                table = "";
                this.key = pathPart;
                id = uds.getIntValueByKey(this.key);
            }
        }
        if (this.uds != null) this.ud = this.uds.hashMap.get(this.key); else this.ud = null;
        if (this.ud == null) this.ud = defaultUniData;
    }

    /**
	 * Copy a complete data set into an other. Except the id
	 * 
	 * @param destination
	 * @param source
	 * @param copyEmptyValues
	 * 		True, if empty values will be copied
	 */
    public static void copy(UniDataSet destination, UniDataSet source, boolean copyEmptyValues) {
        for (String key : source.hashMap.keySet()) {
            if (!key.equals("id")) {
                String value = source.getStringValueByKey(key);
                if (copyEmptyValues || !value.isEmpty()) destination.setStringValueByKey(key, value);
            }
        }
    }
}
