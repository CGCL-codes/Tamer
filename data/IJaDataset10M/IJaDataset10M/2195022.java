package org.cake.scripting;

/**
 *
 * @author Aaron Cake
 */
public interface iScriptObject {

    public boolean has(String key);

    public iScriptObject get(String key);

    public <T> T get(String key, Class<T> clazz);

    public Number getNumber(String key);

    public String getString(String key);

    public boolean getBoolean(String key);

    public void set(String key, Object obj);

    public iCallable getFunction(String key);

    public Object callFunction(String key, Object... params);

    public boolean has(int i);

    public iScriptObject get(int i);

    public <T> T get(int i, Class<T> clazz);

    public Number getNumber(int i);

    public String getString(int i);

    public boolean getBoolean(int i);

    public void set(int i, Object obj);

    public boolean isString();

    public boolean isNumber();

    public boolean isBoolean();

    public boolean isArray();

    public boolean isObject();

    public boolean isNull();

    public boolean isUndefined();

    @Override
    public String toString();

    public String toBoolean();

    public Number toNumber();

    public <T> T toType(Class<T> type);

    public Class getInternalType();

    public Class guessJavaType();
}
