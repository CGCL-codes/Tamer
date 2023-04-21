package com.sin.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sin.client.activity.east.EastStartActivity;
import com.sin.client.places.StartPlace;

public class EastActivityMapper implements ActivityMapper {

    Provider<EastStartActivity> eastStartActivityProvider;

    @Inject
    public EastActivityMapper(Provider<EastStartActivity> eastStartActivityProvider) {
        super();
        this.eastStartActivityProvider = eastStartActivityProvider;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof StartPlace) {
            return eastStartActivityProvider.get().withPlace((StartPlace) place);
        } else {
            return null;
        }
    }
}
