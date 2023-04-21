package net.kansun.bjss.service.impl;

import static org.junit.Assert.assertEquals;
import net.kansun.bjss.domain.goods.Apple;
import net.kansun.bjss.domain.goods.Bread;
import net.kansun.bjss.domain.goods.Item;
import net.kansun.bjss.domain.goods.Item.Type;
import net.kansun.bjss.domain.goods.Milk;
import net.kansun.bjss.domain.goods.Soup;
import net.kansun.bjss.domain.util.Basket;
import net.kansun.bjss.domain.util.impl.MapBasedBasket;
import net.kansun.bjss.service.PriceCalculator;
import org.junit.Before;
import org.junit.Test;

public class PriceCalculatorUnitTest {

    private static final double BREAD_UNIT_PRICE = Item.BREAD_PRICE;

    private static final double SOUP_UNIT_PRICE = Item.SOUP_PRICE;

    private static final double APPLE_UNIT_PRICE = Item.APPLE_PRICE;

    private PriceCalculator calculator;

    private Basket basket;

    private static final Integer APPLE_AMOUNT = 7;

    private static final Integer SOUP_AMOUNT = 5;

    private static final Integer BREAD_AMOUNT = 3;

    @Before
    public void onSetUp() {
        calculator = new PriceCalculatorImpl();
        basket = MapBasedBasket.getInstance();
        loadBasket(APPLE_AMOUNT, Type.APPLE);
        loadBasket(SOUP_AMOUNT, Type.SOUP);
        loadBasket(BREAD_AMOUNT, Type.BREAD);
    }

    private void loadBasket(Integer amount, Type type) {
        for (int i = 1; i <= amount; i++) {
            switch(type) {
                case APPLE:
                    basket.putIn(new Apple());
                    break;
                case BREAD:
                    basket.putIn(new Bread());
                    break;
                case MILK:
                    basket.putIn(new Milk());
                    break;
                case SOUP:
                    basket.putIn(new Soup());
                    break;
            }
        }
    }

    @Test
    public void shouldReturnOriginalPriceOfAllGoods() {
        Double originalPrice = calculator.getOriginalPrice(basket);
        Double expectedPrice = APPLE_UNIT_PRICE * APPLE_AMOUNT + SOUP_UNIT_PRICE * SOUP_AMOUNT + BREAD_UNIT_PRICE * BREAD_AMOUNT;
        assertEquals(expectedPrice, originalPrice);
    }
}
