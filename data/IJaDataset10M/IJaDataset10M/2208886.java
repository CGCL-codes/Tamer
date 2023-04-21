package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolHoldoutGyrojetPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolHoldoutGyrojetPistolWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Holdout Gyrojet Pistol";
        setInternalName(name);
        addLookupName("InfantryHoldoutGyrojetpistol");
        ammoType = AmmoType.T_AC;
        cost = 30;
        bv = 0.03;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.04;
        infantryRange = 0;
    }
}
