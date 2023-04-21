package org.reverspring.engine;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public final class BeanDescriptor {

    public static final int TYPE_BEAN = 0;

    public static final int TYPE_BASE = 1;

    public static final int TYPE_SET = 2;

    public static final int TYPE_LIST = 3;

    public static final int TYPE_MAP = 4;

    public static final String PROPERTY_CONTENT = "__content";

    private Object bean;

    private String id;

    private int type = TYPE_BEAN;

    private boolean isSingletonClass = false;

    private Constructor<?> constructor;

    private List<ObjectBinding> constructorArguments;

    private Map<String, ObjectBinding> propertyBindings;

    private Map<String, Set<ObjectBinding>> setProperties;

    private Map<String, List<ObjectBinding>> listProperties;

    private Map<String, Map<ObjectBinding, ObjectBinding>> mapProperties;

    public BeanDescriptor(Object bean) {
        this.bean = bean;
        propertyBindings = new HashMap<String, ObjectBinding>();
        setProperties = new HashMap<String, Set<ObjectBinding>>();
        listProperties = new HashMap<String, List<ObjectBinding>>();
        mapProperties = new HashMap<String, Map<ObjectBinding, ObjectBinding>>();
        constructorArguments = new ArrayList<ObjectBinding>();
    }

    /**
	 * adds a reference to another bean id to a property of this bean
	 * 
	 * @param property
	 * @param ref
	 */
    public void setPropertyBinding(String property, ObjectBinding ref) {
        propertyBindings.put(property, ref);
    }

    /**
	 * 
	 * @return all the properties of this bean
	 */
    public Set<String> getProperties() {
        Set<String> toReturn = new HashSet<String>();
        toReturn.addAll(propertyBindings.keySet());
        toReturn.addAll(setProperties.keySet());
        toReturn.addAll(listProperties.keySet());
        toReturn.addAll(mapProperties.keySet());
        return toReturn;
    }

    public ObjectBinding getBinding(String property) {
        return propertyBindings.get(property);
    }

    public int hashCode() {
        if (bean == null) return 0;
        if (isSingletonClass) {
            return bean.getClass().hashCode();
        }
        return bean.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (bean == null || ((BeanDescriptor) o).bean == null) return false;
        if (!(o instanceof BeanDescriptor)) return false;
        if (((BeanDescriptor) o).bean.getClass().equals(bean.getClass()) && ((BeanDescriptor) o).isSingletonClass == true) return true;
        if (this.getType() == TYPE_LIST) return false;
        if (this.getType() == TYPE_SET) return false;
        if (this.getType() == TYPE_MAP) return false;
        return ((BeanDescriptor) o).bean == this.bean;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
	 * adds a property that is a Set object
	 * 
	 * @param name
	 */
    public void addSetProperty(String name) {
        Set<ObjectBinding> value = new HashSet<ObjectBinding>();
        setProperties.put(name, value);
    }

    /**
	 * adds an item to a property that is a Set object
	 * 
	 * @param propertyName
	 *          the name of the property
	 */
    public void addSetPropertyValue(String propertyName, ObjectBinding binding) {
        setProperties.get(propertyName).add(binding);
    }

    public void addListProperty(String name) {
        List<ObjectBinding> value = new ArrayList<ObjectBinding>();
        listProperties.put(name, value);
    }

    public void addListPropertyValue(String propertyName, ObjectBinding binding) {
        listProperties.get(propertyName).add(binding);
    }

    public void addMapProperty(String name) {
        Map<ObjectBinding, ObjectBinding> value = new LinkedHashMap<ObjectBinding, ObjectBinding>();
        mapProperties.put(name, value);
    }

    public void addMapPropertyValue(String propertyName, ObjectBinding mapKey, ObjectBinding mapValue) {
        mapProperties.get(propertyName).put(mapKey, mapValue);
    }

    public Set<ObjectBinding> getSetProperty(String property) {
        return setProperties.get(property);
    }

    public List<ObjectBinding> getListProperty(String property) {
        return listProperties.get(property);
    }

    public Map<ObjectBinding, ObjectBinding> getMapProperty(String property) {
        return mapProperties.get(property);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSingletonClass() {
        return isSingletonClass;
    }

    public void setSingletonClass(boolean isSingletonClass) {
        this.isSingletonClass = isSingletonClass;
    }

    public void addConstructorArgument(ObjectBinding arg) {
        constructorArguments.add(arg);
    }

    public ObjectBinding[] getConstructorArguments() {
        ObjectBinding[] ret = new ObjectBinding[constructorArguments.size()];
        for (int i = 0; i < constructorArguments.size(); i++) ret[i] = constructorArguments.get(i);
        return ret;
    }
}
