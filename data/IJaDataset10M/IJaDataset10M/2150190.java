package scythe.dev.db4oviewer;

import java.lang.reflect.Field;
import com.db4o.reflect.ReflectClass;

public class FieldWrapperObjectTree {

    Field theField;

    Object onObject;

    public FieldWrapperObjectTree(Field theField, Object onObject) {
        this.theField = theField;
        this.onObject = onObject;
    }

    public String toString() {
        Object val = ObjectInspector.getFieldValue(theField, onObject);
        String fullFieldName = theField.toString();
        return fullFieldName.substring(fullFieldName.lastIndexOf(".") + 1) + " [" + theField.getType().getName() + "]   = " + val;
    }

    public Field getTheClass() {
        return theField;
    }

    public Field getTheField() {
        return theField;
    }

    public Object getOnObject() {
        return onObject;
    }
}
