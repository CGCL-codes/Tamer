package com.cosmos.acacia.crm.bl.cash;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import com.cosmos.acacia.crm.bl.impl.EntityStoreManagerLocal;
import com.cosmos.acacia.crm.data.accounting.BanknoteQuantity;
import com.cosmos.acacia.crm.data.accounting.CurrencyNominal;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.validation.impl.BanknoteQuantityValidatorLocal;
import com.cosmos.acacia.entity.AcaciaEntityAttributes;
import com.cosmos.beansbinding.EntityProperties;
import com.cosmos.beansbinding.EntityProperty;

/**
 * 
 * Created	:	02.05.2009
 * @author	Petar Milev
 *
 */
@Stateless
public class BanknoteQuantityBean implements BanknoteQuantityRemote, BanknoteQuantityLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private EntityStoreManagerLocal esm;

    @EJB
    private BanknoteQuantityValidatorLocal validator;

    @Override
    public EntityProperties getListingEntityProperties() {
        EntityProperties ep = esm.getEntityProperties(BanknoteQuantity.class);
        ep.removeEntityProperty("currencyNominal");
        ep.addEntityProperty(EntityProperty.createEntityProperty("currencyNominal.currency.enumValue.code", "Currency", String.class.getName(), 10, AcaciaEntityAttributes.getEntityAttributesMap()));
        ep.setOrderPosition("quantity", 20);
        ep.addEntityProperty(EntityProperty.createEntityProperty("currencyNominal.nominal", "Nominal", BigDecimal.class.getName(), 30, AcaciaEntityAttributes.getEntityAttributesMap()));
        return ep;
    }

    @SuppressWarnings("unchecked")
    public List<BanknoteQuantity> listBanknoteQuantitys(UUID parentDataObjectId) {
        throw new UnsupportedOperationException("ToDO");
    }

    public void deleteBanknoteQuantity(BanknoteQuantity banknoteQuantity) {
        if (banknoteQuantity == null) throw new IllegalArgumentException("null: 'BanknoteQuantity'");
        esm.remove(em, banknoteQuantity);
    }

    public BanknoteQuantity newBanknoteQuantity(UUID parentId) {
        BanknoteQuantity c = new BanknoteQuantity();
        c.setParentId(parentId);
        c.setCurrencyNominal(new CurrencyNominal());
        return c;
    }

    public EntityProperties getDetailEntityProperties() {
        EntityProperties ep = esm.getEntityProperties(BanknoteQuantity.class);
        ep.setUpdateStrategy(UpdateStrategy.READ_WRITE);
        ep.addEntityProperty(EntityProperty.createEntityProperty("currencyNominal.currency", "Currency", DbResource.class.getName(), 10, AcaciaEntityAttributes.getEntityAttributesMap()));
        ep.setOrderPosition("quantity", 20);
        ep.addEntityProperty(EntityProperty.createEntityProperty("currencyNominal.nominal", "Nominal", BigDecimal.class.getName(), 30, AcaciaEntityAttributes.getEntityAttributesMap()));
        return ep;
    }

    public BanknoteQuantity saveBanknoteQuantity(BanknoteQuantity entity) {
        validator.validate(entity);
        esm.persist(em, entity);
        return entity;
    }
}
