package test.rules;

import java.util.HashMap;
import java.util.Map;

public class CommentBean {

    private Map changes = new HashMap();

    private String field1 = null;

    private String field2 = null;

    private String field3 = null;

    public String getField1() {
        return field1;
    }

    public void setField1(String f) {
        if (!changes.containsKey("field1")) changes.put("field1", field1);
        field1 = f;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String f) {
        if (!changes.containsKey("field2")) changes.put("field2", field2);
        field2 = f;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String f) {
        if (!changes.containsKey("field3")) changes.put("field3", field3);
        field3 = f;
    }

    public String xgetField3() {
        return field3;
    }

    /**
     * Clear all the changes on this bean. Will cause all future calls to
     * {@link #hasChanged(String)} to return false
     */
    public void clearChanges() {
        changes.clear();
    }

    /**
     * Has the bean changed since it was created or last cleared.
     * @return true if the bean has been modified
     */
    public boolean hasChanged() {
        return changes != null && changes.size() > 0;
    }

    /**
     * Has the specified bean property been changed since the bean was
     * created or last cleared
     * @param property Name of bean property to check
     * @return true if the property has been modified
     */
    public boolean hasChanged(String property) {
        return changes.containsKey(property);
    }

    /**
     * Get the original value for this field.
     * @param property Name of bean property to check
     * @return The object representing the original values. Primitives are return as their
     * Object counterparts.
     */
    public Object getOriginalValue(String property) {
        return changes.get(property);
    }

    /** This method is used for binding various validation interceptors. */
    public void validate() {
    }
}
