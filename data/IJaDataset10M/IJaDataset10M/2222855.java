package org.vexi.tools.autodoc;

import java.util.*;
import freemarker.template.*;

public class Struct implements TemplateHashModel {

    protected final Errors err;

    public Struct(Errors err) {
        this.err = err;
    }

    private final Map<String, Object> map = new HashMap();

    public void setProp(String name, Object value) {
        setProp(name, value, err);
    }

    public void setProp(String name, Object value, Errors err) {
        if (value == null) return;
        if (map.containsKey(name)) err.newWarning("'" + name + "' being specified twice");
        map.put(name, value);
    }

    public Object getProp(String name) {
        return map.get(name);
    }

    public TemplateModel get(String s) {
        Object r = map.get(s);
        if (r == null) return null;
        if (r instanceof String) return new SimpleScalar((String) r);
        if (r instanceof List) return new SimpleSequence((List) r);
        throw new ClassCastException(r.getClass().getName());
    }

    public boolean isEmpty() {
        return false;
    }
}
