package jscorch;

import java.awt.*;
import javax.swing.*;

/**
 * a Nuke
 */
public class Nuke extends Missile {

    /**
	 * creates a new Nuke
	 */
    public Nuke(Tank owner, Point loc, int angle, int power) {
        super(owner, loc, angle, power);
        expType = WeaponTypes.NUKE;
    }
}
