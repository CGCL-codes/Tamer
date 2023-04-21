package com.cosmos.acacia.crm.bl.impl;

import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DataObjectBean;
import com.cosmos.acacia.crm.data.document.BusinessDocument;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.enums.DocumentType;
import com.cosmos.beansbinding.EntityProperties;
import com.cosmos.beansbinding.EntityProperty;
import java.util.UUID;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author miro
 */
@Local
public interface EntityStoreManagerLocal extends EntityStoreManagerRemote {

    DataObject saveDataObject(EntityManager em, Class entityClass);

    DataObject saveDataObject(EntityManager em, DataObject dataObject, String entityClassName, UUID parentId);

    void persist(EntityManager entityManager, Object entity);

    int remove(EntityManager entityManager, Object entity);

    /**
     * Returns entity properties for all annotated fields.
     * @param entityClass
     * @return
     */
    EntityProperties getEntityProperties(Class entityClass);

    /**
     * Create property details for a single property.
     * The property annotated can be either on a field or on a getter method.
     * @param entityClass
     * @param propertyName
     * @param position - any number. will be used to sort the property details according to the others
     * @return
     */
    EntityProperty createEntityProperty(Class entityClass, String propertyName, int position);

    DataObjectBean getDataObjectBean(EntityManager em, DataObject dataObject);

    DataObjectBean getDataObjectBean(EntityManager em, UUID dataObjectId);

    Address getParentAddress(EntityManager em, DataObject dataObject);

    void setDocumentNumber(EntityManager em, BusinessDocument documentEntity);

    <D extends BusinessDocument> D newBusinessDocument(DocumentType documentType);

    List<DbResource> getResources(EntityManager em, Class<? extends Enum> enumClass, Object... categoryClassifiers);
}
