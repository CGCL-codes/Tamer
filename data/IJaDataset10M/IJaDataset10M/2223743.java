package com.americancoders.tests;

import com.americancoders.StockData;

/**
 * class used to generate data for testing application
 * 
 * 
 */
public class MockStockData implements StockData {

    boolean goodData = true;

    double inOpen[] = { 21.1, 22.1, 23.1, 24.1, 25.1, 22.1, 21.1 };

    double inHigh[] = { 21.1, 22.1, 23.1, 24.1, 25.1, 22.1, 21.1 };

    double inLow[] = { 21.1, 22.1, 23.1, 24.1, 25.1, 22.1, 21.1 };

    double inClose[] = { 21.1, 22.1, 23.1, 24.1, 25.1, 22.1, 21.1 };

    double inVolume[] = null;

    String inDate[] = { "Day 21", "Day 22", "Day 23", "Day 24", "Day 25", "Day 22", "Day 21" };

    public MockStockData() {
    }

    public boolean isGoodData() {
        return goodData;
    }

    public double[] getInClose() {
        return inClose;
    }

    public String[] getInDate() {
        return inDate;
    }

    public double[] getInHigh() {
        return inHigh;
    }

    public double[] getInLow() {
        return inLow;
    }

    public double[] getInOpen() {
        return inOpen;
    }

    public double[] getInVolume() {
        return inVolume;
    }
}
