package org.jaffa.flexfields.domain;

import org.jaffa.metadata.*;
import java.util.*;

/** This class has the meta information for the FlexField persistent class.
 */
public class FlexFieldMeta {

    private static String m_name = "org.jaffa.flexfields.domain.FlexField";

    private static String m_labelToken = "[label.Jaffa.FlexFields.FlexField]";

    /** A constant to identity the FlexFieldId field.*/
    public static final String FLEX_FIELD_ID = "FlexFieldId";

    /** A constant to identity the ObjectName field.*/
    public static final String OBJECT_NAME = "ObjectName";

    /** A constant to identity the Key1 field.*/
    public static final String KEY1 = "Key1";

    /** A constant to identity the Key2 field.*/
    public static final String KEY2 = "Key2";

    /** A constant to identity the Key3 field.*/
    public static final String KEY3 = "Key3";

    /** A constant to identity the FieldName field.*/
    public static final String FIELD_NAME = "FieldName";

    /** A constant to identity the Value field.*/
    public static final String VALUE = "Value";

    /** A constant which holds the meta information for the FlexFieldId field.*/
    public static final FieldMetaData META_FLEX_FIELD_ID = new StringFieldMetaData(FLEX_FIELD_ID, "[label.Jaffa.FlexFields.FlexField.FlexFieldId]", Boolean.FALSE, null, new Integer(80), null);

    /** A constant which holds the meta information for the ObjectName field.*/
    public static final FieldMetaData META_OBJECT_NAME = new StringFieldMetaData(OBJECT_NAME, "[label.Jaffa.FlexFields.FlexField.ObjectName]", Boolean.TRUE, null, new Integer(80), null);

    /** A constant which holds the meta information for the Key1 field.*/
    public static final FieldMetaData META_KEY1 = new StringFieldMetaData(KEY1, "[label.Jaffa.FlexFields.FlexField.Key1]", Boolean.TRUE, null, new Integer(80), null);

    /** A constant which holds the meta information for the Key2 field.*/
    public static final FieldMetaData META_KEY2 = new StringFieldMetaData(KEY2, "[label.Jaffa.FlexFields.FlexField.Key2]", Boolean.FALSE, null, new Integer(80), null);

    /** A constant which holds the meta information for the Key3 field.*/
    public static final FieldMetaData META_KEY3 = new StringFieldMetaData(KEY3, "[label.Jaffa.FlexFields.FlexField.Key3]", Boolean.FALSE, null, new Integer(80), null);

    /** A constant which holds the meta information for the FieldName field.*/
    public static final FieldMetaData META_FIELD_NAME = new StringFieldMetaData(FIELD_NAME, "[label.Jaffa.FlexFields.FlexField.FieldName]", Boolean.TRUE, null, new Integer(80), null);

    /** A constant which holds the meta information for the Value field.*/
    public static final FieldMetaData META_VALUE = new StringFieldMetaData(VALUE, "[label.Jaffa.FlexFields.FlexField.Value]", Boolean.FALSE, null, new Integer(2000), null);

    private static Map m_fieldMap = new HashMap();

    static {
        m_fieldMap.put(FLEX_FIELD_ID, META_FLEX_FIELD_ID);
        m_fieldMap.put(OBJECT_NAME, META_OBJECT_NAME);
        m_fieldMap.put(KEY1, META_KEY1);
        m_fieldMap.put(KEY2, META_KEY2);
        m_fieldMap.put(KEY3, META_KEY3);
        m_fieldMap.put(FIELD_NAME, META_FIELD_NAME);
        m_fieldMap.put(VALUE, META_VALUE);
    }

    private static List m_keyFields = new LinkedList();

    static {
        m_keyFields.add(META_FLEX_FIELD_ID);
    }

    private static List m_mandatoryFields = new LinkedList();

    static {
        m_mandatoryFields.add(META_OBJECT_NAME);
        m_mandatoryFields.add(META_KEY1);
        m_mandatoryFields.add(META_FIELD_NAME);
    }

    /** Returns the name of the persistent class.
     * @return the name of the persistent class.
     */
    public static String getName() {
        return m_name;
    }

    /** Getter for property labelToken.
     * @return Value of property labelToken.
     */
    public static String getLabelToken() {
        return m_labelToken;
    }

    /** This returns an array of all the fields of the persistent class.
     * @return an array of all the fields of the persistent class.
     */
    public static String[] getAttributes() {
        return DomainMetaDataHelper.getAttributes(m_fieldMap);
    }

    /** This returns an array of meta information for all the fields of the persistent class.
     * @return an array of meta information for all the fields of the persistent class.
     */
    public static FieldMetaData[] getFieldMetaData() {
        return DomainMetaDataHelper.getFieldMetaData(m_fieldMap);
    }

    /** This returns meta information for the input field.
     * @param fieldName the field name.
     * @return meta information for the input field.
     */
    public static FieldMetaData getFieldMetaData(String fieldName) {
        return DomainMetaDataHelper.getFieldMetaData(m_fieldMap, fieldName);
    }

    /** This returns an array of meta information for all the key fields of the persistent class.
     * @return an array of meta information for all the key fields of the persistent class.
     */
    public static FieldMetaData[] getKeyFields() {
        return (FieldMetaData[]) m_keyFields.toArray(new FieldMetaData[0]);
    }

    /** This returns an array of meta information for all the mandatory fields of the persistent class.
     * @return an array of meta information for all the mandatory fields of the persistent class.
     */
    public static FieldMetaData[] getMandatoryFields() {
        return (FieldMetaData[]) m_mandatoryFields.toArray(new FieldMetaData[0]);
    }
}
