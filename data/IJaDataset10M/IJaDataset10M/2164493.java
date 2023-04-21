package org.yajul.enumtype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.yajul.bean.BeanProperties;
import org.yajul.util.ArrayIterator;
import org.yajul.util.ReflectionUtil;
import org.yajul.util.StringUtil;
import org.yajul.xml.DOMUtil;

/**
 * Provides a collection of enumerated values, otherwise known as an enumerated
 * type.  Each value can be found by it's id, it's XML text
 * value, or it's UI text value.
 * @author Joshua Davis
 */
public class EnumType {

    public static final String TAG_ENUM_VALUE = "enum-value";

    public static final String ATTR_ID = "id";

    public static final String ATTR_VALUE_CLASS = "valueClass";

    public static final String ATTR_SET_PROPERTIES = "setValueProperties";

    public static final String ATTR_CONSTANT_CLASS = "constantClass";

    public static final String ATTR_DEFAULT_VALUE_ID = "defaultValueId";

    /**
     * The logger for this class.
     */
    private static Logger log = Logger.getLogger(EnumType.class.getName());

    /** The enum value id returned when there is an error. **/
    public static final int UNDEFINED = Integer.MIN_VALUE;

    /** The class of the values for this enumerated type. **/
    private Class enumValueClass;

    /** The cached property accessor for the value class. **/
    private BeanProperties valueClassPropertyAccessor;

    /** The class or interface that defines Java constants for this enum. **/
    private Class constantClass;

    /** The id of this enumerated type. **/
    private String id;

    /** The minimum value id. **/
    private int minId;

    /** The maximum value id. **/
    private int maxId;

    /** True if the ids are a contiguous sequence of integers. **/
    private boolean contiguous;

    /**
     * An array of all values, where the index is the id.
     * This will be null, if the value ids are not sequential (ish).
     */
    private EnumValue[] valueArrayById;

    /** A map of values, by their id. **/
    private SortedMap valueById;

    /** Map of values by XML string. **/
    private Map valueByXML;

    /** Map of values by text string. **/
    private Map valueByText;

    /** Map of values by text string (case insensitive). **/
    private Map valuesByLowerCaseText;

    /** Map of property maps, by property name. **/
    private Map propertyMaps;

    private Map groupMaps;

    /** The values of the enumerated type, in array form. */
    private EnumValue[] valueArray;

    /** Array of all ids, in order. **/
    private int[] idArray;

    /** The list of all ids as Integers, in order. **/
    private List idList;

    /** The array of all text values, in ID order. **/
    private String[] textArray;

    /** The enum type has been constructed, but no values have been added. **/
    private static final int STATE_INITIAL = 0;

    /** Ready to add values. **/
    private static final int STATE_ADD_VALUES = 1;

    /** All values have been added, fully initialized. **/
    private static final int STATE_INITIALIZED = 2;

    /** The state of the enum type (STATE_xxx). **/
    private int state = STATE_INITIAL;

    /** The default value id for the enum type.  UNDEFINED if it wasn't specified. **/
    private int defaultValueId = UNDEFINED;

    private boolean setValueProperties = false;

    /**
     * Creates a new EnumType object.
     */
    protected EnumType() {
        valueByXML = new HashMap();
        valueByText = new HashMap();
        valueById = new TreeMap();
    }

    /**
     * Returns the id of this enumerated type in the EnumTypeMap that
     * it is contained by.
     * @return String - The unique id of this enumerated type.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the class that will be used to represent the values of the
     * enumerated type.  This class must extend EnumValue.
     * @return Class - The class of the enum values.
     */
    public Class getEnumValueClass() {
        return enumValueClass;
    }

    /**
     * Returns an array of all values in ID order.
     * @return EnumValue[] - An array of all values, in ID order.
     * @see EnumValue
     */
    public EnumValue[] getValueArray() {
        synchronized (this) {
            if (valueArray == null) {
                ArrayList enumValues = new ArrayList();
                Iterator iterator = iterator();
                while (iterator.hasNext()) {
                    enumValues.add(iterator.next());
                }
                valueArray = (EnumValue[]) enumValues.toArray(new EnumValue[enumValues.size()]);
            }
        }
        return valueArray;
    }

    /**
     * Converts the enum type into an array which can be an array of any EnumValue sub-class.
     * @param array The input array.
     * @return an array of any EnumValue sub-class.
     */
    public EnumValue[] toArray(EnumValue[] array) {
        List list = Arrays.asList(getValueArray());
        return (EnumValue[]) list.toArray(array);
    }

