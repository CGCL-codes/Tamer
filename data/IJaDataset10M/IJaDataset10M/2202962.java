package com.negatlov.Decorator1;

public class Client {

    public static void main(String[] args) {
        Beverage beverage = new Whip(new Mocha(new Coffee()));
        beverage.describe();
        System.out.println(beverage.cost());
    }
}
