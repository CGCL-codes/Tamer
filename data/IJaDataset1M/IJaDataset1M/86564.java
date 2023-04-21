package com.tallsoft.microspring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import com.tallsoft.microspring.BeanProperty.PropertyType;

/**
 * <p>
 * The microspring bean definition. Everything for runtime hangs off bean
 * definitions and properties.
 * </p>
 * 
 * @author J.Gibbons
 */
public class BeanDefinition {

    private String id;

    private String className;

    private String parentBeanId;

    private String dependsOn;

    private boolean isAbstract;

    private String initMethod;

    private String destroyMethod;

    private boolean isSingleton;

    private boolean lazyInit;

    private HashMap<String, BeanProperty> properties = new HashMap<String, BeanProperty>();

    public boolean hasNoClass() {
        return (className == null || className.length() == 0);
    }

    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the parentBeanId.
     */
    public String getParentBeanId() {
        return parentBeanId;
    }

    /**
     * @param parentBeanId
     *            The parentBeanId to set.
     */
    public void setParentBeanId(String parentBeanId) {
        this.parentBeanId = parentBeanId;
    }

    /**
     * @return Returns the initMethod.
     */
    public String getInitMethod() {
        return initMethod;
    }

    /**
     * @param initMethod
     *            The initMethod to set.
     */
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    /**
     * @return Returns the destroyMethod.
     */
    public String getDestroyMethod() {
        return destroyMethod;
    }

    /**
     * @param destroyMethod
     *            The destroyMethod to set.
     */
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    /**
     * @return Returns the isAbstract.
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * @param isAbstract
     *            The isAbstract to set.
     */
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * @return Returns the isSingleton.
     */
    public boolean isSingleton() {
        return isSingleton;
    }

    /**
     * @param isSingleton
     *            The isSingleton to set.
     */
    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }

    /**
     * @return Returns the lazyLoad.
     */
    public boolean isLazyInit() {
        return lazyInit;
    }

    /**
     * @param lazyLoad The lazyLoad to set.
     */
    public void setLazyInit(boolean lazyLoad) {
        this.lazyInit = lazyLoad;
    }

    /**
     * @param dependsOn
     *            The dependsOn to set.
     */
    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void addProperty(String name, BeanProperty prop) {
        properties.put(name, prop);
    }

    public HashMap<String, BeanProperty> getProperties() {
        return properties;
    }

    public ArrayList<String> getAllBeanRefProperties() throws BeansException {
        ArrayList<String> ret = new ArrayList<String>();
        for (BeanProperty prop : properties.values()) {
            if (prop.getType() == PropertyType.VALUE) {
                if (prop.getValue() == null) {
                    throw new BeansException("bean [" + id + "] has value property [" + prop.getName() + "] which has a null value - Illegal.");
                } else if (prop.getValue().getBeanRef() != null) {
                    ret.add(prop.getValue().getBeanRef());
                }
            }
            if (prop.getType() == PropertyType.LIST) {
                ArrayList<PropertyValue> vals = prop.getListValues();
                if (vals == null) {
                    throw new BeansException("bean [" + id + "] has list properties [" + prop.getName() + "] which are null - Illegal.");
                }
                for (PropertyValue val : vals) {
                    if (val.getBeanRef() != null) {
                        ret.add(val.getBeanRef());
                    }
                }
            }
            if (prop.getType() == PropertyType.MAP) {
                HashMap<String, PropertyValue> vals = prop.getMapValues();
                for (PropertyValue val : vals.values()) {
                    if (val.getBeanRef() != null) {
                        ret.add(val.getBeanRef());
                    }
                }
            }
            if (prop.getType() == PropertyType.SET) {
                Set<PropertyValue> vals = prop.getSetValues();
                for (PropertyValue val : vals) {
                    if (val.getBeanRef() != null) {
                        ret.add(val.getBeanRef());
                    }
                }
            }
            if (prop.getType() == PropertyType.PROP) {
                Properties vals = prop.getPropertyValues();
                Iterator iter = vals.values().iterator();
                while (iter.hasNext()) {
                    PropertyValue val = (PropertyValue) iter.next();
                    if (val.getBeanRef() != null) {
                        ret.add(val.getBeanRef());
                    }
                }
            }
        }
        return ret;
    }
}
