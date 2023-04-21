package org.nox.util.json;

public interface JSONContext {

    public Object get(Object key);

    public void put(Object key, Object value);

    public JSONSerializer createSerializer();
}
