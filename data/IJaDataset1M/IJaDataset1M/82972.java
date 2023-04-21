package org.az.calc.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class CalcActivityMapper implements ActivityMapper {

    private final EventBus eventBus;

    private final DefaultLayout layout;

    private final PlaceController placeController;

    private final AppRequestFactory requestFctory;

    @Inject
    public CalcActivityMapper(PlaceController placeController, final DefaultLayout layout, EventBus eventBus, AppRequestFactory requestFctory) {
        super();
        this.layout = layout;
        this.eventBus = eventBus;
        this.requestFctory = requestFctory;
        this.placeController = placeController;
    }

    @Override
    public Activity getActivity(final Place place) {
        if (place instanceof LoanCalcPlace) {
            return new LoanCalcActivity(eventBus, layout, requestFctory);
        }
        return null;
    }
}
