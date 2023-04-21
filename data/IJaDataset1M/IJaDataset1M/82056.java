package org.gdi3d.vrmlloader.impl;

import javax.media.ding3d.*;

/**  Description of the Class */
public class PickSphereTimer extends TransparencyInterpolator {

    WakeupCondition critter;

    RGroup handle;

    /**
     *Constructor for the PickSphereTimer object
     *
     *@param  browser Description of the Parameter
     *@param  handle Description of the Parameter
     *@param  ta Description of the Parameter
     */
    public PickSphereTimer(Browser browser, RGroup handle, TransparencyAttributes ta) {
        super(new Alpha(1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 0, 0, 500, 0, 0), ta);
        this.handle = handle;
        setSchedulingBoundingLeaf(browser.loader.infiniteBoundingLeaf);
        setMinimumTransparency(.5f);
        setMaximumTransparency(.9f);
    }

    /**
     *  Description of the Method
     *
     *@param  critters Description of the Parameter
     */
    public void processStimulus(java.util.Enumeration critters) {
        if (getAlpha().finished()) {
            handle.detach();
            try {
                while (true) {
                    handle.removeChild(0);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ;
            }
        }
        super.processStimulus(critters);
    }
}
