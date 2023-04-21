package com.javector.soashopper.yahoo;

import com.javector.soashopper.OfferImp;
import com.javector.soashopper.OfferImpFactory;

public class YahooOfferImpFactory extends OfferImpFactory {

    @Override
    public OfferImp getOffer(String storeSpecificOfferId) {
        throw new UnsupportedOperationException("Yahoo Shopping does not support Offer IDs.");
    }
}
