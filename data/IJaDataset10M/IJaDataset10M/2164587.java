package com.nw.dsl4j.generator;

import java.util.ArrayList;
import java.util.List;
import org.apache.velocity.context.Context;

public class ImportResolvingVelocityContext implements Context {

    private final Generator generator;

    private List<String> imports = new ArrayList<String>();

    public ImportResolvingVelocityContext(Generator generator, List<String> imports) {
        super();
        this.generator = generator;
        this.imports = imports;
    }

    public boolean containsKey(Object key) {
        return findClass((String) key) != null;
    }

    public Object get(String key) {
        Class<?> c = findClass(key);
        return (c == null ? key : DsljReflectionUtils.getAntlrRuleName(c));
    }

    public Object[] getKeys() {
        return null;
    }

    public Object put(String key, Object value) {
        return null;
    }

    public Object remove(Object key) {
        return null;
    }

    private Class<?> findClass(String name) {
        for (String imp : imports) {
            try {
                Class<?> c = Class.forName(imp + "." + name);
                generator.processClass(c);
                return c;
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }
}
