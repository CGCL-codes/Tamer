package org.gdi3d.vrmlloader.impl;

import javax.media.ding3d.*;

/**  Description of the Class */
public class ProximityBehavior extends Behavior {

    ProximitySensor owner;

    WakeupOnViewPlatformEntry woven;

    WakeupOnViewPlatformExit wovex;

    WakeupCriterion[] conditions;

    WakeupOr wor;

    boolean active = false;

    /**
     *Constructor for the ProximityBehavior object
     *
     *@param  ps Description of the Parameter
     */
    public ProximityBehavior(ProximitySensor ps) {
        owner = ps;
    }

    /**  Description of the Method */
    public void initialize() {
        setSchedulingBounds(owner.bbox);
        wovex = new WakeupOnViewPlatformExit(owner.bbox);
        woven = new WakeupOnViewPlatformEntry(owner.bbox);
        conditions = new WakeupCriterion[2];
        conditions[0] = wovex;
        conditions[1] = woven;
        wor = new WakeupOr(conditions);
        wakeupOn(wor);
    }

    /**
     *  Description of the Method
     *
     *@param  ofElements Description of the Parameter
     */
    public void processStimulus(java.util.Enumeration ofElements) {
        WakeupCriterion wakeup;
        double t = Time.getNow();
        while (ofElements.hasMoreElements()) {
            wakeup = (WakeupCriterion) ofElements.nextElement();
            if (wakeup instanceof WakeupOnViewPlatformExit) {
                owner.exitTime.setValue(t);
                active = false;
                owner.isActive.setValue(active);
                wakeupOn(new WakeupOnViewPlatformEntry(owner.bbox));
            } else if (wakeup instanceof WakeupOnViewPlatformEntry) {
                owner.enterTime.setValue(t);
                active = true;
                owner.isActive.setValue(active);
                wakeupOn(new WakeupOnViewPlatformExit(owner.bbox));
            }
        }
    }
}
