package org.w4tj.model.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import org.hibernate.mapping.MetaAttribute;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.w4tj.context.ContextUtil;
import org.w4tj.model.Entity;
import org.w4tj.model.mapping.PersistedEntity;
import org.w4tj.model.mapping.PersistedProperty;
import org.w4tj.query.Criterion;
import org.w4tj.query.Query;

@Component
public class HibernateMetadataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private void registerEntities() {
        for (final Iterator<PersistentClass> iter = ContextUtil.getHibernateConfiguration().getClassMappings(); iter.hasNext(); ) {
            final PersistentClass meta = iter.next();
            final Query query = new Query(PersistedEntity.class);
            query.getCriterias().add(new Criterion(query, "name", "=", meta.getEntityName()));
            final List<Entity> result = ContextUtil.getDataReader().findByCriteria(query);
            Assert.assertTrue(result.size() <= 1);
            PersistedEntity persistedEntity;
            if (result.size() == 0) {
                persistedEntity = new PersistedEntity();
                persistedEntity.setName(meta.getEntityName());
                persistedEntity.setFriendlyName(meta.getEntityName());
                persistedEntity = (PersistedEntity) ContextUtil.getDataWriter().save(persistedEntity);
            } else {
                persistedEntity = (PersistedEntity) result.get(0);
            }
            registerProperties(persistedEntity);
        }
    }

    private void registerProperties(PersistedEntity persistedEntity) {
        final List<Property> props = new ArrayList<Property>();
        props.add(ContextUtil.getHibernateConfiguration().getClassMapping(persistedEntity.getName()).getIdentifierProperty());
        for (final Iterator<?> iter = ContextUtil.getHibernateConfiguration().getClassMapping(persistedEntity.getName()).getPropertyIterator(); iter.hasNext(); ) {
            final Property property = (Property) iter.next();
            props.add(property);
        }
        for (final Property property : props) {
            final Query query = new Query(PersistedProperty.class);
            query.getCriterias().add(new Criterion(query, "name", "=", property.getName()));
            query.getCriterias().add(new Criterion(query, "entity", "=", persistedEntity));
            final List<Entity> result = ContextUtil.getDataReader().findByCriteria(query);
            Assert.assertTrue(result.size() <= 1);
            PersistedProperty persistedProperty;
            if (result.size() == 0) {
                persistedProperty = new PersistedProperty();
                persistedProperty.setName(property.getName());
                persistedProperty.setFriendlyName(property.getName());
                persistedProperty.setEntity(persistedEntity);
                final MetaAttribute metaAttribute = property.getMetaAttribute("orderno");
                if (metaAttribute != null) {
                    persistedProperty.setOrderno(Integer.valueOf(metaAttribute.getValue()));
                }
                persistedProperty = (PersistedProperty) ContextUtil.getDataWriter().save(persistedProperty);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            registerEntities();
            ContextUtil.getMetaReader().refresh();
        }
    }
}
