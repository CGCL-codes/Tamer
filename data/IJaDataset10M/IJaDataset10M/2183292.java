package mipt.data.indexed;

/**
 * Data with fields that can be accessed by index, not by name
 */
public interface IndexedData {

    /**
/**
 * For two objects being equal their fields must be equal
 * Note: if approapriate fields are null, data can be equal!
 * @return boolean
 * @param obj java.lang.Object
 */
    boolean equals(Object obj);

    /**
 * 
 * @return java.lang.Object
 * @param index int
 */
    Object get(int index);

    /**
 * 
 * @return boolean
 * @param index int
 */
    boolean getBoolean(int index);

    /**
 * 
 * @return mipt.data.Data
 * @param index int
 */
    IndexedData getData(int index);

    /**
 * 
 * @return mipt.data.DataSet
 * @param index int
 */
    mipt.data.DataSet getDataSet(int index);

    /**
 * 
 * @return double
 * @param index int
 */
    double getDouble(int index);

    /**
 * 
 * @return int
 */
    int getFieldCount();

    /**
 * 
 * @return int
 * @param index int
 */
    int getInt(int index);

    /**
 * 
 * @return int
 * @param index int
 */
    long getLong(int index);

    /**
 * 
 * @return java.lang.String
 * @param index int
 */
    String getString(int index);

    /**
 * Returns true if two given fields are equal
 * null must be equals to null
 * See comment to isFieldNull()
 * @return boolean
 * @param field java.lang.Object
 * @param otherField java.lang.Object
 */
    boolean isFieldEqual(Object field, Object otherField);

    /**
 * Returns true if the given field is null
 * Needed because field can store information needed to load field value
 *   or object meaning "null" or some flag object or .., but not the value itself
 * @return boolean
 * @param fieldValue java.lang.Object
 */
    boolean isFieldNull(Object fieldValue);
}
