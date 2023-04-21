package emtigi.card;

import emtigi.core.*;
import emtigi.core.util.BasicLand;

/**
 * @author Thomas Kamps
 *
 */
public class Swamp extends BasicLand {

    /**
	 * Creats a Swamp
	 * @param owner
	 */
    public Swamp(Player owner) {
        super(owner, "Swamp", ManaColor.BLACK);
    }
}
