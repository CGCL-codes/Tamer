package plugin.lsttokens.kit.gear;

import pcgen.core.kit.KitGear;
import pcgen.persistence.lst.KitGearLstToken;

/**
 * EQMOD Token for KitGear
 */
public class EqmodToken implements KitGearLstToken {

    /**
	 * Gets the name of the tag this class will parse.
	 * 
	 * @return Name of the tag this class handles
	 */
    public String getTokenName() {
        return "EQMOD";
    }

    /**
	 * parse
	 * 
	 * @param kitGear
	 *            KitGear
	 * @param value
	 *            String
	 * @return boolean
	 */
    public boolean parse(KitGear kitGear, String value) {
        kitGear.addEqMod(value);
        return true;
    }
}
