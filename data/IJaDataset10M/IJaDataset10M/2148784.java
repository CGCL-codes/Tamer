package com.googlecode.grt192.HH11.actuator;

/**
 * A solenoid is a coil of wire that is energized to provide linear actuation
 * 
 * They are often attached to Pneumatic Valves to control the flow of air
 * 
 * @author ajc
 * 
 */
public interface ISolenoid {

    public static final boolean ACTIVATED = true;

    public static final boolean DEACTIVATED = false;

    public void activate();

    public void deactivate();
}
