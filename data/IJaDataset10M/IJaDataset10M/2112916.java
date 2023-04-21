package org.shopformat.controller.admin.salesOrders.tax;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.shopformat.application.ShopService;
import org.shopformat.domain.order.sales.tax.TaxClass;
import org.shopformat.domain.order.sales.tax.TaxRate;
import org.shopformat.domain.order.sales.tax.TaxZone;

/**
 * @author Stephen Vangasse
 *
 * @version $Id$
 */
public class TestTaxMatrix {

    private Mockery context = new Mockery();

    private TaxMatrix matrix;

    private ShopService mockShopService;

    @Before
    public void setUp() throws Exception {
        matrix = new TaxMatrix();
        mockShopService = context.mock(ShopService.class);
        matrix.setShopService(mockShopService);
    }

    @Test
    public void testPrerender() {
        final TaxRate rateOne = new TaxRate();
        final TaxRate rateTwo = new TaxRate();
        final TaxRate rateThree = new TaxRate();
        final TaxRate rateFour = new TaxRate();
        final TaxZone taxZoneOne = new TaxZone();
        final TaxZone taxZoneTwo = new TaxZone();
        final List<TaxZone> zones = Arrays.asList(taxZoneOne, taxZoneTwo);
        final TaxClass taxClassOne = new TaxClass();
        final TaxClass taxClassTwo = new TaxClass();
        final List<TaxClass> classes = Arrays.asList(taxClassOne, taxClassTwo);
        context.checking(new Expectations() {

            {
                one(mockShopService).findAll(TaxZone.class);
                will(returnValue(zones));
                one(mockShopService).findAll(TaxClass.class);
                will(returnValue(classes));
                one(mockShopService).findRateForZoneAndClass(taxZoneOne, taxClassOne);
                will(returnValue(rateOne));
                one(mockShopService).findRateForZoneAndClass(taxZoneOne, taxClassTwo);
                will(returnValue(rateTwo));
                one(mockShopService).findRateForZoneAndClass(taxZoneTwo, taxClassOne);
                will(returnValue(rateThree));
                one(mockShopService).findRateForZoneAndClass(taxZoneTwo, taxClassTwo);
                will(returnValue(rateFour));
            }
        });
        matrix.prerender();
        assertEquals(2, matrix.getTaxClasses().size());
        assertEquals(2, matrix.getTaxZones().size());
        assertEquals(2, matrix.getMatrix().size());
        assertEquals(2, matrix.getMatrix().get(0).size());
        assertEquals(rateOne, matrix.getMatrix().get(0).get(0));
        assertEquals(rateTwo, matrix.getMatrix().get(1).get(0));
        assertEquals(rateThree, matrix.getMatrix().get(0).get(1));
        assertEquals(rateFour, matrix.getMatrix().get(1).get(1));
    }
}
