package org.xith3d.ui.hud.listeners;

import org.xith3d.ui.hud.widgets.Slider;

/**
 * If a Widget makes use of other Widgets to be built and these Widgets produce
 * events, that are to be catched be the Widget only, then you should create an
 * inner class and let it extend this class to catch them, since it already
 * implements all known Widget-Listeners (with empty method stubs).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class SliderAdapter implements SliderListener {

    /**
     * {@inheritDoc}
     */
    public void onSliderValueChanged(Slider slider, int newValue) {
    }
}
