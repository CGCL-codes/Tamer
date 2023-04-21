package com.javector.soashopper.services;

import java.util.ArrayList;
import java.util.List;
import com.example.retail.OfferType;
import com.javector.soashopper.Category;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Price;
import com.javector.soashopper.Shopper;
import com.javector.util.TypeConverter;

public class ShopperService {

    public List<OfferType> offerSearch(String keywords, String categoryId, String currencyId, Double lowpriceVal, Double highpriceVal) {
        TypeConverter tc = new TypeConverter();
        Category category = tc.toCategory(categoryId);
        Price lowprice = tc.toPrice(currencyId, lowpriceVal);
        Price highprice = tc.toPrice(currencyId, highpriceVal);
        Shopper shoppingService = new Shopper();
        List<Offer> offerList = shoppingService.offerSearch(keywords, category, lowprice, highprice);
        ArrayList<OfferType> offerTypeList = new ArrayList<OfferType>();
        for (Offer o : offerList) {
            offerTypeList.add(tc.toOfferType(o));
        }
        return offerTypeList;
    }
}
