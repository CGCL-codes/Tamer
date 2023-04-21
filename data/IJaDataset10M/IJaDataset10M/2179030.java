package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryRifleGyrojetWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryRifleGyrojetWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Gyrojet Rifle";
        setInternalName(name);
        addLookupName("InfantryGyrojetRifle");
        ammoType = AmmoType.T_NA;
        cost = 1000;
        bv = 0.86;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.35;
        infantryRange = 1;
    }
}
