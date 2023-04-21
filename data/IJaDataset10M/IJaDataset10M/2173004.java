package net.sf.smartcrib.dmx;

/**
 * A DMX device listener for events related with DMX devices.
 */
public interface DMXDeviceListener {

    /** Called when the DMX device value has changed either from a software call or a hardware action.
     * @param event the associated event.
     */
    void valueChanged(DMXDeviceEvent event);
}
