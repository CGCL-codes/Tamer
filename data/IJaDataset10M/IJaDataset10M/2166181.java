package com.videostore.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;

public class Basket implements NodeWrapper {

    Node n;

    Map<String, ArticleInBasket> items;

    private int puntiPromoUsed;

    private double total;

    public Basket() {
        this(null);
        items = new HashMap<String, ArticleInBasket>();
        total = 0;
    }

    public Basket(Node n) {
        this.n = n;
    }

    public Node getNode() {
        return n;
    }

    public Map<String, ArticleInBasket> getItems() {
        return items;
    }

    public void setItems(Map<String, ArticleInBasket> items) {
        this.items = items;
    }

    public void setPuntiPromoUsed(int puntiPromoUsed) {
        this.puntiPromoUsed = puntiPromoUsed;
    }

    public int getPuntiPromoUsed() {
        return puntiPromoUsed;
    }

    public void checkTotal(WareHouse w) {
        double t = 0;
        for (Entry<String, ArticleInBasket> i : items.entrySet()) {
            ArticleInProduct aip = w.findArticle(i.getKey());
            t += aip.getArticle().getPrice() * i.getValue().getAmount();
        }
        t = t - getPuntiPromoUsed();
        if (t < 0) t = 0;
        setTotal(t);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }
}
