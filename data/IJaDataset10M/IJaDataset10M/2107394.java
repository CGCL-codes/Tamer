package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryRifleMakeshiftWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryRifleMakeshiftWeapon() {
        super();
        techLevel = TechConstants.T_IS_TW_ALL;
        name = "Makeshift Rifle";
        setInternalName(name);
        addLookupName("InfantryMakeshiftRifle");
        ammoType = AmmoType.T_NA;
        cost = 20;
        bv = 0.07;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.03;
        infantryRange = 1;
    }
}
