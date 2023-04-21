package net.sf.brightside.belex.service.usage.hibernate;

import java.util.Date;
import org.testng.annotations.Test;
import net.sf.brightside.belex.core.spring.AbstractSpringTest;
import net.sf.brightside.belex.metamodel.Ownership;
import net.sf.brightside.belex.metamodel.SellOrder;
import net.sf.brightside.belex.metamodel.Stock;
import net.sf.brightside.belex.metamodel.Stockholder;
import net.sf.brightside.belex.service.crud.api.GetResults;
import net.sf.brightside.belex.service.usage.api.IssueSellOrder;
import net.sf.brightside.belex.service.usage.exceptions.NoOwnershipOverStockException;

public class HibernateIssueSellOrderTest extends AbstractSpringTest {

    private IssueSellOrder issueSellOrderUnderTest;

    private Stockholder helpStockholder;

    private Stock helpStock;

    private GetResults<SellOrder> dbGetter;

    private Stock helpStock2;

    private SellOrder helpSellOrder;

    private SellOrder provideSellOrder() {
        return (SellOrder) applicationContext.getBean(SellOrder.class.getName());
    }

    @Override
    protected IssueSellOrder createUnderTest() {
        return (IssueSellOrder) applicationContext.getBean(IssueSellOrder.class.getName());
    }

    private Stock provideStock() {
        return (Stock) applicationContext.getBean(Stock.class.getName());
    }

    private Stockholder provideStockholder() {
        return (Stockholder) applicationContext.getBean(Stockholder.class.getName());
    }

    private GetResults<SellOrder> provideDbGetter() {
        return (GetResults<SellOrder>) applicationContext.getBean(GetResults.class.getName());
    }

    private Ownership provideOwnership() {
        return (Ownership) applicationContext.getBean(Ownership.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        issueSellOrderUnderTest = createUnderTest();
        helpSellOrder = provideSellOrder();
        issueSellOrderUnderTest.setSellOrder(helpSellOrder);
        helpStock = provideStock();
        helpStockholder = provideStockholder();
        dbGetter = provideDbGetter();
        helpStock.setName("PLTK");
        getPersistenceManager().save(helpStock);
        helpStockholder.setAccountNum("12345");
        helpStockholder.setName("Nemanja");
        getPersistenceManager().save(helpStockholder);
        Ownership own = provideOwnership();
        own.setAmount(1000);
        own.setDate(new Date(System.currentTimeMillis()));
        own.setPrice(20);
        own.setStock(helpStock);
        own.setStockholder(helpStockholder);
        getPersistenceManager().save(own);
        helpStock2 = provideStock();
        helpStock2.setName("AIKB");
        getPersistenceManager().save(helpStock2);
    }

    @Test
    public void testIssuingSellOrder() throws Exception {
        dbGetter.setType(SellOrder.class);
        assertTrue(dbGetter.execute().isEmpty());
        assertFalse(getPersistenceManager().get(Stock.class).isEmpty());
        issueSellOrderUnderTest.issueSellOrder(helpStockholder, helpStock, 30);
        assertEquals(30, dbGetter.execute().get(0).getAmount());
    }

    @Test(expectedExceptions = NoOwnershipOverStockException.class)
    public void testIssuingSellOrderOnStockYouDontPosess() throws NoOwnershipOverStockException {
        issueSellOrderUnderTest.issueSellOrder(helpStockholder, helpStock2, 20);
    }
}
