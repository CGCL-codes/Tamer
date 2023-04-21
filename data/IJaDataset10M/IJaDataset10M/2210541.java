package com.googlecode.mgwt.examples.showcase.client.activities.animationdone;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * @author Daniel Kurka
 *
 */
public class AnimationSlideUpPlace extends Place {

    public static class AnimationSlideUpPlaceTokenizer implements PlaceTokenizer<AnimationSlideUpPlace> {

        @Override
        public AnimationSlideUpPlace getPlace(String token) {
            return new AnimationSlideUpPlace();
        }

        @Override
        public String getToken(AnimationSlideUpPlace place) {
            return "";
        }
    }
}
