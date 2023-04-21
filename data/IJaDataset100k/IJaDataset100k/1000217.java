package org.photovault.swingui.color;

import org.photovault.image.ColorCurve;

/**
 Listener interface for receiving events from {@link ColorCurvePanel}
 */
public interface ColorCurveChangeListener {

    /**
     This method is called every time use moves any of the control points
     @param p The {@link ColorCurvePanel} that initiated the event
     @param c Curve after applying the change
     */
    public void colorCurveChanging(ColorCurvePanel p, ColorCurve c);

    /**
     This method is called after user has completed changing a control point,
     i.e. releases mouse on it.
     @param p The {@link ColorCurvePanel} that initiated the event
     @param c Curve after applying the change
     */
    public void colorCurveChangeCompleted(ColorCurvePanel p, ColorCurve c);
}
