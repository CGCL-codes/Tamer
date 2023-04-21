package net.kansun.bjss.domain;

public class Item {

    protected Double price;

    public static final Double APPLE_PRICE = 1d;

    public static final Double BREAD_PRICE = 0.8;

    public static final Double MILK_PRICE = 1.3;

    public static final Double SOUP_PRICE = 0.65;

    public enum Type {

        APPLE, BREAD, MILK, SOUP
    }
}
