package data;

import java.util.EventObject;

/**
 * An event that represents a change in a mapping of properties
 * @author Jere
 * @see DataContent
 * @see DataContentListener
 *
 */
public class PropertyEditEvent extends EventObject {

    public static final int ADD = 0;

    public static final int REMOVE = 1;

    public static final int CHANGE = 2;

    private String name;

    private TypedProperty value;

    private int type;

    public PropertyEditEvent(Object source, int editType, String propertyName, TypedProperty propertyValue) {
        super(source);
        type = editType;
        name = propertyName;
        value = propertyValue;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public TypedProperty getValue() {
        return value;
    }
}
