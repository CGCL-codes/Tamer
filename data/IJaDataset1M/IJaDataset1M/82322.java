package org.xith3d.ui.hud.utils;

/**
 * Use this listener to get notified when a Widget is picked.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface HUDPickListener {

    /**
     * This method is called when a Widget has been picked
     * 
     * @param hpr the HUDPickResult holding all information of this picking
     */
    public void onWidgetPicked(HUDPickResult hpr);
}
