package org.dago.wecommand.wiimote.bt;

import java.util.EnumSet;
import org.dago.wecommand.wiimote.*;

/**
 * Alterable Wiimote status.
 * @see Wiimote
 */
public interface MutableWiimoteStatus extends Status {

    /**
	 * Modifies the set of LED which are light on
	 * @param newLeds the new light on leds
	 */
    void setLEDsOn(EnumSet<Led> newLeds);

    /**
	 * Modifies the set of buttons which are light on
	 * @param newPressedButtons the new pressed buttons
	 */
    void setPressedButtons(EnumSet<Button> newPressedButtons);
}
