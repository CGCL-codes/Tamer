package com.neolab.crm.client.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.neolab.crm.shared.resources.HasLabel;

public class HomePlace extends Place implements HasLabel {

    private final String label = "home";

    public static Integer tabID = 0;

    public HomePlace() {
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static class Tokenizer implements PlaceTokenizer<HomePlace> {

        @Override
        public String getToken(HomePlace place) {
            return place.getLabel();
        }

        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
