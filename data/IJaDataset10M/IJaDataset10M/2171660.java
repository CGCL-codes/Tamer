package ua.org.hatu.daos.gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * A place object representing a particular state of the UI. A Place can be converted to and from a
 * URL history token by defining a {@link PlaceTokenizer} for each {@link Place}, and the 
 * {@link PlaceHistoryHandler} automatically updates the browser URL corresponding to each 
 * {@link Place} in your app.
 */
public class WelcomePlace extends Place {

    private static final String PLACE_PREFIX = "welcome";

    private String name;

    public WelcomePlace(String token) {
        this.name = token;
    }

    public String getName() {
        return name;
    }

    @Prefix(PLACE_PREFIX)
    public static class Tokenizer implements PlaceTokenizer<WelcomePlace> {

        public Tokenizer() {
        }

        @Override
        public String getToken(WelcomePlace place) {
            return place.getName();
        }

        @Override
        public WelcomePlace getPlace(String token) {
            return new WelcomePlace(token);
        }
    }
}
