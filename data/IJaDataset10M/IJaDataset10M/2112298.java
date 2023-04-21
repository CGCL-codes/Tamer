package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolPaintGunPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolPaintGunPistolWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Paint Gun LGB-46R";
        setInternalName(name);
        addLookupName("InfantryPaintGun");
        ammoType = AmmoType.T_NA;
        cost = 50;
        bv = 0;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_ENERGY);
        infantryDamage = 0.0;
        infantryRange = 0;
    }
}
