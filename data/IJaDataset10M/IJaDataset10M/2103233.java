package com.msameer.gwttutor.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CustomerPlace extends Place {

    private String token;

    public CustomerPlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Prefix("Customer")
    public static class Tokenizer implements PlaceTokenizer<CustomerPlace> {

        @Override
        public String getToken(CustomerPlace place) {
            return place.getToken();
        }

        @Override
        public CustomerPlace getPlace(String token) {
            return new CustomerPlace(token);
        }
    }
}
