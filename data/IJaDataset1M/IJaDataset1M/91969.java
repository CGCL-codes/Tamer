package com.cosmos.acacia.crm.bl.impl;

import java.util.UUID;
import java.sql.BatchUpdateException;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import com.cosmos.acacia.app.AcaciaSessionLocal;
import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DataObjectBean;
import com.cosmos.acacia.crm.data.DataObjectType;
import com.cosmos.acacia.crm.data.document.BusinessDocument;
import com.cosmos.acacia.crm.data.document.BusinessDocumentStatusLog;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.data.EnumClass;
import com.cosmos.acacia.crm.data.contacts.Organization;
import com.cosmos.acacia.crm.enums.DatabaseResource;
import com.cosmos.acacia.crm.enums.DocumentStatus;
import com.cosmos.acacia.crm.enums.DocumentType;
import com.cosmos.acacia.crm.enums.EnumClassifier;
import com.cosmos.acacia.crm.validation.ValidationException;
import com.cosmos.acacia.entity.AcaciaEntityAttributes;
import com.cosmos.beansbinding.EntityProperties;
import com.cosmos.beansbinding.EntityProperty;
import com.cosmos.util.NumberUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author miro
 */
@Stateless
public class EntityStoreManagerBean implements EntityStoreManagerLocal {

    private static final long DOCUMENT_NUMBER_MULTIPLIER = 10000000;

    @EJB
    private DataObjectTypeLocal dotLocal;

    @EJB
    private AcaciaSessionLocal session;

    @EJB
    private EntitySequenceServiceLocal entitySequenceService;

    private static Integer addressDataObjectTypeId;

    private Map<String, EntityProperties> entityPropertiesMap = new TreeMap<String, EntityProperties>();

    @Override
    public DataObject saveDataObject(EntityManager em, Class entityClass) {
        return saveDataObject(em, null, entityClass.getName(), null);
    }

    @Override
    public DataObject saveDataObject(EntityManager em, DataObject dataObject, String entityClassName, UUID parentId) {
        DataObjectType dot = getDataObjectTypeLocal().getDataObjectType(entityClassName);
        if (dataObject == null) {
            dataObject = new DataObject();
        }
        dataObject.setParentDataObjectId(parentId);
        dataObject.setDataObjectType(dot);
        dataObject.setDataObjectVersion(1);
        UUID creatorId = null;
        UUID ownerId = null;
        try {
            creatorId = session.getUser().getUserId();
            ownerId = session.getBranch().getAddressId();
        } catch (Exception ex) {
            creatorId = NumberUtils.ZERO_UUID;
            ownerId = NumberUtils.ZERO_UUID;
        }
        dataObject.setCreatorId(creatorId);
        dataObject.setOwnerId(ownerId);
        em.persist(dataObject);
        return dataObject;
    }

    @Override
    public void persist(EntityManager em, Object entity) {
        boolean mustMerge = false;
        BusinessDocument oldBusinessDocument = null;
        if (entity instanceof BusinessDocument && ((BusinessDocument) entity).getId() != null) {
            oldBusinessDocument = (BusinessDocument) getDataObjectBean(em, ((BusinessDocument) entity).getDataObject());
        }
        if (entity instanceof DataObjectBean) {
            DataObjectBean doBean = (DataObjectBean) entity;
            UUID id = doBean.getId();
            DataObject dataObject = doBean.getDataObject();
            UUID parentId = doBean.getParentId();
            if (id == null) {
                if (dataObject == null || dataObject.getDataObjectId() == null) {
                    dataObject = saveDataObject(em, dataObject, entity.getClass().getName(), parentId);
                } else {
                    UUID doParentId = dataObject.getParentDataObjectId();
                    boolean mustUpdateDO = false;
                    if (parentId != null) {
                        if (doParentId == null || !parentId.equals(doParentId)) {
                            dataObject.setParentDataObjectId(parentId);
                            mustUpdateDO = true;
                        }
                    } else if (doParentId != null) {
                        dataObject.setParentDataObjectId(parentId);
                        mustUpdateDO = true;
                    }
                    if (mustUpdateDO) {
                        dataObject = em.merge(dataObject);
                        em.persist(dataObject);
                    }
                }
                id = dataObject.getDataObjectId();
                doBean.setId(id);
            } else {
                mustMerge = true;
                if (dataObject == null) {
                    dataObject = em.find(DataObject.class, id);
                } else {
                    dataObject = em.merge(dataObject);
                }
                int version = dataObject.getDataObjectVersion();
                dataObject.setParentDataObjectId(parentId);
                dataObject.setDataObjectVersion(version + 1);
                em.persist(dataObject);
            }
            if (dataObject.getOrderPosition() == null) {
                setOrderPosition(em, dataObject);
            }
            doBean.setDataObject(dataObject);
        } else {
            mustMerge = !(entity.hashCode() == 0);
        }
        if (mustMerge) {
            entity = em.merge(entity);
        }
        em.persist(entity);
        if (entity instanceof BusinessDocument) {
            BusinessDocument businessDocument = (BusinessDocument) entity;
            DbResource docStatus;
            if ((docStatus = businessDocument.getDocumentStatus()) != null) {
                DbResource oldDocStatus;
                if (oldBusinessDocument != null) {
                    oldDocStatus = oldBusinessDocument.getDocumentStatus();
                } else {
                    oldDocStatus = null;
                }
                if (!docStatus.equals(oldDocStatus)) {
                    BusinessDocumentStatusLog log = new BusinessDocumentStatusLog(businessDocument.getDocumentId(), docStatus.getResourceId(), new Date());
                    log.setOfficer(session.getPerson());
                    em.persist(log);
                }
            }
        }
    }