    /**
     * Returns the ids of all values in an array.
     * @return int[] - The array of all value ids.
     */
    public int[] getIdArray() {
        synchronized (this) {
            if (idArray == null) {
                EnumValue[] values = getValueArray();
                idArray = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    idArray[i] = values[i].getId();
                }
            }
        }
        return idArray;
    }

    /**
     * Returns a list of Integers that are the IDs of all the values.
     * @return List(Integer) - The array of all value ids as integers.
     */
    public List getIdList() {
        synchronized (this) {
            if (idList == null) {
                EnumValue[] values = getValueArray();
                idList = new ArrayList(values.length);
                for (int i = 0; i < values.length; i++) {
                    idList.add(values[i].getIdInteger());
                }
            }
        }
        return idList;
    }

    /**
     * Returns the text of all values in an array in 'id' order.
     * @return String[] - The array of all value text.
     */
    public String[] getTextArray() {
        synchronized (this) {
            if (textArray == null) {
                EnumValue[] values = getValueArray();
                textArray = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    textArray[i] = values[i].getTextValue();
                }
            }
        }
        return textArray;
    }

    /**
     * Translate each id into it's text value, and return a parallel
     * array of strings.
     * @param ids A subset of the ids to get the text values for.
     * @return String[] - The text values for each id.
     */
    public String[] getTextArray(int[] ids) {
        String[] text = new String[ids.length];
        for (int i = 0; i < ids.length; i++) text[i] = valueIdToText(ids[i]);
        return text;
    }

    /**
     * Returns the minimum value id.
     * @return int - The minimum value id.
     */
    public int getMinId() {
        return minId;
    }

    /**
     * Returns the maximum value id.
     * @return int - The maximum value id.
     */
    public int getMaxId() {
        return maxId;
    }

    /**
     * Returns true if the ids are a contiguous sequence of integers.
     * @return boolean - True if the ids are a contiguous sequence of integers.
     */
    public boolean isContiguous() {
        synchronized (this) {
            return contiguous;
        }
    }

    /**
     * Returns the number of values in the enumerated type.
     * @return int - The number of values in the type.
     */
    public int size() {
        return valueById.size();
    }

    /**
     * Returns true if the id is valid, false if not.
     * @param id The enum value id.
     * @return boolean - True if 'id' is valid.
     */
    public boolean isValid(int id) {
        return (findValueById(id) != null);
    }

    /**
     * Returns the value, given it's id.
     * @param id The value id to look for.
     * @return EnumValue - The enumerated value, or null if the id is not valid.
     */
    public EnumValue findValueById(int id) {
        if (valueArrayById != null) {
            int index = id - minId;
            if (index < 0 || index >= valueArrayById.length) return null; else return valueArrayById[index];
        } else return (EnumValue) valueById.get(new Integer(id));
    }

    /**
     * Returns the value, given it's id.
     * @param id The id to look for.
     * @return EnumValue - The enumerated value.
     * @throws EnumValueNotFoundException if the required value was not found.
     */
    public EnumValue requireValueById(int id) throws EnumValueNotFoundException {
        EnumValue value = findValueById(id);
        if (value == null) throw new EnumValueNotFoundException(getId(), Integer.toString(id));
        return value;
    }

    /**
     * Shortcut method: Returns the text for a value, given it's id.
     * @param id The value id.
     * @return String - The text for the value.
     */
    public String findTextById(int id) {
        try {
            EnumValue value = requireValueById(id);
            return value.getTextValue();
        } catch (EnumValueNotFoundException e) {
            String message = "Unable to find value by id due to: " + e.getMessage();
            log.error(message, e);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Returns the value, given it's XML text.
     * @param xml The XML text to look for.
     * @return EnumValue - The enumerated value.
     */
    public EnumValue findValueByXml(String xml) {
        return (EnumValue) valueByXML.get(xml);
    }

    /**
     * Returns the value, given it's XML text.
     * @param xml The XML text to look for.
     * @return EnumValue - The enumerated value.
     * @throws EnumValueNotFoundException if the required value was not found.
     */
    public EnumValue requireValueByXml(String xml) throws EnumValueNotFoundException {
        EnumValue value = (EnumValue) valueByXML.get(xml);
        if (value == null) throw new EnumValueNotFoundException(id, xml);
        return value;
    }

    /**
     * Returns the value, given it's UI textual representation.
     * @param text The text to look for.
     * @return EnumValue - The enumerated value.
     */
    public EnumValue findValueByText(String text) {
        return (EnumValue) valueByText.get(text);
    }

    /**
     * Returns the value, given it's UI textual representation, ignoring
     * case.
     * @param text The text to look for.
     * @return EnumValue - The enumerated value.
     */
    public EnumValue findValueByTextIgnoreCase(String text) {
        synchronized (this) {
            if (valuesByLowerCaseText == null) createLowerCaseTextMap();
        }
        return (EnumValue) valuesByLowerCaseText.get(text.toLowerCase());
    }

    /**
     * Converts an XML text value into an id for this map.  Returns
     * UNDEFINED (-1) if the XML text was not valid.
     * @param xml The XML text to look for.
     * @return int - The enum value id, or UNDEFINED if the XML text was not
     * valid.
     */
    public int xmlToValueId(String xml) {
        EnumValue value = findValueByXml(xml);
        return (value == null) ? UNDEFINED : value.getId();
    }

    /**
     * Converts a text value into an id for this map.  Returns
     * UNDEFINED (-1) if the text was not valid.
     * @param text The text to look for.
     * @return int - The enum value id, or UNDEFINED if the text was not
     * valid.
     */
    public int textToValueId(String text) {
        EnumValue value = findValueByText(text);
        return (value == null) ? UNDEFINED : value.getId();
    }

    /**
     * Converts an id into a XML text value for this map.  Returns
     * null if the id was not valid.
     * @param id - The enum value id.
     * @return String - The XML text, or null if the id was invalid.
     */
    public String valueIdToXml(int id) {
        EnumValue value = findValueById(id);
        if (value == null) return null; else return value.getXmlValue();
    }

    /**
     * Converts an id into a UI text value for this map.  Returns
     * null if the id was not valid.
     * @param id - The enum value id.
     * @return String - The UI text, or null if the id was invalid.
     */
    public String valueIdToText(int id) {
        EnumValue value = findValueById(id);
        if (value == null) return null; else return value.getTextValue();
    }

    /**
     * Returns an iterator that will produce all of the values of this type
     * in ID order.
     * @return Iterator - An iterator of all the enumerated values.
     */
    public Iterator iterator() {
        if (valueArrayById != null) return new ValueIterator(); else return valueById.values().iterator();
    }

    /**
     * Creates a subset of this enum type by passing the values
     * through a filter.  EnumValue elements will not be cloned.
     * @param subsetId The enum type id of the subset.
     * @param filter The value filter.
     * @return EnumType - The subset enum-type.
     */
    public EnumType createSubset(String subsetId, EnumValueFilter filter) {
        EnumType subset = new EnumType();
        Iterator iter = iterator();
        while (iter.hasNext()) {
            EnumValue value = (EnumValue) iter.next();
            if (filter.test(value)) subset.addValue(value);
        }
        subset.afterAddingValues();
        return subset;
    }

    /**
     * Returns the default value, or null if no default value was specified
     * for the type.
     * @return EnumValue - The default value for the enum type.
     */
    public EnumValue getDefaultValue() {
        if (defaultValueId == UNDEFINED) return null; else return findValueById(defaultValueId);
    }

    /**
     * Returns the id of the default value, or UNDEFINED if it was not set.
     * @return int - The id of the default value, or UNDEFINED if it was not set.
     */
    public int getDefaultValueId() {
        return defaultValueId;
    }

    /**
     * Returns the enum propertyValue with the specified property having the specified property value.
     * @param propertyName The EnumValue property name.
     * @param propertyValue The value the EnumValue property should have.
     * @return the enum propertyValue with the specified property having the specified property value.
     */
    public EnumValue findValueByProperty(String propertyName, Object propertyValue) {
        return (EnumValue) getUniquePropertyMap(propertyName).get(propertyValue);
    }

    /**
     * Returns a subset EnumType containing all the EnumValues where the specified property has the specified property value.
     * @param propertyName The enum propertyValue property name.
     * @param propertyValue The value the EnumValue property should have.
     * @return a subset EnumType containing all the values  where the specified property has the specified property value.
     */
    public EnumType findGroupByProperty(String propertyName, Object propertyValue) {
        return (EnumType) getGroupPropertyMap(propertyName).get(propertyValue);
    }

    /**
     * Returns a map of enum values where the keys are the values of the specified property name.  This map
     * is an index of the enum values, by the values of the specified property.
     * @param propertyName The property name.
     * @return a map of enum values where the keys are the values of the specified property name.
     */
    public Map getUniquePropertyMap(String propertyName) {
        synchronized (this) {
            if (propertyMaps == null) propertyMaps = new WeakHashMap(getValueClassPropertyAccessor().size());
            Map valuesByProperty = (Map) propertyMaps.get(propertyName);
            if (valuesByProperty == null) {
                valuesByProperty = new HashMap(size());
                Iterator iter = iterator();
                BeanProperties accessor = getValueClassPropertyAccessor();
                while (iter.hasNext()) {
                    EnumValue enumValue = (EnumValue) iter.next();
                    valuesByProperty.put(accessor.getProperty(enumValue, propertyName), enumValue);
                }
                propertyMaps.put(propertyName, Collections.unmodifiableMap(valuesByProperty));
            }
            return valuesByProperty;
        }
    }

    /**
     * Returns a Map of EnumTypes where the keys are the unique values of a the specified property, and
     * the values are EnumTypes that contain all the EnumValues that have the same property value.
     * @param propertyName The property name.
     * @return a Map of EnumTypes where the keys are the unique values of a the specified property, and
     * the values are EnumTypes that contain all the EnumValues that have the same property value.
     */
    public Map getGroupPropertyMap(String propertyName) {
        synchronized (this) {
            if (groupMaps == null) groupMaps = new WeakHashMap(getValueClassPropertyAccessor().size());
            Map groupsByProperty = (Map) groupMaps.get(propertyName);
            if (groupsByProperty == null) {
                groupsByProperty = new HashMap(size());
                Iterator iter = iterator();
                BeanProperties accessor = getValueClassPropertyAccessor();
                while (iter.hasNext()) {
                    EnumValue enumValue = (EnumValue) iter.next();
                    Object key = accessor.getProperty(enumValue, propertyName);
                    EnumType group = (EnumType) groupsByProperty.get(key);
                    if (group == null) {
                        group = new EnumType();
                        group.setId(key.toString());
                        group.beforeAddingValues();
                        groupsByProperty.put(key, group);
                    }
                    group.addValue(enumValue);
                }
                iter = groupsByProperty.values().iterator();
                while (iter.hasNext()) {
                    EnumType group = (EnumType) iter.next();
                    group.afterAddingValues();
                }
                groupMaps.put(propertyName, Collections.unmodifiableMap(groupsByProperty));
            }
            return groupsByProperty;
        }
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * @return  a string representation of the object.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[ EnumType id=").append(id);
        buf.append(" isContiguous=").append(isContiguous());
        buf.append(" valueClass=").append(getEnumValueClass());
        Iterator iter = iterator();
        while (iter.hasNext()) {
            EnumValue value = (EnumValue) iter.next();
            buf.append("\n  ").append(value.toString());
        }
        buf.append("\n]");
        return buf.toString();
    }

    /**
     * Sets the class that will be used for the enumerated values.
     * @param enumValueClass The enumerated value class.
     */
    void setEnumValueClass(java.lang.Class enumValueClass) {
        this.enumValueClass = enumValueClass;
    }

    /**
     * Sets the id of this enumerated type.
     * @param id The id of this enumerated type.
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Loads the enumerated type from a DOM element.
     * @param elem The DOM element.
     * @throws EnumInitializationException If the enumerated type could not
     * be created from the DOM element.
     */
    void loadFromElement(EnumTypeMap map, Element elem) throws EnumInitializationException {
        synchronized (this) {
            setFromAttributes(elem);
            Element[] valueElements = DOMUtil.getChildElements(elem, TAG_ENUM_VALUE);
            beforeAddingValues();
            loadValuesFromElements(valueElements, map);
            afterAddingValues();
            if (defaultValueId != UNDEFINED) {
                EnumValue v = findValueById(defaultValueId);
                if (v == null) throw new EnumInitializationException("Unable to find default value with id " + defaultValueId);
            }
        }
    }

    /**
     * Returns true if this enumerated type is supposed to set the value properties automatically from
     * the XML attributes.
     * @return true if this enumerated type is supposed to set the value properties automatically from
     * the XML attributes.
     */
    boolean isSetValueProperties() {
        return setValueProperties;
    }

    BeanProperties getValueClassPropertyAccessor() {
        if (valueClassPropertyAccessor == null) valueClassPropertyAccessor = new BeanProperties(getEnumValueClass());
        return valueClassPropertyAccessor;
    }

    private void loadValuesFromElements(Element[] valueElements, EnumTypeMap map) throws EnumInitializationException {
        for (int i = 0; i < valueElements.length; i++) {
            Element valueElement = valueElements[i];
            EnumValue value = loadValueFromElement(map, valueElement);
            addValue(value);
        }
    }

    private void beforeAddingValues() {
        if (state != STATE_INITIAL) throw new IllegalStateException("Unable to prepare for adding values, state = " + state);
        maxId = Integer.MIN_VALUE;
        minId = Integer.MAX_VALUE;
        state = STATE_ADD_VALUES;
    }

    private void addValue(EnumValue value) {
        int id;
        id = value.getId();
        if (id > maxId) maxId = id;
        if (id < minId) minId = id;
        valueById.put(value.getIdInteger(), value);
        valueByXML.put(value.getXmlValue(), value);
        valueByText.put(value.getTextValue(), value);
    }

    private void afterAddingValues() {
        initializeIdMap();
        initializeConstantNames();
        state = STATE_INITIALIZED;
    }

    private void initializeConstantNames() {
        if (constantClass != null) {
            Map namesById = ReflectionUtil.getConstantNameMap(constantClass);
            Set entries = namesById.entrySet();
            for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Integer id = (Integer) entry.getKey();
                EnumValue value = findValueById(id.intValue());
                if (value == null) {
                    continue;
                }
                value.setConstantName((String) entry.getValue());
            }
        }
    }

    private void initializeIdMap() {
        contiguous = ((maxId - minId) == (valueById.size() - 1));
        if (contiguous) {
            Set entries = valueById.entrySet();
            ArrayList list = new ArrayList(valueById.size());
            for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Integer key = (Integer) entry.getKey();
                EnumValue value = (EnumValue) entry.getValue();
                int index = key.intValue() - minId;
                list.add(index, value);
            }
            valueArrayById = (EnumValue[]) list.toArray(new EnumValue[list.size()]);
            if (minId == 0) valueArray = valueArrayById;
        } else if (log.isDebugEnabled()) log.debug("afterAddingValues() : Using a TreeMap for the id map");
    }

    private EnumValue loadValueFromElement(EnumTypeMap map, Element valueElement) throws EnumInitializationException {
        EnumValue value;
        try {
            value = (EnumValue) enumValueClass.newInstance();
        } catch (InstantiationException e) {
            throw new EnumInitializationException("Unable to instantiate value class due to: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new EnumInitializationException("Unable to instantiate value class due to: " + e.getMessage(), e);
        }
        try {
            value.loadFromElement(map, this, valueElement);
        } catch (Exception e) {
            throw new EnumInitializationException("Unable to load value instance due to: " + e.getMessage(), e);
        }
        return value;
    }

    private void setFromAttributes(Element elem) throws EnumInitializationException {
        id = elem.getAttribute(ATTR_ID);
        if (StringUtil.isEmpty(id)) throw new EnumInitializationException("Expected 'id' attribute in enum-type!");
        try {
            String valueClassName = elem.getAttribute(ATTR_VALUE_CLASS);
            if (!StringUtil.isEmpty(valueClassName)) enumValueClass = Thread.currentThread().getContextClassLoader().loadClass(valueClassName); else enumValueClass = EnumValue.class;
        } catch (ClassNotFoundException e) {
            throw new EnumInitializationException("Unable to find value class! " + e.getMessage(), e);
        }
        String setValuePropertiesString = elem.getAttribute(ATTR_SET_PROPERTIES);
        if (!StringUtil.isEmpty(setValuePropertiesString)) setValueProperties = (setValuePropertiesString.equalsIgnoreCase("true") || setValuePropertiesString.equalsIgnoreCase("yes"));
        try {
            String constantClassName = elem.getAttribute(ATTR_CONSTANT_CLASS);
            if (!StringUtil.isEmpty(constantClassName)) constantClass = Class.forName(constantClassName); else constantClass = null;
        } catch (ClassNotFoundException e) {
            throw new EnumInitializationException("Unable to find value class! " + e.getMessage(), e);
        }
        String defaultValueIdString = elem.getAttribute(ATTR_DEFAULT_VALUE_ID);
        try {
            if (!StringUtil.isEmpty(defaultValueIdString)) defaultValueId = Integer.parseInt(defaultValueIdString);
        } catch (NumberFormatException e) {
            throw new EnumInitializationException("Illegal value for attribute 'defaultValueId': '" + defaultValueIdString, e);
        }
    }

    private void createLowerCaseTextMap() {
        valuesByLowerCaseText = new HashMap();
        Map.Entry entry;
        String key;
        EnumValue value;
        for (Iterator iterator = valuesByLowerCaseText.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            key = (String) entry.getKey();
            value = (EnumValue) entry.getValue();
            valuesByLowerCaseText.put(key.toLowerCase(), value);
        }
    }

    /** Iterates the values of the enumerated type. **/
    private class ValueIterator extends ArrayIterator {

        /**
         * Creates a new iterator for the specified array.
         */
        public ValueIterator() {
            super(valueArrayById, true);
        }
    }
}
