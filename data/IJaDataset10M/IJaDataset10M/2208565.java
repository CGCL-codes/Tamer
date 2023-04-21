package com.yeep.study.patterns.factory.simpleFactory;

public class Client {

    public static void main(String[] args) {
        PizzaStore store = new PizzaStore();
        Pizza cheesePizza = store.orderPizza("CHEESE");
        Pizza vegglePizza = store.orderPizza("VEGGLE");
    }
}
