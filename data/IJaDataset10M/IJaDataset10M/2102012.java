package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolSpitballGasPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolSpitballGasPistolWeapon() {
        super();
        techLevel = TechConstants.T_IS_TW_ALL;
        name = "Spitball Gas Weapon";
        setInternalName(name);
        addLookupName("InfantrySpitballGaspistol");
        ammoType = AmmoType.T_AC;
        cost = 6;
        bv = 0;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.0;
        infantryRange = 0;
    }
}
