package edu.ohiou.lev_neiman.jung.volume_render.event;

/**
 * <p>Title: Scientific Volume Rendering</p>
 *
 * <p>Description: Lev Neiman's Summer Job</p>
 *
 * <p>Copyright: Copyright (c) 2008, Lev A. Neiman</p>
 *
 * <p>Company: Dr. Peter Jung</p>
 *
 * @author Lev A. Neiman
 * @version 1.0
 */
public class GenericEvent implements RenderEvent {

    Object carry_on;

    String event_name;

    public GenericEvent(Object carry_on, String event_name) {
        this.event_name = event_name;
        this.carry_on = carry_on;
    }

    public Object getCarryOn() {
        return carry_on;
    }

    public String getEvent() {
        return event_name;
    }
}
