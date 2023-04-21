package com.ee.bruscar.datamaker;

public class DataMaker {

    public String createData(String hint) {
        String[] items = hint.split(",");
        int index = getRandomIndex(items.length);
        return items[index];
    }

    public int getRandomIndex(int length) {
        double random = Math.random();
        double multiplied = random * length;
        Double dbl = new Double(multiplied);
        return dbl.intValue();
    }
}
