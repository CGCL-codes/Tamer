package net.sf.jtonic.core;

public class Drink {

    private int price;

    private final int maxprice;

    private final int minprice;

    private int sellstotal;

    private int sellslast;

    private String name;

    private int[] pricehistory = new int[60];

    private int ticker = 0;

    public Drink(int initprice, int minprice, int maxprice, String name) {
        this.maxprice = maxprice;
        this.minprice = minprice;
        this.setName(name);
        price = initprice;
        sellstotal = 0;
        sellslast = 0;
        pricehistory[0] = price;
        for (int i = 1; i < 60; i++) {
            pricehistory[i] = 0;
        }
        ticker = 0;
    }

    public void tick() {
        ticker++;
        pricehistory[ticker] = price;
    }

    public void buyDrink() {
        sellstotal++;
        sellslast++;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSellstotal() {
        return sellstotal;
    }

    public void setSellstotal(int sellstotal) {
        this.sellstotal = sellstotal;
    }

    public int getSellslast() {
        return sellslast;
    }

    public void setSellslast(int sellslast) {
        this.sellslast = sellslast;
    }

    public int getMaxprice() {
        return maxprice;
    }

    public int getMinprice() {
        return minprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getPricehistory() {
        return pricehistory;
    }
}
