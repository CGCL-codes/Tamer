package mipt.search.analytic;

public class DefaultIndexedGroupExpression implements IndexedGroupExpression {

    public int fieldIndex;

    public DefaultIndexedGroupExpression(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    /**
 * 
 * @return boolean
 * @param obj java.lang.Object
 */
    public boolean equals(Object obj) {
        if (!(obj instanceof IndexedGroupExpression)) return false;
        return fieldIndex == ((IndexedGroupExpression) obj).getFieldIndex();
    }

    /**
 * @see mipt.search.IndexedGroupField
 */
    public final int getFieldIndex() {
        return fieldIndex;
    }

    /**
 * 
 * @return int
 */
    public int hashCode() {
        return fieldIndex;
    }

    /**
 * 
 * @return java.lang.String
 */
    public String toString() {
        return Integer.toString(fieldIndex);
    }
}
