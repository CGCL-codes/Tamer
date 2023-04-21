package com.googlecode.progrartifacts.sales.invoice.model;

import java.util.EnumSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import com.google.code.progrartifacts.sales.invoice.model.BasketItemAbstractFactory;
import com.google.code.progrartifacts.sales.invoice.model.BasketItems;
import com.google.code.progrartifacts.sales.invoice.model.ProductTaxRateType;

/**
 * Unit tests for the Basket Item Abstract Factory.
 * 
 * @author Marcello de Sales (marcello.desales@gmail.com)
 *
 */
public class BasketItemFactoryTests {

    private void assertBasketItemParsedCorrectly(int expectedQuantity, String expectedDescription, float expectedPrice, Set<ProductTaxRateType> expectedTaxRates, BasketItems item) {
        Assert.assertEquals("The quantity is incorrect.", expectedQuantity, item.getQuantity());
        Assert.assertEquals("The description is incorrect.", expectedDescription, item.getDescription());
        Assert.assertEquals("The price is incorrect.", expectedPrice, item.getUnitPrice().getValue());
        Assert.assertTrue(expectedTaxRates.containsAll(item.getApplicableTaxes()));
    }

    @Test
    public void parsingImportedGoods() {
        BasketItems item = BasketItemAbstractFactory.INSTANCE.create("1 imported bottle of perfume at 47.50");
        Set<ProductTaxRateType> expectedTaxes = EnumSet.of(ProductTaxRateType.IMPORTED, ProductTaxRateType.REGULAR);
        assertBasketItemParsedCorrectly(1, "imported bottle of perfume", 47.5f, expectedTaxes, item);
    }

    @Test
    public void parsingRegularGoods() {
        BasketItems item = BasketItemAbstractFactory.INSTANCE.create("1 music CD at 14.99");
        Set<ProductTaxRateType> expectedTaxes = EnumSet.of(ProductTaxRateType.REGULAR);
        assertBasketItemParsedCorrectly(1, "music CD", 14.99f, expectedTaxes, item);
    }

    @Test
    public void parsingExemptGoods() {
        BasketItems item = BasketItemAbstractFactory.INSTANCE.create("35 packet of headache pills at 9.75");
        Set<ProductTaxRateType> expectedTaxes = EnumSet.of(ProductTaxRateType.TAX_EXEMPT);
        assertBasketItemParsedCorrectly(35, "packet of headache pills", 9.75f, expectedTaxes, item);
    }

    @Test
    public void parsingExemptImportedGood() {
        BasketItems item = BasketItemAbstractFactory.INSTANCE.create("27 box of imported chocolates at 11.25");
        Set<ProductTaxRateType> expectedTaxes = EnumSet.of(ProductTaxRateType.TAX_EXEMPT, ProductTaxRateType.IMPORTED);
        assertBasketItemParsedCorrectly(27, "box of imported chocolates", 11.25f, expectedTaxes, item);
    }
}
