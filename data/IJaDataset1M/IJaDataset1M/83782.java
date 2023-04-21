package jscorch;

import java.awt.*;
import javax.swing.*;

/**
 * a Baby Missile
 */
public class BabyMissile extends Missile {

    /**
	 * creates a new Baby Missile
	 */
    public BabyMissile(Tank o, Point loc, int angle, int power) {
        super(o, loc, angle, power);
        expType = WeaponTypes.BABY_MISSILE;
    }
}
