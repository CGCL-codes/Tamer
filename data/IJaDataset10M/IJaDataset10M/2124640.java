package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryGrenadeInfernoWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryGrenadeInfernoWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Inferno Grenades";
        setInternalName(name);
        addLookupName("InfantryInfernoGrenade");
        ammoType = AmmoType.T_NA;
        cost = 16;
        bv = 0.22;
        flags = flags.or(F_INFERNO).or(F_BALLISTIC).or(F_INF_SUPPORT);
        infantryDamage = 0.15;
        infantryRange = 0;
    }
}
