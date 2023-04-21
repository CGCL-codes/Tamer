package mipt.search;

/**
 * Default implementation of IndexedCriterion
 */
public class DefaultIndexedCriterion implements IndexedCriterion, Compares {

    public int fieldIndex;

    public Object fieldValue;

    public boolean notValue = false;

    public int compare = EQUAL;

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue double
 */
    public DefaultIndexedCriterion(int fieldIndex, double fieldValue) {
        this(fieldIndex, new Double(fieldValue));
    }

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue double
 */
    public DefaultIndexedCriterion(int fieldIndex, float fieldValue) {
        this(fieldIndex, new Float(fieldValue));
    }

    /**
 * 
 * @param fieldNumber int
 * @param fieldValue int
 */
    public DefaultIndexedCriterion(int fieldNumber, int fieldValue) {
        this(fieldNumber, new Integer(fieldValue));
    }

    /**
 * 
 * @param fieldNumber int
 * @param fieldValue int
 */
    public DefaultIndexedCriterion(int fieldNumber, long fieldValue) {
        this(fieldNumber, new Long(fieldValue));
    }

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue java.lang.Object
 */
    public DefaultIndexedCriterion(int fieldIndex, Object fieldValue) {
        this.fieldIndex = fieldIndex;
        this.fieldValue = fieldValue;
    }

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue java.lang.Object
 */
    public DefaultIndexedCriterion(int fieldIndex, Object fieldValue, boolean notValue) {
        this(fieldIndex, fieldValue);
        this.notValue = notValue;
    }

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue java.lang.Object
 */
    public DefaultIndexedCriterion(int fieldIndex, Object fieldValue, boolean notValue, int compare) {
        this(fieldIndex, fieldValue, notValue);
        this.compare = compare;
    }

    /**
 * 
 * @param fieldIndex int
 * @param fieldValue boolean
 */
    public DefaultIndexedCriterion(int fieldIndex, boolean fieldValue) {
        this(fieldIndex, new Boolean(fieldValue));
    }

    /**
 * 
 * @return boolean
 * @param obj java.lang.Object
 */
    public boolean equals(Object obj) {
        if (!(obj instanceof IndexedCriterion)) return false;
        IndexedCriterion iCrit = (IndexedCriterion) obj;
        if (notValue != iCrit.isNotValue()) return false;
        if (compare != iCrit.getCompare()) return false;
        if (fieldIndex != iCrit.getFieldIndex()) return false;
        return fieldValue.equals(iCrit.getFieldValue());
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
 * @return int
 */
    public final int getFieldIndex() {
        return fieldIndex;
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
        int code = fieldIndex + compare;
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
        return "field(" + fieldIndex + ")" + (notValue ? " not" : "") + test + fieldValue;
    }
}
