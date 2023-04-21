package org.hooliguns.ninja.telnet.phiPiMod;

import org.hooliguns.ninja.telnet.CommandExecutionUnit;
import org.hooliguns.ninja.telnet.ValueOutOfRangeException;

/**
 * Set the desired Y (altitude) value to the number following. For example Y70
 * sets Y to 70. Y can range from -1200 (far down) to 550 (far up). Values
 * outside this range will saturate to these limits. The center (home) position
 * is zero (looking straight ahead). This value is persistent until changed with
 * another Y command, except when the I (increment position) command is
 * executed.
 * 
 * @author Manish Pandya (July 1 2008)
 */
public class SetY extends CommandIntParam {

    /**
	 * 550 (far up)
	 */
    public static final int YMAX = 550;

    /**
	 * -1200 (far down)
	 */
    public static final int YMIN = -1200;

    /**
	 * Constructs a SetY command with desired altitude
	 * 
	 * @param yVal
	 *          desired altitude in integer
	 * @throws ValueOutOfRangeException
	 *           when desired altitude is out of range
	 */
    public SetY(int yVal) throws ValueOutOfRangeException {
        if (yVal < YMIN || yVal > YMAX) {
            throw new ValueOutOfRangeException("Y (altitude) must fall within the range of " + YMIN + " (far down) to " + YMAX + " (far up) as per PhiPi firmware; You requested " + yVal);
        }
        command = "y";
        this.paramVal = yVal;
    }

    public String getHumanName() {
        return "SetY command";
    }

    @Override
    public byte[] execute(CommandExecutionUnit ceu) {
        ceu.setDestinationY(paramVal);
        return super.execute(ceu);
    }
}
