package com.genia.toolbox.projects.csv.bean.impl;

import org.springframework.util.StringUtils;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.projects.csv.bean.BinderVisitor;
import com.genia.toolbox.projects.csv.bean.EntityDescriptor;
import com.genia.toolbox.projects.csv.bean.MutableEntityBinder;

/**
 * implementation of {@link MutableEntityBinder}.
 */
public class EntityBinderImpl extends AbstractBinder implements MutableEntityBinder {

    /**
   * the name of the field.
   */
    private String entityName;

    /**
   * getter for the entityName property.
   * 
   * @return the entityName
   */
    public String getEntityName() {
        return entityName;
    }

    /**
   * setter for the entityName property.
   * 
   * @param entityName
   *          the entityName to set
   */
    public void setEntityName(String entityName) {
        this.entityName = StringUtils.trimWhitespace(entityName);
    }

    /**
   * visit method.
   * 
   * @param visitor
   *          the visitor to use
   * @throws BundledException
   *           if an error occured
   * @see com.genia.toolbox.projects.csv.bean.Binder#visit(com.genia.toolbox.projects.csv.bean.BinderVisitor)
   */
    public void visit(BinderVisitor visitor) throws BundledException {
        visitor.visitEntityBinder(this);
    }

    /**
   * returns the {@link EntityDescriptor} of the entity being binded.
   * 
   * @return the {@link EntityDescriptor} of the entity being binded
   * @see com.genia.toolbox.projects.csv.bean.EntityBinder#getBindedEntity()
   */
    public EntityDescriptor getBindedEntity() {
        return getEntityDescriptor().getFolderDescriptor().getEntity(getEntityName());
    }
}
