package dev.echoservice;

import java.util.Collection;
import java.util.HashMap;
import java.io.Serializable;

public class TestTypedObject implements Serializable {

    /**
     * Simple typed object
     */
    private static final long serialVersionUID = 1L;

    public Collection theCollection;

    public HashMap map;

    public TestTypedObject me;

    private Object _prop1;

    private String _prop2 = "b";

    public TestTypedObject() {
        System.out.println("Constructor Call................\n\n\n\n");
    }

    public void setProp1(Object p) {
        _prop1 = p;
    }

    public Object getProp1() {
        return _prop1;
    }

    public void setProp2(String p) {
        _prop2 = p;
    }

    public String getProp2() {
        return _prop2;
    }

    public Object getReadOnlyProp1() {
        return "This is a ServerSide ReadOnly Property";
    }
}
