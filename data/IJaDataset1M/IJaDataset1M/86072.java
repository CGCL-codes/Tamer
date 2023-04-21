package com.genia.toolbox.projects.csv.bean;

import java.util.List;

/**
 * mutable {@link EntityDescriptor}.
 */
public interface MutableEntityDescriptor extends EntityDescriptor, MutableFolderDescriptorLinkable {

    /**
   * setter for the binders property.
   * 
   * @param binders
   *          the binders to set
   */
    public abstract void setBinders(List<Binder> binders);

    /**
   * setter for the entityClass property.
   * 
   * @param entityClass
   *          the entityClass to set
   */
    public abstract void setEntityClass(Class<?> entityClass);

    /**
   * setter for the name property.
   * 
   * @param name
   *          the name to set
   */
    public abstract void setName(String name);

    /**
   * setter for the unique property.
   * 
   * @param unique
   *          the unique to set
   */
    public abstract void setUnique(Boolean unique);

    /**
   * setter for the createIfNull property.
   * 
   * @param createIfNull
   *          the createIfNull to set
   */
    public abstract void setCreateIfNull(Boolean createIfNull);

    /**
   * setter for the errorIfNull property.
   * 
   * @param errorIfNull
   *          the errorIfNull to set
   */
    public abstract void setErrorIfNull(Boolean errorIfNull);
}
