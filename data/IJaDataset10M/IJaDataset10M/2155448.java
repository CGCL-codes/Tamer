package com.genia.toolbox.projects.csv.bean;

import com.genia.toolbox.basics.exception.BundledException;

/**
 * interface that represent a condition for retrieve an entity described by a
 * CSV line.
 */
public interface Binder {

    /**
   * getter for the mappedPropertyName property.
   * 
   * @return the mappedPropertyName
   */
    public abstract String getMappedPropertyName();

    /**
   * returns the {@link Class} of the object to bind with.
   * 
   * @return the {@link Class} of the object to bind with
   */
    public abstract Class<?> getBindedObjectClass();

    /**
   * getter for the propertyName property.
   * 
   * @return the propertyName
   */
    public abstract String getPropertyName();

    /**
   * getter for the nullable property.
   * 
   * @return the nullable
   */
    public abstract Boolean getNullable();

    /**
   * returns the {@link EntityDescriptor} containing this binder.
   * 
   * @return the {@link EntityDescriptor} containing this binder
   */
    public abstract EntityDescriptor getEntityDescriptor();

    /**
   * visit method.
   * 
   * @param visitor
   *          the visitor to use
   * @throws BundledException
   *           if an error occured
   */
    public abstract void visit(BinderVisitor visitor) throws BundledException;
}
