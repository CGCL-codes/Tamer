package com.rezzix.kowa.market;

import java.io.Serializable;
import java.util.ArrayList;
import com.rezzix.kowa.util.Constants;

public class Merchandise implements Serializable {

    String name;

    String unit;

    int trend;

    int refPrice;

    int price;

    ArrayList<Integer> pricesHist = new ArrayList<Integer>();

    public ArrayList<Integer> getPricesHist() {
        return pricesHist;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        pricesHist.add(price);
        if (pricesHist.size() > Constants.priceHistorySize) {
            pricesHist.remove(0);
        }
        this.price = price;
    }

    public int getRefPrice() {
        return refPrice;
    }

    public Merchandise(String name, String unit, int price) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.refPrice = price;
        pricesHist.add(price);
        trend = Market.randomTrend();
    }

    @Override
    public String toString() {
        return "Merchandise " + name + " (" + price + "/" + unit + ")";
    }

    public String getEvolution() {
        StringBuffer buff = new StringBuffer();
        for (Integer price : pricesHist) {
            buff.append(price + "\t");
        }
        return buff.toString();
    }

    public Boolean isRising() {
        int size = pricesHist.size();
        if (size > 1) {
            if (pricesHist.get(size - 1) > pricesHist.get(size - 2)) {
                return true;
            } else if (pricesHist.get(size - 1) < pricesHist.get(size - 2)) {
                return false;
            }
        }
        return null;
    }
}
