package com.inepex.example.ineForm.entity.dao;

import javax.persistence.EntityManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.inepex.example.ineForm.entity.Contact;
import com.inepex.example.ineForm.entity.dao.query.ContactQuery;
import com.inepex.example.ineForm.entity.mapper.ContactMapper;
import com.inepex.ineForm.server.BaseDao;
import com.inepex.ineForm.server.BaseMapper;
import com.inepex.ineForm.server.BaseQuery;
import com.inepex.ineForm.server.CriteriaSelector;
import com.inepex.ineForm.server.SelectorCustomizer;
import com.inepex.ineForm.shared.dispatch.ManipulationObjectFactory;
import com.inepex.ineom.shared.AssistedObjectHandlerFactory;
import com.inepex.ineom.shared.descriptor.DescriptorStore;

/**
 * Just generated once, don't need to regenerate after modifying attributes!
 * 
 * To customize persist, merge or remove behaviour override persist(E), merge(E) or remove(E). (Don't 
 * forget to call super.persist, super.merge ...)
 * 
 */
@Singleton
public class ContactDao extends BaseDao<Contact> {

    public static interface ContactSelectorCustomizer extends SelectorCustomizer<CriteriaSelector<?, Contact>> {
    }

    private final DescriptorStore descStore;

    @Inject
    public ContactDao(Provider<EntityManager> em, ManipulationObjectFactory objectFactory, AssistedObjectHandlerFactory handlerFactory, DescriptorStore descStore) {
        super(em, objectFactory, handlerFactory);
        this.descStore = descStore;
    }

    @Override
    public BaseQuery<Contact> getQuery() {
        return new ContactQuery(descStore);
    }

    @Override
    public BaseMapper<Contact> getMapper() {
        return new ContactMapper(descStore);
    }

    @Override
    public CriteriaSelector<Contact, Contact> getSelector() {
        return new CriteriaSelector<Contact, Contact>(em, getQuery(), Contact.class, Contact.class);
    }

    @Override
    public CriteriaSelector<Long, Contact> getCountSelector() {
        return new CriteriaSelector<Long, Contact>(em, getQuery(), Long.class, Contact.class);
    }

    @Override
    public Class<Contact> getClazz() {
        return Contact.class;
    }

    @Override
    public Contact newInstance() {
        return new Contact();
    }
}
