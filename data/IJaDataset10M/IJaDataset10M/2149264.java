package org.objectwiz.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Defines a restriction on a property ex: "lastname" like "qin%".
 * Consists of the name of the target property, an operation and one or
 * several values.
 *
 * @author Ailing Qin <ailing.qin at helmet.fr>
 */
public class PropertyRestriction implements java.io.Serializable {

    public static final String OPERATION_ILIKE = "ilike";

    public static final String OPERATION_LIKE = "like";

    public static final String OPERATION_EQUALS = "=";

    public static final String OPERATION_NOT_EQUALS = "!=";

    public static final String OPERATION_STRICTLY_GREATER = ">";

    public static final String OPERATION_STRICTLY_LOWER = "<";

    public static final String OPERATION_GREATER_OR_EQUAL = ">=";

    public static final String OPERATION_LOWER_OR_EQUAL = "<=";

    public static final String TEST_IS_NULL = "is null";

    public static final String TEST_NOT_NULL = "is not null";

    public static final String BETWEEN = "between";

    public static final String[] STRING_OPERATIONS = new String[] { OPERATION_EQUALS, OPERATION_LIKE, OPERATION_ILIKE, TEST_IS_NULL, TEST_NOT_NULL };

    public static final String[] NUMERIC_OPERATIONS = new String[] { OPERATION_EQUALS, OPERATION_NOT_EQUALS, OPERATION_STRICTLY_LOWER, OPERATION_STRICTLY_GREATER, OPERATION_LOWER_OR_EQUAL, OPERATION_GREATER_OR_EQUAL, BETWEEN, TEST_IS_NULL, TEST_NOT_NULL };

    public static final String[] DATE_OPERATIONS = NUMERIC_OPERATIONS;

    public static final String[] BOOLEAN_OPERATIONS = new String[] { OPERATION_EQUALS, OPERATION_NOT_EQUALS, TEST_IS_NULL, TEST_NOT_NULL };

    private static final Set<String> allPossibleOperations;

    private static final Pattern propertyNamePattern;

    static {
        allPossibleOperations = new HashSet();
        allPossibleOperations.addAll(Arrays.asList(STRING_OPERATIONS));
        allPossibleOperations.addAll(Arrays.asList(DATE_OPERATIONS));
        allPossibleOperations.addAll(Arrays.asList(NUMERIC_OPERATIONS));
        allPossibleOperations.addAll(Arrays.asList(BOOLEAN_OPERATIONS));
        propertyNamePattern = Pattern.compile("\\A[\\p{Alnum}_-]+\\z");
    }

    private String propertyName;

    private List<Object> parameterValues;

    private String operation;

    public PropertyRestriction() {
    }

    public PropertyRestriction(MappedProperty property, String operation, Object parameterValue) {
        this(property.getName(), operation, parameterValue);
    }

    public PropertyRestriction(String propertyName, String operation, Object parameterValue) {
        this.parameterValues = new ArrayList();
        parameterValues.add(parameterValue);
        this.propertyName = propertyName;
        this.operation = operation;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public List<Object> getParameterValues() {
        return parameterValues;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String value) {
        this.operation = value;
    }

    public void addParameterValues(Object value) {
        this.parameterValues.add(value);
    }

    public boolean isOperationWithOutValue() {
        return "IS NULL".equalsIgnoreCase(this.operation) || "IS NOT NULL".equalsIgnoreCase(this.operation);
    }

    @Override
    public String toString() {
        return propertyName + " " + operation + " " + parameterValues;
    }

    /**
     * This methods checks that the values (property name, mode and operation)
     * provided are valid in order to prevent injection of illegal values that
     * may result in a security breach.
     *
     * @throws IllegalAccessException if one of the parameters is not within the accepted range.
     */
    public void checkValid(MappedClass mc) throws IllegalAccessException {
        if (mc.getProperty(propertyName) == null) {
            throw new IllegalAccessException("Invalid property name: " + propertyName);
        }
        if (!allPossibleOperations.contains(operation)) {
            throw new IllegalAccessException("Invalid operation: " + operation);
        }
    }
}
