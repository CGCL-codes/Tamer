package mipt.search;

/**
 * Default implementation of AbstractCriterion
 */
public class DefaultCriterion implements AbstractCriterion, Compares {

    public String fieldName;

    public Object fieldValue;

    public boolean notValue = false;

    public int compare = EQUAL;

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue double
 */
    public DefaultCriterion(String fieldName, double fieldValue) {
        this(fieldName, new Double(fieldValue));
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue double
 */
    public DefaultCriterion(String fieldName, float fieldValue) {
        this(fieldName, new Float(fieldValue));
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue int
 */
    public DefaultCriterion(String fieldName, int fieldValue) {
        this(fieldName, new Integer(fieldValue));
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue double
 */
    public DefaultCriterion(String fieldName, long fieldValue) {
        this(fieldName, new Long(fieldValue));
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue java.lang.Object
 */
    public DefaultCriterion(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue java.lang.Object
 */
    public DefaultCriterion(String fieldName, Object fieldValue, boolean notValue) {
        this(fieldName, fieldValue);
        this.notValue = notValue;
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue java.lang.Object
 */
    public DefaultCriterion(String fieldName, Object fieldValue, boolean notValue, int compare) {
        this(fieldName, fieldValue, notValue);
        this.compare = compare;
    }

    /**
 * 
 * @param fieldName java.lang.String
 * @param fieldValue boolean
 */
    public DefaultCriterion(String fieldName, boolean fieldValue) {
        this(fieldName, new Boolean(fieldValue));
    }

    /**
 * 
 * @return boolean
 * @param obj java.lang.Object
 */
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractCriterion)) return false;
        AbstractCriterion aCrit = (AbstractCriterion) obj;
        if (notValue != aCrit.isNotValue()) return false;
        if (compare != aCrit.getCompare()) return false;
        if (!fieldName.equals(aCrit.getFieldName())) return false;
        return fieldValue.equals(aCrit.getFieldValue());
    }

    /**
 * 
 * @return int
 */
    public final int getCompare() {
        return compare;
    }

    /**
 * 
 * @return java.lang.String
 */
    public final String getFieldName() {
        return fieldName;
    }

    /**
 * 
 * @return java.lang.Object
 */
    public final Object getFieldValue() {
        return fieldValue;
    }

    /**
 * 
 * @return int
 */
    public int hashCode() {
        int code = fieldName.hashCode() + compare;
        if (notValue) code += 7;
        return fieldValue == null ? code : code + fieldValue.hashCode();
    }

    /**
 * 
 * @return boolean
 */
    public final boolean isNotValue() {
        return notValue;
    }

    /**
 * 
 * @return java.lang.String
 */
    public String toString() {
        String test;
        switch(compare) {
            case MORE:
                test = " > ";
                break;
            case LESS:
                test = " < ";
                break;
            case LIKE:
                test = " like ";
                break;
            case EQUAL:
            default:
                test = " = ";
        }
        return fieldName + (notValue ? " not" : "") + test + fieldValue;
    }
}
