package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryRifleBoltActionSniperWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryRifleBoltActionSniperWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Infantry Sniper Rifle";
        setInternalName(name);
        addLookupName("InfantryBoltActionSniperRifle");
        ammoType = AmmoType.T_NA;
        cost = 350;
        bv = 0.75;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.18;
        infantryRange = 2;
    }
}
