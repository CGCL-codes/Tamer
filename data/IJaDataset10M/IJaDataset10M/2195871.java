package com.google.gwt.place.testplaces;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Used by tests of {@link com.google.gwt.place.rebind.PlaceHistoryMapperGenerator}.
 */
public class Tokenizer3 implements PlaceTokenizer<Place3> {

    public Place3 getPlace(String token) {
        return new Place3(token);
    }

    public String getToken(Place3 place) {
        return place.content;
    }
}
