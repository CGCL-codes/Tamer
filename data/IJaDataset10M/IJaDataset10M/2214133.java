package com.trohko.jfsim.aircraft.cockpit.c2d.elements.gauges;

import com.trohko.jfsim.aircraft.cockpit.base.elements.gauges.IAttitudeIndicatorGauge;
import com.trohko.jfsim.aircraft.cockpit.c2d.elements.IGauge2D;
import com.trohko.jfsim.aircraft.cockpit.c2d.elements.indicators.IAttitudeIndicator2D;
import com.trohko.jfsim.aircraft.cockpit.c2d.elements.indicators.IRollIndicator2D;
import com.trohko.jfsim.core.IPosition2D;
import com.trohko.jfsim.core.ISize2D;

public interface IAttitudeIndicatorGauge2D<A extends IAttitudeIndicator2D, R extends IRollIndicator2D> extends IAttitudeIndicatorGauge<IPosition2D, ISize2D, A, R>, IGauge2D {
}
