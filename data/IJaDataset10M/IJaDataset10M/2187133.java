package org.tagbox.xpath.primitive;

import org.tagbox.util.Log;

/**
 * specialised subclasses should overload 
 * nodeset(), stringValue(), numberValue(), booleanValue()
 */
public class ObjectPrimitive extends Primitive {

    protected Object value;

    public ObjectPrimitive(Object value) {
        super(Primitive.OBJECT_EXPRESSION);
        this.value = value;
    }

    public String stringValue() {
        return value.toString();
    }

    public Object objectValue() {
        return value;
    }

    public String toString() {
        return super.toString() + "[" + value + "]";
    }
}
