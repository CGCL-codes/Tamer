package com.abra.j2xb.beans.propertyEditors;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: YOAVA
 * Date: May 24, 2007
 * Time: 3:37:38 PM
 * To change this template use File | Settings | File Templates.
 * @see MOPropertyEditorRegistry
 */
public class MOPropertyEditorRegistryMapImpl implements MOPropertyEditorRegistry {

    private Map<Class, MOPropertyEditor> propertyMappings = new HashMap<Class, MOPropertyEditor>();

    public void registerPropertyEditor(Class clazz, MOPropertyEditor pe) {
        propertyMappings.put(clazz, pe);
    }

    public MOPropertyEditor get(Class clazz) {
        Class<?> registeredClass = hasSuperClass(clazz);
        return propertyMappings.get(registeredClass);
    }

    public boolean has(Class clazz) {
        Class<?> registeredClass = hasSuperClass(clazz);
        return propertyMappings.containsKey(registeredClass);
    }

    private Class<?> hasSuperClass(Class<?> clazz) {
        if (propertyMappings.containsKey(clazz)) return clazz; else {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) return null; else return hasSuperClass(superClass);
        }
    }
}
