package org.apache.jdo.model.java;

/**
 * A JavaProperty instance represents a JavaBeans property.
 * 
 * @author Michael Bouschen
 * @version JDO 2.0
 */
public interface JavaProperty extends JavaField {

    /**
     * Returns the JavaMethod representation of the getter method for this
     * JavaProperty. If there is no getter method for this JavaProperty
     * (i.e. the property is write-only), then the method returns
     * <code>null</code>.
     * @return the getter method if available; or <code>null</code>
     * otherwise.
     */
    public JavaMethod getGetterMethod();

    /**
     * Returns the JavaMethod representation of the setter method for this
     * JavaProperty. If there is no setter method for this JavaProperty
     * (i.e. the property is read-only), then the method returns
     * <code>null</code>.
     * @return the setter method if available; or <code>null</code>
     * otherwise.
     */
    public JavaMethod getSetterMethod();
}
