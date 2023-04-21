package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryRifleBlazerRifleWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryRifleBlazerRifleWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Infantry Blazer Rifle";
        setInternalName(name);
        addLookupName("InfantryBlazerRifle");
        ammoType = AmmoType.T_NA;
        cost = 2190;
        bv = 1.46;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_LASER).or(F_ENERGY);
        infantryDamage = 0.35;
        infantryRange = 2;
    }
}
