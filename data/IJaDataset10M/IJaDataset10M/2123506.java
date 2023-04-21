package com.servengine.ecommerce;

import java.util.Comparator;
import com.servengine.ecommerce.ejb.Item;

public class PriceItemComparator implements Comparator<Item> {

    public PriceItemComparator() {
        super();
    }

    public int compare(Item o1, Item o2) {
        if (o1.getId().equals(o2.getId())) return 0;
        if (o1.getPrice() == o2.getPrice()) return o1.getId().compareTo(o2.getId());
        return o1.getPrice() < o2.getPrice() ? -1 : 1;
    }
}
