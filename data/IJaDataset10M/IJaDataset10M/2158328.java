package com.cosmos.acacia.crm.bl.contactbook;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import com.cosmos.acacia.crm.bl.impl.EntityStoreManagerLocal;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.data.contacts.Passport;
import com.cosmos.acacia.crm.enums.PassportType;
import com.cosmos.beansbinding.EntityProperties;
import java.util.UUID;

/**
 * The implementation of handling passports (see interface for more info)
 *
 * @author Bozhidar Bozhanov
 */
@Stateless
public class PassportsListBean implements PassportsListRemote, PassportsListLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private EntityStoreManagerLocal esm;

    @SuppressWarnings("unchecked")
    public List<Passport> getPassports(UUID parentDataObjectId) {
        Query q;
        if (parentDataObjectId != null) {
            q = em.createNamedQuery("Passport.findByParentDataObjectAndDeleted");
            q.setParameter("parentDataObjectId", parentDataObjectId);
        } else {
            q = em.createNamedQuery("Passport.findByParentDataObjectIsNullAndDeleted");
        }
        q.setParameter("deleted", false);
        return new ArrayList<Passport>(q.getResultList());
    }

    public EntityProperties getPassportEntityProperties() {
        EntityProperties entityProperties = esm.getEntityProperties(Passport.class);
        entityProperties.setUpdateStrategy(UpdateStrategy.READ_WRITE);
        return entityProperties;
    }

    public Passport newPassport() {
        return new Passport();
    }

    public Passport savePassport(Passport passport, UUID parentDataObjectId) {
        passport.setParentId(parentDataObjectId);
        if (passport.getDataObject() == null) {
            DataObject dataObject = new DataObject();
            dataObject.setParentDataObjectId(parentDataObjectId);
            passport.setDataObject(dataObject);
        }
        esm.persist(em, passport);
        return passport;
    }

    public int deletePassport(Passport passport) {
        return esm.remove(em, passport);
    }

    public List<DbResource> getPassportTypes() {
        return PassportType.getDbResources();
    }
}
