package com.tll.dao;

import org.testng.Assert;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProdCat;
import com.tll.model.ProductCategory;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;

/**
 * ProdCatDaoTestHandler
 * @author jpk
 */
public class ProdCatDaoTestHandler extends AbstractEntityDaoTestHandler<ProdCat> {

    private Long pkC, pkA, pkP, pkCa;

    @Override
    public Class<ProdCat> entityClass() {
        return ProdCat.class;
    }

    @Override
    public boolean supportsPaging() {
        return false;
    }

    @Override
    public void doPersistDependentEntities() {
        final Currency currency = createAndPersist(Currency.class, true);
        pkC = currency.getId();
        Asp account = create(Asp.class, true);
        account.setCurrency(currency);
        account = persist(account);
        pkA = account.getId();
        ProductInventory product = create(ProductInventory.class, true);
        product.setProductGeneral(create(ProductGeneral.class, true));
        product.setParent(account);
        product = persist(product);
        pkP = product.getId();
        ProductCategory category = create(ProductCategory.class, true);
        category.setParent(account);
        category = persist(category);
        pkCa = category.getId();
    }

    @Override
    public void doPurgeDependentEntities() {
        purge(ProductCategory.class, pkCa);
        purge(ProductInventory.class, pkP);
        purge(Account.class, pkA);
        purge(Currency.class, pkC);
    }

    @Override
    public void assembleTestEntity(ProdCat e) throws Exception {
        e.setProduct(load(ProductInventory.class, pkP));
        e.setCategory(load(ProductCategory.class, pkCa));
    }

    @Override
    public void verifyLoadedEntityState(ProdCat e) throws Exception {
        super.verifyLoadedEntityState(e);
        Assert.assertNotNull(e.getProduct());
        Assert.assertNotNull(e.getCategory());
        Assert.assertNotNull(e.getParent());
    }

    @Override
    public void alterTestEntity(ProdCat e) {
        super.alterTestEntity(e);
        Assert.assertTrue(e.getIsFeaturedProduct());
        e.setIsFeaturedProduct(false);
    }

    @Override
    public void verifyEntityAlteration(ProdCat e) throws Exception {
        super.verifyEntityAlteration(e);
        Assert.assertFalse(e.getIsFeaturedProduct());
    }
}
