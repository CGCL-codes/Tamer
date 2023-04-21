package org.jaffa.modules.printing.domain;

import org.jaffa.metadata.*;
import java.util.*;

/** This class has the meta information for the PrinterOutputType persistent class.
 */
public class PrinterOutputTypeMeta {

    private static String m_name = "org.jaffa.modules.printing.domain.PrinterOutputType";

    private static String m_labelToken = "[label.Jaffa.Printing.PrinterOutputType]";

    /** A constant to identity the OutputType field.*/
    public static final String OUTPUT_TYPE = "OutputType";

    /** A constant to identity the Description field.*/
    public static final String DESCRIPTION = "Description";

    /** A constant to identity the DirectPrinting field.*/
    public static final String DIRECT_PRINTING = "DirectPrinting";

    /** A constant to identity the CreatedOn field.*/
    public static final String CREATED_ON = "CreatedOn";

    /** A constant to identity the CreatedBy field.*/
    public static final String CREATED_BY = "CreatedBy";

    /** A constant to identity the LastChangedOn field.*/
    public static final String LAST_CHANGED_ON = "LastChangedOn";

    /** A constant to identity the LastChangedBy field.*/
    public static final String LAST_CHANGED_BY = "LastChangedBy";

    /** A constant which holds the meta information for the OutputType field.*/
    public static final FieldMetaData META_OUTPUT_TYPE = new StringFieldMetaData(OUTPUT_TYPE, "[label.Jaffa.Printing.PrinterOutputType.OutputType]", Boolean.TRUE, null, new Integer(20), FieldMetaData.UPPER_CASE);

    /** A constant which holds the meta information for the Description field.*/
    public static final FieldMetaData META_DESCRIPTION = new StringFieldMetaData(DESCRIPTION, "[label.Jaffa.Printing.PrinterOutputType.Description]", Boolean.FALSE, null, new Integer(100), FieldMetaData.MIXED_CASE);

    /** A constant which holds the meta information for the DirectPrinting field.*/
    public static final FieldMetaData META_DIRECT_PRINTING = new BooleanFieldMetaData(DIRECT_PRINTING, "[label.Jaffa.Printing.PrinterOutputType.DirectPrinting]", Boolean.TRUE, null, null);

    /** A constant which holds the meta information for the CreatedOn field.*/
    public static final FieldMetaData META_CREATED_ON = new DateTimeFieldMetaData(CREATED_ON, "[label.Jaffa.Common.CreatedOn]", Boolean.FALSE, null, null, null);

    /** A constant which holds the meta information for the CreatedBy field.*/
    public static final FieldMetaData META_CREATED_BY = new StringFieldMetaData(CREATED_BY, "[label.Jaffa.Common.CreatedBy]", Boolean.FALSE, null, new Integer(20), FieldMetaData.UPPER_CASE);

    /** A constant which holds the meta information for the LastChangedOn field.*/
    public static final FieldMetaData META_LAST_CHANGED_ON = new DateTimeFieldMetaData(LAST_CHANGED_ON, "[label.Jaffa.Common.LastChangedOn]", Boolean.FALSE, null, null, null);

    /** A constant which holds the meta information for the LastChangedBy field.*/
    public static final FieldMetaData META_LAST_CHANGED_BY = new StringFieldMetaData(LAST_CHANGED_BY, "[label.Jaffa.Common.LastChangedBy]", Boolean.FALSE, null, new Integer(20), FieldMetaData.UPPER_CASE);

    private static Map m_fieldMap = new HashMap();

    static {
        m_fieldMap.put(OUTPUT_TYPE, META_OUTPUT_TYPE);
        m_fieldMap.put(DESCRIPTION, META_DESCRIPTION);
        m_fieldMap.put(DIRECT_PRINTING, META_DIRECT_PRINTING);
        m_fieldMap.put(CREATED_ON, META_CREATED_ON);
        m_fieldMap.put(CREATED_BY, META_CREATED_BY);
        m_fieldMap.put(LAST_CHANGED_ON, META_LAST_CHANGED_ON);
        m_fieldMap.put(LAST_CHANGED_BY, META_LAST_CHANGED_BY);
    }

    private static List m_keyFields = new LinkedList();

    static {
        m_keyFields.add(META_OUTPUT_TYPE);
    }

    private static List m_mandatoryFields = new LinkedList();

    static {
        m_mandatoryFields.add(META_OUTPUT_TYPE);
        m_mandatoryFields.add(META_DIRECT_PRINTING);
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
