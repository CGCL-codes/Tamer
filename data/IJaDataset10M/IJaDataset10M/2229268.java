package gov.lanl.tools.data.context;

/** Check that a record's value corresponding to the specified key compares to the specified value based on the comparison type. */
public class KeyValueQualifier implements IQualifier {

    /** comparison operation - record's value corresponding to qualifier's key must be less than the qualifier's value */
    public static final int COMPARE_LESS_THAN = -2;

    /** comparison operation - record's value corresponding to qualifier's key must be less than or equal to the qualifier's value */
    public static final int COMPARE_LESS_THAN_OR_EQUALS = -1;

    /** comparison operation - record's value corresponding to qualifier's key must be equal to the qualifier's value */
    public static final int COMPARE_EQUALS = 0;

    /** comparison operation - record's value corresponding to qualifier's key must be greater than or equal to the qualifier's value */
    public static final int COMPARE_GREATER_THAN_OR_EQUALS = 1;

    /** comparison operation - record's value corresponding to qualifier's key must be greater than the qualifier's value */
    public static final int COMPARE_GREATER_THAN = 2;

    /** the key for which to fetch the record's value */
    private final String _key;

    /** the value with which to compare against */
    private final Comparable _value;

    /** comparison operation */
    private final int _operation;

    /**
	 * Primary Constructor
	 * @param key the key for which the record's value is fetched
	 * @param value the value with which to the compare the record's value for the specified key
	 * @param operation the code corresponding to the comparison operation
	 */
    public KeyValueQualifier(final String key, final Comparable value, final int operation) {
        _key = key;
        _value = value;
        _operation = operation;
    }

    /**
	 * Constructor in which the default comparison operation (equality) is used.
	 * @param key the key for which the record's value is fetched
	 * @param value the value with which to the compare the record's value for the specified key
	 */
    public KeyValueQualifier(final String key, final Comparable value) {
        this(key, value, COMPARE_EQUALS);
    }

    /** 
	 * Determine if the specified object is a match to this qualifier's criteria.
	 * The object must implement the IKeyedRecord interface and its value corresponding to <code>_key</code>
	 * must implement the java.util.Comparable interface.
	 * @param object the object to test for matching
	 * @return true if the object matches the criteria and false if not.
	 * @throws java.lang.ClassCastException if the object does not implement the Comparable interface
	 * @see java.util.Comparable
	 */
    public boolean matches(final Object object) {
        final Comparable value = (Comparable) ((ITableRecord) object).getValue(_key);
        final int comparisonResult = value.compareTo(_value);
        switch(_operation) {
            case COMPARE_LESS_THAN:
                return comparisonResult < 0;
            case COMPARE_LESS_THAN_OR_EQUALS:
                return comparisonResult <= 0;
            case COMPARE_EQUALS:
                return comparisonResult == 0;
            case COMPARE_GREATER_THAN_OR_EQUALS:
                return comparisonResult >= 0;
            case COMPARE_GREATER_THAN:
                return comparisonResult > 0;
            default:
                return false;
        }
    }

    /**
	 * Get a string representation of this instance
	 * @return a string representation of this instance.
	 */
    public String toString() {
        String operationToken;
        switch(_operation) {
            case COMPARE_LESS_THAN:
                operationToken = "<";
                break;
            case COMPARE_LESS_THAN_OR_EQUALS:
                operationToken = "<=";
                break;
            case COMPARE_EQUALS:
                operationToken = "=";
                break;
            case COMPARE_GREATER_THAN_OR_EQUALS:
                operationToken = ">=";
                break;
            case COMPARE_GREATER_THAN:
                operationToken = ">";
                break;
            default:
                operationToken = "?";
                break;
        }
        return _key + " " + operationToken + " " + _value;
    }
}
