package com.yeep.study.patterns.factory.factoryMethod;

/**
 * Subclass of Pizza Store
 */
public class SHPizzaStore extends PizzaStore {

    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        if (PIZZA_TYPE_CHEESE.equalsIgnoreCase(type)) pizza = new SHCheesePizza(); else if (PIZZA_TYPE_VEGGLE.equalsIgnoreCase(type)) pizza = new SHVegglePizza();
        return pizza;
    }
}
