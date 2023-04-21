package com.javector.soashopper;

import com.javector.soashopper.ebay.EBayOfferImpFactory;
import com.javector.soashopper.yahoo.YahooOfferImpFactory;

public abstract class OfferImpFactory {

    public static OfferImpFactory newItemFactory(Store store) {
        switch(store) {
            case EBAY:
                return new EBayOfferImpFactory();
            case YAHOO:
                return new YahooOfferImpFactory();
            default:
                throw new IllegalArgumentException("unsupported store");
        }
    }

    public abstract OfferImp getOffer(String storeSpecificItemId);
}
