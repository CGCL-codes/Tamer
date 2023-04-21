package org.jaffa.components.finder;

import org.jaffa.datatypes.Currency;
import java.util.*;
import org.apache.log4j.Logger;
import org.jaffa.metadata.CurrencyFieldMetaData;
import org.jaffa.datatypes.Parser;
import org.jaffa.datatypes.exceptions.FormatCurrencyException;
import org.jaffa.util.StringHelper;

/**
 * This class will be used by the Finder components to hold a Currency criteria.
 */
public class CurrencyCriteriaField implements CriteriaField {

    private static final Logger log = Logger.getLogger(CurrencyCriteriaField.class);

    private String m_operator = null;

    private Currency[] m_values = null;

    /** Default constructor.
     */
    public CurrencyCriteriaField() {
    }

    /** Adds a Criteria.
     * @param operator the operator of the criteria.
     * @param value the value of the criteria.
     */
    public CurrencyCriteriaField(String operator, Currency value) {
        this(operator, value != null ? new Currency[] { value } : null);
    }

    /** Adds a Criteria.
     * @param operator the operator of the criteria.
     * @param values the value array of the criteria.
     */
    public CurrencyCriteriaField(String operator, Currency[] values) {
        m_operator = operator;
        m_values = values;
    }

    /** Getter for property operator.
     * @return Value of property operator.
     */
    public String getOperator() {
        return m_operator;
    }

    /** Setter for the property operator.
     * @param operator The value of the property operator.
     */
    public void setOperator(String operator) {
        m_operator = operator;
    }

    /** Getter for property values.
     * @return An array of values for the Criteria.
     */
    public Currency[] getValues() {
        return m_values;
    }

    /** Setter for the property values.
     * @param values The value of the property values.
     */
    public void setValues(Currency[] values) {
        m_values = values;
    }

    /** Getter for property values.
     * This basically invokes the getValues() method.
     * @return An array of values for the Criteria.
     */
    public Object[] returnValuesAsObjectArray() {
        return getValues();
    }

    /** Returns diagnostic information.
     * @return diagnostic information.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("<CurrencyCriteriaField>");
        buf.append("<operator>");
        if (m_operator != null) buf.append(m_operator);
        buf.append("</operator>");
        if (m_values != null) {
            for (int i = 0; i < m_values.length; i++) {
                Object value = m_values[i];
                buf.append("<value>");
                if (value != null) buf.append(value);
                buf.append("</value>");
            }
        }
        buf.append("</CurrencyCriteriaField>");
        return buf.toString();
    }

    /** This will generate a CriteriaField object based on the input parameters.
     * @param operator The operator of the criteria.
     * @param value The value for the criteria. Multiple values should be separated by comma.
     * @param meta The FieldMetaData object to obtain the layout for parsing.
     * @return a CriteriaField object based on the input parameters.
     * @throws FormatCurrencyException if the value is incorrectly formatted.
     */
    public static CurrencyCriteriaField getCurrencyCriteriaField(String operator, String value, CurrencyFieldMetaData meta) throws FormatCurrencyException {
        CurrencyCriteriaField criteriaField = null;
        Currency nullValue = null;
        if (value != null) value = value.trim();
        if (value != null && value.length() > 0) {
            List values = new ArrayList();
            if (RELATIONAL_BETWEEN.equals(operator) || RELATIONAL_IN.equals(operator)) {
                value = StringHelper.replace(value, CONSECUTIVE_SEPARATORS, CONSECUTIVE_SEPARATORS_WITH_SPACE);
                if (value.startsWith(SEPARATOR_FOR_IN_BETWEEN_OPERATORS)) values.add(null);
                StringTokenizer tknzr = new StringTokenizer(value, SEPARATOR_FOR_IN_BETWEEN_OPERATORS);
                while (tknzr.hasMoreTokens()) parseAndAdd(tknzr.nextToken().trim(), meta, values);
                if (value.endsWith(SEPARATOR_FOR_IN_BETWEEN_OPERATORS)) values.add(null);
            } else {
                parseAndAdd(value, meta, values);
            }
            if (values.size() > 0) criteriaField = new CurrencyCriteriaField(operator, (Currency[]) values.toArray(new Currency[0])); else criteriaField = new CurrencyCriteriaField(operator, nullValue);
        } else criteriaField = new CurrencyCriteriaField(operator, nullValue);
        return criteriaField;
    }

    private static void parseAndAdd(String str, CurrencyFieldMetaData meta, List values) throws FormatCurrencyException {
        try {
            Currency parsedValue = null;
            if (str != null && str.length() > 0) {
                if (meta != null) parsedValue = Parser.parseCurrency(str, meta.getLayout()); else parsedValue = Parser.parseCurrency(str);
            }
            values.add(parsedValue);
        } catch (FormatCurrencyException e) {
            e.setField(meta != null ? meta.getLabelToken() : "");
            throw e;
        }
    }

    /** Validates the criteria.
     * @throws InvalidCriteriaRuntimeException if the criteria is invalid.
     */
    public void validate() throws InvalidCriteriaRuntimeException {
        if (m_operator != null && m_operator.length() > 0 && !CriteriaDropDownOptions.getNumericalCriteriaDropDownOptions().containsKey(m_operator)) {
            String s = "Operator '" + m_operator + "' is invalid. Valid values are " + CriteriaDropDownOptions.getNumericalCriteriaDropDownOptions().keySet().toString();
            log.error(s);
            throw new InvalidCriteriaRuntimeException(s);
        }
    }
}
