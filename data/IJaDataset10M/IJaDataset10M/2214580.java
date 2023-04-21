package com.aurorasoftworks.signal.tools.core.context.loader;

public class BeanReference implements IBeanReference {

    private String name;

    private Object object;

    public BeanReference(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public Object getObject() {
        return object;
    }

    public String toString() {
        return name + ": " + object.getClass().getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + name.hashCode();
        result = prime * result + object.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result;
        if (obj instanceof BeanReference) {
            BeanReference ref = (BeanReference) obj;
            result = name.equals(ref.name) && (object.equals(ref.object));
        } else {
            result = false;
        }
        return result;
    }
}
