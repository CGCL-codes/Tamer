package plugin.lsttokens.kit.gear;

import pcgen.core.kit.KitGear;
import pcgen.persistence.lst.KitGearLstToken;
import pcgen.util.Logging;

/**
 * GEAR Token for KitGear
 */
public class GearToken implements KitGearLstToken {

    /**
	 * Gets the name of the tag this class will parse.
	 * 
	 * @return Name of the tag this class handles
	 */
    public String getTokenName() {
        return "GEAR";
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
        Logging.errorPrint("Ignoring second GEAR tag \"" + value + "\" in Kit.");
        return false;
    }
}
