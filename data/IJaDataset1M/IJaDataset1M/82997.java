package org.hibernate.search.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.search.SearchException;
import org.hibernate.search.annotations.FilterCacheModeType;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.util.ReflectionHelper;

public class FilterDef {

    private Method factoryMethod;

    private Method keyMethod;

    private Map<String, Method> setters = new HashMap<String, Method>();

    private final FilterCacheModeType cacheMode;

    private final Class<?> impl;

    private final String name;

    public FilterDef(FullTextFilterDef def) {
        this.name = def.name();
        this.impl = def.impl();
        this.cacheMode = def.cache();
    }

    public String getName() {
        return name;
    }

    public FilterCacheModeType getCacheMode() {
        return cacheMode;
    }

    public Class<?> getImpl() {
        return impl;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public Method getKeyMethod() {
        return keyMethod;
    }

    public void setKeyMethod(Method keyMethod) {
        this.keyMethod = keyMethod;
    }

    public void addSetter(String name, Method method) {
        ReflectionHelper.setAccessible(method);
        setters.put(name, method);
    }

    public void invoke(String parameterName, Object filter, Object parameterValue) {
        Method method = setters.get(parameterName);
        if (method == null) throw new SearchException("No setter " + parameterName + " found in " + this.impl);
        try {
            method.invoke(filter, parameterValue);
        } catch (IllegalAccessException e) {
            throw new SearchException("Unable to set Filter parameter: " + parameterName + " on filter class: " + this.impl, e);
        } catch (InvocationTargetException e) {
            throw new SearchException("Unable to set Filter parameter: " + parameterName + " on filter class: " + this.impl, e);
        } catch (IllegalArgumentException e) {
            throw new SearchException("Unable to set Filter parameter: " + parameterName + " on filter class: " + this.impl + " : " + e.getMessage(), e);
        }
    }
}
