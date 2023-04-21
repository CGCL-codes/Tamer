package com.googlecode.hibernate.audit.synchronization.work;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import com.googlecode.hibernate.audit.HibernateAudit;
import com.googlecode.hibernate.audit.configuration.AuditConfiguration;
import com.googlecode.hibernate.audit.model.AuditEvent;
import com.googlecode.hibernate.audit.model.clazz.AuditType;
import com.googlecode.hibernate.audit.model.clazz.AuditTypeField;
import com.googlecode.hibernate.audit.model.object.ComponentAuditObject;
import com.googlecode.hibernate.audit.model.object.EntityAuditObject;
import com.googlecode.hibernate.audit.model.property.AuditObjectProperty;
import com.googlecode.hibernate.audit.model.property.ComponentObjectProperty;
import com.googlecode.hibernate.audit.model.property.EntityObjectProperty;
import com.googlecode.hibernate.audit.model.property.SimpleObjectProperty;

public abstract class AbstractCollectionAuditWorkUnit extends AbstractAuditWorkUnit {

    protected void processElement(Session session, AuditConfiguration auditConfiguration, Object entityOwner, Object element, Type elementType, String propertyName, long index, EntityAuditObject auditObject, AuditEvent auditEvent) {
        AuditTypeField auditField = HibernateAudit.getAuditField(session, entityOwner.getClass().getName(), propertyName);
        AuditObjectProperty property = null;
        if (elementType.isEntityType()) {
            Serializable id = null;
            if (element != null) {
                id = session.getSessionFactory().getClassMetadata(((EntityType) elementType).getAssociatedEntityName()).getIdentifier(element, session.getEntityMode());
            }
            property = new EntityObjectProperty();
            property.setAuditObject(auditObject);
            property.setAuditField(auditField);
            property.setIndex(new Long(index));
            ((EntityObjectProperty) property).setTargetEntityId(auditConfiguration.getExtensionManager().getPropertyValueConverter().toString(id));
        } else if (elementType.isComponentType()) {
            CompositeType componentType = (CompositeType) elementType;
            property = new ComponentObjectProperty();
            property.setAuditObject(auditObject);
            property.setAuditField(auditField);
            property.setIndex(new Long(index));
            ComponentAuditObject targetComponentAuditObject = null;
            if (element != null) {
                targetComponentAuditObject = new ComponentAuditObject();
                targetComponentAuditObject.setAuditEvent(auditEvent);
                targetComponentAuditObject.setParentAuditObject(auditObject);
                AuditType auditComponentType = HibernateAudit.getAuditType(session, element.getClass().getName());
                targetComponentAuditObject.setAuditType(auditComponentType);
                for (int j = 0; j < componentType.getPropertyNames().length; j++) {
                    String componentPropertyName = componentType.getPropertyNames()[j];
                    Type componentPropertyType = componentType.getSubtypes()[j];
                    Object componentPropertyValue = componentType.getPropertyValue(element, j, (SessionImplementor) session);
                    processProperty(session, auditConfiguration, auditEvent, element, componentPropertyName, componentPropertyValue, componentPropertyType, targetComponentAuditObject);
                }
            }
            ((ComponentObjectProperty) property).setTargetComponentAuditObject(targetComponentAuditObject);
        } else if (elementType.isCollectionType()) {
        } else {
            property = new SimpleObjectProperty();
            property.setAuditObject(auditObject);
            property.setAuditField(auditField);
            property.setIndex(new Long(index));
            ((SimpleObjectProperty) property).setValue(auditConfiguration.getExtensionManager().getPropertyValueConverter().toString(element));
        }
        if (property != null) {
            AuditType auditType = null;
            if (element != null) {
                auditType = HibernateAudit.getAuditType(session, element.getClass().getName());
                if (auditType == null) {
                    auditType = property.getAuditField().getFieldType();
                }
            }
            property.setAuditType(auditType);
            auditObject.getAuditObjectProperties().add(property);
        }
    }
}
