package org.taak;

/**
 * Holds a Java object and indentifier.
 */
public class BoundJavaId extends Code {

    public Object object;

    public Class type;

    public String id;

    public BoundJavaId(Object object, Class type, String id) {
        this.object = object;
        this.type = type;
        this.id = id;
    }

    public Object eval(Context context) {
        return this;
    }

    public String toString() {
        return "<boundjavaid " + object + ", " + type + ", " + id + ">";
    }
}
