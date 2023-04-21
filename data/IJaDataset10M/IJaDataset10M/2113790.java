package plugin.lstcompatibility.equipment;

import java.math.BigDecimal;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.inst.CDOMEquipment;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMCompatibilityToken;

/**
 * Deals with WT token
 */
public class Wt512Token implements CDOMCompatibilityToken<CDOMEquipment> {

    /**
	 * Get token name
	 * 
	 * @return token name
	 */
    public String getTokenName() {
        return "WT";
    }

    public boolean parse(LoadContext context, CDOMEquipment eq, String value) {
        try {
            Double.valueOf(value);
            return false;
        } catch (NumberFormatException nfe) {
            context.getObjectContext().put(eq, ObjectKey.WEIGHT, BigDecimal.ZERO);
            return true;
        }
    }

    public int compatibilityLevel() {
        return 5;
    }

    public int compatibilityPriority() {
        return 0;
    }

    public int compatibilitySubLevel() {
        return 12;
    }

    public Class<CDOMEquipment> getTokenClass() {
        return CDOMEquipment.class;
    }
}
