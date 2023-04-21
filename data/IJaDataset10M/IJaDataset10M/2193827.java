package com.knitml.validation.engine;

import com.knitml.validation.engine.settings.MarkerBehavior;

public interface Marker {

    MarkerBehavior getWhenWorkedThrough();

    boolean isGapMarker();
}