    @Override
    public void setDocumentNumber(EntityManager em, BusinessDocument businessDocument) {
        businessDocument.setDocumentDate(new Date());
        UUID parentEntityId = businessDocument.getParentId();
        DataObject dataObject = businessDocument.getDataObject();
        int dataObjectTypeId = dataObject.getDataObjectType().getDataObjectTypeId();
        Address branch = businessDocument.getPublisherBranch();
        if (branch.getOrderPosition() == null) {
            persist(em, branch);
        }
        long initialValue = branch.getOrderPosition() * DOCUMENT_NUMBER_MULTIPLIER;
        long docNumber = entitySequenceService.nextValue(parentEntityId, dataObjectTypeId, initialValue);
        businessDocument.setDocumentNumber(docNumber);
    }

    private void setOrderPosition(EntityManager em, DataObject dataObject) {
        UUID parentDataObjectId;
        Query q;
        if ((parentDataObjectId = dataObject.getParentDataObjectId()) != null) {
            q = em.createNamedQuery("DataObject.maxOrderPositionByParentDataObjectIdAndDataObjectType");
            q.setParameter("parentDataObjectId", parentDataObjectId);
        } else {
            q = em.createNamedQuery("DataObject.maxOrderPositionByDataObjectType");
        }
        q.setParameter("dataObjectType", dataObject.getDataObjectType());
        Integer maxOrderPosition;
        try {
            maxOrderPosition = (Integer) q.getSingleResult();
        } catch (NoResultException ex) {
            maxOrderPosition = 0;
        }
        if (maxOrderPosition == null) {
            maxOrderPosition = 0;
        }
        dataObject.setOrderPosition(maxOrderPosition + 1);
        em.persist(dataObject);
    }

    @Override
    public int remove(EntityManager em, Object entity) {
        int version = -1;
        if (entity instanceof DataObjectBean) {
            DataObjectBean doBean = (DataObjectBean) entity;
            DataObject dataObject = doBean.getDataObject();
            if (dataObject == null) {
                dataObject = em.find(DataObject.class, doBean.getId());
            } else {
                dataObject = em.merge(dataObject);
            }
            version = dataObject.getDataObjectVersion() + 1;
            dataObject.setDataObjectVersion(version);
            dataObject.setDeleted(true);
            em.persist(dataObject);
        }
        entity = em.merge(entity);
        em.remove(entity);
        try {
            em.flush();
        } catch (PersistenceException pe) {
            throw new ValidationException(getRootCauseMessage(pe));
        }
        return version;
    }

    private String getRootCauseMessage(Throwable ex) {
        if (ex.getCause() != null) {
            return getRootCauseMessage(ex.getCause());
        }
        if (ex instanceof BatchUpdateException) {
            BatchUpdateException bue = (BatchUpdateException) ex;
            String message = bue.getNextException().getMessage();
            message = message.substring(message.lastIndexOf("from table "));
            message = message.replaceAll("\"", "");
            message = message.replaceAll("\\.", "");
            message = message.replaceAll("from table ", "");
            return message;
        }
        return "";
    }

