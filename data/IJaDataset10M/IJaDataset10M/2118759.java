package se.sics.cooja.interfaces;

import se.sics.cooja.*;

/**
 * A PIR represents a passive infrared sensor. An implementation should notify all
 * observers if the PIR discovers any changes.
 * 
 * @author Fredrik Osterlind
 */
@ClassDescription("Passive IR")
public abstract class PIR extends MoteInterface {

    /**
   * Simulates a change in the PIR sensor.
   */
    public abstract void triggerChange();
}