    private DataObjectTypeLocal getDataObjectTypeLocal() {
        if (dotLocal == null) {
            try {
                dotLocal = InitialContext.doLookup(DataObjectTypeLocal.class.getName());
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return dotLocal;
    }

    @Override
    public void prePersist(DataObjectBean entity) {
        System.out.println("EntityStoreManager.prePersist: " + entity);
    }

    @Override
    public EntityProperties getEntityProperties(Class entityClass) {
        boolean debug = false && Organization.class == entityClass;
        String entityClassName = entityClass.getName();
        EntityProperties entityProperties = entityPropertiesMap.get(entityClassName);
        if (debug && entityProperties != null) {
            System.out.println("entityProperties=" + entityProperties);
        }
        if (entityProperties == null) {
            entityProperties = new EntityProperties(entityClass, AcaciaEntityAttributes.getEntityAttributesMap());
            entityPropertiesMap.put(entityClassName, entityProperties);
        }
        EntityProperties clonedEP = (EntityProperties) entityProperties.clone();
        if (debug) {
            System.out.println("clonedEP=" + clonedEP);
        }
        return clonedEP;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityProperty createEntityProperty(Class entityClass, String propertyName, int position) {
        return EntityProperty.createEntityProperty(entityClass, propertyName, position, AcaciaEntityAttributes.getEntityAttributesMap());
    }

    @Override
    public DataObjectBean getDataObjectBean(EntityManager em, DataObject dataObject) {
        if (dataObject == null) {
            return null;
        }
        DataObjectType dot = dataObject.getDataObjectType();
        Class cls;
        try {
            cls = Class.forName(dot.getDataObjectType());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        DataObjectBean dob = (DataObjectBean) em.find(cls, dataObject.getDataObjectId());
        return dob;
    }

    @Override
    public DataObjectBean getDataObjectBean(EntityManager em, UUID dataObjectId) {
        DataObject dataObject = em.find(DataObject.class, dataObjectId);
        if (dataObject == null) {
            return null;
        }
        return getDataObjectBean(em, dataObject);
    }

    public DataObjectBean getParentEntity(EntityManager em, DataObjectBean entity) {
        return getParentEntity(em, entity.getDataObject());
    }

    public DataObjectBean getParentEntity(EntityManager em, DataObject dataObject) {
        UUID parentDataObjectId;
        if ((parentDataObjectId = dataObject.getParentDataObjectId()) == null) {
            return null;
        }
        return getDataObjectBean(em, parentDataObjectId);
    }

    private Integer getAddressDotId() {
        if (addressDataObjectTypeId == null) {
            DataObjectType dot = dotLocal.getDataObjectType(Address.class.getName());
            addressDataObjectTypeId = dot.getDataObjectTypeId();
        }
        return addressDataObjectTypeId;
    }

    @Override
    public Address getParentAddress(EntityManager em, DataObject dataObject) {
        int addressDotId = getAddressDotId();
        while (dataObject != null && dataObject.getDataObjectType().getDataObjectTypeId() != addressDotId) {
            UUID parentId;
            if ((parentId = dataObject.getParentDataObjectId()) == null) {
                return null;
            }
            dataObject = em.find(DataObject.class, parentId);
        }
        if (dataObject == null) {
            return null;
        }
        return em.find(Address.class, dataObject.getDataObjectId());
    }

    @Override
    public <D extends BusinessDocument> D newBusinessDocument(DocumentType documentType) {
        try {
            Class<? extends BusinessDocument> cls = documentType.getDocumentClass();
            D document = (D) cls.newInstance();
            document.setPublisher(session.getOrganization());
            document.setPublisherBranch(session.getBranch());
            document.setPublisherOfficer(session.getPerson());
            document.setDocumentType(documentType.getDbResource());
            document.setDocumentStatus(DocumentStatus.Draft.getDbResource());
            document.setCreationTime(new Date());
            DataObject dataObject = new DataObject();
            dataObject.setDataObjectType(dotLocal.getDataObjectType(cls.getName()));
            document.setDataObject(dataObject);
            return document;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<DbResource> getResources(EntityManager em, Class<? extends Enum> cls, Object... categoryClassifiers) {
        Enum[] enumConstants = cls.getEnumConstants();
        Enum firstItem = enumConstants[0];
        if (firstItem instanceof EnumClassifier) {
            List<? extends DatabaseResource> list = ((EnumClassifier) firstItem).getEnums(categoryClassifiers);
            List<DbResource> resources = new ArrayList<DbResource>(list.size());
            for (DatabaseResource item : list) {
                resources.add(item.getDbResource());
            }
            return resources;
        } else if (firstItem instanceof DatabaseResource) {
            List<DbResource> resources = new ArrayList<DbResource>(enumConstants.length);
            for (Enum item : enumConstants) {
                resources.add(((DatabaseResource) item).getDbResource());
            }
            return resources;
        }
        Query q = em.createNamedQuery("EnumClass.findByEnumClassName");
        q.setParameter("enumClassName", cls.getName());
        EnumClass enumClass = (EnumClass) q.getSingleResult();
        q = em.createNamedQuery("DbResource.findAllByEnumClass");
        q.setParameter("enumClass", enumClass);
        return new ArrayList<DbResource>(q.getResultList());
    }
}
